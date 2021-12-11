package com.example.demo.src.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProductTag {

    private int productTagIdx;
    private int productIdx;
    private String tagName;
}
