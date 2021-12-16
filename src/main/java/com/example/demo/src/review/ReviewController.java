package com.example.demo.src.review;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.review.model.*;
import com.example.demo.src.review.model.GetReviewRes;
import com.example.demo.src.review.model.PatchReviewReq;
import com.example.demo.src.review.model.Review;
import com.example.demo.src.user.UserDao;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;


@RestController
@RequestMapping("/app/reviews")
public class ReviewController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final ReviewProvider reviewProvider;
    @Autowired
    private final ReviewService reviewService;
    @Autowired
    private final JwtService jwtService;
    @Autowired
    private final UserDao userDao;
    @Autowired
    private final ReviewDao reviewDao;


    public ReviewController(ReviewProvider reviewProvider, ReviewService reviewService, JwtService jwtService, UserDao userDao, ReviewDao reviewDao){
        this.reviewProvider = reviewProvider;
        this.reviewService = reviewService;
        this.jwtService = jwtService;
        this.userDao = userDao;
        this.reviewDao = reviewDao;
    }

    // GET 상점 후기 조회 API
    @ResponseBody
    @GetMapping("/{userIdx}")
    public BaseResponse<List<GetReviewRes>> getReviews(@PathVariable("userIdx") int userIdx) {
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

            List<GetReviewRes> getReviewRes = reviewProvider.getReviews(userIdx);
            return new BaseResponse<>(getReviewRes);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    // POST 상점 후기 등록 API
    @ResponseBody
    @PostMapping("/{userIdx}/{purchaseIdx}")
    public BaseResponse<PostReviewRes> createReview(@PathVariable("userIdx") int userIdx, @PathVariable("purchaseIdx") int purchaseIdx, @RequestBody PostReviewReq postReviewReq) {

        try{
            // 유저 유무 확인
            if(userDao.checkUserIdx(userIdx) == 0) {
                throw new BaseException(NOT_EXIST_USER);
            }
            // 유저 탈퇴 확인
            if(userDao.checkStatusUserIdx(userIdx) == 1) {
                throw new BaseException(BREAKAWAY_USER);
            }

            // 구매한 이력 확인
            if(reviewDao.checkPurchaseIdx(purchaseIdx, userIdx) == 0) {
                throw new BaseException(NOT_EXIST_USER);
            }
            // 구매한 이력 상태 확인
            if(reviewDao.checkStatusPurchaseIdx(purchaseIdx, userIdx) == 1) {
                throw new BaseException(BREAKAWAY_USER);
            }


            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            PostReviewRes postReviewRes = reviewService.createReview(userIdx, purchaseIdx, postReviewReq);
            return new BaseResponse<>(postReviewRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    // PATCH 상점 후기 수정 API
    @ResponseBody
    @PatchMapping("/{userIdx}/{purchaseIdx}/{reviewIdx}")
    public BaseResponse<String> modifyReview(@PathVariable("userIdx") int userIdx, @PathVariable("purchaseIdx") int purchaseIdx, @PathVariable("reviewIdx") int reviewIdx, @RequestBody Review review){


        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            PatchReviewReq patchReviewReq = new PatchReviewReq(userIdx, purchaseIdx, reviewIdx, review.getScore(), review.getContent(), review.getImgUrl1(), review.getImgUrl2(), review.getImgUrl3(), review.getStatus() );
            reviewService.modifyReview(patchReviewReq);

            String result = "상점 후기를 수정하였습니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }



}
