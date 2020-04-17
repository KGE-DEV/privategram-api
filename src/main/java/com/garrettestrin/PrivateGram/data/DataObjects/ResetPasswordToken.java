package com.garrettestrin.PrivateGram.data.DataObjects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordToken {
    public String token;
    public String expiration;
}
