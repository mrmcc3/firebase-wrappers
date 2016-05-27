(ns firebase.core
  (:refer-clojure :exclude [ref set get update key val])
  (:require [clojure.walk :as walk])
  (:import
    [com.google.firebase FirebaseApp FirebaseOptions$Builder FirebaseOptions]
    [com.google.firebase.database
     FirebaseDatabase DatabaseReference Query DataSnapshot DatabaseError
     DatabaseReference$CompletionListener ValueEventListener ChildEventListener
     OnDisconnect ServerValue]
    [java.util Map List]
    [java.io InputStream]))

;; ----------------------------------------------------------------------------
;; firebase app initialization

(defn ^FirebaseOptions$Builder options-builder
  [^String url ^InputStream json auth]
  (-> (FirebaseOptions$Builder.)
      (.setServiceAccount json)
      (.setDatabaseUrl url)
      (.setDatabaseAuthVariableOverride (walk/stringify-keys auth))))

(defn ^FirebaseOptions options
  ([^String url ^InputStream json]
   (options url json {}))
  ([^String url ^InputStream json auth]
   (.build (options-builder url json auth))))

(defn ^FirebaseApp init-app
  ([^FirebaseOptions options]
   (FirebaseApp/initializeApp options))
  ([^FirebaseOptions options ^String name]
   (FirebaseApp/initializeApp options name)))

(defn ^FirebaseApp get-app
  ([] (FirebaseApp/getInstance))
  ([^String name] (FirebaseApp/getInstance name)))

(defn ^FirebaseDatabase app->db [^FirebaseApp app]
  (FirebaseDatabase/getInstance app))

;; ----------------------------------------------------------------------------
;; ref creation/navigation

(defprotocol IRef
  (ref [o]))

(extend-protocol IRef
  FirebaseDatabase
  (ref [db] (.getReference db))
  DataSnapshot
  (ref [ss] (.getRef ss))
  Query
  (ref [q] (.getRef q)))

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
  (instance? DatabaseError x))

(defn completion-listener [prom]
  (reify DatabaseReference$CompletionListener
    (^void onComplete [_ ^DatabaseError err ^DatabaseReference _]
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
     (^void onCancelled [_ ^DatabaseError err] (when cbe (cbe err))))))

(defn child-listener [{:keys [added changed moved removed error]}]
  (reify ChildEventListener
    (^void onChildAdded [_ ^DataSnapshot ss ^String _] (when added (added ss)))
    (^void onChildChanged [_ ^DataSnapshot ss ^String _] (when changed (changed ss)))
    (^void onChildMoved [_ ^DataSnapshot ss ^String _] (when moved (moved ss)))
    (^void onChildRemoved [_ ^DataSnapshot ss] (when removed (removed ss)))
    (^void onCancelled [_ ^DatabaseError err] (when error (error err)))))

(defn add-listener [r el]
  (cond
    (instance? ValueEventListener el) (.addValueEventListener r el)
    (instance? ChildEventListener el) (.addChildEventListener r el)))

(defn remove-listener [r el]
  (.removeEventListener r el))

(defn get [r]
  (let [prom (promise)
        cb #(deliver prom %)]
    (.addListenerForSingleValueEvent r (value-listener cb cb))
    prom))
