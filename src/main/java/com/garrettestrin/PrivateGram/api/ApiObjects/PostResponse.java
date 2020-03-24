package com.garrettestrin.PrivateGram.api.ApiObjects;

import com.garrettestrin.PrivateGram.data.DataObjects.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {
  boolean success;
  String message;
  List<Post> posts;
}
