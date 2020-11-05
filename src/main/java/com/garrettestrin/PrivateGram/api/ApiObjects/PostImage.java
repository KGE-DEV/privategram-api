package com.garrettestrin.PrivateGram.api.ApiObjects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.io.InputStream;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostImage {
    private String name;
    private String type;
    private int height;
    private int width;
    private InputStream file;
}
