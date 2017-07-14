# firebase-clj

Minimalist Firebase Wrapper for Clojure.

Work in progress. alpha

### example usage

```clojure
(require '[firebase.app :refer [init-once!]]
         '[firebase.database :as db]
         '[firebase.users :as u])

;; simple app init. safe to call multiple times. assumes creds.json in project dir
(def app (init-once! "creds.json" "https://<your-db>.firebaseio.com"))

;; basic reads
(def root (db/ref app))
(db/val @(db/get root))

;; listeners
(def vl (db/value-listener #(println "value" (db/val %))))
(def cl (db/child-listener {:added   #(println "added" (db/val %))
                            :changed #(println "changed" (db/val %))
                            :removed #(println "removed" (db/val %))}))
(db/add-listener root vl cl)
(db/remove-listener root vl cl)

;; basic writes
@(db/set root {"a" {"b" {"c" 123}}})
@(db/update (db/child root "a" "b") {"d" 456})
@(db/set (db/push (db/child root)) 789)
@(db/set root nil)

;; map based user management. api returns deref-able firebase tasks
@(u/create-user app {:uid "blah"})
@(u/update-user app {:uid "blah" :display-name "blah"})
@(u/get-user app "blah")
@(u/delete-user app "blah")

```
