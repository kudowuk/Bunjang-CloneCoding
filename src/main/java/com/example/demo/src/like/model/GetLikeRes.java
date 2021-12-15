package com.example.demo.src.like.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetLikeRes {

    private int likeIdx;
    private String imgUrl;
    private String safePayment;
    private String likeStatus;
    private String productStatus;
    private String productName;
    private int prices;
    private String storeName;
    private String createdAt;
}
