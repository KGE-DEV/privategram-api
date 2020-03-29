package com.garrettestrin.PrivateGram.api.ApiObjects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Post {
  @JsonProperty("postId")
  public int postId;
  @JsonProperty("postContent")
  public String postContent;
  @JsonProperty("postImageUrl")
  public String postImageUrl;
}



