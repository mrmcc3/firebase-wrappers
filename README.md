# firebase-clj

Minimalist Clojure[Script] Firebase Wrapper.

Work in progress.

[![Clojars Project](https://img.shields.io/clojars/v/mrmcc3/firebase-clj.svg)](https://clojars.org/mrmcc3/firebase-clj)

#### Goals

- avoid complicated wrappers where possible. prefer operations on
vanilla firebase objects
- provide uniform clj/cljs api where possible
- where possible simplify with data driven api

#### TODO

- implement complete firebase api
- docs/docstrings
- tests

#### Keyword/String Keys?

- when persisting maps keyword **keys** are automatically converted to strings
- when retrieving maps string **keys** are automatically converted to keywords
- the library does no other serialization and just hands off to the firebase impl.
