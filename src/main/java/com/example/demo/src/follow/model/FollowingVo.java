package com.example.demo.src.follow.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FollowingVo {

    private int followIdx;
    private String profiles;
    private String storeName;
    private int cntProducts;
    private int cntFollowers;

}
