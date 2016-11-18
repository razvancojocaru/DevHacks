// Initialize Firebase
var config = {
    apiKey: "AIzaSyBdG8UT9PC-83GWXnnAMupvLfdoGrMZfy0",
    authDomain: "devhacks-eb8fc.firebaseapp.com",
    databaseURL: "https://devhacks-eb8fc.firebaseio.com",
    storageBucket: "devhacks-eb8fc.appspot.com",
    messagingSenderId: "156241042556"
};
firebase.initializeApp(config);

// FIREBASE AUTH USING EMAIL AND PASSWORD
$( ".signIn" ).click(function() {
    var credentials = $( "form" ).serializeArray();
    var email = credentials[1].value;
    var password = credentials[2].value;
    firebase.auth().signInWithEmailAndPassword(email, password).then(function() {
        console.log("User successfully signed In");
        window.location.replace("main.html");
    }).catch(function(error) {
      // Handle Errors here.
      var errorCode = error.code;
      var errorMessage = error.message;
      console.log("Sign In error");
    });
});

$( ".signUp" ).click(function() {
    var credentials = $( "form" ).serializeArray();
    var email = credentials[1].value;
    var password = credentials[2].value;
    firebase.auth().createUserWithEmailAndPassword(email, password).catch(function(error) {
      // Handle Errors here.
      var errorCode = error.code;
      var errorMessage = error.message;
      console.log(errorCode, errorMessage);
      // ...
    });
});

$( ".signOut" ).click(function() {
    var signOut = firebase.auth().signOut().then(function() {
        window.location.replace("index.html");
    });
    console.log(signOut);
});

// FIREBASE DATABASE MANIPULATION

var database = firebase.database();

$( ".getUsers" ).click(function() {
    var userId = firebase.auth().currentUser.uid;
    var path = firebase.database().ref('users/' + userId).once('value').then(function(snapshot){
        console.log(snapshot.val());
    });
});
