(ns firebase.users
  (:require
    [firebase.tasks :refer [derefify continue-with]])
  (:import
    (com.google.firebase.auth
      FirebaseAuth
      UserRecord
      UserRecord$CreateRequest
      UserRecord$UpdateRequest)))

(defn user->map [^UserRecord user-record]
  {:disabled       (.isDisabled user-record)
   :display-name   (.getDisplayName user-record)
   :email          (.getEmail user-record)
   :email-verified (.isEmailVerified user-record)
   :photo-url      (.getPhotoUrl user-record)
   :uid            (.getUid user-record)
   :provider-id    (.getProviderId user-record)
   :metadata       (when-let [md (.getUserMetadata user-record)]
                     {:created      (.getCreationTimestamp md)
                      :last-sign-in (.getLastSignInTimestamp md)})})

(defn ^UserRecord$CreateRequest create-user-request
  [{:keys [disabled display-name email email-verified password photo-url uid]}]
  (cond-> (UserRecord$CreateRequest.)
          (boolean? disabled) (.setDisabled disabled)
          display-name (.setDisplayName display-name)
          email (.setEmail email)
          (boolean? email-verified) (.setEmailVerified email-verified)
          password (.setPassword password)
          photo-url (.setPhotoUrl photo-url)
          uid (.setUid uid)))

(defn ^UserRecord$UpdateRequest update-user-request
  [{:keys [disabled display-name email email-verified password photo-url uid]}]
  (cond-> (UserRecord$UpdateRequest. uid)
          (boolean? disabled) (.setDisabled disabled)
          display-name (.setDisplayName display-name)
          email (.setEmail email)
          (boolean? email-verified) (.setEmailVerified email-verified)
          password (.setPassword password)
          photo-url (.setPhotoUrl photo-url)))

(defn create-user [app user]
  (-> (FirebaseAuth/getInstance app)
      (.createUser (create-user-request user))
      (derefify)
      (continue-with user->map)))

(defn update-user [app user]
  (-> (FirebaseAuth/getInstance app)
      (.updateUser (update-user-request user))
      (derefify)
      (continue-with user->map)))

(defn delete-user [app uid]
  (-> (FirebaseAuth/getInstance app)
      (.deleteUser uid)
      (derefify)))

(defn get-user [app uid]
  (-> (FirebaseAuth/getInstance app)
      (.getUser uid)
      (derefify)
      (continue-with user->map)))
