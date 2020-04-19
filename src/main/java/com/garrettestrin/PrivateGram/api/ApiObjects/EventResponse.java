package com.garrettestrin.PrivateGram.api.ApiObjects;

import com.garrettestrin.PrivateGram.data.DataObjects.Event;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventResponse {
  public boolean success;
  public List<Event> events;
  public String message;
}
