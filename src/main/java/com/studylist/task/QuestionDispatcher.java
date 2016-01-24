package com.studylist.task;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.sendgrid.SendGrid;
import com.sendgrid.SendGridException;
import com.sendgrid.smtpapi.SMTPAPI;
import com.studylist.Email;
import com.studylist.model.Question;
import com.studylist.model.StudyList;
import com.studylist.model.UserStudyList;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Interval;
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
  private List<String> motivationalQuotes;
  private Gson gson = new Gson();

  @PostConstruct
  public void init() throws IOException {
    studyListMap = loadStudyLists();
    userStudyLists = loadUserStudyList();
    motivationalQuotes = loadMotivationalQuotes();
    log.info("**** Question dispatcher initialised");
  }

  @Scheduled(cron = "0 0 7,13,21 * * *")
  public void dispatch() {

      for (UserStudyList userStudyList : userStudyLists) {
        final List<StudyList> studyList = studyListMap.get(userStudyList.getList());
        StudyList randomList = getRandomStudyList(studyList);
        Question question = buildQuestion(randomList);
        Email email = new Email(question, userStudyList.getExamDate(), userStudyList.getEmail());
        sendEmail(email);
        log.info("**** Question sent!");
      }
  }

  private StudyList getRandomStudyList(List<StudyList> studyList) {
    if (studyList.size() > 1){
      final int randomListIndex = randomGenerator.nextInt(studyList.size() - 1);
      return studyList.get(randomListIndex);
    }

    return studyList.get(0);
  }

  private String getRandomMotivationalQuote(){
    if (motivationalQuotes.size() > 1) {
      final int randomIndex = randomGenerator.nextInt(motivationalQuotes.size() - 1);
      return motivationalQuotes.get(randomIndex);
    }

    return motivationalQuotes.get(0);
  }

  private void sendEmail(Email emailData) {

    SendGrid sendgrid = new SendGrid("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");

    SendGrid.Email email = new SendGrid.Email();
    email.addTo(emailData.getEmailAddress());
    email.setFrom("studylist.io");
    email.setSubject(getRandomMotivationalQuote());
    email.setHtml(emailData.getEmailBody());
    email.setTemplateId("eff8b124-455d-439e-afe3-d801b1b0fed3");
    email.addSubstitution("-daysLeft-", new String[]{String.valueOf(Days.daysBetween(new DateTime(), new DateTime(emailData.getExamDate())).getDays())});

    try {
      sendgrid.send(email);
    } catch (SendGridException e) {
      System.out.println(e);
    }
  }

  public Question buildQuestion(StudyList originalList) {
    if(originalList.getList().size() > 1){
      final int randomIndex = randomGenerator.nextInt(originalList.getList().size() - 1);
      return new Question(originalList, originalList.getListTitle(), randomIndex, originalList.getAnswer());
    }

    return new Question(originalList, originalList.getListTitle(), 0, originalList.getAnswer());
  }

  @SuppressWarnings("unchecked")
  public Map<String, List<StudyList>> loadStudyLists() throws IOException {

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

  public List<UserStudyList> loadUserStudyList() throws IOException {
    String jsonStudyList =
            IOUtils.toString(resourceLoader.getResource("classpath:studylist/user-studylist.json").getInputStream());
    Type listType = new TypeToken<ArrayList<UserStudyList>>() {}.getType();
    Gson gson = new Gson();
    return gson.fromJson(jsonStudyList, listType);
  }

  public List<String> loadMotivationalQuotes() throws IOException {
    String jsonStudyList =
            IOUtils.toString(resourceLoader.getResource("classpath:studylist/motivation.json").getInputStream());
    Type listType = new TypeToken<ArrayList<String>>() {}.getType();
    Gson gson = new Gson();
    return gson.fromJson(jsonStudyList, listType);
  }

}
