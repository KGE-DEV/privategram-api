package com.garrettestrin.PrivateGram.data.DataObjects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Invite {
  public int id;
  public String email;
  public String name;
}
