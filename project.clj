(defproject mrmcc3/firebase-clj "0.1.1"
  :description "Minimalist Clojure[Script] Firebase Wrapper"
  :url "http://github.com/mrmcc3/firebase-clj"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :java-source-paths ["src/java"]
  :dependencies [[org.clojure/clojure "1.8.0" :scope "provided"]
                 [org.clojure/clojurescript "1.8.51" :scope "provided"]
                 [com.firebase/firebase-client-jvm "2.5.2"]
                 [com.firebase/firebase-token-generator "2.0.0"]
                 [cljsjs/firebase "2.4.1-0"]])
