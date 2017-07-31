package com.baz.oops.api.JSON;

import com.baz.oops.service.model.Option;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by arahis on 6/12/17.
 */
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreatePollRequest {
    private String name = "";
    private List<Option> options = new ArrayList<>();
    private Set<String> tags = new HashSet<>();
    private String expireDate;
    private boolean hidden;
    private boolean multiOptions;
    private boolean multipleVotesIp;

    //required minimum
    public CreatePollRequest(String name, List<Option> options) {
        this.name = name;
        this.options = options;
    }
}
