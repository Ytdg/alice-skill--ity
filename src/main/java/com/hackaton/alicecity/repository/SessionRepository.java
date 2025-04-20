package com.hackaton.alicecity.repository;

import com.hackaton.alicecity.entity.SessionEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

//session - 60min
@Repository
public interface SessionRepository extends JpaRepository<SessionEntity, String> {
    //15:20 //16:20   //
    @Modifying
    @Transactional
    @Query("DELETE FROM SessionEntity s WHERE s.expire < :thresholdDate")
    void deleteSessionsBefore(@Param("thresholdDate") LocalDateTime thresholdDate);
}
