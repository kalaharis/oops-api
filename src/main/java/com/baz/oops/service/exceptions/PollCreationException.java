package com.baz.oops.service.exceptions;

/**
 * Created by arahis on 6/23/17.
 */
public class PollCreationException extends ServiceException {
    public PollCreationException(String message) {
        super(message);
    }

    public PollCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
