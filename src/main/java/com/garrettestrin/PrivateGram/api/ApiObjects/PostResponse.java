package com.garrettestrin.PrivateGram.api.ApiObjects;

import com.garrettestrin.PrivateGram.data.DataObjects.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostResponse {
  boolean success;
  String message;
  List<Post> posts;
}
