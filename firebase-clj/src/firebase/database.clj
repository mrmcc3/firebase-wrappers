(ns firebase.database
  (:refer-clojure :exclude [ref set get update key val])
  (:require
    [clojure.walk :as walk])
  (:import
    (com.google.firebase FirebaseApp)
    (com.google.firebase.database
      FirebaseDatabase
      DatabaseReference
      Query
      DataSnapshot
      DatabaseError
      DatabaseReference$CompletionListener
      ValueEventListener
      ChildEventListener
      OnDisconnect
      ServerValue)
    (java.util Map List)))

;; ----------------------------------------------------------------------------
;; ref creation/navigation

(defprotocol IRef
  (ref [o]))

(extend-protocol IRef
  FirebaseApp
  (ref [app] (ref (FirebaseDatabase/getInstance app)))
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
            (assoc m k (->clj v)))
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

(defn add-listener [r & listeners]
  (doseq [el listeners]
    (cond
      (instance? ValueEventListener el) (.addValueEventListener r el)
      (instance? ChildEventListener el) (.addChildEventListener r el))))

(defn remove-listener [r & listeners]
  (doseq [el listeners]
    (.removeEventListener r el)))

(defn get [r]
  (let [prom (promise)
        cb #(deliver prom %)]
    (.addListenerForSingleValueEvent r (value-listener cb cb))
    prom))


