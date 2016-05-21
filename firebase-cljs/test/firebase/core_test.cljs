(ns firebase.core_test
  (:require-macros [user :refer [config]])
  (:require [cljs.test :refer-macros [deftest is async run-tests use-fixtures]]
            [firebase.core :as fb]))

;; ---------------------------------------------------------------------------------
;; setup

(js/console.clear)
(defn clog [x] (js/console.log x))
(enable-console-print!)

(def cfg (config))
(def user-cfg (-> cfg :secrets :firebase :user))
(def app (-> cfg :secrets :firebase :config fb/app))

(use-fixtures
  :once
  {:before
   (fn []
     (async done
       (let [{:keys [email password]} user-cfg]
         (-> app .auth
             (.signInWithEmailAndPassword email password)
             (.then #(done))))))
   :after
   (fn []
     (-> app .auth .signOut
         (.then #(println "\ndone! unauthenticated from firebase"))))})

;; ---------------------------------------------------------------------------------
;; WIP tests

(deftest current-user
  (let [{:keys [email uid]} user-cfg
        user (-> app .auth .-currentUser)]
    (is (= (.-email user) email))
    (is (false? (.-emailVerified user)))
    (is (false? (.-isAnonymous user)))
    (is (= (.-uid user) uid))
    (is (string? (.-refreshToken user)))))

(run-tests)
