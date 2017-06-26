package com.baz.oops.api;

import com.baz.oops.api.JSON.CreatePollRequest;
import com.baz.oops.api.JSON.ErrorResponse;
import com.baz.oops.api.spring.PollsFilter;
import com.baz.oops.persistence.PollsRepository;
import com.baz.oops.service.PollService;
import com.baz.oops.service.exceptions.PollNotFoundException;
import com.baz.oops.service.exceptions.PollVotingException;
import com.baz.oops.service.exceptions.ServiceException;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

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
    public PollsController(PollService pollService) {
        this.pollService = pollService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity listPolls(Pageable pageable, PollsFilter filter) {
        Page<Poll> polls = pollService.listAllByPage(pageable, filter);
        return new ResponseEntity(polls, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity createPoll(@RequestBody(required = false) CreatePollRequest req) {
        try {
            Poll poll = pollService.createPoll(req);
            return new ResponseEntity(poll, HttpStatus.CREATED);
        } catch (ServiceException ex) {
            ErrorResponse err = new ErrorResponse(400, ex.getMessage());
            return new ResponseEntity(err, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity showPoll(@PathVariable("id") String id) {

        try {
            Poll poll = pollService.getById(id);
            return new ResponseEntity(poll, HttpStatus.OK);
        } catch (PollNotFoundException ex) {
            ErrorResponse err = new ErrorResponse(404, ex.getMessage());
            return new ResponseEntity(err, HttpStatus.NOT_FOUND);
        }catch (ServiceException ex){
            ErrorResponse err = new ErrorResponse(500, ex.getMessage());
            return new ResponseEntity(err, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity vote(@PathVariable("id") String id,
                               @RequestParam(value = "vote", required = true) Set<Integer> indexes) {

        try {
            Poll poll = pollService.vote(id, indexes);
            return new ResponseEntity(poll, HttpStatus.OK);
        } catch (PollNotFoundException ex) {
            ErrorResponse err = new ErrorResponse(404, ex.getMessage());
            return new ResponseEntity(err, HttpStatus.NOT_FOUND);
        } catch (PollVotingException ex) {
            ErrorResponse err = new ErrorResponse(400, ex.getMessage());
            return new ResponseEntity(err, HttpStatus.BAD_REQUEST);

        } catch (ServiceException ex) {
            ErrorResponse err = new ErrorResponse(500, ex.getMessage());
            return new ResponseEntity(err, HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

}
