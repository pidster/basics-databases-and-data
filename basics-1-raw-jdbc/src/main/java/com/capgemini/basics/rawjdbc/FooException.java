package com.capgemini.basics.rawjdbc;

public class FooException extends Exception {

    public FooException(String message) {
        super(message);
    }

    public FooException(Throwable cause) {
        super(cause);
    }
}
