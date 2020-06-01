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

package com.google.sps.servlets;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {
  ArrayList<String> units=new ArrayList<String>();
  
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    units.add("feet");
    units.add("inches");
    units.add("meters");
    units.add("miles");
    units.add("pounds");
    response.setContentType("text/html;");
    response.getWriter().println("Hello Olu!");
    String json = convertToJson(units);
    response.setContentType("application/json;");
    response.getWriter().println(json);
  }

  private String convertToJson(ArrayList<String> units) {
    String json = "{";
    json += "\"unit 1\": ";
    json += "\"" + units.get(0) + "\"";
    json += ", ";
    json += "\"unit 2\": ";
    json += "\"" + units.get(1) + "\"";
    json += ", ";
    json += "\"unit 3\": ";
    json += "\"" + units.get(2) + "\"";
    json += ", ";
    json += "\"unit 4\": ";
    json += "\"" + units.get(3) + "\"";
    json += ", ";
    json += "\"unit 5\": ";
    json += "\"" + units.get(4) + "\"";
    json += "}";
    return json;
  }
}


