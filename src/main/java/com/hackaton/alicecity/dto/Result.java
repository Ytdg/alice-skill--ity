package com.hackaton.alicecity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Result {
    @NotNull
    private String question;
    @JsonProperty("correct_answer")
    private  boolean correctAnswer;
}
