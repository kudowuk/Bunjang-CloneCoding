package com.example.demo.src.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PatchProductReq {

    private int userIdx;
    private int productIdx;
    private String productName;
    private int subcategoryIdx;
    private String content;
    private int prices;
    private String freeShipping;
    private String negotiable;
    private String areaIdx;
    private int quantity;
    private String conditions;
    private String changes;

}
