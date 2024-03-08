package com.example.testsibs.exception;

public class InvalidFieldsException extends Exception {

    public InvalidFieldsException(){
        super("The data is invalid, so it can't be persisted!");
    }
}
