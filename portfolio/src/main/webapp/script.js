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
async function loadComments() {
  const loggedIn = await isLoggedIn();
  if (loggedIn) {
    const max = getMax();
    fetch('/data?numCommentsToLoad=' + max).then(response => response.json()).then((comments) => {
    const commentListElement = document.getElementById('comment-list');
    commentListElement.innerHTML = '';
    comments.forEach((comment) => {
      commentListElement.appendChild(createListElement(comment));
      });
    });
  } else {
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

/** Creates a chart and adds it to the page. */
function drawChart() {
  const data = new google.visualization.DataTable();
  data.addColumn('string', 'Animal');
  data.addColumn('number', 'Count');
  data.addRows([
    ['Lions', 10],
    ['Tigers', 5],
    ['Bears', 15],
    ['Elephants', 4],
    ['Snakes', 8]
  ]);
  const options = {
    'title': 'Zoo Animals',
    'pieHole': 0.4,
    'width':500,
    'height':400
  };
  const chart = new google.visualization.PieChart(document.getElementById('chart-div'));
  chart.draw(data, options);
}

function drawChart2() {
  var data = new google.visualization.DataTable();
  data.addColumn('number', 'Week');
  data.addColumn('number', 'Stock A');
  data.addColumn('number', 'Stock B');
  data.addRows([
    [1,  50, 70],
    [2,  130, 100],
  ]);
  var options = {
    chart: {
      title: 'Weekly Performcance of Stock A and Stock B',
      subtitle: 'in dollars (USD)'
    },
    width: 900,
    height: 500
  };
  var chart = new google.charts.Line(document.getElementById('linechart_material'));
  chart.draw(data, google.charts.Line.convertOptions(options));
}

function loadChartsApi() {
  google.charts.load('current', {'packages':['corechart']});
  google.charts.setOnLoadCallback(drawChart);
}

function loadChartsApi2() {
  google.charts.load('current', {'packages':['line']});
  google.charts.setOnLoadCallback(drawChart2);
}
