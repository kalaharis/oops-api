package com.baz.oops.service.impl;

import com.baz.oops.api.filters.PollsFiler;
import com.baz.oops.persistence.PollsRepository;
import com.baz.oops.service.PollService;
import com.baz.oops.service.enums.State;
import com.baz.oops.service.model.Option;
import com.baz.oops.service.model.Poll;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by arahis on 6/11/17.
 */
@Service
public class PollServiceImpl implements PollService {

    private PollsRepository pollsRepository;

    @Autowired
    public PollServiceImpl(PollsRepository pollsRepository) {
        this.pollsRepository = pollsRepository;
    }

    @Override
    public Page<Poll> listAllByPage(Pageable pageable, PollsFiler filter) {
        return pollsRepository.findAll(filter, pageable);
    }

    @Override
    public Poll createPoll(String name, List<Option> options) {
        Poll poll = new Poll(name);
        poll.addOptions(options);
        Poll createdPoll = pollsRepository.save(poll);
        return createdPoll;
    }

    @Override
    public Poll getById(long id) {
        Poll poll = pollsRepository.findOne(id);
        return poll;
    }
}
