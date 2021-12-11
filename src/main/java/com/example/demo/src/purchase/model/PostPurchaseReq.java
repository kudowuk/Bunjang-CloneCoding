package com.example.demo.src.purchase.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostPurchaseReq {

    private int productIdx;
    private int addressIdx;
    private String requestMsg;
    private String purchaseType;
    private int points;
}
