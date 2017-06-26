package com.baz.oops.service.exceptions;

/**
 * Created by arahis on 6/24/17.
 */
public class PollNotFoundException extends ServiceException{

    private final static String DEFAULT_MESSAGE = "Poll not found";

    public PollNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public PollNotFoundException(String message) {
        super(message);
    }

    public PollNotFoundException() {
        super(DEFAULT_MESSAGE);
    }
}
