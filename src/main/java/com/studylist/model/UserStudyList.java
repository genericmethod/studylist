package com.studylist.model;

public class UserStudyList {

  private String email;
  private String list;
  private Long examDate;

  public UserStudyList(String email, String studyList, Long examDate) {
    this.email = email;
    this.list = studyList;
    this.examDate = examDate;
  }

  public String getEmail() {
    return email;
  }

  public String getList() {
    return list;
  }

  public Long getExamDate() {
    return examDate;
  }


}
