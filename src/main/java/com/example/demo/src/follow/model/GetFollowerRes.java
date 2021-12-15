package com.example.demo.src.follow.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetFollowerRes {

    private int followIdx;
    private String profile;
    private String storeName;
    private int cntProducts;
    private int cntFollowers;


}
