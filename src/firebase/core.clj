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
            OnDisconnect
            FirebaseError DataSnapshot AuthData
            ServerValue]
           [com.firebase.security.token
            TokenGenerator
            TokenOptions]
           [java.time LocalDateTime ZoneId]
           [java.util Date Map List]))

;; WIP minimal clojure wrapper for firebase JVM client library

;; ----------------------------------------------------------------------------
;; ref creation/navigation

(defn ref [o]
  (if (string? o)
    (Firebase. o)
    (.getRef o)))

(defn key [r]
  (.getKey r))

(defn push [r]
  (.push r))

(defn child [r & args]
  (reduce #(.child %1 %2) r args))

(defn parent [r]
  (.getParent r))

(defn root [r]
  (.getRoot r))

(defn on-disconnect [r]
  (.onDisconnect r))

(def server-timestamp ServerValue/TIMESTAMP)

;; ----------------------------------------------------------------------------
;; mutations

(defn error? [x]
  (instance? FirebaseError x))

(defn completion-listener [prom]
  (reify Firebase$CompletionListener
    (^void onComplete [_ ^FirebaseError err ^Firebase _]
      (deliver prom err))))

(defn set [o val]
  (let [prom (promise)]
    (.setValue o (walk/stringify-keys val) (completion-listener prom))
    prom))

(defn update [o val]
  (let [prom (promise)]
    (.updateChildren o (walk/stringify-keys val) (completion-listener prom))
    prom))

(defn cancel [^OnDisconnect od]
  (let [prom (promise)]
    (.cancel od (completion-listener prom))
    prom))

;; ----------------------------------------------------------------------------
;; snapshots

(defprotocol ConvertibleToClojure
  (->clj [o]))

(extend-protocol ConvertibleToClojure
  nil (->clj [_] nil)
  Object (->clj [o] o)
  List (->clj [o] (into [] (map ->clj) o))
  Map (->clj [o]
        (reduce
          (fn [m [^String k v]]
            (assoc m (keyword k) (->clj v)))
          {} (.entrySet o))))

(defn val [ss]
  (-> ss .getValue ->clj))

;; ----------------------------------------------------------------------------
;; listeners + reads

(defn value-listener
  ([cb] (value-listener cb nil))
  ([cb cbe]
   (reify ValueEventListener
     (^void onDataChange [_ ^DataSnapshot ss] (cb ss))
     (^void onCancelled [_ ^FirebaseError err] (when cbe (cbe err))))))

(defn child-listener [{:keys [added changed moved removed error]}]
  (reify ChildEventListener
    (^void onChildAdded [_ ^DataSnapshot ss ^String _] (when added (added ss)))
    (^void onChildChanged [_ ^DataSnapshot ss ^String _] (when changed (changed ss)))
    (^void onChildMoved [_ ^DataSnapshot ss ^String _] (when moved (moved ss)))
    (^void onChildRemoved [_ ^DataSnapshot ss] (when removed (removed ss)))
    (^void onCancelled [_ ^FirebaseError err] (when error (error err)))))

(defn auth-listener [cb]
  (reify Firebase$AuthStateListener
    (^void onAuthStateChanged [_ ^AuthData d] (cb d))))

(defn add-listener [r el]
  (cond
    (instance? ValueEventListener el) (.addValueEventListener r el)
    (instance? ChildEventListener el) (.addChildEventListener r el)
    (instance? Firebase$AuthStateListener el) (.addAuthStateListener r el)))

(defn remove-listener [r el]
  (cond
    (instance? Firebase$AuthStateListener el) (.removeAuthStateListener r el)
    :else (.removeEventListener r el)))

(defn get-value [r]
  (let [prom (promise)
        cb #(deliver prom %)]
    (.addListenerForSingleValueEvent r (value-listener cb cb))
    prom))

;; ----------------------------------------------------------------------------
;; token generation

(defn offset-now [days]
  (-> (LocalDateTime/now)
      (.plusDays days)
      (.atZone (ZoneId/systemDefault))
      .toInstant
      Date/from))

(defn map->token-options [{:keys [expire-in-days active-in-days admin]}]
  (let [opts (TokenOptions.)]
    (when expire-in-days (.setExpires opts (offset-now expire-in-days)))
    (when active-in-days (.setNotBefore opts (offset-now active-in-days)))
    (when admin (.setAdmin opts true))
    opts))

(defn gen-token [secret payload options]
  (.createToken (TokenGenerator. secret)
                (walk/stringify-keys payload)
                (map->token-options options)))

;; ----------------------------------------------------------------------------
;; authentication

(defn auth-handler [prom]
  (reify Firebase$AuthResultHandler
    (^void onAuthenticated [_ ^AuthData d] (deliver prom d))
    (^void onAuthenticationError [_ ^FirebaseError err] (deliver prom err))))

(defn auth->map [data]
  {:uid           (.getUid data)
   :provider      (.getProvider data)
   :token         (.getToken data)
   :auth          (->clj (.getAuth data))
   :expires       (.getExpires data)
   :provider-data (->clj (.getProviderData data))})

(defn auth-token [r tok]
  (let [prom (promise)]
    (.authWithCustomToken r tok (auth-handler prom))
    prom))

(defn get-auth [r]
  (.getAuth r))

(defn unauth [r]
  (.unauth r))
