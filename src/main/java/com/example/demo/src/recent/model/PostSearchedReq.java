package com.example.demo.src.recent.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostSearchedReq {

    private int userIdx;
    private String word;

}
