package com.studylist.model;

public class UserStudyList {

  private String email;
  private String list;

  public UserStudyList(String email, String studyList) {
    this.email = email;
    this.list = studyList;
  }

  public String getEmail() {
    return email;
  }

  public String getList() {
    return list;
  }
}
