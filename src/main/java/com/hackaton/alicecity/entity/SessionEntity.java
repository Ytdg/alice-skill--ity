package com.hackaton.alicecity.entity;

import com.hackaton.alicecity.common.StateSession;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;


@Data
@Entity
@Table(name = "sessions")
public
class SessionEntity {
    @Id
    private String sessionId;
    @NotNull
    @Enumerated(EnumType.STRING)
    private StateSession state;

    @NotNull
    private LocalDateTime expire;

    @NotNull
    private String userId;

    @Nullable
    private String payload;

    public SessionEntity(String sessionId) {
        this.sessionId = sessionId;
    }

    public SessionEntity(String sessionId, @NonNull StateSession state, @NonNull String userId) {
        this.sessionId = sessionId;
        this.state = state;
        this.userId = userId;
    }

    public SessionEntity() {

    }
}
