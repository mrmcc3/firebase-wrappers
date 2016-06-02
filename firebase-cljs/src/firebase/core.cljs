(ns firebase.core)

(defn app
  ([cfg] (app cfg "[DEFAULT]"))
  ([cfg name]
   (try
     (js/window.firebase.initializeApp (clj->js cfg) name)
     (catch js/Error _ (js/window.firebase.app name)))))

(defn child [r & args]
  (reduce #(.child %1 %2) r args))

(defn val [ss]
  (-> ss .val js->clj))
