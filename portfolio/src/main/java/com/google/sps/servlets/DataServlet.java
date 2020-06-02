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
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;

import java.util.List;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {
  private ArrayList<String> units = new ArrayList<String>();
  private ArrayList<String> perm = new ArrayList<String>();
  
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    /* Commented out to preserve code
    units.add("feet");
    units.add("inches");
    units.add("meters");
    units.add("miles");
    units.add("pounds");
    response.setContentType("text/html;");
    response.getWriter().println("Hello Olu!");
    String json = convertToJsonUsingGson(units);
    response.setContentType("application/json;");
    response.getWriter().println(json);*/
    String comment = request.getParameter("comment");
    long timestamp = System.currentTimeMillis();
    Entity commentEntity = new Entity("Comment");
    commentEntity.setProperty("comment", comment);
    commentEntity.setProperty("timestamp", timestamp);
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(commentEntity);
    Query query = new Query("Comment");
    PreparedQuery results = datastore.prepare(query);
    ArrayList<Entity> comms = new ArrayList<>();
    for (Entity entity : results.asIterable()) {
      long id = entity.getKey().getId();
      String comment2 = (String) entity.getProperty("comment");
      long timestamp2 = (long) entity.getProperty("timestamp");
      if(comment2!=null){
        Entity tempCommentEntity = new Entity("Comment");
        tempCommentEntity.setProperty("comment", comment2);
        tempCommentEntity.setProperty("timestamp", timestamp2);
        comms.add(tempCommentEntity);
      }
    }
    Gson gson = new Gson();
    response.setContentType("application/json;");
    response.getWriter().println(gson.toJson(comms));
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

  private String convertToJsonUsingGson(ArrayList<String> units) {
    Gson gson = new Gson();
    String json = gson.toJson(units);
    return json;
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    /* Preserved code from step 4
    String name = getParameter(request, "name-input", "");
    String email = getParameter(request, "email-input", "");
    String phone = getParameter(request, "phone-input", "");
    boolean emailContact = Boolean.parseBoolean(getParameter(request, "email", "false"));
    boolean phoneContact = Boolean.parseBoolean(getParameter(request, "phone", "false"));
    String message= "Thank You "+name+" You have not selected how you prefer to be contacted, I will reach out to you at "+email;
    if (emailContact) {
      message= "Thank You "+name+" I will E-mail you at "+email;
    }
    if (phoneContact && !emailContact) {
      message= "Thank You "+name+" I will text out to you at "+phone;
    }
    if (phoneContact && emailContact) {
      message+= ", and I will text you at "+phone;
    }
    response.setContentType("text/html;");
    response.getWriter().println(message);*/
    String comment = request.getParameter("comment");
    long timestamp = System.currentTimeMillis();
    Entity commentEntity = new Entity("Comment");
    commentEntity.setProperty("comment", comment);
    commentEntity.setProperty("timestamp", timestamp);
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(commentEntity);
    response.sendRedirect("comments.html");
  }

  /**
   * @return the request parameter, or the default value if the parameter
   *         was not specified by the client
   */
  private String getParameter(HttpServletRequest request, String name, String defaultValue) {
    String value = request.getParameter(name);
    if (value == null) {
      return defaultValue;
    }
    return value;
  }
}
