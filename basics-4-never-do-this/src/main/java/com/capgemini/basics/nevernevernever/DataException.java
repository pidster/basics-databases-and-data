package com.capgemini.basics.nevernevernever;

public class DataException extends Exception {

    public DataException(String message) {
        super(message);
    }

    public DataException(Throwable cause) {
        super(cause);
    }
}
