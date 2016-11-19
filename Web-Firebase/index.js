// Initialize Firebase
var config = {
    apiKey: "AIzaSyBdG8UT9PC-83GWXnnAMupvLfdoGrMZfy0",
    authDomain: "devhacks-eb8fc.firebaseapp.com",
    databaseURL: "https://devhacks-eb8fc.firebaseio.com",
    storageBucket: "devhacks-eb8fc.appspot.com",
    messagingSenderId: "156241042556"
};
firebase.initializeApp(config);

// FACEBOOK LOGIN
$( ".signIn" ).click(function() {
    if (firebase.auth().currentUser) {
        firebase.auth().signOut();
        $( ".signIn > span" ).text("Facebook");
    } else {
        var provider = new firebase.auth.FacebookAuthProvider();
        firebase.auth().signInWithPopup(provider).then(function(result) {
          // This gives you a Facebook Access Token. You can use it to access the Facebook API.
          var token = result.credential.accessToken;
          // The signed-in user info.
          var user = result.user;
          console.log(user);
          $( ".signIn > span" ).text("Log Out");
          // ...
        }).catch(function(error) {
          // Handle Errors here.
          var errorCode = error.code;
          var errorMessage = error.message;
          // The email of the user's account used.
          var email = error.email;
          // The firebase.auth.AuthCredential type that was used.
          var credential = error.credential;
          // ...
          console.log(errorCode, errorMessage, credential);
        });

    }
});

// FIREBASE DATABASE MANIPULATION

var database = firebase.database();

$( ".getUsers" ).click(function() {
    var userId = firebase.auth().currentUser.uid;
    var path = firebase.database().ref('users/' + userId).once('value').then(function(snapshot){
        console.log(snapshot.val());
    });
});


// GOOGLE MAPS INIT
var map = new google.maps.Map(document.getElementById('map-container'), {
  zoom: 10,
  center: new google.maps.LatLng(-33.92, 151.25),
  mapTypeId: google.maps.MapTypeId.ROADMAP
});

function addMarker(latitude, longitude) {
    var contentString = '<div id="content">'+
                '<div id="siteNotice">'+
                '</div>'+
                    '<h1 id="firstHeading" class="firstHeading">Crucea Rosie Romana</h1>'+
                    '<h2>Copii, batrani</h2>' +
                    '<div id="bodyContent">'+
                        '<p>Societatea Nationala de Cruce Rosie din Romania este o organizatie' +
                        ' umanitara membra a Miscarii Internationale de Cruce Rosie si Semiluna Rosie,' +
                        ' auxiliara autoritatii publice si abilitata prin lege sa asigure asistenta umanitara ' +
                        'in caz de dezastre si sa vina in sprijinul persoanelor vulnerabile. </p>'+
                        '<a class="btn btn-default waves-effect waves-light">Default</a>' +
                    '</div>'+
                '</div>';

    var infowindow = new google.maps.InfoWindow({
      content: contentString
    });

    marker = new google.maps.Marker({
      position: new google.maps.LatLng(latitude,longitude),
      map: map
    });

    marker.addListener('click', function() {
          infowindow.open(map, marker);
        });
}

addMarker(44.471689, 26.133213);


//google.maps.event.addDomListener(window, 'load', init_map);
