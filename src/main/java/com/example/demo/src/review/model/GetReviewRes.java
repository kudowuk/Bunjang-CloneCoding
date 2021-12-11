package com.example.demo.src.review.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class GetReviewRes {

    private int reviewIdx;
    private String storeName;
    private int score;
    private String content;
    private String productName;
    private String imgUrl1;
    private String imgUrl2;
    private String imgUrl3;
    private Timestamp createdAt;
}
