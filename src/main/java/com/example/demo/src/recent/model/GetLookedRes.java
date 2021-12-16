package com.example.demo.src.recent.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetLookedRes {

    private int lookedIdx;
    private String imgUrl;
    private String status;
    private String productName;
    private int prices;
    private String createdAt;

}
