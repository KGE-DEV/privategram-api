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
    public long id;
    public String email;
    public String name;
    public String role;
    public boolean wp_pass;
}