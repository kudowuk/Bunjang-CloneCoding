package com.example.demo.src.category;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.category.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/app/categories")
public class CategoryController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final CategoryProvider categoryProvider;
    @Autowired
    private final JwtService jwtService;

    public CategoryController(CategoryProvider categoryProvider, JwtService jwtService){
        this.categoryProvider = categoryProvider;
        this.jwtService = jwtService;
    }

    // GET 전체 메뉴 조회
    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<GetCategoryRes>> getCategories() {
        try {

            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();

            List<GetCategoryRes> getCategoryRes = categoryProvider.getCategories();
            return new BaseResponse<>(getCategoryRes);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    // GET 하위 범주 상품 조회 API
    @ResponseBody
    @GetMapping("/{subcategoryIdx}") // (GET) 127.0.0.1:9000/app/categorys/categoryIdx?=1
    public BaseResponse<GetSubcategoryRes> getSubcategory(@PathVariable("subcategoryIdx") int subcategoryIdx) {
        // Get Category
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();

            GetSubcategoryRes getSubcategoryRes = categoryProvider.getSubcategory(userIdxByJwt, subcategoryIdx);
            return new BaseResponse<>(getSubcategoryRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }

    }



}
