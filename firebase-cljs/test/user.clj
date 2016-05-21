(ns user
  (:require [reloaded.repl :refer [set-init! init start stop reset system go]]
            [com.stuartsierra.component :as component]
            [fbc.core :refer [figwheel]]
            [aero.core :refer [read-config]]))

(def profile :dev)

(defn load-config []
  (read-config "test-resources/config.edn" {:profile profile}))

(defmacro config []
  (load-config))

(set-init!
  #(let [cfg (load-config)]
    (component/system-map
      :figwheel (figwheel cfg))))

(comment

  (init)
  (start)
  (stop)
  (reset)

  )
