package com.example.demo.src.review;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.review.model.Review;
import com.example.demo.src.review.model.PatchReviewReq;
import com.example.demo.src.review.model.GetReviewRes;
import com.example.demo.src.review.model.*;
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


    public ReviewController(ReviewProvider reviewProvider, ReviewService reviewService, JwtService jwtService){
        this.reviewProvider = reviewProvider;
        this.reviewService = reviewService;
        this.jwtService = jwtService;
    }

    // 가게 스토어 리뷰 조회하기 API
    @ResponseBody
    @GetMapping("/{userIdx}/{storeIdx}")
    public BaseResponse<List<GetReviewRes>> getReviews(@PathVariable("userIdx") int userIdx, @PathVariable("storeIdx") int storeIdx) {
        try {
            List<GetReviewRes> getReviewRes = reviewProvider.getReviews(userIdx, storeIdx);
            return new BaseResponse<>(getReviewRes);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    @ResponseBody
    @GetMapping("/{userIdx}/{reviewIdx}")
    public BaseResponse<GetReviewRes> getReview(@PathVariable("userIdx") int userIdx, @PathVariable("reviewIdx") int reviewIdx) {
        try {
            GetReviewRes getReviewRes = reviewProvider.getReview(userIdx, reviewIdx);
            return new BaseResponse<>(getReviewRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    // POST 유저 리뷰 등록 API
    @ResponseBody
    @PostMapping("/{userIdx}/{orderIdx}")
    public BaseResponse<PostReviewRes> createReview(@PathVariable("userIdx") int userIdx, @PathVariable("orderIdx") int orderIdx, @RequestBody PostReviewReq postReviewReq) {

        if(postReviewReq.getReviewContent().length() > 100) {
            return new BaseResponse<>(POST_REVIEWS_OVER_CONTENT);
        }

        try{
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            PostReviewRes postReviewRes = reviewService.createReview(userIdx, orderIdx, postReviewReq);
            return new BaseResponse<>(postReviewRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    // PATCH 유저 리뷰 수정 API
    @ResponseBody
    @PatchMapping("/{userIdx}/{orderIdx}/{reviewIdx}")
    public BaseResponse<String> modifyReview(@PathVariable("userIdx") int userIdx, @PathVariable("orderIdx") int orderIdx, @PathVariable("reviewIdx") int reviewIdx, @RequestBody Review review){

        if(review.getReviewContent().length() > 100) {
            return new BaseResponse<>(POST_REVIEWS_OVER_CONTENT);
        }


        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            PatchReviewReq patchReviewReq = new PatchReviewReq(userIdx, orderIdx, reviewIdx, review.getStoreIdx(), review.getReviewScore(), review.getReviewContent(), review.getReviewImg1(), review.getReviewImg2(), review.getReviewImg3(), review.getReviewImg4(), review.getReviewImg5(), review.getDeliveryLike(), review.getStatus());
            reviewService.modifyReview(patchReviewReq);

            String result = "";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }



}
