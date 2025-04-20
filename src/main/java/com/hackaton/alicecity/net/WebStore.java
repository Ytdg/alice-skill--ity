package com.hackaton.alicecity.net;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackaton.alicecity.dto.Query;
import com.hackaton.alicecity.dto.Result;
import com.hackaton.alicecity.repository.QuizRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.json.JsonParseException;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Slf4j
@Component
public class WebStore implements QuizRepository {

    private final ObjectMapper mapper;


    @Override
    public Result getQuestion() throws Exception {
        String urlQuery = "https://opentdb.com/api.php?amount=1&category=22&difficulty=easy&type=boolean";
        log.info("new question");
        Query query = mapper.readValue(request(urlQuery), Query.class);
        Result result = query.getResults().stream().findFirst().orElseThrow();
        if (!result.getQuestion().isBlank()) {
            return result;
        }
        throw new JsonParseException();
    }

    private String request(String urlStr) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "application/json");
        int CONNECTION_TIMEOUT = 1100; // optimal time_out_to_connect
        con.setConnectTimeout(CONNECTION_TIMEOUT);
        con.setReadTimeout(CONNECTION_TIMEOUT);

        try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            log.info("time_out");
            return content.toString();
        } finally {
            con.disconnect();
        }
    }


    WebStore(ObjectMapper mapper) {
        this.mapper = mapper;
    }


}
