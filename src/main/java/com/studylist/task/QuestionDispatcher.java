package com.studylist.task;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.sendgrid.SendGrid;
import com.sendgrid.SendGridException;
import com.studylist.model.Question;
import com.studylist.model.StudyList;
import com.studylist.model.UserStudyList;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.PostConstruct;

@Component
public class QuestionDispatcher {

  private Random randomGenerator = new Random();

  private static final Logger log = Logger.getLogger(QuestionDispatcher.class);

  @Autowired
  ResourceLoader resourceLoader;

  private Map<String, List<StudyList>> studyListMap;
  private List<UserStudyList> userStudyLists;
  private Gson gson = new Gson();

  @PostConstruct
  public void init() throws IOException {
    studyListMap = getStudyLists();
    userStudyLists = getUserStudyList();
    log.info("**** Question dispatcher initialised");
  }

  @Scheduled(cron = "0 0 * * * * ")
  public void dispatch() {

      for (UserStudyList userStudyList : userStudyLists) {
        final List<StudyList> studyList = studyListMap.get(userStudyList.getList());
        StudyList randomList = getRandomStudyList(studyList);
        Question question = buildQuestion(randomList);
        String emailAddress = userStudyList.getEmail();
        sendQuestion(question, emailAddress);
        log.info("**** Question sent!");
      }
  }

  private StudyList getRandomStudyList(List<StudyList> studyList) {
    if (studyList.size() < 1){
      final int randomListIndex = randomGenerator.nextInt(studyList.size() - 1);
      return studyList.get(randomListIndex);
    }

    return studyList.get(0);
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
    if(originalList.getList().size() < 1){
      final int randomIndex = randomGenerator.nextInt(originalList.getList().size() - 1);
      return new Question(originalList, originalList.getListTitle(), randomIndex, originalList.getAnswer());
    }

    return new Question(originalList, originalList.getListTitle(), 0, originalList.getAnswer());
  }

  @SuppressWarnings("unchecked")
  public Map<String, List<StudyList>> getStudyLists() throws IOException {

    String[] jsonFileNames = {
            "radiology.rapids.studylist.json",
            "pragmatic.programming.studylist.json"
    };

    Map<String, List<StudyList>> studyList = new HashMap<>();

    for (String jsonFileName : jsonFileNames) {
      String jsonStudyList =
              IOUtils.toString(resourceLoader.getResource("classpath:studylist/"+jsonFileName).getInputStream());
      Type listType = new TypeToken<ArrayList<StudyList>>() {
      }.getType();

      studyList.put(jsonFileName, (List<StudyList>) gson.fromJson(jsonStudyList, listType));
    }
    return studyList;
  }

  public List<UserStudyList> getUserStudyList() throws IOException {
    String jsonStudyList =
            IOUtils.toString(resourceLoader.getResource("classpath:studylist/user-studylist.json").getInputStream());
    Type listType = new TypeToken<ArrayList<UserStudyList>>() {}.getType();
    Gson gson = new Gson();
    return gson.fromJson(jsonStudyList, listType);
  }
}
