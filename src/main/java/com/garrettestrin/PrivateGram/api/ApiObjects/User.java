package com.garrettestrin.PrivateGram.api.ApiObjects;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @JsonProperty
    private long id;
    private String email;
    private String first_name;
    private String last_name;
    private String password;
}