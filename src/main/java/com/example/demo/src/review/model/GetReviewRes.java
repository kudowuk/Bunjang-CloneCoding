package com.example.demo.src.review.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class GetReviewRes {

    private String userName;
    private int reviewScore;
    private Timestamp createdAt;
    private String menuName;
    private String reviewContent;
    private String reviewImg1;
    private String reviewImg2;
    private String reviewImg3;
    private String reviewImg4;
    private String reviewImg5;
    private int cntHelp;
}
