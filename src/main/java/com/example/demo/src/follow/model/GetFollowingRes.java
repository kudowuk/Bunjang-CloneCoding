package com.example.demo.src.follow.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetFollowingRes {

    private int followIdx;
    private String profiles;
    private String storeName;
    private int cntProducts;
    private int cntFollowers;

    private List<ProductVo> productList;

}
