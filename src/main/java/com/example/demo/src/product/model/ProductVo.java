package com.example.demo.src.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class ProductVo {

    private int productIdx;
    private int prices;
    private String productName;
    private String areaName;
    private Timestamp createdAt;
    private String safePayment;
    private int cntLikes;
    private String conditions;
    private String freeShipping;
    private String negotiable;
    private String changes;
    private int quantity;
    private String content;
    private String subcategoryName;
    private String storeName;
    private int cntFollowers;
    private float avgScores;
    private String statusLike;

}

//productIdx
//price
//productName
//tradeArea
//condition
//freeShipping
//quantity
//content
//subcategoryName
//storeName