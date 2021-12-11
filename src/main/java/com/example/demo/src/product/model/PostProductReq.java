package com.example.demo.src.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PostProductReq {

    private String productName;
    private int subcategoryIdx;
    private String content;
    private int prices;
    private String freeShipping;
    private String negotiable;
    private int areaIdx;
    private int quantity;
    private String conditions;
    private String changes;

    private List<PostProductImgReq> imgList;
    private List<PostProductTagReq> tagList;
}
