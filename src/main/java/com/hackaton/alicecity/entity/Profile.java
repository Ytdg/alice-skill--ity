package com.hackaton.alicecity.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Data
@Table(name = "profile")
@AllArgsConstructor
public class Profile {
    @Column(name = "user_id")
    @Id
    private String userId;
    private int countBox;
    private int countStar;
    @Column(name = "last_visit_char")
    @Nullable
    private String lastCityChar;

    public Profile() {

    }
}
