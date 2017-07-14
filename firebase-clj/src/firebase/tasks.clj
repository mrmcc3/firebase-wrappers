(ns firebase.tasks
  (:require
    [clojure.walk :as walk])
  (:import
    (clojure.lang IDeref IBlockingDeref IPending)
    (com.google.firebase.tasks Task Tasks OnCompleteListener Continuation)
    (java.util.concurrent TimeUnit TimeoutException)))

(defprotocol IContinue
  (continue-with [_ f]))

(deftype DerefableTask [task]
  IContinue
  (continue-with [_ f]
    (DerefableTask.
      (.continueWith
        task
        (reify Continuation
          (then [_ task]
            (f (.getResult task)))))))
  IDeref
  (deref [_] (Tasks/await task))
  IBlockingDeref
  (deref [_ timeout-ms timeout-val]
    (try
      (Tasks/await task timeout-ms TimeUnit/MILLISECONDS)
      (catch TimeoutException _ timeout-val)))
  IPending
  (isRealized [_] (.isComplete task)))

(defn derefify [^Task task]
  (DerefableTask. task))