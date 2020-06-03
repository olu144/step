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
function LoadComments(){
  fetch('/data').then(response => response.json()).then((comments) => {
  const commentListElement = document.getElementById('comment-list');
  commentListElement.innerHTML = ' ';
  comments.forEach((comment)=>{
  commentListElement.appendChild(createListElement(comment));
    });
  });
}

//Creates an element that represents a comment
function createListElement(comment){
    const commentListElement = document.createElement('li');
    commentListElement.innerText = comment.comment
    return commentListElement;
}
