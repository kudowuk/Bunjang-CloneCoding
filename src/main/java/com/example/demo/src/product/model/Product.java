package com.example.demo.src.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Product {
    private String productName;
    private Integer subcategoryIdx;
    private String content;
    private int prices;
    private String freeShipping;
    private String negotiable;
    private String areaName;
    private Integer quantity;
    private String conditions;
    private String changes;
}
