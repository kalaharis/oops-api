package com.baz.oops.api.util;

import com.baz.oops.api.exceptions.BadRequestParamException;
import com.baz.oops.service.enums.State;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by arahis on 6/13/17.
 */
@Slf4j
public class ParametersHandler {

    public static void checkTags(String[] tags) throws BadRequestParamException {
        if (tags.length < 2) {
            throw new BadRequestParamException();
        }
    }

    public static void checkState(String state) throws BadRequestParamException {
        try {
            State.valueOf(state.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException ex) {
            throw new BadRequestParamException();
        }
    }

    public static Date checkDateFormat(String date) throws BadRequestParamException {
        Date d = null;
        log.debug("input: "+ date);
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssXXX");
            df.setTimeZone(TimeZone.getTimeZone("UTC+00:00"));
            df.setLenient(false);
            d = df.parse(date);
            if (d == null) {
                throw new NullPointerException();
            }
            log.debug("output: "+ df.format(d));
        } catch (NullPointerException | ParseException ex) {
            throw new BadRequestParamException();
        }
        return d;
    }
}
