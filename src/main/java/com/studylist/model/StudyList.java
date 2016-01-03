package com.studylist.model;

import java.util.List;

/**
 * This class stores a list of items represented as strings.
 * Each study list is also assigned a title.
 */
public class StudyList{

  private String listTitle;
  private List<String> list;

  public StudyList(String listTitle, List<String> list) {
    this.list = list;
    this.listTitle = listTitle;
  }

  public String getListTitle() {
    return listTitle;
  }

  public List<String> getList() {
    return list;
  }
}
