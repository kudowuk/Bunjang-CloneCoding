package com.example.demo.src.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProductImg {

    private int productImgIdx;
    private int productIdx;
    private String imgUrl;

}
