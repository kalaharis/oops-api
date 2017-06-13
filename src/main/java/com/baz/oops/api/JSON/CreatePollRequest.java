package com.baz.oops.api.JSON;

import com.baz.oops.service.model.Option;

import java.util.List;

import lombok.Getter;

/**
 * Created by arahis on 6/12/17.
 */
@Getter
public class CreatePollRequest {
    private String name;
    private List<Option> options;
}