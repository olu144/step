package com.google.sps.servlets;

public class Comment {
  private long id;
  private String comment;
  private long timestamp;
  private String email;

  public Comment(long id, String comment, long timestamp, String email) {
    this.id = id;
    this.comment = comment;
    this.timestamp = timestamp;
    this.email = email;
  }
}
