package com.baz.oops.api.functional;

import com.baz.oops.api.spring.ResponsePage;
import com.baz.oops.persistence.PollsRepository;
import com.baz.oops.service.PollService;
import com.baz.oops.service.enums.State;
import com.baz.oops.service.model.Option;
import com.baz.oops.service.model.Poll;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by arahis on 6/14/17.
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ListPollsApiTests {

    private final int TEST_POLLS_AMOUNT = 3;
    private final int POLL_ONE_INDEX = 0;
    private final int POLL_TWO_INDEX = 1;
    private final int POLL_THREE_INDEX = 2;

    private final String FIRST_POLL_CREATION_DATE = "2017-04-15T12:54:45Z";
    private final String SECOND_POLL_CREATION_DATE = "2017-06-16T15:11:50Z";
    private final String THIRD_POLL_CREATION_DATE = "2017-06-17T05:09:02Z";

    private final int ONE_PAGE_ELEMENT = 1;
    private final int TWO_PAGE_ELEMENTS = 2;

    @LocalServerPort
    private int port;

    private RestTemplate client = new RestTemplate();
    private ObjectMapper mapper = new ObjectMapper();
    private DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");

    private Poll firstSavedPoll;
    private Poll secondSavedPoll;
    private Poll thirdSavedPoll;

    @Autowired
    private PollsRepository pollsRepository;

    @Before
    public void init() throws ParseException {
        pollsRepository.deleteAll();

        df.setTimeZone(TimeZone.getTimeZone("UTC+00:00"));
        df.setLenient(false);

        Poll toBeOrNotPoll = new Poll("To be or not to be?");
        toBeOrNotPoll.addOption(new Option("To be! :3"));
        toBeOrNotPoll.addOption(new Option("Not to be! D:"));
        Set<String> tags = new HashSet<>(Arrays.asList("books", "classic"));
        toBeOrNotPoll.setTags(tags);
        toBeOrNotPoll.setCreateDate(df.parse(FIRST_POLL_CREATION_DATE));

        Poll simpleMathPoll = new Poll("2+2?");
        simpleMathPoll.addOption(new Option("3"));
        simpleMathPoll.addOption(new Option("4"));
        simpleMathPoll.addOption(new Option("5"));
        tags = new HashSet<>(Arrays.asList("math", "classic"));
        simpleMathPoll.setTags(tags);
        simpleMathPoll.setCreateDate(df.parse(SECOND_POLL_CREATION_DATE));
        simpleMathPoll.setState(State.CLOSED);

        Poll rickRollPoll = new Poll("Have you ever been rickrolled?");
        rickRollPoll.addOption(new Option("Yes"));
        rickRollPoll.addOption(new Option("No"));
        tags = new HashSet<>(Arrays.asList("music", "internet"));
        rickRollPoll.setTags(tags);
        rickRollPoll.setCreateDate(df.parse(THIRD_POLL_CREATION_DATE));

        Poll privatePoll = new Poll("Should this poll be public?");
        privatePoll.addOption(new Option("I dont think so"));
        privatePoll.addOption(new Option("Yes"));
        privatePoll.setHidden(true);

        firstSavedPoll = pollsRepository.save(toBeOrNotPoll);
        secondSavedPoll = pollsRepository.save(simpleMathPoll);
        thirdSavedPoll = pollsRepository.save(rickRollPoll);
        pollsRepository.save(privatePoll);
    }

    @Test
    public void getListPolls_ThereIsTreePolls_ShouldReturn200andBody() {
        ResponseEntity<ResponsePage<Poll>> response = client.exchange(
                getUriForEndPoint("polls"),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ResponsePage<Poll>>() {
                });

        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
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
        Assert.assertEquals(TEST_POLLS_AMOUNT, pollsPage.getNumberOfElements());

    }

    @Test
    public void getListPolls_ThereIsThreePolls_ReturnedPollsShouldBeEqualWithSaved() {
        ResponseEntity<ResponsePage<Poll>> response = client.exchange(
                getUriForEndPoint("polls"),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ResponsePage<Poll>>() {
                });

        ResponsePage<Poll> pollsPage = response.getBody();
        Assert.assertEquals(TEST_POLLS_AMOUNT, pollsPage.getNumberOfElements());

        List<Poll> pollsList = pollsPage.getContent();

        Poll firstPoll = pollsList.get(POLL_ONE_INDEX);
        Assert.assertEquals(firstSavedPoll, firstPoll);

        Poll secondPoll = pollsList.get(POLL_TWO_INDEX);
        Assert.assertEquals(secondSavedPoll, secondPoll);

        Poll thirdPoll = pollsList.get(POLL_THREE_INDEX);
        Assert.assertEquals(thirdSavedPoll, thirdPoll);
    }

    @Test
    public void getListPollsWithTags_OneParamValue_ShouldReturnTwoPollsWithSameTags() {
        String tag = "classic";
        ResponseEntity<ResponsePage<Poll>> response = client.exchange(
                getUriForEndPoint("polls?tags=" + tag),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ResponsePage<Poll>>() {
                });

        ResponsePage<Poll> pollsPage = response.getBody();
        Assert.assertEquals(TWO_PAGE_ELEMENTS, pollsPage.getNumberOfElements());
        Assert.assertTrue(pollsPage.getContent().get(POLL_ONE_INDEX).getTags().contains(tag));
        Assert.assertTrue(pollsPage.getContent().get(POLL_TWO_INDEX).getTags().contains(tag));
    }

    @Test
    public void getListPollsWithTags_MultipleParamsValue_ShouldReturnOnePollWithSameTags() {
        String tagClassic = "classic";
        String tagBooks = "books";
        ResponseEntity<ResponsePage<Poll>> response = client.exchange(
                getUriForEndPoint("polls?tags=" + tagClassic + "," + tagBooks),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ResponsePage<Poll>>() {
                });

        ResponsePage<Poll> pollsPage = response.getBody();
        Assert.assertEquals(ONE_PAGE_ELEMENT, pollsPage.getNumberOfElements());
        Assert.assertTrue(pollsPage.getContent().get(POLL_ONE_INDEX).getTags().contains(tagClassic));
        Assert.assertTrue(pollsPage.getContent().get(POLL_ONE_INDEX).getTags().contains(tagBooks));
    }

    @Test
    public void getListPollsWithState_ParamValueIsUppercase_ShouldReturnTwoPollsWithSaneState() {
        String state = "OPEN";
        ResponseEntity<ResponsePage<Poll>> response = client.exchange(
                getUriForEndPoint("polls?state=" + state),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ResponsePage<Poll>>() {
                });

        ResponsePage<Poll> pollsPage = response.getBody();
        Assert.assertEquals(TWO_PAGE_ELEMENTS, pollsPage.getNumberOfElements());
        Assert.assertEquals(pollsPage.getContent().get(POLL_ONE_INDEX).getState(), State.OPEN);
        Assert.assertEquals(pollsPage.getContent().get(POLL_TWO_INDEX).getState(), State.OPEN);
    }

    @Test
    public void getListPollsWithState_ParamIsLowercase_ShouldReturnTwoPollsWithSameState() {
        String state = "open";
        ResponseEntity<ResponsePage<Poll>> response = client.exchange(
                getUriForEndPoint("polls?state=" + state),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ResponsePage<Poll>>() {
                });

        ResponsePage<Poll> pollsPage = response.getBody();
        Assert.assertEquals(TWO_PAGE_ELEMENTS, pollsPage.getNumberOfElements());
        Assert.assertEquals(pollsPage.getContent().get(POLL_ONE_INDEX).getState(), State.OPEN);
        Assert.assertEquals(pollsPage.getContent().get(POLL_TWO_INDEX).getState(), State.OPEN);
    }

    @Test
    public void getListPollsWithStartDate_ParamIsValidDateString_ShouldReturnTwoPolls()
            throws ParseException {
        String date = SECOND_POLL_CREATION_DATE;
        ResponseEntity<ResponsePage<Poll>> response = client.exchange(
                getUriForEndPoint("polls?start=" + date),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ResponsePage<Poll>>() {
                });

        ResponsePage<Poll> pollsPage = response.getBody();
        Assert.assertEquals(TWO_PAGE_ELEMENTS, pollsPage.getNumberOfElements());

        List<Poll> pollsList = pollsPage.getContent();
        Date startParamDate = df.parse(SECOND_POLL_CREATION_DATE);

        Date firstPollDate = pollsList.get(POLL_ONE_INDEX).getCreateDate();
        Assert.assertFalse(firstPollDate.before(startParamDate));

        Date secondPollDate = pollsList.get(POLL_TWO_INDEX).getCreateDate();
        Assert.assertFalse(secondPollDate.before(startParamDate));
    }

    @Test
    public void getListPollsWithEndDate_ParamIsValidDateString_ShouldReturnOnePoll()
            throws ParseException {
        String date = FIRST_POLL_CREATION_DATE;
        ResponseEntity<ResponsePage<Poll>> response = client.exchange(
                getUriForEndPoint("polls?end=" + date),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ResponsePage<Poll>>() {
                });

        ResponsePage<Poll> pollsPage = response.getBody();
        Assert.assertEquals(ONE_PAGE_ELEMENT, pollsPage.getNumberOfElements());

        List<Poll> pollsList = pollsPage.getContent();
        Date endParamDate = df.parse(FIRST_POLL_CREATION_DATE);

        Date firstPollDate = pollsList.get(POLL_ONE_INDEX).getCreateDate();
        Assert.assertFalse(firstPollDate.after(endParamDate));
    }

    @Test
    public void getListPollsInDateRange_ParamsAreValidDateStrings_ShouldReturnOnePoll()
            throws ParseException {
        String startRange = "2017-06-16T15:11:49Z";
        String endRange = "2017-06-16T15:11:51Z";
        ResponseEntity<ResponsePage<Poll>> response = client.exchange(
                getUriForEndPoint("polls?start=" + startRange + "&end=" + endRange),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<ResponsePage<Poll>>() {
                });

        ResponsePage<Poll> pollsPage = response.getBody();
        Assert.assertEquals(ONE_PAGE_ELEMENT, pollsPage.getNumberOfElements());

        List<Poll> pollsList = pollsPage.getContent();
        Date startParamDate = df.parse(startRange);
        Date endParamDate = df.parse(endRange);

        Date pollDate = pollsList.get(POLL_ONE_INDEX).getCreateDate();
        Assert.assertFalse(pollDate.before(startParamDate) && pollDate.after(endParamDate));
    }


    private String getUriForEndPoint(String endpoint) {
        return "http://localhost:" + port + "api/" + endpoint;
    }

}
