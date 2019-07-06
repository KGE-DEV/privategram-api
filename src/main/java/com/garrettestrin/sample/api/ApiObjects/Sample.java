package com.garrettestrin.sample.api.ApiObjects;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Sample {
    @JsonProperty
    private String id;
}