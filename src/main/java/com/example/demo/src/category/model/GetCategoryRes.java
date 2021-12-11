package com.example.demo.src.category.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetCategoryRes {

    private int categoryIdx;
    private String categoryName;

    private List<SubcategoryVo> subcategoryList;
}
