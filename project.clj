(defproject mrmcc3/firebase-clj "0.1.0-SNAPSHOT"
  :description "Minimalist Clojure[Script] Firebase Wrapper"
  :url "http://github.com/mrmcc3/firebase-clj"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :repositories [["clojars" {:sign-releases false}]]
  :java-source-paths ["src/java"]
  :profiles {:dev {:source-paths ["src" "dev"]}}
  :dependencies [[org.clojure/clojure "1.8.0" :scope "provided"]
                 [org.clojure/clojurescript "1.7.228" :scope "provided"]
                 [com.firebase/firebase-client-jvm "2.5.2"]
                 [com.firebase/firebase-token-generator "2.0.0"]
                 [cljsjs/firebase "2.3.1-0"]])
