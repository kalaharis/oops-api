package com.baz.oops.api;

import com.baz.oops.OopsApplication;
import com.baz.oops.persistence.PollsRepository;
import com.baz.oops.service.enums.State;
import com.baz.oops.service.model.Option;
import com.baz.oops.service.model.Poll;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.validation.constraints.Max;

import lombok.extern.slf4j.Slf4j;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by arahis on 6/14/17.
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ListPollsControllerTests {
    private final int ONE_HOUR = 3600;
    private final int ONE_DAY = 86400;
    private final int DATE_Y2017_M06_D14 = 1497440736;

    @Value("${server.contextPath}")
    private String contextPath;

    @LocalServerPort
    private int port;

    private RestTemplate client = new RestTemplate();

    @Autowired
    private PollsRepository pollsRepository;

    @Before
    public void init() {

        Poll pollToBe = new Poll("To be or not to be?");
        pollToBe.addOption(new Option("To be! :3"));
        pollToBe.addOption(new Option("Not to be! D:"));
        Set<String> tags = new HashSet<>(Arrays.asList("books", "classic"));
        pollToBe.setTags(tags);
        pollToBe.setCreateDate(new Date(DATE_Y2017_M06_D14));

        Poll pollLeagueDiv = new Poll("2+2?");
        pollLeagueDiv.addOption(new Option("3"));
        pollLeagueDiv.addOption(new Option("4"));
        pollLeagueDiv.addOption(new Option("5"));
        tags = new HashSet<>(Arrays.asList("math", "classic"));
        pollLeagueDiv.setTags(tags);
        pollLeagueDiv.setCreateDate(new Date(DATE_Y2017_M06_D14 - ONE_DAY + ONE_HOUR * 3));

        Poll pollRickroll = new Poll("Have you ever been rickrolled?");
        pollRickroll.addOption(new Option("Yes"));
        pollRickroll.addOption(new Option("No"));
        tags = new HashSet<>(Arrays.asList("music", "classic", "internet"));
        pollRickroll.setTags(tags);
        pollRickroll.setCreateDate(new Date(DATE_Y2017_M06_D14 - ONE_HOUR * 5));

        pollsRepository.save(pollToBe);
        pollsRepository.save(pollLeagueDiv);
        pollsRepository.save(pollRickroll);
    }

    @Test
    public void shouldReturn200andPollsList() {
        ResponseEntity response = client.getForEntity(
                "http://localhost:" + port + "api/polls", Object.class);
        Assert.assertTrue(response.getStatusCode() == HttpStatus.OK);
        Assert.assertNotNull(response.getBody());
    }


}
