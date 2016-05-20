(defproject mrmcc3/firebase-cljs "0.2.0-SNAPSHOT"
  :description "Minimalist ClojureScript Firebase Wrapper"
  :url "http://github.com/mrmcc3/firebase-clj"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}
  :repositories [["clojars" {:sign-releases false}]]
  :dependencies [[org.clojure/clojure "1.8.0" :scope "provided"]
                 [org.clojure/clojurescript "1.8.51" :scope "provided"]
                 [cljsjs/firebase "3.0.0-0"]]
  :profiles {:dev {:source-paths ["dev" "src"]
                   :resource-paths ["dev-resources"]
                   :dependencies [[reloaded.repl "0.2.1"]
                                  [com.stuartsierra/component "0.3.1"]
                                  [mrmcc3/frontend-build-components "0.1.1"]
                                  [org.slf4j/slf4j-nop "1.7.13"]
                                  [aero "1.0.0-beta2"]]}})
