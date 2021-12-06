package com.example.demo.src.user.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class GetUserRes {
    private int userIdx;
    private String storeName;
    private Float avgScore;
    private int cntLikes;
    private int cntReviews;
    private int cntFollowers;
    private int cntFollowings;
}