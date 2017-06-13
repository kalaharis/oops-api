package com.baz.oops.api.util;

import com.baz.oops.api.exceptions.BadRequestParamException;

import org.junit.Test;

import java.util.Date;

/**
 * Created by arahis on 6/13/17.
 */
public class ParametersHandlerTests {

    @Test(expected = BadRequestParamException.class)
    public void checkTags_NotEnoughTags_ExceptionThrown() throws BadRequestParamException {
        String[] tags = {"one"};
        ParametersHandler.checkTags(tags);
    }

    @Test
    public void checkTags_EnoughTags_NoExceptionThrown() throws BadRequestParamException {
        String[] tags = {"one", "two"};
        ParametersHandler.checkTags(tags);
    }

    @Test
    public void checkState_ValidStateLowerCase_NoExceptionThrown() throws BadRequestParamException {
        String state = "open";
        ParametersHandler.checkState(state);
    }

    @Test
    public void checkState_ValidStateUpperCase_NoExceptionThrown() throws BadRequestParamException {
        String state = "OPEN";
        ParametersHandler.checkState(state);
    }

    @Test(expected = BadRequestParamException.class)
    public void checkState_NotValidState_ExceptionThrown() throws BadRequestParamException {
        String state = "meow";
        ParametersHandler.checkState(state);
    }

    @Test
    public void checkDateFormat_ValidDateFormat_NoExceptionThrown() throws BadRequestParamException {
        String date = "2017-06-21T06:34:00Z";
        ParametersHandler.checkDateFormat(date);

    }

    @Test(expected = BadRequestParamException.class)
    public void checkDateFormat_NotValidDateFormat_ExceptionThrown() throws BadRequestParamException {
        String date = "2017-JUN-22";
        ParametersHandler.checkDateFormat(date);
    }

    @Test(expected = BadRequestParamException.class)
    public void checkDateFormat_ValidDateFormatAndNotValidDate_NoExceptionThrown()
            throws BadRequestParamException {
        String date = "2017-54-54T25:34:00Z";
        ParametersHandler.checkDateFormat(date);
    }
}
