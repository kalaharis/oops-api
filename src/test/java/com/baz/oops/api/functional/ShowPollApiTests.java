package com.baz.oops.api.functional;

import com.baz.oops.api.spring.ResponsePage;
import com.baz.oops.persistence.PollsRepository;
import com.baz.oops.service.enums.State;
import com.baz.oops.service.model.Option;
import com.baz.oops.service.model.Poll;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.TimeZone;

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

        Poll javaOrCsharpPoll = new Poll("Java or C#?");
        javaOrCsharpPoll.addOption(new Option("Java"));
        javaOrCsharpPoll.addOption(new Option("Not C#"));

        savedPoll = pollsRepository.save(javaOrCsharpPoll);
    }

    @Test
    public void showPoll_ExistingPollIdInPath_ShouldReturn200AndBody() {
        ResponseEntity<Poll> response = client.exchange(
                getUriForEndPoint("polls/" + savedPoll.getId()),
                HttpMethod.GET,
                null,
                Poll.class);
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertNotNull(response.getBody());
    }

    @Test
    public void showPoll_ExistingPollIdInPath_BodyShouldContainSavedPoll() {
        ResponseEntity<Poll> response = client.exchange(
                getUriForEndPoint("polls/" + savedPoll.getId()),
                HttpMethod.GET,
                null,
                Poll.class);
        Poll poll = response.getBody();

        Assert.assertEquals(savedPoll, poll);
        Assert.assertEquals(savedPoll.getOptions().toArray(), poll.getOptions().toArray());
    }

    @Test
    public void showPoll_NotExistingPollIdInPath_ShouldReturn404() {
        long notExistingId = savedPoll.getId() + 1;
        Assert.assertNotEquals(notExistingId, savedPoll.getId());

        ResponseEntity<Poll> response = client.exchange(
                getUriForEndPoint("polls/" + notExistingId),
                HttpMethod.GET,
                null,
                Poll.class);

        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void showPoll_NegativePollIdInPath_ShouldReturn400() {
        long negativeId = -1;

        ResponseEntity<Poll> response = client.exchange(
                getUriForEndPoint("polls/" + negativeId),
                HttpMethod.GET,
                null,
                Poll.class);

        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }


    private String getUriForEndPoint(String endpoint) {
        return "http://localhost:" + port + "api/" + endpoint;
    }
}
