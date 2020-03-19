package com.garrettestrin.PrivateGram.api.ApiObjects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Comment {
    @JsonProperty("comment")
    public String comment;
    @JsonProperty("postId")
    public int postId;
    @JsonProperty("commentId")
    public int commentId;
}
