package com.baz.oops.service.exceptions;

/**
 * Created by arahis on 6/22/17.
 */
public class PollVotingException extends ServiceException {

    public PollVotingException(String message, Throwable cause) {
        super(message, cause);
    }

    public PollVotingException(String message) {
        super(message);
    }
}
