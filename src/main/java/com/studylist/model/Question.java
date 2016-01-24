package com.studylist.model;


public class Question {

  private StudyList studyList;
  private String topic;
  private Integer missingListIdx;
  private String answer;

  public Question(StudyList studyList, String topic, Integer missingListIdx, String answer) {
    this.studyList = studyList;
    this.topic = topic;
    this.missingListIdx = missingListIdx;
    this.answer = answer;
  }

  public StudyList getStudyList() {return studyList;}
  public String getAnswer() {return answer;}
  public String getTopic() {return topic;}
  public Integer getMissingListIdx() {return missingListIdx;}

}
