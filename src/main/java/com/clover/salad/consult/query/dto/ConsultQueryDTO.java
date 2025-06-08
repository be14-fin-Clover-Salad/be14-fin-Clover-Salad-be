package com.clover.salad.consult.query.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ConsultQueryDTO {
    private int id;

    @JsonProperty("consultAt")
    private String consultAt;

    private String content;

    @JsonProperty("isDeleted")
    private boolean isDeleted;

    private String etc;

    @JsonProperty("feedbackScore")
    private double feedbackScore;

    @JsonProperty("employeeId")
    private int employeeId;

    @JsonProperty("customerId")
    private int customerId;
}
