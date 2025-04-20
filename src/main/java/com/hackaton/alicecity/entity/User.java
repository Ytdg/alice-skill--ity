package com.hackaton.alicecity.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@Entity
@Table(name = "users")
@AllArgsConstructor
public class User {
    @JsonProperty("user_id")
    @Id
    private String userId;

    public User() {

    }
}
