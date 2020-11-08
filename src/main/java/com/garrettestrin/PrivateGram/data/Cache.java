package com.garrettestrin.PrivateGram.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.garrettestrin.PrivateGram.api.ApiObjects.PostResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Cache {
    Map<String, String> postCache = new HashMap<>();

    public final String POST_PAGE = "POST_PAGE_";

    public void setPost(String key, String value) {
        postCache.put(key, value);
    }

    public String getPost(String key) {
        return postCache.get(key);
    }

    public String encode(Object value) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(value);
    }

    public <T> T decode(String value, Class<T> clazz) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(value, clazz);
    }

    public void clearPostCache() {
        postCache.clear();
    }
}
