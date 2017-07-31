package com.baz.oops.service.reqhandlers;

import com.baz.oops.api.JSON.CreatePollRequest;
import com.baz.oops.service.exceptions.PollCreationException;
import com.baz.oops.service.exceptions.ServiceException;
import com.baz.oops.service.model.Option;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by arahis on 6/23/17.
 */
public class CreatePollRequestHandler {

    public static String getNameFromRequest(CreatePollRequest req) throws ServiceException {
        String name = req.getName();
        if (name == null) {
            throw new PollCreationException("Cannot create poll: no \"name\" found");
        }
        if (name.trim().isEmpty()) {
            throw new PollCreationException("Cannot create poll: not valid name for poll");
        }
        return name;
    }

    public static List<Option> getOptionsFromRequest(CreatePollRequest req) throws ServiceException {
        List<Option> options = req.getOptions();
        if (options == null) {
            throw new PollCreationException("Cannot create poll: no \"options\" found");
        }
        if (options.size() < 2) {
            throw new PollCreationException("Cannot create poll: there should be at least 2 options");
        }
        return options;
    }

    public static Date getExpireDateFromRequest(CreatePollRequest req) throws ServiceException {
        Date expireDate = ParametersHandler.getDateFromString(req.getExpireDate());
        return expireDate;
    }
}
