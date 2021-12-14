package com.ml4fakenews.accounts;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.List;

public class CaptchaResponse {
    private Boolean success;
    private Date timestamp;
    private String hostname;
    @JsonProperty("error-codes")
    private List<String> errorCodes;

    
    public Boolean getSuccess() {
        return success;
    }

    public List<String> getErrorCodes() {
        return errorCodes;
    }
}
