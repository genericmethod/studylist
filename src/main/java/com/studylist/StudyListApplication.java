package com.studylist;

import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StudyListApplication {

    private static final Logger log = Logger.getLogger(StudyListApplication.class);

    public static void main(String[] args) {
        log.info("**** Starting Application");
        SpringApplication.run(StudyListApplication.class, args);
        log.info("**** Application Started");
    }
}
