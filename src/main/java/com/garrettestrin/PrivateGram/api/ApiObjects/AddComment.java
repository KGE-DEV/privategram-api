package com.garrettestrin.PrivateGram.api.ApiObjects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AddComment {
    @JsonProperty("userId")
    public int userId;
    @JsonProperty("comment")
    public String comment;
    @JsonProperty("postId")
    public int postId;
}
