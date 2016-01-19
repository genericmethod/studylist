package com.studylist;

import com.studylist.model.Question;

public class Email {

  private Question question;
  private Long examDate;
  private String emailAddress;

  public Email(Question question, Long examDate, String email) {
    this.question = question;
    this.examDate = examDate;
    this.emailAddress = email;
  }

  public String getEmailBody() {

    StringBuilder emailBody = new StringBuilder();
    emailBody.append(question.getTopic()).append("<br/><br/>");

    for (int index = 0; index < question.getStudyList().getList().size(); index ++) {
      String listItem = question.getStudyList().getList().get(index);
      if (index == question.getMissingListIdx()) {
        emailBody.append(index+1).append("____________ ? ").append("<br/>");
      } else {
        emailBody.append(index+1).append(".").append(listItem).append("<br/>");
      }
    }
    emailBody.append("<br/><br/>").append("Answer: ").append(question.getAnswer());
    emailBody.append("<br/><br/>").append("-daysLeft- days left!");

    return emailBody.toString();
  }


  public Long getExamDate() {
    return examDate;
  }

  public String getEmailAddress() {
    return emailAddress;
  }
}
