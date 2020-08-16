package com.garrettestrin.PrivateGram.app.Auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class AuthenticatedUser {
  public int userId;
  public String token;
  public boolean isAdmin;
}
