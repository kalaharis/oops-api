package com.baz.oops.api;

import com.baz.oops.api.JSON.CreatePollRequest;
import com.baz.oops.api.spring.PollsFiler;
import com.baz.oops.persistence.PollsRepository;
import com.baz.oops.service.PollService;
import com.baz.oops.service.model.Option;
import com.baz.oops.service.model.Poll;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import javax.ws.rs.Produces;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by arahis on 6/9/17.
 */
@Slf4j
@RestController
@RequestMapping(value = "/polls")
public class ListPollsController {

    private PollService pollService;

    @Autowired
    private PollsRepository pollsRepository; //TODO: remove

    @Autowired
    public ListPollsController(PollService pollService) {
        this.pollService = pollService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity listPolls(Pageable pageable, PollsFiler filter) {
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
        return new ResponseEntity(poll, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity showPoll(@PathVariable("id") long id) {
        if (id <= 0) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        Poll poll = pollService.getById(id);
        return new ResponseEntity(poll, HttpStatus.OK);
    }

}
