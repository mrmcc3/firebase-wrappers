(ns firebase.test-externs
  (:require-macros [user :refer [config]])
  (:require [cljs.test :refer-macros [deftest is async use-fixtures]]))

;; --------------------------------------------------------------------------------
;; setup

(defn clog [x] (js/console.log x))
(enable-console-print!)

(def cfg (config))
(def user-cfg (-> cfg :secrets :firebase :user))
(def app-cfg (-> (config) :secrets :firebase :config))
(def app
  (try (js/window.firebase.initializeApp (clj->js app-cfg))
       (catch js/Error _ (js/window.firebase.app))))

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

;; --------------------------------------------------------------------------------
;; tests

(deftest firebase
  (is js/firebase)
  (is (array? js/window.firebase.apps))
  (is (string? js/window.firebase.SDK_VERSION))
  (is (ifn? js/window.firebase.initializeApp)))

(deftest firebase-app
  (is (ifn? js/window.firebase.app)))

(deftest firebase-auth
  (is (ifn? js/window.firebase.auth)))

(deftest firebase-database
  (is (ifn? js/window.firebase.database))
  (is (-> js/window.firebase.database .-ServerValue .-TIMESTAMP object?))
  (is (-> js/window.firebase.database .-enableLogging ifn?)))

(deftest firebase-storage
  (is (ifn? js/window.firebase.storage))
  (is (-> js/window.firebase.storage .-TaskEvent .-STATE_CHANGED string?))
  (is (-> js/window.firebase.storage .-TaskState .-RUNNING))
  (is (-> js/window.firebase.storage .-TaskState .-PAUSED))
  (is (-> js/window.firebase.storage .-TaskState .-SUCCESS))
  (is (-> js/window.firebase.storage .-TaskState .-CANCELED))
  (is (-> js/window.firebase.storage .-TaskState .-ERROR)))

(deftest firebase-app-App
  (is (-> app .-name (= "[DEFAULT]")))
  (is (-> app .-options object?))
  (is (-> app .-auth ifn?))
  (is (-> app .-database ifn?))
  (is (-> app .-delete ifn?))
  (is (-> app .-storage ifn?)))

(deftest firebase-auth-Auth
  (let [auth (.auth app)]
    (is auth)
    (is (-> auth .-app (= app)))
    (is (-> auth .-currentUser))
    (is (-> auth .-applyActionCode ifn?))
    (is (-> auth .-checkActionCode ifn?))
    (is (-> auth .-confirmPasswordReset ifn?))
    (is (-> auth .-createUserWithEmailAndPassword ifn?))
    (is (-> auth .-fetchProvidersForEmail ifn?))
    (is (-> auth .-getRedirectResult ifn?))
    (is (-> auth .-onAuthStateChanged ifn?))
    (is (-> auth .-signInAnonymously ifn?))
    (is (-> auth .-signInWithCredential ifn?))
    (is (-> auth .-signInWithCustomToken ifn?))
    (is (-> auth .-signInWithEmailAndPassword ifn?))
    (is (-> auth .-signInWithPopup ifn?))
    (is (-> auth .-signInWithRedirect ifn?))
    (is (-> auth .-signOut ifn?))
    (is (-> auth .-verifyPasswordResetCode ifn?))))

(deftest firebase-database-Database
  (let [db (.database app)]
    (is db)
    (is (-> db .-app (= app)))
    (is (-> db .-goOffline ifn?))
    (is (-> db .-goOnline ifn?))))

(deftest firebase-database-Storage
  (let [storage (.storage app)]
    (is storage)
    (is (-> storage .-app (= app)))
    (is (-> storage .-maxOperationRetryTime number?))
    (is (-> storage .-maxUploadRetryTime number?))
    (is (-> storage .-ref ifn?))
    (is (-> storage .-refFromURL ifn?))
    (is (-> storage .-setMaxOperationRetryTime ifn?))
    (is (-> storage .-setMaxUploadRetryTime ifn?))))

(deftest firebase-User
  (let [{:keys [email uid]} user-cfg
        user (-> app .auth .-currentUser)]
    (is (-> user .-displayName string?))
    (is (-> user .-email (= email)))
    (is (-> user .-emailVerified false?))
    (is (-> user .-isAnonymous false?))
    (is (-> user .-photoURL string?))
    (is (-> user .-providerData array?))
    (is (-> user .-providerId string?))
    (is (-> user .-refreshToken string?))
    (is (-> user .-uid (= uid)))
    (is (-> user .-delete ifn?))
    (is (-> user .-getToken ifn?))
    (is (-> user .-link ifn?))
    (is (-> user .-linkWithPopup ifn?))
    (is (-> user .-linkWithRedirect ifn?))
    (is (-> user .-reauthenticate ifn?))
    (is (-> user .-reload ifn?))
    (is (-> user .-sendEmailVerification ifn?))
    (is (-> user .-unlink ifn?))
    (is (-> user .-updateEmail ifn?))
    (is (-> user .-updatePassword ifn?))
    (is (-> user .-updateProfile ifn?))))

(deftest firebase-database-Reference
  (let [ref (-> app .database .ref)]
    (is ref)
    (is (-> ref .-key nil?))
    (is (-> ref .-parent nil?))
    (is (-> ref .-root (= ref)))
    (is (-> ref .-child ifn?))
    (is (-> ref .-endAt ifn?))
    (is (-> ref .-equalTo ifn?))
    (is (-> ref .-limitToFirst ifn?))
    (is (-> ref .-limitToLast ifn?))
    (is (-> ref .-off ifn?))
    (is (-> ref .-on ifn?))
    (is (-> ref .-once ifn?))
    (is (-> ref .-onDisconnect ifn?))
    (is (-> ref .-orderByChild ifn?))
    (is (-> ref .-orderByKey ifn?))
    (is (-> ref .-orderByPriority ifn?))
    (is (-> ref .-orderByValue ifn?))
    (is (-> ref .-push ifn?))
    (is (-> ref .-remove ifn?))
    (is (-> ref .-set ifn?))
    (is (-> ref .-setWithPriority ifn?))
    (is (-> ref .-startAt ifn?))
    (is (-> ref .-toString ifn?))
    (is (-> ref .-transaction ifn?))
    (is (-> ref .-update ifn?))))

(deftest firebase-storage-Reference
  (let [ref (-> app .storage .ref)]
    (is ref)
    (is (-> ref .-bucket (= (:storageBucket app-cfg))))
    (is (-> ref .-fullPath string?))
    (is (-> ref .-name string?))
    (is (-> ref .-parent nil?))
    (is (-> ref .-root .toString (= (.toString ref))))
    (is (-> ref .-storage))
    (is (-> ref .-child ifn?))
    (is (-> ref .-delete ifn?))
    (is (-> ref .-getDownloadURL ifn?))
    (is (-> ref .-getMetadata ifn?))
    (is (-> ref .-put ifn?))
    (is (-> ref .-toString ifn?))
    (is (-> ref .-updateMetadata ifn?))))

(deftest firebase-database-DataSnapshot
  (async done
    (let [ref (-> app .database .ref)]
      (.then
        (.once ref "value")
        (fn [ss]
          (is ss)
          (is (-> ss .-key nil?))
          (is (-> ss .-ref .toString (= (.toString ref))))
          (is (-> ss .-child ifn?))
          (is (-> ss .-exists ifn?))
          (is (-> ss .-exportVal ifn?))
          (is (-> ss .-forEach ifn?))
          (is (-> ss .-getPriority ifn?))
          (is (-> ss .-hasChild ifn?))
          (is (-> ss .-hasChildren ifn?))
          (is (-> ss .-numChildren ifn?))
          (is (-> ss .-val ifn?))
          (done))))))
