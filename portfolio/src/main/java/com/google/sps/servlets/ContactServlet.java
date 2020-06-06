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
import java.util.Arrays;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.gson.Gson;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/contact")
public class ContactServlet extends HttpServlet {

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String name = getParameter(request, "name-input", "");
    String email = getParameter(request, "email-input", "");
    String phone = getParameter(request, "phone-input", "");
    long timestamp = System.currentTimeMillis();
    if(email.strip() == "" && phone.strip()== "") {
      response.setContentType("text/html;");
      response.getWriter().println("You have not entered valid info, please go back and re-fill the form");
      return;
    }
    Entity contactEntity = new Entity("Contact");
    contactEntity.setProperty("Name", name);
    contactEntity.setProperty("E-Mail", email);
    contactEntity.setProperty("Phone", phone);
    contactEntity.setProperty("timestamp", timestamp);
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(contactEntity);
    boolean emailContact = Boolean.parseBoolean(getParameter(request, "email", "false"));
    boolean phoneContact = Boolean.parseBoolean(getParameter(request, "phone", "false"));
    String message="";
    if (!emailContact && !phoneContact){
      message = "Thank You " + name + ", you have not selected how you prefer to be contacted, I will reach out to you at ";
      if (email.strip() != "") {
        message += email;
      } else {
        message += phone;
      }
    }
    if (emailContact) {
      message = "Thank You " + name + ", I will E-mail you at " + email;
    }
    if (phoneContact && !emailContact) {
      message = "Thank You " + name + ", I will text out to you at " + phone;
    }
    if (phoneContact && emailContact) {
      message += ", and I will text you at " + phone;
    }
    response.setContentType("text/html;");
    response.getWriter().println(message);
  }

  private String getParameter(HttpServletRequest request, String name, String defaultValue) {
    String value = request.getParameter(name);
    if (value == null) {
      return defaultValue;
    }
    return value;
  }
}