(ns firebase.test-runner
  (:require [firebase.test-externs]
            ;[firebase.core-test]
            [cljs.test :refer-macros [run-tests]]))

(run-tests
  'firebase.test-externs
  ;'firebase.core-test
  )
