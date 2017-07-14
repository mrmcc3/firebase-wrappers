(ns firebase.app
  (:require
    [clojure.walk :as walk]
    [clojure.java.io :as io])
  (:import
    (clojure.lang IDeref)
    (com.google.firebase FirebaseApp FirebaseOptions FirebaseOptions$Builder)
    (com.google.firebase.auth FirebaseCredentials)
    (java.io InputStream)))

(defn ^FirebaseOptions$Builder options-builder
  "Creates a firebase options builder with:
  database-url - Realtime Database URL (i.e https://<db-name>.firebaseio.com)
  certificate - InputStream of the json certificate generated from the firebase console
  auth-overrides - if nil, security rules are evaluated against an unauthenticated user. when a
  map is provided it will by used to evaluate the security rules for realtime database actions.
  (an empty map will act as admin, full privileges)"
  [^String database-url ^InputStream certificate auth-overrides]
  (-> (FirebaseOptions$Builder.)
      (.setCredential (FirebaseCredentials/fromCertificate certificate))
      (.setDatabaseUrl database-url)
      (.setDatabaseAuthVariableOverride (walk/stringify-keys auth-overrides))))

(defn ^FirebaseOptions options
  "Builds firebase options. see options-builder for more details. By default options will provide
  full admin privileges."
  ([^String url ^InputStream json]
   (options url json {}))
  ([^String url ^InputStream json auth]
   (.build (options-builder url json auth))))

(defn- assoc-app [m ^FirebaseApp app]
  (assoc m (keyword (.getName app)) app))

(def apps
  "A global reference to all initialized firebase apps. deref will produce a map
  of keywords to corresponding FirebaseApp objects."
  (reify
    IDeref
    (deref [_] (reduce assoc-app {} (FirebaseApp/getApps)))))

(defn init!
  "Initialize a firebase app with the provided options and keyword.
  returns an updated map of all firebase apps.
  - if an app doesn't exist for kw it will be initialized with provided opts
  - if an app already exists with the same opts and kw then add! is a noop
  - if an app exists with different opts it will replace the old app"
  [^FirebaseOptions opts kw]
  (if-let [^FirebaseApp app (get @apps kw)]
    (when (not= opts (.getOptions app))
      (.delete app)
      (FirebaseApp/initializeApp opts (name kw)))
    (FirebaseApp/initializeApp opts (name kw)))
  @apps)

(defn delete! [kw]
  "Delete the firebase app that is identified by kw. returns an updated map of all firebase apps."
  (when-let [^FirebaseApp app (get @apps kw)]
    (.delete app))
  @apps)

(defn delete-all!
  "Delete all initialized firebase apps."
  []
  (doseq [[_ app] @apps]
    (.delete app))
  @apps)

(defn init-once!
  "Initialize a firebase app using provided cred-source and database url.
  Will return the initialized app. Calling init-once! when an app has already been
  initialized with app-kw will return the app and cred-source will not be read.

  cred-source - source of firebase credentials. cred-source can be anything
  clojure.java.io/input-stream can coerce. File, URI, String etc.
  database-url - Realtime Database URL (i.e https://<db-name>.firebaseio.com)
  auth-overrides - optional auth map. see options-builder for more details
  app-kw - optional keyword used to identify the app. :default if not provided."
  ([cred-source database-url]
   (init-once! cred-source database-url {} :default))
  ([cred-source database-url auth-overrides]
   (init-once! cred-source database-url auth-overrides :default))
  ([cred-source database-url auth-overrides app-kw]
   (or (get @apps app-kw)
       (let [opts (with-open [creds (io/input-stream cred-source)]
                    (options database-url creds auth-overrides))]
         (get (init! opts app-kw) app-kw)))))

