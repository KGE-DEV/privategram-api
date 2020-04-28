package com.garrettestrin.PrivateGram.data.DataObjects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Post {
  public int id;
  public String post_content;
  public String post_image_url;
  public String date_time_added;
}
