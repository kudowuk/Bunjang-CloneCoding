package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;

@Getter
@Setter
@AllArgsConstructor
public class Store {

    private String profiles;
    private String storeName;
    private String storeAddress;
    private String storeIntro;
    private Time contactableFrom;
    private Time contactableTo;
    private String tradePolicy;
    private String flag;
}
