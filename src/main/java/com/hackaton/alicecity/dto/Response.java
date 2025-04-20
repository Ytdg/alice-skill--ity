package com.hackaton.alicecity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class Response {
    @NonNull
    private String text;
    @JsonProperty("end_session")
    private boolean endSession;

    private List<Button> buttons;

    public Response(@NonNull String text, Boolean endSession) {
        this.text = text;
        this.endSession = endSession;
    }
}
