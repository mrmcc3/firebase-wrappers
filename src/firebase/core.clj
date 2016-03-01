(ns firebase.core
  (:refer-clojure :exclude [ref set update key val])
  (:require [clojure.walk :as walk]
            [clojure.core :as core])
  (:import [com.firebase.client
            Firebase$CompletionListener
            Firebase$AuthResultHandler
            Firebase$AuthStateListener
            ValueEventListener
            ChildEventListener
            Firebase
            AuthData]
           [com.firebase.security.token
            TokenGenerator
            TokenOptions]
           [java.time LocalDateTime ZoneId]
           [java.util Date]))

;; WIP minimal clojure wrapper for firebase JVM client library

;; ----------------------------------------------------------------------------
;; ref creation/navigation


;; make this work with datasnapshots
(defn ref [s]
  (Firebase. s))

(defn key [r]
  (.getKey r))

(defn push [r]
  (.push r))

(defn child [r & args]
  (reduce (fn [acc c] (.child acc (if (keyword? c) (name c) c))) r args))

(defn parent [r]
  (.getParent r))

(defn root [r]
  (.getRoot r))

;; ----------------------------------------------------------------------------
;; mutations

(defn cb->complete [cb]
  (reify Firebase$CompletionListener
    (onComplete [_ err _] (cb err))))

(defn set
  ([r v] (.setValue r (walk/stringify-keys v)))
  ([r v cb] (.setValue r (walk/stringify-keys v) (cb->complete cb))))

(defn update
  ([r v]
   (.updateChildren r (walk/stringify-keys v)))
  ([r v cb]
   (.updateChildren r (walk/stringify-keys v) (cb->complete cb))))

(defn set-on-disconnect
  ([r v]
   (.setValue (.onDisconnect r) v))
  ([r v cb]
   (.setValue (.onDisconnect r) v
              ^Firebase$CompletionListener (cb->complete cb))))

(defn cancel-on-disconnect
  ([r]
   (.cancel (.onDisconnect r)))
  ([r cb]
   (.setValue (.onDisconnect r)
              ^Firebase$CompletionListener (cb->complete cb))))

;; ----------------------------------------------------------------------------
;; reads

(defn callbacks->el [callbacks]
  (if-let [cb (:value callbacks)]
    (reify ValueEventListener
      (onDataChange [_ ss]
        (cb ss))
      (onCancelled [_ err]
        (when-let [cbe (:error callbacks)] (cbe err))))
    (reify ChildEventListener
      (onChildAdded [_ ss _]
        (when-let [cb (:child-added callbacks)] (cb ss)))
      (onChildChanged [_ ss _]
        (when-let [cb (:child-changed callbacks)] (cb ss)))
      (onChildMoved [_ ss _]
        (when-let [cb (:child-moved callbacks)] (cb ss)))
      (onChildRemoved [_ ss]
        (when-let [cb (:child-removed callbacks)] (cb ss)))
      (onCancelled [_ err]
        (when-let [cb (:error callbacks)] (cb err))))))

(defn on-value
  ([r cb]
   (.addValueEventListener r (callbacks->el {:value cb})))
  ([r cb err-cb]
   (.addValueEventListener r (callbacks->el {:value cb
                                             :error err-cb}))))

(defn once-value
  ([r cb]
   (.addListenerForSingleValueEvent r (callbacks->el {:value cb})))
  ([r cb err-cb]
   (.addListenerForSingleValueEvent r (callbacks->el {:value cb
                                                      :error err-cb}))))
(defn- el-off [r el]
  (.removeEventListener r el))

(def off-value el-off)

(defn on-child-added
  ([r cb]
   (on-child-added r cb nil))
  ([r cb err-cb]
   (let [el (callbacks->el {:child-added cb :error err-cb})]
     (.addChildEventListener r el)
     el)))

(def off-child-added el-off)

;; ----------------------------------------------------------------------------
;; snapshots

(defprotocol ConvertibleToClojure
  (->clj [o]))

(extend-protocol ConvertibleToClojure
  java.util.Map
  (->clj [o]
    (let [entries (.entrySet o)]
      (reduce
        (fn [m [^String k v]]
          (assoc m (keyword k) (->clj v)))
        {} entries)))
  java.util.List
  (->clj [o] (into [] (map ->clj) o))
  java.lang.Object
  (->clj [o] o)
  nil
  (->clj [_] nil))

(defn val [ss]
  (-> ss .getValue ->clj))

;; ----------------------------------------------------------------------------
;; authentication

(defn unauth [r]
  (.unauth r))

(defn xform-auth [auth-data]
  (let [provider (keyword (.getProvider auth-data))]
    {:uid      (.getUid auth-data)
     :provider provider
     :token    (.getToken auth-data)
     :auth     (clojure.core/update
                 (->clj (.getAuth auth-data)) :provider keyword)
     :expires  (.getExpires auth-data)
     provider  (->clj (.getProviderData auth-data))}))

(defn auth-cb->el [cb]
  (reify Firebase$AuthResultHandler
    (onAuthenticated [_ auth-data]
      (cb nil (xform-auth auth-data)))
    (onAuthenticationError [_ err]
      (cb err nil))))

(defn offset-now [days]
  (-> (LocalDateTime/now)
      (.plusDays days)
      (.atZone (ZoneId/systemDefault))
      .toInstant
      Date/from))

(defn map->token-options [{:keys [expire-in-days active-in-days admin]}]
  (let [opts (TokenOptions.)]
    (when expire-in-days
      (.setExpires opts (offset-now expire-in-days)))
    (when active-in-days
      (.setNotBefore opts (offset-now active-in-days)))
    (when admin
      (.setAdmin opts true))
    opts))

(defn gen-token [{:keys [secret payload options]}]
  (let [update-fn (fn [p] (if (keyword? p) (name p) p))]
    (.createToken
      (TokenGenerator. secret)
      (walk/stringify-keys (core/update payload :provider update-fn))
      (map->token-options options))))

(defn auth
  ([r creds]
   (auth r creds (fn [_ _])))
  ([r {:keys [token anonymous password oauth]} cb]
   (cond
     token
     (let [t (if (string? token) token (gen-token token))]
       (.authWithCustomToken r t (auth-cb->el cb)))
     password
     (let [{:keys [email password]} password]
       (.authWithPassword r email password (auth-cb->el cb)))
     anonymous
     (.authAnonymously r (auth-cb->el cb))
     oauth
     (println "not impl. yet"))))

(defn get-auth [r]
  (when-let [auth-data (.getAuth r)]
    (xform-auth auth-data)))

(defn on-auth-cb->el [cb]
  (reify Firebase$AuthStateListener
    (onAuthStateChanged [_ auth-data]
      (cb (when auth-data (xform-auth auth-data))))))

(defn on-auth [r cb]
  (.addAuthStateListener r (on-auth-cb->el cb)))

(defn off-auth [r el]
  (.removeAuthStateListener r el))


