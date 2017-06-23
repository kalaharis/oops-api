package com.baz.oops.api.functional;

import com.baz.oops.api.JSON.CreatePollRequest;
import com.baz.oops.persistence.PollsRepository;
import com.baz.oops.service.PollService;
import com.baz.oops.service.enums.State;
import com.baz.oops.service.exceptions.ServiceException;
import com.baz.oops.service.model.Option;
import com.baz.oops.service.model.Poll;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.test.annotation.Timed;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.TimeZone;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by arahis on 6/22/17.
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ExpiringPollTests {

    private final int ONE_SECOND = 1000;

    @LocalServerPort
    private int port;

    @Autowired
    private PollsRepository pollsRepository;
    @Autowired
    private PollService pollService;

    private DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
    private RestTemplate client = new RestTemplate();

    private Poll savedPoll;


    @Before
    public void init() throws ServiceException {
        pollsRepository.deleteAll();

        List<Option> options = new ArrayList<>();
        options.add(new Option("1"));
        options.add(new Option("2"));
        options.add(new Option("3"));

        CreatePollRequest createReq =
                new CreatePollRequest("In how many seconds this poll will expire", options);

        Date currentTime = new Date();
        Date timeInThreeSeconds = new Date(currentTime.getTime() + ONE_SECOND * 3);
        createReq.setExpireDate(df.format(timeInThreeSeconds));

        savedPoll = pollService.createPoll(createReq);
    }


    @Test
    public void shouldBecomeClosedAfterThreeSeconds() throws InterruptedException {
        Thread.sleep(ONE_SECOND * 4);

        String existingId = savedPoll.getPublicId();

        ResponseEntity<Poll> response = client.exchange(
                getUriForEndPoint("polls/" + existingId),
                HttpMethod.GET,
                null,
                Poll.class);

        Poll poll = pollsRepository.findOne(savedPoll.getPrivateId());
        Assert.assertEquals(State.CLOSED, poll.getState());
    }

    @Test
    public void shouldBeStillOpenAfterOneSecond() throws InterruptedException {
        Thread.sleep(ONE_SECOND);

        String existingId = savedPoll.getPublicId();

        ResponseEntity<Poll> response = client.exchange(
                getUriForEndPoint("polls/" + existingId),
                HttpMethod.GET,
                null,
                Poll.class);

        Poll poll = pollsRepository.findOne(savedPoll.getPrivateId());
        Assert.assertEquals(State.OPEN, poll.getState());
    }

    private String getUriForEndPoint(String endpoint) {
        return "http://localhost:" + port + "api/" + endpoint;
    }
}
