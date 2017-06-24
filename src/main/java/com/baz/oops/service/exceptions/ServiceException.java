package com.baz.oops.service.exceptions;

/**
 * Created by arahis on 6/23/17.
 */
public class ServiceException extends Exception {
    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceException(String message) {
        super(message);
    }
}
