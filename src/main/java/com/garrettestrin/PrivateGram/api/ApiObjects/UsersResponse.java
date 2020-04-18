package com.garrettestrin.PrivateGram.api.ApiObjects;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsersResponse {
  public boolean success;
  public List<User> users;
}
