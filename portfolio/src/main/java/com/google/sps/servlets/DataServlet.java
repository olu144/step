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
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;


/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Query query = new Query("Comment").addSort("timestamp", SortDirection.DESCENDING);
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);
    ArrayList<Comment>comments = new ArrayList<>();
    int numCommentsToLoad = getNumCommentsToLoad(request);
    for (Entity commentEntity : results.asIterable()) {
      if (comments.size() < numCommentsToLoad) {
        long tempId = (long) commentEntity.getKey().getId();
        String tempComment = (String) commentEntity.getProperty("comment");
        long tempTimestamp = (long) commentEntity.getProperty("timestamp");
        String email = (String) commentEntity.getProperty("email");
        if (tempComment != null && tempComment.strip() != "") {
          Comment comment = new Comment(tempId, tempComment, tempTimestamp, email);
          comments.add(comment);
        }
      }
    }
    Gson gson = new Gson();
    response.setContentType("application/json;");
    response.getWriter().println(gson.toJson(comments));
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    UserService userService = UserServiceFactory.getUserService();
    if (userService.isUserLoggedIn()) {
      String comment = request.getParameter("comment");
      long timestamp = System.currentTimeMillis();
      String email = userService.getCurrentUser().getEmail();
      Entity commentEntity = new Entity("Comment");
      commentEntity.setProperty("comment", comment);
      commentEntity.setProperty("timestamp", timestamp);
      commentEntity.setProperty("email", email);
      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
      datastore.put(commentEntity);
      response.sendRedirect("comments.html");
    } else {
      response.sendRedirect("/_ah/login?continue=%2Fcomments.html");  
    }
  }

  private int getNumCommentsToLoad(HttpServletRequest request) {
    // Get the input from the form.
    String numCommentsToLoadString = request.getParameter("numCommentsToLoad");
    // Convert the input to an int.
    int numCommentsToLoad;
    try {
      numCommentsToLoad = Integer.parseInt(numCommentsToLoadString);
    } catch (NumberFormatException e) {
      System.err.println("Could not convert to int: " + numCommentsToLoadString);
      return -1;
    }
    return numCommentsToLoad;
  }
}
