package com.studylist.task;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.sendgrid.SendGrid;
import com.sendgrid.SendGridException;
import com.studylist.model.Question;
import com.studylist.model.StudyList;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class QuestionDispatcher {

  private Random randomGenerator = new Random();

  private static final Logger log = Logger.getLogger(QuestionDispatcher.class);

  @Autowired
  ResourceLoader resourceLoader;

  @Scheduled(cron = "0 0 9,15,21 * * * ")
  public void dispatch() {

    try {
      List<StudyList> studyList = loadStudyList();
      StudyList randomList = getRandomStudyList(studyList);
      Question question = buildQuestion(randomList);
      String emailAddress = "farrugia.alexia@gmail.com";
      sendQuestion(question, emailAddress);
      log.info("**** Question sent!");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private StudyList getRandomStudyList(List<StudyList> studyList) {
    final int randomListIndex = randomGenerator.nextInt(studyList.size() - 1);
    return studyList.get(randomListIndex);
  }

  private void sendQuestion(Question question, String emailAddress) {

    SendGrid sendgrid = new SendGrid("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");

    SendGrid.Email invite = new SendGrid.Email();
    invite.addTo(emailAddress);
    invite.setFrom("studylist.io");
    invite.setSubject("Hey! Try this one out! " + new DateTime().toString());
    invite.setHtml(question.getEmailBodyString());

    SendGrid.Response response = null;
    try {
      response = sendgrid.send(invite);
    } catch (SendGridException e) {
      System.out.println(e);
    }
  }

  public Question buildQuestion(StudyList originalList) {
    final int randomIndex = randomGenerator.nextInt(originalList.getList().size() - 1);
    return new Question(originalList, originalList.getListTitle(), randomIndex,
            "http://www.radiologycafe.com/images/documents/Radiologycafe_Rapids_Checklist.pdf");
  }

  public List<StudyList> loadStudyList() throws IOException {

    String jsonStudyList =
            IOUtils.toString(resourceLoader.getResource("classpath:studylist/studylist.json").getInputStream());
    Type listType = new TypeToken<ArrayList<StudyList>>() {
    }.getType();
    Gson gson = new Gson();
    return gson.fromJson(jsonStudyList, listType);
  }
}
