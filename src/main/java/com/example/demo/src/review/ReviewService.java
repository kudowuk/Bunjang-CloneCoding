package com.example.demo.src.review;

import com.example.demo.config.BaseException;
import com.example.demo.src.review.model.*;
import com.example.demo.src.review.model.PatchReviewReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
@Transactional(rollbackFor = BaseException.class)
public class ReviewService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ReviewDao reviewDao;
    private final ReviewProvider reviewProvider;

    @Autowired
    public ReviewService(ReviewDao reviewDao, ReviewProvider reviewProvider) {
        this.reviewDao = reviewDao;
        this.reviewProvider = reviewProvider;
    }

    //POST
    public PostReviewRes createReview(int userIdx, int purchaseIdx, PostReviewReq postReviewReq) throws BaseException {
        try {
            int reviewIdx = reviewDao.createReview(userIdx, purchaseIdx, postReviewReq);
            return new PostReviewRes(reviewIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // PATCH
    public void modifyReview(PatchReviewReq patchReviewReq) throws BaseException {
        try {
            int result = reviewDao.modifyReview(patchReviewReq);
            if (result == 0) {
                throw new BaseException(MODIFY_FAIL_REVIEW);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
