package com.garrettestrin.PrivateGram.api.ApiObjects;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Event {
  @JsonProperty("userId")
  int userId;
  @JsonProperty("event")
  String event;
  @JsonProperty("page")
  String page;
  @JsonProperty("meta")
  String meta;
}
