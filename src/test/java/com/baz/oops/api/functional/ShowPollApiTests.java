package com.baz.oops.api.functional;

import com.baz.oops.persistence.PollsRepository;
import com.baz.oops.service.PollService;
import com.baz.oops.service.impl.PollServiceImpl;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.text.ParseException;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by arahis on 6/18/17.
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ShowPollApiTests {

    @Autowired
    private PollsRepository pollsRepository;
    @Autowired
    private PollService pollService;

    @LocalServerPort
    private int port;

    private RestTemplate client = new RestTemplate();

    private Poll savedPoll;

    @Before
    public void init() throws ParseException {
        client.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) throws IOException {
                return false;
            }
        });

        pollsRepository.deleteAll();

        Poll jVsCSharpPoll = new Poll("Java or C#?");
        jVsCSharpPoll.addOption(new Option("Java"));
        jVsCSharpPoll.addOption(new Option("Not C#"));

        savedPoll = pollService.createPoll(jVsCSharpPoll.getName(), jVsCSharpPoll.getOptions());
    }

    @Test
    public void showPoll_ExistingPollIdInPath_ShouldReturn200AndBody() {
        String existingId = savedPoll.getPublicId();

        ResponseEntity<Poll> response = client.exchange(
                getUriForEndPoint("polls/" + existingId),
                HttpMethod.GET,
                null,
                Poll.class);


        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertNotNull(response.getBody());
    }

    @Test
    public void showPoll_ExistingPollIdInPath_BodyShouldContainSavedPoll() {
        String existingId = savedPoll.getPublicId();

        ResponseEntity<Poll> response = client.exchange(
                getUriForEndPoint("polls/" + existingId),
                HttpMethod.GET,
                null,
                Poll.class);
        Poll poll = response.getBody();

        Assert.assertEquals(savedPoll, poll);
        Assert.assertArrayEquals(savedPoll.getOptions().toArray(), poll.getOptions().toArray());
    }

    @Test
    public void showPoll_NotExistingPollIdInPath_ShouldReturn404() {
        String notExistingId = "0000";
        Assert.assertNotEquals(notExistingId, savedPoll.getPublicId());

        ResponseEntity<Poll> response = client.exchange(
                getUriForEndPoint("polls/" + notExistingId),
                HttpMethod.GET,
                null,
                Poll.class);

        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }


    private String getUriForEndPoint(String endpoint) {
        return "http://localhost:" + port + "api/" + endpoint;
    }
}
