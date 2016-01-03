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

  public String getEmailBodyString() {

    StringBuilder emailBody = new StringBuilder();
    emailBody.append(this.topic).append("<br/><br/>");

    for (int index = 0; index < this.getStudyList().getList().size(); index ++) {
      String listItem = getStudyList().getList().get(index);
      if (index == missingListIdx) {
        emailBody.append(index+1).append(" ??? ").append("<br/>");
      } else {
        emailBody.append(index+1).append(".").append(listItem).append("<br/>");
      }
    }
    emailBody.append("<br/><br/>").append("Answer: ").append(this.getAnswer());

    return emailBody.toString();
  }
}
