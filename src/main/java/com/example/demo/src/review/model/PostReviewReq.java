package com.example.demo.src.review.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostReviewReq {

   private int score;
   private String content;
   private String imgUrl1;
   private String imgUrl2;
   private String imgUrl3;
}
