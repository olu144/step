package com.google.sps.servlets;
public class Comment {
  public long id;
  public String comment;
  public long timestamp;

  public Comment(long id, String comment, long timestamp){
    this.id=id;
    this.comment=comment;
    this.timestamp=timestamp;
  }
}
