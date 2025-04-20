package com.hackaton.alicecity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Convert;
import jakarta.validation.ValidationException;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Objects;

@Data
@RequiredArgsConstructor
public class Request {
    private String command;
    @NonNull
    @JsonProperty("type")
    private TypeRequest typeRequest;
    @NonNull
    private Nlu nlu;
}
