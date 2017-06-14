package com.baz.oops.api.util;

import com.baz.oops.api.exceptions.BadRequestParamException;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arahis on 6/13/17.
 */
public class ParametersHandlerTests {

    @Test
    public void checkTags_NotEnoughTags_ReturnsFalse() {
        List<String> tags = new ArrayList<>();
        tags.add("one");
        Assert.assertFalse(ParametersHandler.isTagsValid(tags));
    }

    @Test
    public void checkTags_EnoughTags_ReturnsTrue() {
        List<String> tags = new ArrayList<>();
        tags.add("one");
        tags.add("two");
        Assert.assertTrue(ParametersHandler.isTagsValid(tags));
    }

    @Test
    public void checkState_ValidStateLowerCase_ReturnTrue() {
        String state = "open";
        Assert.assertTrue(ParametersHandler.isStateValid(state));
    }

    @Test
    public void checkState_ValidStateUpperCase_ReturnsTrue() {
        String state = "OPEN";
        Assert.assertTrue(ParametersHandler.isStateValid(state));
    }

    @Test
    public void checkState_NotValidState_ReturnsFalse() {
        String state = "meow";
        Assert.assertFalse(ParametersHandler.isStateValid(state));
    }

    @Test
    public void checkDateFormat_ValidDateFormat_ReturnsTrue() {
        String date = "2017-06-21T06:34:00Z";
        Assert.assertTrue(ParametersHandler.isDateValid(date));

    }

    @Test
    public void checkDateFormat_NotValidDateFormat_ReturnsFalse() {
        String date = "2017-JUN-22";
        Assert.assertFalse(ParametersHandler.isDateValid(date));
    }

    @Test
    public void checkDateFormat_ValidDateFormatAndNotValidDate_ReturnsFalse() {
        String date = "2017-54-54T25:34:00Z";
        Assert.assertFalse(ParametersHandler.isDateValid(date));
    }
}
