package com.example.demo.src.purchase.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetPurchaseRes {

    private int purchaseIdx;
    private String imgUrl;
    private String status;
    private String productName;
    private int prices;
    private String storeName;
    private String createdAt;


}
