package com.baz.oops.api;

import com.baz.oops.api.spring.ResponsePage;
import com.baz.oops.persistence.PollsRepository;
import com.baz.oops.service.enums.State;
import com.baz.oops.service.model.Option;
import com.baz.oops.service.model.Poll;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;

import static com.baz.oops.service.enums.State.CLOSED;

/**
 * Created by arahis on 6/14/17.
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ListPollsApiTests {
    private final int ONE_HOUR = 3600;
    private final int ONE_DAY = 86400;
    private final int DATE_Y2017_M06_D14 = 1497440736;

    private final int TEST_POLLS_AMOUNT = 3;

    @Value("${server.contextPath}")
    private String contextPath;

    @LocalServerPort
    private int port;

    private RestTemplate client = new RestTemplate();
    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private PollsRepository pollsRepository;

    @Before
    public void init() {
        pollsRepository.deleteAll();

        Poll toBeOrNotPoll = new Poll("To be or not to be?");
        toBeOrNotPoll.addOption(new Option("To be! :3"));
        toBeOrNotPoll.addOption(new Option("Not to be! D:"));
        Set<String> tags = new HashSet<>(Arrays.asList("books", "classic"));
        toBeOrNotPoll.setTags(tags);
        toBeOrNotPoll.setCreateDate(new Date(DATE_Y2017_M06_D14));

        Poll simpleMathPoll = new Poll("2+2?");
        simpleMathPoll.addOption(new Option("3"));
        simpleMathPoll.addOption(new Option("4"));
        simpleMathPoll.addOption(new Option("5"));
        tags = new HashSet<>(Arrays.asList("math", "classic"));
        simpleMathPoll.setTags(tags);
        simpleMathPoll.setCreateDate(new Date(DATE_Y2017_M06_D14 - ONE_DAY + ONE_HOUR * 3));
        simpleMathPoll.setState(State.CLOSED);

        Poll rickRollPoll = new Poll("Have you ever been rickrolled?");
        rickRollPoll.addOption(new Option("Yes"));
        rickRollPoll.addOption(new Option("No"));
        tags = new HashSet<>(Arrays.asList("music", "internet"));
        rickRollPoll.setTags(tags);
        rickRollPoll.setCreateDate(new Date(DATE_Y2017_M06_D14 - ONE_HOUR * 5));

        pollsRepository.save(toBeOrNotPoll);
        pollsRepository.save(simpleMathPoll);
        pollsRepository.save(rickRollPoll);
    }

    @Test
    public void getListPolls_ThereIsTreePolls_ShouldReturn200andBody() {
        ResponseEntity<ResponsePage<Poll>> response = client.exchange(
                getUriForEndPoint("polls"),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ResponsePage<Poll>>() {
                });

        Assert.assertTrue(response.getStatusCode() == HttpStatus.OK);
        Assert.assertNotNull(response.getBody());
    }

    @Test
    public void getListPolls_ThereIsThreePolls_ShouldReturnPageWithThreeElements() {
        ResponseEntity<ResponsePage<Poll>> response = client.exchange(
                getUriForEndPoint("polls"),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ResponsePage<Poll>>() {
                });

        ResponsePage<Poll> pollsPage = response.getBody();
        Assert.assertEquals(TEST_POLLS_AMOUNT, pollsPage.getTotalElements());

    }

    @Test
    public void getListPolls_ThereIsThreePolls_FirstPollShouldContainTwoTags() {
        ResponseEntity<ResponsePage<Poll>> response = client.exchange(
                getUriForEndPoint("polls"),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ResponsePage<Poll>>() {
                });

        ResponsePage<Poll> pollsPage = response.getBody();
        List<Poll> pollsList = pollsPage.getContent();
        Poll firstPoll = pollsList.get(0);
        Assert.assertNotNull(firstPoll.getTags());
        Assert.assertTrue(firstPoll.getTags().contains("books"));
        Assert.assertTrue(firstPoll.getTags().contains("classic"));
    }

    @Test
    public void getListPolls_ThereIsThreePolls_SecondPollShouldContainThreeOptions() {
        ResponseEntity<ResponsePage<Poll>> response = client.exchange(
                getUriForEndPoint("polls"),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ResponsePage<Poll>>() {
                });

        ResponsePage<Poll> pollsPage = response.getBody();
        List<Poll> pollsList = pollsPage.getContent();
        Poll secondPoll = pollsList.get(1);
        Assert.assertNotNull(secondPoll.getOptions());
        Assert.assertEquals(secondPoll.getOptions().get(0).getName(), "3");
        Assert.assertEquals(secondPoll.getOptions().get(1).getName(), "4");
        Assert.assertEquals(secondPoll.getOptions().get(2).getName(), "5");
    }

    @Test
    public void getListPolls_ThereIsThreePolls_ThirdPollShouldBeCorrectlyNamed() {
        ResponseEntity<ResponsePage<Poll>> response = client.exchange(
                getUriForEndPoint("polls"),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ResponsePage<Poll>>() {
                });

        ResponsePage<Poll> pollsPage = response.getBody();
        List<Poll> pollsList = pollsPage.getContent();
        Poll thirdPoll = pollsList.get(2);
        Assert.assertEquals("Have you ever been rickrolled?", thirdPoll.getName());
    }

    @Test
    public void getListPollsWithTags_ThereIsThreePolls_ShouldReturnTwoPollsWithSameTags() {
        String tag = "classic";
        ResponseEntity<ResponsePage<Poll>> response = client.exchange(
                getUriForEndPoint("polls?tags=" + tag),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ResponsePage<Poll>>() {
                });

        ResponsePage<Poll> pollsPage = response.getBody();
        Assert.assertEquals(2, pollsPage.getNumberOfElements());
        Assert.assertTrue(pollsPage.getContent().get(0).getTags().contains(tag));
        Assert.assertTrue(pollsPage.getContent().get(1).getTags().contains(tag));
    }

    @Test
    public void getListPollsWithState_ThereIsThreePolls_ShouldReturnTwoPollsWithSaneState() {
        String state = "OPEN";
        ResponseEntity<ResponsePage<Poll>> response = client.exchange(
                getUriForEndPoint("polls?state=" + state),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ResponsePage<Poll>>() {
                });

        ResponsePage<Poll> pollsPage = response.getBody();
        Assert.assertEquals(2, pollsPage.getNumberOfElements());
        Assert.assertEquals(pollsPage.getContent().get(0).getState(), State.valueOf(state));
        Assert.assertEquals(pollsPage.getContent().get(1).getState(), State.valueOf(state));
    }



    private String getUriForEndPoint(String endpoint) {
        return "http://localhost:" + port + "api/" + endpoint;
    }

}
