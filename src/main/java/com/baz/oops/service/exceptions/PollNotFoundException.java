package com.baz.oops.service.exceptions;

/**
 * Created by arahis on 6/24/17.
 */
public class PollNotFoundException extends ServiceException{
    public PollNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public PollNotFoundException(String message) {
        super(message);
    }
}
