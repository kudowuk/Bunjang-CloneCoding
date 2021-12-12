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

    private List<ProductImg> imgArray;
    private List<ProductTag> tagArray;

}
