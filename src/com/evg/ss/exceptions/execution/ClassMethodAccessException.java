package com.evg.ss.exceptions.execution;

public class ClassMethodAccessException extends SSExecutionException {

    public ClassMethodAccessException() {
        super("Unable to change method value.");
    }
}