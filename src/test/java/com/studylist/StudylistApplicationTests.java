package com.studylist;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.studylist.model.StudyList;
import com.studylist.model.UserStudyList;
import com.studylist.task.QuestionDispatcher;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = StudyListApplication.class)
@WebAppConfiguration
public class StudylistApplicationTests {

  @Autowired
  QuestionDispatcher questionDispatcher;

  @Test
  public void contextLoads() {
  }

  @Test
  public void studyListToGson() {

    StudyList shoulderStudyList = new StudyList("Shoulder Rapids Review",
            "answer", Arrays.asList("Anterior / posterior dislocation",
                    "# greater tuberosity",
                    "ACJ subluxation / dislocation",
                    "# rib",
                    "Pneumothorax",
                    "Pulmonary mass",
                    "NG lung apex",
                    "Clavicle #"));

    List<StudyList> list = new ArrayList<>();
    list.add(shoulderStudyList);
    list.add(shoulderStudyList);

    Gson gson = new Gson();
    final String s = gson.toJson(list);

    String json = "[{\n" +
            "    \"listTitle\": \"Shoulder Rapids Review\",\n" +
            "    \"answer\" : \"answer\",\n" +
            "    \"list\": [\"Anterior / posterior dislocation\", \"# greater tuberosity\", \"ACJ subluxation / dislocation\", \"# rib\", \"Pneumothorax\", \"Pulmonary mass\", \"NG lung apex\", \"Clavicle #\"]\n" +
            "}, {\n" +
            "    \"listTitle\": \"Shoulder Rapids Review\",\n" +
            "    \"answer\" : \"answer\",\n" +
            "    \"list\": [\"Anterior / posterior dislocation\", \"# greater tuberosity\", \"ACJ subluxation / dislocation\", \"# rib\", \"Pneumothorax\", \"Pulmonary mass\", \"NG lung apex\", \"Clavicle #\"]\n" +
            "}]";

    Type listType = new TypeToken<ArrayList<StudyList>>() {}.getType();
    gson.fromJson(json, listType);

  }

  @Test
  public void userStudyListToGson() {

    UserStudyList userStudyList = new UserStudyList("test@email.com","test.json");

    List<UserStudyList> list = new ArrayList<>();
    list.add(userStudyList);
    list.add(userStudyList);

    Gson gson = new Gson();
    final String s = gson.toJson(list);

    String json = "[\n" +
            "  {\n" +
            "    \"email\": \"farrugia.alexia@gmail.com\",\n" +
            "    \"list\": \"radiology.rapids.studylist.json\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"email\": \"cfarrugia@gmail.com\",\n" +
            "    \"list\": \"pragmatic.programming.studylist.json\"\n" +
            "  }\n" +
            "]";

    Type listType = new TypeToken<ArrayList<UserStudyList>>() {}.getType();
    gson.fromJson(json, listType);

  }

  @Test
  public void loadStudyList() throws IOException {
    final Map<String, List<StudyList>> studyListMap = questionDispatcher.loadStudyLists();
    assertEquals(2, studyListMap.size());
  }

  @Test
  public void loadMotivationalQuotes() throws IOException {
    final List<String> motivationalQuotes = questionDispatcher.getMotivationalQuotes();
    assertNotNull(motivationalQuotes);
  }

}
