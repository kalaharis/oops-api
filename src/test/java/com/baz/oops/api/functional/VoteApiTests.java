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
 * Created by arahis on 6/19/17.
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class VoteApiTests {

    private final String BASE_URL = "http://localhost";
    private final int OPTION_ONE_INDEX = 0;
    private final int OPTION_TWO_INDEX = 1;

    @Autowired
    private PollsRepository pollsRepository;
    @Autowired
    private PollService pollService;

    @Autowired
    private ServletContext context;
    @LocalServerPort
    private int port;

    private RestTemplate client = new RestTemplate();

    private Poll savedNormalPoll;
    private Poll savedMultiOptionedPoll;


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
        options.add(new Option("YES"));
        options.add(new Option("hell no"));
        CreatePollRequest createReq = new CreatePollRequest("Build the wall?", options);

        savedNormalPoll = pollService.createPoll(createReq);

        options.clear();
        options.add(new Option("Red"));
        options.add(new Option("Blue"));
        options.add(new Option("Green"));
        options.add(new Option("White"));
        createReq = new CreatePollRequest("Whick colors do you like", options);
        createReq.setMultiOptions(true);

        savedMultiOptionedPoll = pollService.createPoll(createReq);
    }

    @Test
    public void voteForOneOptionInPoll_ExistingPollIdInPath_ShouldReturn200AndBody() {
        String existingId = savedNormalPoll.getPublicId();

        UriComponents uri = UriComponentsBuilder
                .fromHttpUrl(BASE_URL).port(port)
                .path(context.getContextPath() + "/polls/" + existingId)
                .queryParam("vote", OPTION_ONE_INDEX)
                .build().encode();

        ResponseEntity<Poll> response = client.exchange(
                uri.toUri(), HttpMethod.PUT, null, Poll.class);

        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertNotNull(response.getBody());
    }

    @Test
    public void multipleVoteRequests_ExistingPollIdInPath_ShouldUpdatePollInRepository() {
        String existingId = savedNormalPoll.getPublicId();

        UriComponents optionOneUri = UriComponentsBuilder
                .fromHttpUrl(BASE_URL).port(port)
                .path(context.getContextPath() + "/polls/" + existingId)
                .queryParam("vote", OPTION_ONE_INDEX)
                .build().encode();

        UriComponents optionTwoUri = UriComponentsBuilder
                .fromHttpUrl(BASE_URL).port(port)
                .path(context.getContextPath() + "/polls/" + existingId)
                .queryParam("vote", OPTION_TWO_INDEX)
                .build().encode();


        int optionOneTimesVoted = 3;
        int optionTwoTimesVoted = 2;
        int expectedTotalVotes = optionOneTimesVoted + optionTwoTimesVoted;

        for (int i = 0; i < optionOneTimesVoted; i++) {
            client.exchange(optionOneUri.toUri(), HttpMethod.PUT, null, Poll.class);
        }

        for (int i = 0; i < optionTwoTimesVoted; i++) {
            client.exchange(optionTwoUri.toUri(), HttpMethod.PUT, null, Poll.class);
        }

        Poll repositoryPoll = pollsRepository.findOne(savedNormalPoll.getPrivateId());

        int optionOneVotes = repositoryPoll.getOptions().get(OPTION_ONE_INDEX).getVotesCount();
        int optionTwoVotes = repositoryPoll.getOptions().get(OPTION_TWO_INDEX).getVotesCount();
        int actualTotalVotes = repositoryPoll.getTotalVotes();

        Assert.assertEquals(expectedTotalVotes, actualTotalVotes);
        Assert.assertEquals(optionOneTimesVoted, optionOneVotes);
        Assert.assertEquals(optionTwoTimesVoted, optionTwoVotes);
    }

    @Test
    public void voteForMultipleOptions_PollSupportsMultipleChoices_ShouldReturnUpdatedPoll() {
        String existingId = savedMultiOptionedPoll.getPublicId();

        UriComponents uri = UriComponentsBuilder
                .fromHttpUrl(BASE_URL).port(port)
                .path(context.getContextPath() + "/polls/" + existingId)
                .queryParam("vote", OPTION_ONE_INDEX, OPTION_TWO_INDEX)
                .build().encode();


        ResponseEntity<Poll> response = client.exchange(
                uri.toUri(), HttpMethod.PUT, null, Poll.class);

        Poll updatedPoll = response.getBody();

        int actualTotalVotes = updatedPoll.getTotalVotes();
        int optionOneVotes = updatedPoll.getOptions().get(OPTION_ONE_INDEX).getVotesCount();
        int optionTwoVotes = updatedPoll.getOptions().get(OPTION_TWO_INDEX).getVotesCount();

        Assert.assertEquals(true, updatedPoll.isMultiOptions());
        Assert.assertEquals(1, actualTotalVotes);
        Assert.assertEquals(1, optionOneVotes);
        Assert.assertEquals(1, optionTwoVotes);
    }

    @Test
    public void sameOptionVotedMultipleTimes_PollSupportsMultipleChoices_ShouldVoteOncePerOption() {
        String existingId = savedMultiOptionedPoll.getPublicId();

        UriComponents uri = UriComponentsBuilder
                .fromHttpUrl(BASE_URL).port(port)
                .path(context.getContextPath() + "/polls/" + existingId)
                .queryParam("vote", OPTION_ONE_INDEX, OPTION_ONE_INDEX, OPTION_TWO_INDEX)
                .build().encode();

        ResponseEntity<Poll> response = client.exchange(
                uri.toUri(), HttpMethod.PUT, null, Poll.class);

        Poll updatedPoll = response.getBody();

        int optionOneVotes = updatedPoll.getOptions().get(OPTION_ONE_INDEX).getVotesCount();
        int optionTwoVotes = updatedPoll.getOptions().get(OPTION_TWO_INDEX).getVotesCount();
        int actualTotalVotes = updatedPoll.getTotalVotes();

        Assert.assertEquals(1, actualTotalVotes);
        Assert.assertEquals(1, optionOneVotes);
        Assert.assertEquals(1, optionTwoVotes);
    }

    @Test
    public void voteForMultipleOptions_PollDoesNotSupportMultipleChoices_ShouldReturn400() {
        String existingId = savedNormalPoll.getPublicId();

        UriComponents uri = UriComponentsBuilder
                .fromHttpUrl(BASE_URL).port(port)
                .path(context.getContextPath() + "/polls/" + existingId)
                .queryParam("vote", OPTION_ONE_INDEX, OPTION_TWO_INDEX)
                .build().encode();

        ResponseEntity<Poll> response = client.exchange(
                uri.toUri(), HttpMethod.PUT, null, Poll.class);

        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void voteForOneOptionInPoll_ExistingPollIdInPath_ShouldUpdatePollInRepository() {
        String existingId = savedNormalPoll.getPublicId();

        UriComponents uri = UriComponentsBuilder
                .fromHttpUrl(BASE_URL).port(port)
                .path(context.getContextPath() + "/polls/" + existingId)
                .queryParam("vote", OPTION_ONE_INDEX)
                .build().encode();

        ResponseEntity<Poll> response = client.exchange(
                uri.toUri(), HttpMethod.PUT, null, Poll.class);

        Poll repositoryPoll = pollsRepository.findOne(savedNormalPoll.getPrivateId());

        int optionOneVotes = repositoryPoll.getOptions().get(OPTION_ONE_INDEX).getVotesCount();
        int actualTotalVotes = repositoryPoll.getTotalVotes();

        Assert.assertEquals(1, optionOneVotes);
        Assert.assertEquals(1, actualTotalVotes);
    }


    @Test
    public void voteForOneOptionInPoll_ExistingPollIdInPath_ShouldReturnUpdatedPoll() {
        String existingId = savedNormalPoll.getPublicId();

        UriComponents uri = UriComponentsBuilder
                .fromHttpUrl(BASE_URL).port(port)
                .path(context.getContextPath() + "/polls/" + existingId)
                .queryParam("vote", OPTION_ONE_INDEX)
                .build().encode();

        ResponseEntity<Poll> response = client.exchange(
                uri.toUri(), HttpMethod.PUT, null, Poll.class);

        Poll updatedPoll = response.getBody();

        int optionOneVotes = updatedPoll.getOptions().get(OPTION_ONE_INDEX).getVotesCount();
        int actualTotalVotes = updatedPoll.getTotalVotes();

        Assert.assertEquals(1, optionOneVotes);
        Assert.assertEquals(1, actualTotalVotes);
    }

    @Test
    public void voteForOneOptionInPoll_NotExistingPollIdInPath_ShouldReturn404() {
        String notExistingId = "0";

        UriComponents uri = UriComponentsBuilder
                .fromHttpUrl(BASE_URL).port(port)
                .path(context.getContextPath() + "/polls/" + notExistingId)
                .queryParam("vote", OPTION_ONE_INDEX)
                .build().encode();

        ResponseEntity<Poll> response = client.exchange(
                uri.toUri(), HttpMethod.PUT, null, Poll.class);

        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void voteForOneOptionInPoll_NotExistingOptionIndx_ShouldReturn400() {
        String existingId = savedNormalPoll.getPublicId();
        int notExistingOptionIndx = savedNormalPoll.getOptions().size();

        UriComponents uri = UriComponentsBuilder
                .fromHttpUrl(BASE_URL).port(port)
                .path(context.getContextPath() + "/polls/" + existingId)
                .queryParam("vote", notExistingOptionIndx)
                .build().encode();

        ResponseEntity<Poll> response = client.exchange(
                uri.toUri(), HttpMethod.PUT, null, Poll.class);

        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void voteForOptionOneInPoll_NegativePollOptionIndxInPath_ShouldReturn400() {
        String existingId = savedNormalPoll.getPublicId();
        int negativeOptionIndx = -1;

        UriComponents uri = UriComponentsBuilder
                .fromHttpUrl(BASE_URL).port(port)
                .path(context.getContextPath() + "/polls/" + existingId)
                .queryParam("vote", negativeOptionIndx)
                .build().encode();

        ResponseEntity<Poll> response = client.exchange(
                uri.toUri(), HttpMethod.PUT, null, Poll.class);

        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

}
