package com.google.sps.servlets;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/season-data")
public class SeasonDataServlet extends HttpServlet {
  private Map<String, Integer> seasonVotes = new HashMap<>();

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    Gson gson = new Gson();
    String json = gson.toJson(seasonVotes);
    response.getWriter().println(json);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String season = request.getParameter("season");
    int currentVotes = seasonVotes.containsKey(season) ? seasonVotes.get(season) : 0;
    seasonVotes.put(season, currentVotes + 1);
    response.sendRedirect("/chart.html");
  }
}
