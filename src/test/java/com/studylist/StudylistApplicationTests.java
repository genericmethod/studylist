package com.studylist;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.studylist.model.StudyList;
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

import static org.junit.Assert.assertEquals;

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
            Arrays.asList("Anterior / posterior dislocation",
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
            "    \"list\": [\"Anterior / posterior dislocation\", \"# greater tuberosity\", \"ACJ subluxation / dislocation\", \"# rib\", \"Pneumothorax\", \"Pulmonary mass\", \"NG lung apex\", \"Clavicle #\"]\n" +
            "}, {\n" +
            "    \"listTitle\": \"Shoulder Rapids Review\",\n" +
            "    \"list\": [\"Anterior / posterior dislocation\", \"# greater tuberosity\", \"ACJ subluxation / dislocation\", \"# rib\", \"Pneumothorax\", \"Pulmonary mass\", \"NG lung apex\", \"Clavicle #\"]\n" +
            "}]";

    Type listType = new TypeToken<ArrayList<StudyList>>() {
    }.getType();
    gson.fromJson(json, listType);

  }

  @Test
  public void loadStudyList() throws IOException {
    final List<StudyList> studyLists = questionDispatcher.loadStudyList();
    assertEquals(7,studyLists.size());
  }

}
