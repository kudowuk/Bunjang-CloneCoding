package com.example.demo.src.follow;

import com.example.demo.config.BaseException;
import com.example.demo.src.follow.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;
import static com.example.demo.config.BaseResponseStatus.MODIFY_FAIL_LIKE;

@Service
@Transactional(rollbackFor = BaseException.class)
public class FollowService {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final FollowDao followDao;
    private final FollowProvider followProvider;

    @Autowired
    public FollowService(FollowDao followDao, FollowProvider followProvider) {
        this.followDao = followDao;
        this.followProvider = followProvider;
    }

    // POST 즐겨찾기 등록 API
    public PostFollowRes createFollow(int userIdx, PostFollowReq postFollowReq) throws BaseException {

        try {
            int followIdx = followDao.createFollow(userIdx, postFollowReq);
            return new PostFollowRes(followIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void modifyFollow(PatchFollowReq patchFollowReq) throws BaseException {
        try {
            int result = followDao.modifyFollow(patchFollowReq);
            if (result == 0) {
                throw new BaseException(MODIFY_FAIL_LIKE);
            }
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }


}
