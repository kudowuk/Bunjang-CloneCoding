package com.example.demo.src.category.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
public class Goods {

    private int productIdx;
    private String imgUrl;
    private int prices;
    private String productName;
    private String areaName;
    private Timestamp createdAt;
    private String safePayment;
    private int cntLikes;
}
