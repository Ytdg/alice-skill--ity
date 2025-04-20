package com.hackaton.alicecity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NonNull;

import java.util.List;

@Data
public class Nlu {
    @JsonProperty("entities")
    @NonNull
    private List<EntityAlice> entities;
}
