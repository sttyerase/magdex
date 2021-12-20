package com.dbmi.demos.magdex;

import  org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScidexApplication {

    private static final Logger myLog = LoggerFactory.getLogger(ScidexApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ScidexApplication.class, args);
    } // MAIN(STRING[])

} // CLASS
