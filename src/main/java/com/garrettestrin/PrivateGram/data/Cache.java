package com.garrettestrin.PrivateGram.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.garrettestrin.PrivateGram.api.ApiObjects.PostResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Cache {
    Map<String, String> postCache = new HashMap<>();
    Map<String, String> commentCache = new HashMap<>();

    public final String POST_PAGE = "POST_PAGE_";
    public final String POST_COUNT = "POST_COUNT";
    public final String COMMENTS_FOR_ = "COMMENTS_FOR_";
    public final String PREVIEW_COMMENTS_FOR_ = "PREVIEW_COMMENTS_FOR_";

    // Post Cache
    public void setPost(String key, String value, boolean admin) {
        if (admin) {
            postCache.put(key + "_PRIVATE", value);
        } else {
            postCache.put(key, value);
        }
    }

    public String getPost(String key, boolean admin) {
        if (admin) {
            return postCache.get(key + "_PRIVATE");
        }
        return postCache.get(key);
    }

    public void clearPostCache() {
        postCache.clear();
    }

    // Comments Cache
    public void setComments(String key, String value) {commentCache.put(key, value); }

    public String getComments(String key) {return commentCache.get(key); }

    public void removeCommentCache(String key) {commentCache.remove(key); }

    public void clearCommentCache() {
        commentCache.clear();
    }

    // Utilities
    public String encode(Object value) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(value);
    }

    public <T> T decode(String value, Class<T> clazz) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(value, clazz);
    }
}
