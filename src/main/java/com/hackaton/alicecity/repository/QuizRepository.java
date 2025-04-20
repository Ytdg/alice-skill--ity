package com.hackaton.alicecity.repository;

import com.hackaton.alicecity.dto.Query;
import com.hackaton.alicecity.dto.Result;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizRepository {
    Result getQuestion() throws Exception;
}
