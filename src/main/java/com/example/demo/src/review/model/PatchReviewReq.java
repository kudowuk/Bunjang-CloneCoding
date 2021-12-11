package com.example.demo.src.review.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PatchReviewReq {

    private int reviewIdx;
    private int userIdx;
    private int orderIdx;
    private int storeIdx;
    private int reviewScore;
    private String reviewContent;
    private String reviewImg1;
    private String reviewImg2;
    private String reviewImg3;
    private String reviewImg4;
    private String reviewImg5;
    private String deliveryLike;
    private String status;
}
