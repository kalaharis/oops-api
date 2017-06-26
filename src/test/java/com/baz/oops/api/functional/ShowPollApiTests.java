package com.baz.oops.api.functional;

import com.baz.oops.api.JSON.CreatePollRequest;
import com.baz.oops.persistence.PollsRepository;
import com.baz.oops.service.PollService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by arahis on 6/18/17.
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ShowPollApiTests {

    private static final String BASE_URL = "http://localhost";

    @Autowired
    private ServletContext context;

    @Autowired
    private PollsRepository pollsRepository;
    @Autowired
    private PollService pollService;

    @LocalServerPort
    private int port;

    private RestTemplate client = new RestTemplate();

    private Poll savedPoll;

    @Before
    public void init() throws ServiceException {
        client.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) throws IOException {
                return false;
            }
        });
        pollsRepository.deleteAll();

        List<Option> options = new ArrayList<>();
        options.add(new Option("Java"));
        options.add(new Option("Definitely not C#"));
        CreatePollRequest createReq = new CreatePollRequest("Java or C#?", options);

        savedPoll = pollService.createPoll(createReq);
    }

    @Test
    public void showPoll_ExistingPollIdInPath_ShouldReturn200AndBody() {
        String existingId = savedPoll.getPublicId();

        UriComponents uri = UriComponentsBuilder
                .fromHttpUrl(BASE_URL).port(port)
                .path(context.getContextPath() + "/polls/" + existingId)
                .build().encode();

        ResponseEntity<Poll> response = client.exchange(
                uri.toUri(), HttpMethod.GET,
                null, Poll.class);

        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertNotNull(response.getBody());
    }

    @Test
    public void showPoll_ExistingPollIdInPath_ShouldReturnPoll() {
        String existingId = savedPoll.getPublicId();

        UriComponents uri = UriComponentsBuilder
                .fromHttpUrl(BASE_URL).port(port)
                .path(context.getContextPath() + "/polls/" + existingId)
                .build().encode();

        ResponseEntity<Poll> response = client.exchange(
                uri.toUri(), HttpMethod.GET,
                null, Poll.class);

        Poll poll = response.getBody();

        Assert.assertEquals(savedPoll, poll);
        Assert.assertArrayEquals(savedPoll.getOptions().toArray(), poll.getOptions().toArray());
    }

    @Test
    public void showPoll_NotExistingPollIdInPath_ShouldReturn404() {
        pollsRepository.deleteAll();
        String notExistingId = "0";

        UriComponents uri = UriComponentsBuilder
                .fromHttpUrl(BASE_URL).port(port)
                .path(context.getContextPath() + "/polls/" + notExistingId)
                .build().encode();

        ResponseEntity<Poll> response = client.exchange(
                uri.toUri(), HttpMethod.GET,
                null, Poll.class);

        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

}
