package com.baz.oops.api;

import com.baz.oops.api.JSON.CreatePollRequest;
import com.baz.oops.api.exceptions.BadRequestParamException;
import com.baz.oops.api.util.ParametersHandler;
import com.baz.oops.persistence.PollsRepository;
import com.baz.oops.service.PollService;
import com.baz.oops.service.enums.State;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;

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


    //TODO: remove
    @PostConstruct
    private void init() {
        for (int i = 0; i < 5; i++) {
            Poll poll = new Poll("test" + i, new Date(), State.OPEN);
            List<Option> options = new ArrayList<>();

            for (int j = 0; j < 3; j++) {
                Option option = new Option("test options" + i);
                options.add(option);
            }

            List<String> tags = new ArrayList<>();
            tags.add("tag" + i);
            tags.add(i + "tag");

            poll.setTags(tags);
            poll.setTotalVotes(i);
            poll.setOptions(options);

            pollsRepository.save(poll);
        }
    }
    //

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity listPolls(Pageable pageable,
                                    @RequestParam(value = "tags", required = false) String[] tags,
                                    @RequestParam(value = "state", required = false) String state,
                                    @RequestParam(value = "start", required = false) String start,
                                    @RequestParam(value = "end", required = false) String end) {
        try {
            if (tags != null) {
                ParametersHandler.checkTags(tags);
            }
            if (state != null) {
                ParametersHandler.checkState(state);
            }
            if (start != null) {
                ParametersHandler.checkDateFormat(start);
            }
            if (end != null) {
                ParametersHandler.checkDateFormat(end);
            }
        } catch (BadRequestParamException ex) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        Page<Poll> polls = pollService.listAllByPage(pageable);
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
