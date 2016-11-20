var map;
var markers = [];
var category;
// Initialize Firebase
var config = {
    apiKey: "AIzaSyBdG8UT9PC-83GWXnnAMupvLfdoGrMZfy0",
    authDomain: "devhacks-eb8fc.firebaseapp.com",
    databaseURL: "https://devhacks-eb8fc.firebaseio.com",
    storageBucket: "devhacks-eb8fc.appspot.com",
    messagingSenderId: "156241042556"
};
firebase.initializeApp(config);

$( document ).ready(function() {
    $( "#map-container" ).height($( "#categories" ).height());
    firebase.auth().onAuthStateChanged(function(user) {
        if (user) {
        // User is signed in.
        // GOOGLE MAPS INIT
            map = new google.maps.Map(document.getElementById('map-container'), {
              zoom: 10,
              center: new google.maps.LatLng(44.471689, 26.133213),
              mapTypeId: google.maps.MapTypeId.ROADMAP
            });

            //getCurrentLocation();
            getNGOs('0');
        }
    });

    $( "#signOut" ).click(function() {
        firebase.auth().signOut();
        window.location.replace("./landing_page.html");
    });

});

// FIREBASE DATABASE MANIPULATION
function getNGOs(category) {
    var userId = firebase.auth().currentUser.uid;
    var path = firebase.database().ref('NGOs/').once('value', function(snapshot){
        snapshot.forEach(function(childSnapshot){
            var name = childSnapshot.key;
            var childData = childSnapshot.val();
            var categories = childData.categories;
            var domain = childData.domains;
            var goal = childData.goal;
            var telephone = childData.telephone;
            var website = childData.website;
            var latitude = childData.latitude;
            var longitude = childData.longitude;

            var contentString = '<div id="content">'+
                                    '<div id="siteNotice">'+
                                    '</div>'+
                                    '<div id="bodyContent">'+
                                        '<h4 id="ong_name">' + name + '</h4>' +
                                        '<h5>' + domain + '</h5>' +
                                        '<p class="text-center">' +
                                            goal +
                                        '</p>' +
                                        '<p><span><i class="fa fa-globe" aria-hidden="true"></i><a href="'+ website +'"> ' + website + '</a></span></p>' +
                                        '<p><span><i class="fa fa-phone" aria-hidden="true"></i> ' + telephone + '</span></p>' +
                                        '<button class="btn btn-defautl pull-right" onClick="donate();"><i class="fa fa-shopping-cart" aria-hidden="true"></i></button>' +
                                    '</div>' +
                                '</div>';


            if (category === '0') {
                addMarker(latitude, longitude, contentString);
            }
            else if (categories.hasOwnProperty(category)) {
                addMarker(latitude, longitude, contentString);
            }
        });

        setMapOnAll(map);
    });
}

$( ".category" ).click(function() {
    category = $( this ).attr('id');
    clearMarkers();
    getNGOs(category);
});

function clearMarkers() {
    setMapOnAll(null);
    markers = [];
  }

function setMapOnAll(map) {
    for (var i = 0; i < markers.length; i++) {
      markers[i].setMap(map);
    }
  }

var location;

function displayRoute() {

}


function calcRoute() {
    var directionsService = new google.maps.DirectionsService();
    var directionsDisplay = new google.maps.DirectionsRenderer();

    var currentLocation = getCurrentLocation();

    var request = {
      origin: new google.maps.LatLng(44.434765, 26.097226),
      destination: new google.maps.LatLng(44.424713, 26.070705),
      // Note that Javascript allows us to access the constant
      // using square brackets and a string value as its
      // "property."
      travelMode: google.maps.TravelMode['DRIVING']
    };
    directionsService.route(request, function(response, status) {
    if (status == 'OK') {
      directionsDisplay.setDirections(response);
    }
    });
}

function getCurrentLocation() {
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(function(position) {
        location = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);

        return location;

      }, function() {
        handleLocationError(true, infoWindow, map.getCenter());
      });
    } else {
      // Browser doesn't support Geolocation
      handleLocationError(false, infoWindow, map.getCenter());
    }
}

function handleLocationError(browserHasGeolocation, infoWindow, pos) {
    infoWindow.setPosition(pos);
    infoWindow.setContent(browserHasGeolocation ?
        'Error: The Geolocation service failed.' :
        'Error: Your browser doesn\'t support geolocation.');
}

function addMarker(latitude, longitude, contentString) {

    var infowindow = new google.maps.InfoWindow({
      content: contentString
    });

    var location = new google.maps.LatLng(latitude,longitude);

    marker = new google.maps.Marker({
      position: location,
      map: map
    });

    marker.addListener('click', function() {
          infowindow.open(map, marker);
    });

    markers.push(marker);
}


function donate() {
    console.log("Donate");
    var userId = firebase.auth().currentUser.uid;

    var reference = firebase.database().ref('users/' + userId).push();
    if (!category) {
        $('#wrongCategory').modal();
        return;
    }

    var path = reference.set({
        timestamp: Date.now(),
        donation_category: category,
        donation_location: $( "#ong_name" ).text(),
        donation_status: 'PENDING'
    });
    $('#donationSuccess').modal();
}


var categories = {1: './img/appliances_color.png', 2: './img/clothing_color.png', 3: './img/food_color.png', 4: './img/furniture_color.png', 5: './img/misc_color.png', 6: './img/toys_color.png'};

function getDonations() {
    $( '#history' ).empty();
    var userId = firebase.auth().currentUser.uid;
    var path = firebase.database().ref('users/' + userId).once('value', function(snapshot) {
        snapshot.forEach(function(childSnapshot) {
            var childData = childSnapshot.val();
            var date = new Date(childData.timestamp);
            var newItem = '<li class="list-group-item">' +
                '<div class="row">' +
                    '<div class="col-md-1">' +
                        '<img src="./img/Badges/Badge_1.png" class="badge-image"/>' +
                    '</div>' +
                    '<div class="col-md-3">' +
                        '<h6 class="h5-responsive">' + new Date(childData.timestamp).toString() + '</h6>' +
                    '</div>' +
                    '<div class="col-md-4">' +
                        '<h6 class="h5-responsive">' + childData.donation_location + '</h6>' +
                    '</div>' +
                    '<div class="col-md-2">' +
                        '<img src="' + categories[childData.donation_category] + '" style="height: 50px;">' +
                    '</div>' +
                    '<div class="col-md-2">' +
                        '<h3 class="donation-status"><span class="label label-donation-status">' + childData.donation_status + '</span></h3>' +
                    '</div>' +
                '</div>' +
            '</li>';
            $( '#history' ).append(newItem);

            if (childData.donation_status === 'PENDING') {
                $( ".label-donation-status" ).addClass( 'label-default' );
            }
            else {
                $( ".label-donation-status" ).addClass( 'label-success' );
            }

            console.log(childData);
        });
    });
}

$( "#reloadHistory" ).click(function() {
    getDonations();
});
