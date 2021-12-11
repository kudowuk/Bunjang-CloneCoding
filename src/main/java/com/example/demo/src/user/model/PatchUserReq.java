package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class PatchUserReq {
    private int userIdx;
    private String profiles;
    private String StoreName;
    private String storeAddress;
    private Time contactableFrom;
    private Time contactableTo;
    private String storeIntro;
    private String tradePolicy;
    private String flag;
}
