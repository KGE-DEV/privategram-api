package com.garrettestrin.PrivateGram.api.ApiObjects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponse {
  boolean success;
  String message;
}
