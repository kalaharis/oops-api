package com.baz.oops.service;

import com.baz.oops.api.filters.PollsFiler;
import com.baz.oops.service.model.Option;
import com.baz.oops.service.model.Poll;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

/**
 * Created by arahis on 6/11/17.
 */
public interface PollService {

    Page<Poll> listAllByPage(Pageable pageable, PollsFiler filter);

    Poll createPoll(String name, List<Option> options);

    Poll getById(long id);
}
