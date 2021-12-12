package com.example.demo.src.product;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.product.model.*;
import com.example.demo.src.user.UserDao;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/app/products")
public class ProductController {


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final ProductProvider productProvider;
    @Autowired
    private final ProductService productService;
    @Autowired
    private final JwtService jwtService;
    @Autowired
    private final ProductDao productDao;
    @Autowired
    private final UserDao userDao;

    public ProductController(ProductProvider productProvider, ProductService productService, JwtService jwtService, ProductDao productDao, UserDao userDao){
        this.productProvider = productProvider;
        this.productService = productService;
        this.jwtService = jwtService;
        this.productDao = productDao;
        this.userDao = userDao;
    }



    /**
     * 상품 조회 API
     * [GET] /products/
     */
    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<GetMainRes>> getMains() {
        try {

//            //jwt에서 idx 추출.
//            int userIdxByJwt = jwtService.getUserIdx();
//            //userIdx와 접근한 유저가 같은지 확인
//            if(userIdx != userIdxByJwt) {
//                return new BaseResponse<>(INVALID_USER_JWT);
//            }

            List<GetMainRes> getMainRes = productProvider.getMains();
            return new BaseResponse<>(getMainRes);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    /**
     * 특정 상품 조회 API
     * [GET] /products/
     */
    @ResponseBody
    @GetMapping("/{productIdx}") // (GET) 127.0.0.1:9000/app/products/productIdx?=1
    public BaseResponse<GetProductRes> getProduct(@PathVariable("productIdx") int productIdx) {
        // Get Product
        try {

//            //jwt에서 idx 추출.
//            int userIdxByJwt = jwtService.getUserIdx();
//            //userIdx와 접근한 유저가 같은지 확인
//            if(userIdx != userIdxByJwt) {
//                return new BaseResponse<>(INVALID_USER_JWT);
//            }

            GetProductRes getProductRes = productProvider.getProduct(productIdx);
            return new BaseResponse<>(getProductRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }




    }

    /**
     * 상품 등록 API
     * [POST] /products/
     */
    @ResponseBody
    @PostMapping("/{userIdx}")
    public BaseResponse<PostProductRes> createProduct(@PathVariable("userIdx") int userIdx, @RequestBody PostProductReq postProductReq) {
        try{
            // 유저 유무 확인
            if(userDao.checkUserIdx(userIdx) == 0) {
                throw new BaseException(NOT_EXIST_USER);
            }
            // 유저 탈퇴 확인
            if(userDao.checkStatusUserIdx(userIdx) == 1) {
                throw new BaseException(BREAKAWAY_USER);
            }

//            //jwt에서 idx 추출.
//            int userIdxByJwt = jwtService.getUserIdx();
//            //userIdx와 접근한 유저가 같은지 확인
//            if(userIdx != userIdxByJwt) {
//                return new BaseResponse<>(INVALID_USER_JWT);
//            }

            PostProductRes postProductRes = productService.createProduct(userIdx, postProductReq);
            return new BaseResponse<>(postProductRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }



    /**
     * 특정 상품 수정 API
     * [PATCH] /products/
     */
    @ResponseBody
    @PatchMapping("/{userIdx}/{productIdx}")
    public BaseResponse<String> modifyProduct(@PathVariable("userIdx") int userIdx, @PathVariable("productIdx") int productIdx, @RequestBody Product product){
        try {

//            //jwt에서 idx 추출.
//            int userIdxByJwt = jwtService.getUserIdx();
//            //userIdx와 접근한 유저가 같은지 확인
//            if(userIdx != userIdxByJwt) {
//                return new BaseResponse<>(INVALID_USER_JWT);
//            }
            PatchProductReq patchProductReq = new PatchProductReq(userIdx, productIdx, product.getProductName(), product.getSubcategoryIdx(), product.getContent(), product.getPrices(), product.getFreeShipping(), product.getNegotiable(), product.getAreaName(), product.getQuantity(), product.getConditions(), product.getChanges());
            productService.modifyProduct(patchProductReq);
            String result = "";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }



}
//userIdx, productIdx, product.getProductName(), product.getSubcategoryIdx(), product.getContent(), product.getPrice(), product.getFreeShipping(), product.getTradeArea(), product.getQuantity(), product.getConditions(), product.getChanges()