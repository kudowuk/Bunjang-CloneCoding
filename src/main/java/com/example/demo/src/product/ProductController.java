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
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();

            List<GetMainRes> getMainRes = productProvider.getMains(userIdxByJwt);
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
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();

            GetProductRes getProductRes = productProvider.getProduct(userIdxByJwt, productIdx);
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

        // 상품명명 입력하기
        if(postProductReq.getProductName() == null || postProductReq.getProductName().equals("")){
            return new BaseResponse<>(POST_PRODUCTS_EMPTY_PRODUCTNAME);
        }

        // 서브카테고리 입력하기
        if(postProductReq.getSubcategoryIdx() == null){
            return new BaseResponse<>(POST_PRODUCTS_EMPTY_SUBCATEGORYIDX);
        }
//        if(postProductReq.getSubcategoryIdx() == Integer.parseInt(null)){
//            return new BaseResponse<>(POST_USERS_EMPTY_PRODUCTNAME);
//        }

        // 상품설명 길이 제한
        if(postProductReq.getContent().length() > 2000){
            return new BaseResponse<>(POST_PRODUCTS_LENGTH_CONTENT);
        }

        // 배송비 여부를 입력하기
        if(postProductReq.getFreeShipping() == null || postProductReq.getFreeShipping().isEmpty()){
            return new BaseResponse<>(POST_PRODUCTS_EMPTY_FREESHIPPING);
        }
        // 배송비 포함에 'Y':포함 또는 'N'비포함 한 글자만 입력하기
        if(!postProductReq.getFreeShipping().equals("Y") && !postProductReq.getFreeShipping().equals("N")){
            return new BaseResponse<>(POST_PRODUCTS_INVALID_FREESHIPPING);
        }

        // 협의 여부를 입력하기
        if(postProductReq.getNegotiable() == null || postProductReq.getNegotiable().isEmpty()){
            return new BaseResponse<>(POST_PRODUCTS_EMPTY_NEGOTIABLE);
        }
        // 배송비 포함에 'Y':포함 또는 'N'비포함 한 글자만 입력하기
        if(!postProductReq.getNegotiable().equals("Y") && !postProductReq.getNegotiable().equals("N")){
            return new BaseResponse<>(POST_PRODUCTS_INVALID_NEGOTIABLE);
        }

        // 개수를 입력하기
        if(postProductReq.getQuantity() == null){
            return new BaseResponse<>(POST_PRODUCTS_EMPTY_QUANTITY);
        }
        // 개수 범위 1~999개 사이 입력하기
        if(postProductReq.getQuantity() < 1 || postProductReq.getQuantity() > 999){
            return new BaseResponse<>(POST_PRODUCTS_RANGE_QUANTITY);
        }

        // 상품 상태를 입력하기
        if(postProductReq.getConditions() == null || postProductReq.getConditions().isEmpty()){
            return new BaseResponse<>(POST_PRODUCTS_EMPTY_CONDITIONS);
        }
        // 상품 상태에 'U'(Used):중고상품 또는 'N'(New): 새상품 중 한 글자만 입력하기
        if(!postProductReq.getConditions().equals("U") && !postProductReq.getConditions().equals("N")){
            return new BaseResponse<>(POST_PRODUCTS_INVALID_CONDITIONS);
        }

        // 교환 여부를 입력하기[
        if(postProductReq.getConditions() == null || postProductReq.getConditions().isEmpty()){
            return new BaseResponse<>(POST_PRODUCTS_EMPTY_CHANGES);
        }
        // 상품 상태에 'U'(Used):중고상품 또는 'N'(New): 새상품 중 한 글자만 입력하기
        if(!postProductReq.getConditions().equals("U") && !postProductReq.getConditions().equals("N")){
            return new BaseResponse<>(POST_PRODUCTS_INVALID_CHANGES);
        }

        // 이미지 리스트 입력하기
        if(postProductReq.getImgList() == null || postProductReq.getImgList().isEmpty()){
            return new BaseResponse<>(POST_PRODUCTS_EMPTY_IMGLIST);
        }
        // 이미지 리스트 최대 12개
        if(postProductReq.getImgList().size() > 12){
            return new BaseResponse<>(POST_PRODUCTS_MAX_IMAGELIST);
        }
        // 태그 리스트 최대 5개
        if(postProductReq.getTagList().size() > 5){
            return new BaseResponse<>(POST_PRODUCTS_MAX_IMAGELIST);
        }
        for (PostProductImgReq img : postProductReq.getImgList()){
            // 이미지URL 입력하기
            if (img.getImgUrl() == null || img.getImgUrl().isEmpty()){
                return new BaseResponse<>(POST_PRODUCTS_EMPTY_IMAGEURL);
            }
            // 이미지 URL 문자열 1000자 이하
            else if (img.getImgUrl().length() > 1000){
                return new BaseResponse<>(POST_PRODUCTS_LENGTH_IMAGEURL);
            }
        }

        for (PostProductTagReq tag : postProductReq.getTagList()) {
            // 태그이름 최대 9자
            if (tag.getTagName().length() > 9) {
                return new BaseResponse<>(POST_PRODUCTS_LENGTH_TAGNAME);
            }
        }


        try{
            // 유저 유무 확인
            if(userDao.checkUserIdx(userIdx) == 0) {
                throw new BaseException(NOT_EXIST_USER);
            }
            // 유저 탈퇴 확인
            if(userDao.checkStatusUserIdx(userIdx) == 1) {
                throw new BaseException(BREAKAWAY_USER);
            }

            // 서브카테고리 유무 확인
            if(productDao.checkSubcategoryIdx(postProductReq.getSubcategoryIdx()) == 0) {
                throw new BaseException(NOT_EXIST_SUBCATEGORY);
            }
            // 서브카테고리 활성화 확인
            if(productDao.checkStatusSubcategoryIdx(postProductReq.getSubcategoryIdx()) == 1) {
                throw new BaseException(INACTIVE_SUBCATEGORY);
            }

            // 거래지역 활성화 확인
            if(productDao.checkStatusAreaIdx(postProductReq.getAreaIdx()) == 1) {
                throw new BaseException(INACTIVE_AREA);
            }

            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }

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

            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }

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