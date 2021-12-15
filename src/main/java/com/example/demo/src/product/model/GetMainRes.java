package com.example.demo.src.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetMainRes {

    private int productIdx;
    private int prices;
    private String productName;
    private String areaName;
    private String createdAt;
    private String safePayment;
    private String conditions;
    private int cntLikes;
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

    private List<ProductImg> imgList;
    private List<ProductTag> tagList;


}
