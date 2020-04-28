package com.garrettestrin.PrivateGram.data.DataObjects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Event {
  public int id;
  public int user_id;
  public String name;
  public String event;
  public String page;
  public String meta;
  public String date_time;
}
