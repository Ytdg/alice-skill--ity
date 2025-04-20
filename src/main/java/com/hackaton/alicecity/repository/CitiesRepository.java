package com.hackaton.alicecity.repository;

import com.hackaton.alicecity.entity.Cities;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CitiesRepository extends JpaRepository<Cities,Long> {
    boolean existsByCityNameAndUserId(@NotNull String cityName, @NotNull String userId);
}
