package com.example.demo.src.purchase;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.purchase.model.GetPurchaseerRes;
import com.example.demo.src.purchase.model.*;
import com.example.demo.src.user.UserDao;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/app/purchases")
public class PurchaseController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final PurchaseProvider purchaseProvider;
    @Autowired
    private final PurchaseService purchaseService;
    @Autowired
    private final JwtService jwtService;
    @Autowired
    private final UserDao userDao;

    public PurchaseController(PurchaseProvider purchaseProvider, PurchaseService purchaseService, JwtService jwtService, UserDao userDao){
        this.purchaseProvider = purchaseProvider;
        this.purchaseService = purchaseService;
        this.jwtService = jwtService;
        this.userDao = userDao;
    }

    // GET 구매내역 조회
    @GetMapping("/{userIdx}/purchased") // (GET) 127.0.0.1:8000/app/purchases/userIdx?=1/purchased
    public BaseResponse<List<GetPurchaseRes>> getPurchased(@PathVariable("userIdx") int userIdx) {
        try {
            // 유저 유무 확인
            if(userDao.checkUserIdx(userIdx) == 0) {
                throw new BaseException(NOT_EXIST_USER);
            }
            // 유저 탈퇴 확인
            if(userDao.checkStatusUserIdx(userIdx) == 1) {
                throw new BaseException(BREAKAWAY_USER);
            }


            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            List<GetPurchaseRes> getPurchaseRes = purchaseProvider.getPurchased(userIdx);
            return new BaseResponse<>(getPurchaseRes);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    // GET 판매 내역 조회
    @GetMapping("/{userIdx}/sold") // (GET) 127.0.0.1:8000/app/purchases/userIdx?=1/purchaseers
    public BaseResponse<List<GetPurchaseRes>> getSold(@PathVariable("userIdx") int userIdx) {
        try {
            // 유저 유무 확인
            if(userDao.checkUserIdx(userIdx) == 0) {
                throw new BaseException(NOT_EXIST_USER);
            }
            // 유저 탈퇴 확인
            if(userDao.checkStatusUserIdx(userIdx) == 1) {
                throw new BaseException(BREAKAWAY_USER);
            }


            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            List<GetPurchaseRes> getPurchaseRes = purchaseProvider.getSold(userIdx);
            return new BaseResponse<>(getPurchaseRes);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    // POST 상품 구매등록 API
    @ResponseBody
    @PostMapping("/{userIdx}")
    public BaseResponse<PostPurchaseRes> createPurchase(@PathVariable("userIdx") int userIdx, @RequestBody PostPurchaseReq postPurchaseReq) {
        try{
            // 유저 유무 확인
            if(userDao.checkUserIdx(userIdx) == 0) {
                throw new BaseException(NOT_EXIST_USER);
            }
            // 유저 탈퇴 확인
            if(userDao.checkStatusUserIdx(userIdx) == 1) {
                throw new BaseException(BREAKAWAY_USER);
            }

            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            PostPurchaseRes postPurchaseRes = purchaseService.createPurchase(userIdx, postPurchaseReq);
            return new BaseResponse<>(postPurchaseRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}
