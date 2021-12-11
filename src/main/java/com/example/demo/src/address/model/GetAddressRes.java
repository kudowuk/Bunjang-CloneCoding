package com.example.demo.src.address.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetAddressRes {

    private int addressIdx;
    private String recipient;
    private String phone;
    private String latitude;
    private String longitude;
    private String roadName;
    private String detailedAddress;
    private String requestMsg;
    private String status;
}
