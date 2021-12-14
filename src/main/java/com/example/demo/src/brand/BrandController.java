package com.example.demo.src.brand;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.brand.model.GetBrandRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/app/brands")
public class BrandController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final BrandProvider brandProvider;
    @Autowired
    private final JwtService jwtService;


    public BrandController(BrandProvider brandProvider, JwtService jwtService) {
        this.brandProvider = brandProvider;
        this.jwtService = jwtService;
    }

    // GET 전체 브랜드 조회 API(전체메뉴 -> 브랜드)
    @GetMapping("") // (GET) 127.0.0.1:8000/app/brands
    public BaseResponse<List<GetBrandRes>> getBrands() {

        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();

            List<GetBrandRes> getBrandRes = brandProvider.getBrands(userIdxByJwt);
            return new BaseResponse<>(getBrandRes);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}
