package com.baz.oops.api.functional;

import com.baz.oops.persistence.PollsRepository;
import com.baz.oops.service.PollService;
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

/**
 * Created by arahis on 6/19/17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class VoteApiTests {

    private final int OPTION_ONE_INDEX = 0;

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

        Poll buildTheWallPoll = new Poll("Build the wall?");
        buildTheWallPoll.addOption(new Option("YES"));
        buildTheWallPoll.addOption(new Option("hell no"));

        savedPoll = pollService.createPoll(buildTheWallPoll.getName(), buildTheWallPoll.getOptions());
    }

    @Test
    public void voteForOptionOneInPoll_ExistingPollIdInPath_ShouldReturn200AndBody() {
        String existingId = savedPoll.getPublicId();

        String votingEndpoint = "polls/" + existingId
                + "/options/" + OPTION_ONE_INDEX + "/vote";

        ResponseEntity<Poll> response = client.exchange(
                getUriForEndPoint(votingEndpoint),
                HttpMethod.PUT,
                null,
                Poll.class);

        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertNotNull(response.getBody());
    }

    @Test
    public void voteForOptionOneInPoll_ExistingPollIdInPath_ShouldUpdatePollInRepository() {
        String existingId = savedPoll.getPublicId();

        String votingEndpoint = "polls/" + existingId
                + "/options/" + OPTION_ONE_INDEX + "/vote";

        ResponseEntity<Poll> response = client.exchange(
                getUriForEndPoint(votingEndpoint),
                HttpMethod.PUT,
                null,
                Poll.class);

        int votesCount = 1;
        Poll repositoryPoll = pollsRepository.findOne(savedPoll.getPrivateId());
        Assert.assertEquals(votesCount,
                repositoryPoll.getOptions().get(OPTION_ONE_INDEX).getVotesCount());
    }

    @Test
    public void voteForOptionOneInPoll_ExistingPollIdInPath_ShouldReturnPollWithVotedOption() {
        String existingId = savedPoll.getPublicId();

        String votingEndpoint = "polls/" + existingId
                + "/options/" + OPTION_ONE_INDEX + "/vote";

        ResponseEntity<Poll> response = client.exchange(
                getUriForEndPoint(votingEndpoint),
                HttpMethod.PUT,
                null,
                Poll.class);

        int votesCount = 1;
        Poll poll = response.getBody();
        Assert.assertEquals(votesCount, poll.getOptions().get(OPTION_ONE_INDEX).getVotesCount());
    }

    @Test
    public void voteForOptionOneInPoll_NotExistingPollIdInPath_ShouldReturn404() {
        String notExistingPollId = Long.toString(savedPoll.getPrivateId());

        String votingEndpoint = "polls/" + notExistingPollId
                + "/options/" + OPTION_ONE_INDEX + "/vote";

        ResponseEntity<Poll> response = client.exchange(
                getUriForEndPoint(votingEndpoint),
                HttpMethod.PUT,
                null,
                Poll.class);

        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void voteForOptionOneInPoll_NotExistingPollOptionIndxInPath_ShouldReturn404() {
        String existingPollId = savedPoll.getPublicId();
        int notExistingOptionIndx = savedPoll.getOptions().size();

        String votingEndpoint = "polls/" + existingPollId
                + "/options/" + notExistingOptionIndx + "/vote";

        ResponseEntity<Poll> response = client.exchange(
                getUriForEndPoint(votingEndpoint),
                HttpMethod.PUT,
                null,
                Poll.class);

        Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void voteForOptionOneInPoll_NegativePollOptionIndxInPath_ShouldReturn400() {
        String existingPollId = savedPoll.getPublicId();
        int negativeOptionIndx = -1;

        String votingEndpoint = "polls/" + existingPollId
                + "/options/" + negativeOptionIndx + "/vote";

        ResponseEntity<Poll> response = client.exchange(
                getUriForEndPoint(votingEndpoint),
                HttpMethod.PUT,
                null,
                Poll.class);

        Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    private String getUriForEndPoint(String endpoint) {
        return "http://localhost:" + port + "api/" + endpoint;
    }

}
