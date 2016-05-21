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
// https://firebase.google.com/docs/reference/js/firebase.app.App
var app = {};
app.name = "";
app.options = {};
app.auth = function() {};
app.database = function() {};
app.delete = function() {};
app.storage = function() {};

// --------------------------------------------------------------------------------
// https://firebase.google.com/docs/reference/js/firebase.auth.Auth
var auth = {};
auth.app = {};
auth.currentUser = null;
auth.signOut = function() {};
auth.signInWithEmailAndPassword = function() {};
// INCOMPLETE

// --------------------------------------------------------------------------------
// https://firebase.google.com/docs/reference/js/firebase.User
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
// https://firebase.google.com/docs/reference/js/firebase.database.Database
var database = {};
database.ref = function(path) {};
database.refFromUrl = function(url) {};
database.goOffline = function() {};
database.goOnline = function() {};

