package com.baz.oops.service;

import com.baz.oops.api.spring.PollsFilter;
import com.baz.oops.service.model.Option;
import com.baz.oops.service.model.Poll;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by arahis on 6/11/17.
 */
public interface PollService {

    Page<Poll> listAllByPage(Pageable pageable, PollsFilter filter);

    Poll createPoll(String name, List<Option> options);

    Poll getById(long id);

    Poll vote(long id, int optionIndx);
}
