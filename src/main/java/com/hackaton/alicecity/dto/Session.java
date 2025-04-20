package com.hackaton.alicecity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hackaton.alicecity.entity.User;
import jakarta.annotation.Nonnull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Session {
    @JsonProperty("session_id")
    @NonNull
    private String sessionId;
    @JsonProperty("new")
    private boolean newSession;
    private User user;
}
