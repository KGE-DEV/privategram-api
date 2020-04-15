package com.garrettestrin.PrivateGram.data.DataObjects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
  public int id;
  public String name;
  public String email;
  public String password;
  public String role;
  public boolean  wp_pass;
}
