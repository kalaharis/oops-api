package com.baz.oops.api;

import com.baz.oops.api.JSON.CreatePollRequest;
import com.baz.oops.api.spring.PollsFilter;
import com.baz.oops.persistence.PollsRepository;
import com.baz.oops.service.PollService;
import com.baz.oops.service.model.Option;
import com.baz.oops.service.model.Poll;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by arahis on 6/9/17.
 */
@Slf4j
@RestController
@RequestMapping(value = "/polls")
public class PollsController {

    private PollService pollService;

    @Autowired
    private PollsRepository pollsRepository; //TODO: remove

    @Autowired
    public PollsController(PollService pollService) {
        this.pollService = pollService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity listPolls(Pageable pageable, PollsFilter filter) {
        Page<Poll> polls = pollService.listAllByPage(pageable, filter);
        return new ResponseEntity(polls, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity createPoll(@RequestBody CreatePollRequest req) {
        String name = req.getName();
        List<Option> options = req.getOptions();
        if (name.equals("")) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        if (options.size() < 2) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        Poll poll = pollService.createPoll(name, options);
        if (poll == null) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity(poll, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity showPoll(@PathVariable("id") String id) {

        Poll poll = pollService.getById(id);
        if (poll == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity(poll, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/options/{indx}/vote", method = RequestMethod.PUT)
    public ResponseEntity vote(@PathVariable("id") String id,
                               @PathVariable("indx") int optionIndx) {
        if (optionIndx < 0) {
            log.warn("negative option index in path");
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        Poll poll = pollService.vote(id, optionIndx);
        if (poll == null) {
            log.warn("poll or poll's option not found");
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(poll, HttpStatus.OK);
    }

}
