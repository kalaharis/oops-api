package com.baz.oops.api.JSON;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by arahis on 6/22/17.
 */
@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {
    private int httpStatusCode;
    private String errorMsg;
}
