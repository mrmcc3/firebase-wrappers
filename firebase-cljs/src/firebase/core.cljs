(ns firebase.core)

(defn app
  ([cfg] (app cfg "[DEFAULT]"))
  ([cfg name]
   (try
     (js/window.firebase.initializeApp (clj->js cfg) name)
     (catch js/Error _ (js/window.firebase.app name)))))
