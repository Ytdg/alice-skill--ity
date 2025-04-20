package com.hackaton.alicecity.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NonNull;

@Entity
@Data
@Table(name = "cities")
public class Cities {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String userId;
    @NotNull
    private String cityName;

    public Cities(@NonNull String cityName, @NonNull String userId) {
        this.cityName=cityName;
        this.userId=userId;
    }

    public Cities() {

    }
}
