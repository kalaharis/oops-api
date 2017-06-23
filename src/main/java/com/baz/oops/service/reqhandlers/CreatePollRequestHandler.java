package com.baz.oops.service.reqhandlers;

import com.baz.oops.api.JSON.CreatePollRequest;
import com.baz.oops.api.util.ParametersHandler;
import com.baz.oops.service.exceptions.PollCreationException;
import com.baz.oops.service.exceptions.ServiceException;
import com.baz.oops.service.model.Option;

import org.springframework.util.StringUtils;

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
            throw new PollCreationException("No \"name\" parameter found", null);
        }
        if (name.trim().isEmpty()) {
            throw new PollCreationException("Not valid name for poll", null);
        }
        return name;
    }

    public static List<Option> getOptionsFromRequest(CreatePollRequest req) throws ServiceException {
        List<Option> options = req.getOptions();
        if (options == null) {
            throw new PollCreationException("No \"options\" parameter found", null);
        }
        if (options.size() < 2) {
            throw new PollCreationException("There should be at least 2 options", null);
        }
        return options;
    }

    public static Set<String> getTagsFromRequest(CreatePollRequest req) throws ServiceException {
        Set<String> tags = req.getTags();
        return tags;
    }

    public static Date getExpireDateFromRequest(CreatePollRequest req) throws ServiceException {
        Date expireDate = ParametersHandler.getDateFromString(req.getExpireDate());
        return expireDate;
    }
}
