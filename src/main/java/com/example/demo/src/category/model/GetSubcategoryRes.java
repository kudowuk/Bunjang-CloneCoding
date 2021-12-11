package com.example.demo.src.category.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetSubcategoryRes {

    private int subcategoryIdx;
    private String subcategoryName;
    private String imgUrl;

    private List<Goods> goodsList;
}
