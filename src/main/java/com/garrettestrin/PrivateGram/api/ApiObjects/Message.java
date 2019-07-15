package com.garrettestrin.PrivateGram.api.ApiObjects;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// TODO: JAVADOC
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    public String message;
    public boolean success;
    public int code;
    public String jwt;
}
