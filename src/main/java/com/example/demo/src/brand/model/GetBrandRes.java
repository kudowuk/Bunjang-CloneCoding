package com.example.demo.src.brand.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetBrandRes {

    private int brandIdx;
    private String brandKorean;
    private String brandEnglish;
    private String imgUrl;
    private int cntFollowers;
    private String statusFollow;
}
