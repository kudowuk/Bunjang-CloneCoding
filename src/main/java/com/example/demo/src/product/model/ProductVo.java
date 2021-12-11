package com.example.demo.src.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProductVo {

    private int productIdx;
    private int prices;
    private String productName;
    private String areaName;
    private String conditions;
    private String freeShipping;
    private String negotiable;
    private String changes;
    private int quantity;
    private String content;
    private String subcategoryName;
    private String storeName;

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