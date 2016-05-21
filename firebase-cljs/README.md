# firebase-cljs

Minimalist Firebase Wrapper for ClojureScript.

Work in progress.

### Goals

1. develop externs for firebase 3.0 api with the intention of contributing to cljsjs
2. develop tests that verify externs for advanced compilation
3. use `firebase.core` namespace for utilities that make using firebase
from clojurescript more tractable

### Usage

add `[mrmcc3/firebase-cljs "0.2.0-SNAPSHOT"]` to `:dependencies`

Note: this library takes the [extern only package](https://github.com/cljsjs/packages/wiki/Extern-only-packages)
approach so you'll need to provide the firebase.js lib by some other mechanism:

in build via `:foreign-libs`

```clojure
:foreign-libs [{:file "https://www.gstatic.com/firebasejs/live/3.0/firebase.js"
                :provides ["google.firebase"]}]
```

```clojure
(:require [google.firebase])
```

manually via script tag (before compiled js)

```html
<script src="https://www.gstatic.com/firebasejs/live/3.0/firebase.js"></script>
```

### Running tests

1. You need access to a firebase to run the tests. create one at https://firebase.google.com/
2. Enable email authentication and add a user
3. put config in `secrets.edn`
```clojure
{:firebase
 {:config {:apiKey        "your-firebase-key"
           :authDomain    "your-firebase.firebaseapp.com"
           :databaseURL   "https://your-firebase.firebaseio.com"
           :storageBucket "your-firebase.appspot.com"}
  :user   {:email        "your-email"
           :password     "your-password"
           :uid          "your-uid"}}}
```
4. start a repl and the build system with `(go)`
5. view test results in browser console at `http://localhost:3449/`
6. to test externs with advanced compilation set profile to `:dist` and `(reset)`
