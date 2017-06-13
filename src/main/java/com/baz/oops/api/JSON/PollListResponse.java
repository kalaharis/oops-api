package com.baz.oops.api.JSON;

import com.baz.oops.service.model.Poll;

import java.util.List;

/**
 * Created by arahis on 6/11/17.
 */
public class PollListResponse {
    List<Poll> polls;

    public PollListResponse(List<Poll> polls) {
        this.polls = polls;
    }
}
