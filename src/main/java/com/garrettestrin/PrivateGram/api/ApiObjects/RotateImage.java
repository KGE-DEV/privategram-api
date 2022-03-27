package com.garrettestrin.PrivateGram.api.ApiObjects;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RotateImage {
    @JsonProperty
    public String imgUrl;
    public int rotation;
}
