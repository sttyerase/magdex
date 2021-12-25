package com.dbmi.demos.magdex;

import  org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MagdexApplication {

    private static final Logger myLog = LoggerFactory.getLogger(MagdexApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(MagdexApplication.class, args);
    } // MAIN(STRING[])

} // CLASS
