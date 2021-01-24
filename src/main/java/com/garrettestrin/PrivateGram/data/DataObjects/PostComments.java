package com.garrettestrin.PrivateGram.data.DataObjects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostComments {
    public int id;
    public String comment;
    public int post_id;
    public int user_id;
    public String name;
}
