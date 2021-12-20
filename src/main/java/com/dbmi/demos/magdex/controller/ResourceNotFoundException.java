package com.dbmi.demos.magdex.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends Exception {

    public ResourceNotFoundException() {
        this("Unable to find the requested URL.");
    } // DEFAULT CONSTRUCTOR

    public ResourceNotFoundException(String message) {
        super(message);
    } // CONSTRUCTOR(STRING)
} // CLASS
