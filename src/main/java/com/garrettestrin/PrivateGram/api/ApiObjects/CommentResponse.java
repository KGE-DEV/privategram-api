package com.garrettestrin.PrivateGram.api.ApiObjects;

import com.garrettestrin.PrivateGram.data.DataObjects.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponse {
  boolean success;
  String message;
  List<Comment> comments;
}
