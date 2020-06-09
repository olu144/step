// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/**
 * Adds a random greeting to the page.
 */
function addRandomGreeting() {
  const greetings =
      ['Hello world!', '¡Hola Mundo!', '你好，世界！', 'Bonjour le monde!', 'Hej Verden!', 'Ciao mondo!', 'Salut Lume!', 'Kaixo Mundua!', 'Halló heimur!', 'Olá Mundo!', 'Salom Dunyo!', 'こんにちは世界！', 'העלא וועלט!', 'Chào thế giới!'];

  // Pick a random greeting.
  const greeting = greetings[Math.floor(Math.random() * greetings.length)];

  // Add it to the page.
  const greetingContainer = document.getElementById('greeting-container');
  greetingContainer.innerText = greeting;
}

function addRandomStock() {
  const stocks =
      ['Amazon.com, Inc (AMZN)', 'Apple Inc (AAPL)', 'Netflix Inc (NFLX)', 'Alphabet Inc Class A (GOOGL)','Facebook, Inc (FB)', 'Johnson & Johnson (JNJ)', 'Nike Inc (NKE)', 'Microsoft (MSFT)', 'Berkshire Hathaway Inc. Class B (BRK.B)', 'Visa (V)', 'Intel (INTC)', 'Verizon (VZ)', 'Pfizer (PFE)', 'PepsiCo (PEP)', 'Advanced Micro Devices Inc. (AMD)', 'Starbucks (SBUX)', 'Waste Management (WM)', 'Costco Wholesale (COST)'];

  // Pick a random stock.
  const stock = stocks[Math.floor(Math.random() * stocks.length)];

  // Add it to the page.
  const stockContainer = document.getElementById('stock-container');
  stockContainer.innerText = stock;
}

// Fetches comments from the server and adds them to the DOM.
async function LoadComments() {
  const loggedIn = await isLoggedIn();
  if(loggedIn) {
    const max = getMax();
    fetch('/data?numCommentsToLoad=' + max).then(response => response.json()).then((comments) => {
    const commentListElement = document.getElementById('comment-list');
    commentListElement.innerHTML = ' ';
    comments.forEach((comment) => {
      commentListElement.appendChild(createListElement(comment));
      });
    });
  }else{
    location.replace("/_ah/login?continue=%2Fcomments.html");
  }
}

// Creates an element that represents a comment
function createListElement(comment) {
  const commentListElement = document.createElement('li');
  commentListElement.innerText = comment.email + " : " + comment.comment
  return commentListElement;
}

function getMax() {
  const maxElement = document.getElementById("maxwant");
  const max = maxElement.options[maxElement.selectedIndex].value;
  return max;
}

function deleteComment() {
  fetch("/delete-list", {method: 'POST'})
}

async function isLoggedIn() {
  const response = await fetch('/login-status',);
  const status = await response.json();
  return status;
}

google.charts.load('current', {'packages':['corechart']});
google.charts.setOnLoadCallback(drawChart);
/** Fetches Amazon Stock data and uses it to create a chart. */
function drawChart() {
  fetch('/amazon-data').then(response => response.json())
  .then((AmazonData) => {
    const data = new google.visualization.DataTable();
    data.addColumn('string', 'Year');
    data.addColumn('number', 'Price ($)');
    Object.keys(AmazonData).forEach((year) => {
      data.addRow([year, AmazonData[year]]);
    });
    const options = {
      'title': 'Amazon Stock Price',
      'width':1200,
      'height':1000
    };
    const chart = new google.visualization.LineChart(
        document.getElementById('chart-container'));
    chart.draw(data, options);
  });
}

google.charts.load('current', {'packages':['corechart']});
google.charts.setOnLoadCallback(drawChart2);
/** Fetches season data and uses it to create a chart. */
function drawChart2() {
  fetch('/season-data').then(response => response.json())
  .then((seasonVotes) => {
    const data = new google.visualization.DataTable();
    data.addColumn('string', 'Season');
    data.addColumn('number', 'Votes');
    Object.keys(seasonVotes).forEach((season) => {
      data.addRow([season, seasonVotes[season]]);
    });
    const options = {
      'title': 'Favorite Seasons',
      'width':600,
      'height':500
    };
    const chart = new google.visualization.ColumnChart(
        document.getElementById('chart2-container'));
    chart.draw(data, options);
  });
}

/** Creates a map and adds it to the page. */
function createMap() {
  const myLatLng = { lat: 36.964, lng: -122.015 };
  const map = new google.maps.Map(document.getElementById('map'), {
    center: myLatLng,
    zoom: 16,
    styles: [
      {elementType: 'geometry', stylers: [{color: '#242f3e'}]},
      {elementType: 'labels.text.stroke', stylers: [{color: '#242f3e'}]},
      {elementType: 'labels.text.fill', stylers: [{color: '#746855'}]},
      {
        featureType: 'administrative.locality',
        elementType: 'labels.text.fill',
        stylers: [{color: '#d59563'}]
      },
      {
        featureType: 'poi',
        elementType: 'labels.text.fill',
        stylers: [{color: '#d59563'}]
      },
      {
        featureType: 'poi.park',
        elementType: 'geometry',
        stylers: [{color: '#263c3f'}]
      },
      {
        featureType: 'poi.park',
        elementType: 'labels.text.fill',
        stylers: [{color: '#6b9a76'}]
      },
      {
        featureType: 'road',
        elementType: 'geometry',
        stylers: [{color: '#38414e'}]
      },
      {
        featureType: 'road',
        elementType: 'geometry.stroke',
        stylers: [{color: '#212a37'}]
      },
      {
        featureType: 'road',
        elementType: 'labels.text.fill',
        stylers: [{color: '#9ca5b3'}]
      },
      {
        featureType: 'road.highway',
        elementType: 'geometry',
        stylers: [{color: '#746855'}]
      },
      {
        featureType: 'road.highway',
        elementType: 'geometry.stroke',
        stylers: [{color: '#1f2835'}]
      },
      {
        featureType: 'road.highway',
        elementType: 'labels.text.fill',
        stylers: [{color: '#f3d19c'}]
      },
      {
        featureType: 'transit',
        elementType: 'geometry',
        stylers: [{color: '#2f3948'}]
      },
      {
        featureType: 'transit.station',
        elementType: 'labels.text.fill',
        stylers: [{color: '#d59563'}]
      },
      {
        featureType: 'water',
        elementType: 'geometry',
        stylers: [{color: '#17263c'}]
      },
      {
        featureType: 'water',
        elementType: 'labels.text.fill',
        stylers: [{color: '#515c6d'}]
      },
      {
        featureType: 'water',
        elementType: 'labels.text.stroke',
        stylers: [{color: '#17263c'}]
      }
    ]
  });
  map.setTilt(45);
  var contentString = '<p>Santa Cruz Boardwalk</p> Amusement park offers a variety of games & rides right on a mile-long stretch of sandy beach. ';
  var infowindow = new google.maps.InfoWindow({
    content: contentString
  });
  const marker = new google.maps.Marker({
    position: myLatLng,
    map: map,
    title: 'Santa Cruz Boardwalk'
  });
  marker.addListener('click', function() {
    infowindow.open(map, marker);
  });
}
