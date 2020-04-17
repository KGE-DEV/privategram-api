package com.garrettestrin.PrivateGram.api.ApiObjects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Invite {
  @JsonProperty("email")
  public String email;
  @JsonProperty("name")
  public String name;
}
