package com.example.demo.src.like;

import com.example.demo.config.BaseException;
import com.example.demo.src.like.model.PatchLikeReq;
import com.example.demo.src.like.model.PostLikeReq;
import com.example.demo.src.like.model.PostLikeRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;
import static com.example.demo.config.BaseResponseStatus.MODIFY_FAIL_LIKE;

@Service
@Transactional(rollbackFor = BaseException.class)
public class LikeService {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final LikeDao likeDao;
    private final LikeProvider likeProvider;

    @Autowired
    public LikeService(LikeDao likeDao, LikeProvider likeProvider) {
        this.likeDao = likeDao;
        this.likeProvider = likeProvider;
    }

    // POST 즐겨찾기 등록 API
    public PostLikeRes createLike(int userIdx, PostLikeReq postLikeReq) throws BaseException {

        try {
            int likeIdx = likeDao.createLike(userIdx, postLikeReq);
            return new PostLikeRes(likeIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void modifyLike(PatchLikeReq patchLikeReq) throws BaseException {
        try {
            int result = likeDao.modifyLike(patchLikeReq);
            if (result == 0) {
                throw new BaseException(MODIFY_FAIL_LIKE);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }




}
