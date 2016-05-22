/*! Externs for firebase.js v3.0 */

// --------------------------------------------------------------------------------
// https://firebase.google.com/docs/reference/js/firebase
window.firebase = {};
window.firebase.app = function(name) {};
window.firebase.auth = function(name) {};
window.firebase.database = function(name) {};
window.firebase.storage = function(name) {};
window.firebase.apps = [];
window.firebase.SDK_VERSION = "";
window.firebase.initializeApp = function(name) {};

// --------------------------------------------------------------------------------
// App -> https://firebase.google.com/docs/reference/js/firebase.app.App
var app = {};
app.name = "";
app.options = {};
app.auth = function() {};
app.database = function() {};
app.delete = function() {};
app.storage = function() {};

// --------------------------------------------------------------------------------
// Auth -> https://firebase.google.com/docs/reference/js/firebase.auth.Auth
var auth = {};
auth.app = {};
auth.currentUser = null;
auth.applyActionCode = function(code) {};
auth.checkActionCode = function(code) {};
auth.confirmPasswordReset = function(code, newPassword) {};
auth.createUserWithEmailAndPassword = function(email, password) {};
auth.fetchProvidersForEmail = function(email) {};
auth.getRedirectResult = function() {};
auth.onAuthStateChanged = function(nextOrObserver, opt_error, opt_completed) {};
auth.signInAnonymously = function() {};
auth.signInWithCredential = function(credential) {};
auth.signInWithCustomToken = function(token) {};
auth.signInWithEmailAndPassword = function(email, password) {};
auth.signInWithPopup = function(provider) {};
auth.signInWithRedirect = function(provider) {};
auth.signOut = function() {};
auth.verifyPasswordResetCode = function(code) {};

// --------------------------------------------------------------------------------
// User -> https://firebase.google.com/docs/reference/js/firebase.User
var user = {};
user.displayName = null;
user.email = null;
user.emailVerified = true;
user.isAnonymous = true;
user.photoURL = null;
user.providerData = [];
user.providerId = null;
user.refreshToken = "";
user.uid = null;
user.delete = function() {};
user.getToken = function(refresh) {};
user.link = function(credential) {};
user.linkWithPopup = function(provider) {};
user.linkWithRedirect = function(provider) {};
user.reauthenticate = function(credential) {};
user.reload = function() {};
user.sendEmailVerification = function() {};
user.unlink = function(providerId) {};
user.updateEmail = function(newEmail) {};
user.updatePassword = function(newPassword) {};
user.updateProfile = function(profile) {};

// --------------------------------------------------------------------------------
// Database -> https://firebase.google.com/docs/reference/js/firebase.database.Database
var database = {};
database.ref = function(path) {};
database.refFromUrl = function(url) {};
database.goOffline = function() {};
database.goOnline = function() {};

// --------------------------------------------------------------------------------
// Reference -> https://firebase.google.com/docs/reference/js/firebase.database.Reference
var reference = {};
reference.key = "";
reference.parent = {};
reference.root = {};
reference.ref = {};
reference.child = function(path) {};
reference.endAt = function(value, key) {};
reference.equalTo = function(value, key) {};
reference.limitToFirst = function(limit) {};
reference.limitToLast = function(limit) {};
reference.off = function(eventType, callback, context) {};
reference.on = function(eventType, callback, cancelCallbackOrContext, context) {};
reference.once = function(eventType, userCallback) {};
reference.onDisconnect = function() {};
reference.orderByChild = function(path) {};
reference.orderByKey = function() {};
reference.orderByPriority = function() {};
reference.orderByValue = function() {};
reference.push = function(value, onComplete) {};
reference.remove = function(onComplete) {};
reference.set = function(newVal, onComplete) {};
reference.setPriority = function(priority, onComplete) {};
reference.setWithPriority = function(newVal, newPriority, onComplete) {};
reference.startAt = function(value, key) {};
reference.toString = function() {};
reference.transaction = function(transactionUpdate, onComplete, applyLocally) {};
reference.update = function(objectToMerge, onComplete) {};

// --------------------------------------------------------------------------------
// DataSnapshot -> https://firebase.google.com/docs/reference/js/firebase.database.DataSnapshot
var snapshot = {};
snapshot.key = "";
snapshot.ref = {};
snapshot.child = function(path) {};
snapshot.exists = function() {};
snapshot.forEach = function(action) {};
snapshot.getPriority = function() {};
snapshot.hasChild = function(path) {};
snapshot.hasChildren = function() {};
snapshot.numChildren = function() {};
snapshot.val = function() {};
