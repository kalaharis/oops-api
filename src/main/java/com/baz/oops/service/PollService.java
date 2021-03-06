package com.baz.oops.service;

import com.baz.oops.api.JSON.CreatePollRequest;
import com.baz.oops.api.spring.PollsFilter;
import com.baz.oops.service.exceptions.ServiceException;
import com.baz.oops.service.model.Poll;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

/**
 * Created by arahis on 6/11/17.
 */
public interface PollService {

    Page<Poll> listAllByPage(Pageable pageable, PollsFilter filter);

    Poll createPoll(CreatePollRequest request) throws ServiceException;

    Poll getById(String id) throws ServiceException;

    Poll vote(String id, Set<Integer> indexes, String ip) throws ServiceException;
}
