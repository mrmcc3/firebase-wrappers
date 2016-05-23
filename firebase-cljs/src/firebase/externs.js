/*! Externs for firebase.js v3.0 */

// firebase
window.firebase = {};
window.firebase.apps = [];
window.firebase.SDK_VERSION = "";
window.firebase.initializeApp = function(name) {};

// firebase.app
window.firebase.app = function(name) {};

// firebase.auth
window.firebase.auth = function(name) {};

// firebase.database
window.firebase.database = function(name) {};
// window.firebase.database.SDK_VERSION = "";
window.firebase.database.ServerValue = {};
window.firebase.database.ServerValue.TIMESTAMP = {};
window.firebase.database.enableLogging = function(logger, persistent) {};

// firebase.storage
window.firebase.storage = function(name) {};
window.firebase.storage.TaskEvent = {};
window.firebase.storage.TaskEvent.STATE_CHANGED = "";
window.firebase.storage.TaskState = {};
window.firebase.storage.TaskState.RUNNING = "";
window.firebase.storage.TaskState.PAUSED = "";
window.firebase.storage.TaskState.SUCCESS = "";
window.firebase.storage.TaskState.CANCELED = "";
window.firebase.storage.TaskState.ERROR = "";

// firebase.app.App
var app = {};
app.name = "";
app.options = {};
app.auth = function() {};
app.database = function() {};
app.delete = function() {};
app.storage = function() {};

// firebase.auth.Auth
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

// firebase.database.Database
var database = {};
database.app = {};
database.ref = function(path) {};
database.refFromURL = function(url) {};
database.goOffline = function() {};
database.goOnline = function() {};

// firebase.storage.Storage
var storage = {};
storage.app = {};
storage.maxOperationRetryTime = 0;
storage.maxUploadRetryTime = 0;
storage.ref = function(path) {};
storage.refFromURL = function(url) {};
storage.setMaxOperationRetryTime = function(time) {};
storage.setMaxUploadRetryTime = function(time) {};


// firebase.UserInfo
var userInfo = {};
userInfo.displayName = null;
userInfo.email = null;
userInfo.photoURL = null;
userInfo.providerId = null;
userInfo.uid = null;

// firebase.User
var user = {};
user.emailVerified = true;
user.isAnonymous = true;
user.providerData = [];
user.refreshToken = "";
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

// firebase.auth.UserCredential

var userCredential = {};
userCredential.user = {};
userCredential.credential = {};

// firebase.auth.AuthCredential
var authCredential = {};
authCredential.provider = "";

// firebase.database.Query
var databaseQuery = {};
databaseQuery.ref = {};
databaseQuery.endAt = function(value, key) {};
databaseQuery.equalTo = function(value, key) {};
databaseQuery.limitToFirst = function(limit) {};
databaseQuery.limitToLast = function(limit) {};
databaseQuery.off = function(eventType, callback, context) {};
databaseQuery.on = function(eventType, callback, cancelCallbackOrContext, context) {};
databaseQuery.once = function(eventType, userCallback) {};
databaseQuery.orderByChild = function(path) {};
databaseQuery.orderByKey = function() {};
databaseQuery.orderByPriority = function() {};
databaseQuery.orderByValue = function() {};
databaseQuery.startAt = function(value, key) {};
databaseQuery.toString = function() {};

// firebase.database.Reference
var databaseReference = {};
databaseReference.key = "";
databaseReference.parent = {};
databaseReference.root = {};
databaseReference.child = function(path) {};
databaseReference.onDisconnect = function() {};
databaseReference.push = function(value, onComplete) {};
databaseReference.remove = function(onComplete) {};
databaseReference.set = function(newVal, onComplete) {};
databaseReference.setPriority = function(priority, onComplete) {};
databaseReference.setWithPriority = function(newVal, newPriority, onComplete) {};
databaseReference.transaction = function(transactionUpdate, onComplete, applyLocally) {};
databaseReference.update = function(objectToMerge, onComplete) {};

// firebase.storage.Reference
var storageReference = {};
storageReference.bucket = "";
storageReference.fullPath = "";
storageReference.name = "";
storageReference.parent = {};
storageReference.root = {};
storageReference.storage = {};
storageReference.child = function(path) {};
storageReference.delete = function() {};
storageReference.getDownloadURL = function() {};
storageReference.getMetadata = function() {};
storageReference.put = function(blob, metadata) {};
storageReference.toString = function() {};
storageReference.updateMetadata = function(metadata) {};

// firebase.database.DataSnapshot
var snapshot = {};
snapshot.key = "";
snapshot.ref = {};
snapshot.child = function(path) {};
snapshot.exists = function() {};
snapshot.exportVal = function() {};
snapshot.forEach = function(action) {};
snapshot.getPriority = function() {};
snapshot.hasChild = function(path) {};
snapshot.hasChildren = function() {};
snapshot.numChildren = function() {};
snapshot.val = function() {};
