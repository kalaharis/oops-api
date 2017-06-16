package com.baz.oops.api.util;

import com.baz.oops.api.exceptions.BadRequestParamException;
import com.baz.oops.service.enums.State;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by arahis on 6/13/17.
 */
@Slf4j
public class ParametersHandler {

    public static boolean isTagsValid(List<String> tags) {
        if (tags == null) {
            return false;
        }
        return true;
    }

    public static boolean isStateValid(String state) {
        try {
            State.valueOf(state.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException ex) {
            return false;
        }
        return true;
    }

    public static boolean isDateValid(String date) {
        Date d = null;
        log.debug("input: " + date);
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssXXX");
            df.setTimeZone(TimeZone.getTimeZone("UTC+00:00"));
            df.setLenient(false);
            d = df.parse(date);
            if (d == null) {
                throw new NullPointerException();
            }
            log.debug("output: " + df.format(d));
        } catch (NullPointerException | ParseException ex) {
            return false;
        }
        return true;
    }
}
