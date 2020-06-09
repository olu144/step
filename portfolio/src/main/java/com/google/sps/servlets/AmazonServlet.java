package com.google.sps.servlets;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Scanner;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Returns bigfoot data as a JSON object, e.g. {"2017": 52, "2018": 34}] */
@WebServlet("/amazon-data")
public class AmazonServlet extends HttpServlet {
  private LinkedHashMap<String, Float> AmazonData = new LinkedHashMap<>();

  @Override
  public void init() {
    Scanner scanner = new Scanner(getServletContext().getResourceAsStream("/WEB-INF/AMZNtrain.csv"));
    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();
      String[] cells = line.split(",");
      String date = String.valueOf(cells[0]);
      Float open = Float.valueOf(cells[1]);
      Float high = Float.valueOf(cells[2]);
      Float low = Float.valueOf(cells[3]);
      Float close = Float.valueOf(cells[4]);
      Float adjClose = Float.valueOf(cells[5]);
      Float volume = Float.valueOf(cells[6]);
      AmazonData.put(date, adjClose);
    }
    scanner.close();
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    Gson gson = new Gson();
    String json = gson.toJson(AmazonData);
    response.getWriter().println(json);
  }
}