package com.hackaton.alicecity.util;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonCoder {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static <T> String encode(T obj) throws Exception {
        return mapper.writeValueAsString(obj);
    }

    public static <T> T decode(Class<T> tClass, String content) throws Exception {
        return mapper.readValue(content, tClass);
    }
}
