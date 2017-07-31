package com.baz.oops.api.functional;

import com.baz.oops.api.JSON.CreatePollRequest;
import com.baz.oops.persistence.PollsRepository;
import com.baz.oops.service.model.Option;
import com.baz.oops.service.model.Poll;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletContext;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by arahis on 6/19/17.
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CreatePollApiTests {
    private static final String BASE_URL = "http://localhost";

    private final String POLL_NAME = "Howbow dah?";
    private final String OPTION_ONE_NAME = "Cash me outside";
    private final String OPTION_TWO_NAME = "Failfish";
    private final int OPTION_ONE_INDEX = 0;
    private final int OPTION_TWO_INDEX = 1;

    @Autowired
    private ServletContext context;
    @LocalServerPort
    private int port;

    @Autowired
    private PollsRepository pollsRepository;

    private RestTemplate client = new RestTemplate();

    @Before
    public void init() {
        client.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) throws IOException {
                return false;
            }
        });

        pollsRepository.deleteAll();
    }

    @Test
    public void createPoll_ValidPostRequest_ShouldReturn201AndBody() {
        List<Option> options = new ArrayList<>();
        options.add(new Option(OPTION_ONE_NAME));
        options.add(new Option(OPTION_TWO_NAME));

        CreatePollRequest requestBody = new CreatePollRequest(POLL_NAME, options);

        UriComponents uri = UriComponentsBuilder
                .fromHttpUrl(BASE_URL).port(port)
                .path(context.getContextPath() + "/polls/")
                .build().encode();

        ResponseEntity<Poll> response = client.exchange(
                uri.toUri(), HttpMethod.POST,
                new HttpEntity<>(requestBody), Poll.class);

        Assert.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assert.assertNotNull(response.getBody());
    }

    @Test
    public void createPoll_ValidPostRequest_ShouldReturnCreatedPoll() {
        List<Option> options = new ArrayList<>();
        options.add(new Option(OPTION_ONE_NAME));
        options.add(new Option(OPTION_TWO_NAME));

        CreatePollRequest requestBody = new CreatePollRequest(POLL_NAME, options);

        UriComponents uri = UriComponentsBuilder
                .fromHttpUrl(BASE_URL).port(port)
                .path(context.getContextPath() + "/polls/")
                .build().encode();

        ResponseEntity<Poll> response = client.exchange(
                uri.toUri(), HttpMethod.POST,
                new HttpEntity<>(requestBody), Poll.class);

        Poll poll = response.getBody();
        Option optionOne = poll.getOptions().get(OPTION_ONE_INDEX);
        Option optionTwo = poll.getOptions().get(OPTION_TWO_INDEX);

        Assert.assertEquals(POLL_NAME, poll.getName());
        Assert.assertEquals(0, poll.getTotalVotes());

        Assert.assertEquals(OPTION_ONE_NAME, optionOne.getName());
        Assert.assertEquals(0, optionOne.getVotesCount());

        Assert.assertEquals(OPTION_TWO_NAME, optionTwo.getName());
        Assert.assertEquals(0, optionTwo.getVotesCount());
    }

    @Test
    public void createPoll_ValidPostRequest_PollShouldHavePublicIdAndNotPrivateId() {
        List<Option> options = new ArrayList<>();
        options.add(new Option(OPTION_ONE_NAME));
        options.add(new Option(OPTION_TWO_NAME));

        CreatePollRequest requestBody = new CreatePollRequest(POLL_NAME, options);

        UriComponents uri = UriComponentsBuilder
                .fromHttpUrl(BASE_URL).port(port)
                .path(context.getContextPath() + "/polls/")
                .build().encode();

        ResponseEntity<Poll> response = client.exchange(
                uri.toUri(), HttpMethod.POST,
                new HttpEntity<>(requestBody), Poll.class);

        Poll poll = response.getBody();

        Assert.assertNotNull(poll.getPublicId());
        Assert.assertNull(poll.getVotedIps());
        Assert.assertFalse(poll.getPublicId().isEmpty());
        Assert.assertEquals(0, poll.getPrivateId());
    }

    @Test
    public void createPoll_CreateRequestWithOneOption_ShouldReturn400() {
        List<Option> options = new ArrayList<>();
        options.add(new Option(OPTION_ONE_NAME));

        CreatePollRequest requestBody = new CreatePollRequest(POLL_NAME, options);

        UriComponents uri = UriComponentsBuilder
                .fromHttpUrl(BASE_URL).port(port)
                .path(context.getContextPath() + "/polls/")
                .build().encode();

        ResponseEntity<Poll> response = client.exchange(
                uri.toUri(), HttpMethod.POST,
                new HttpEntity<>(requestBody), Poll.class);

        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void createPoll_CreateRequestWithEmptyName_ShouldReturn400() {
        List<Option> options = new ArrayList<>();
        options.add(new Option(OPTION_ONE_NAME));
        options.add(new Option(OPTION_TWO_NAME));

        CreatePollRequest requestBody = new CreatePollRequest("", options);

        UriComponents uri = UriComponentsBuilder
                .fromHttpUrl(BASE_URL).port(port)
                .path(context.getContextPath() + "/polls/")
                .build().encode();

        ResponseEntity<Poll> response = client.exchange(
                uri.toUri(), HttpMethod.POST,
                new HttpEntity<>(requestBody), Poll.class);

        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
