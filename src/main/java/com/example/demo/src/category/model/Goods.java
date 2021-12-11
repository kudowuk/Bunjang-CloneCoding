package com.example.demo.src.category.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Goods {

    private int productIdx;
    private String imgUrl;
    private int prices;
    private String productName;
}
