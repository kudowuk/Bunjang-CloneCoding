package com.example.demo.src.follow;

import com.example.demo.config.BaseException;
import com.example.demo.src.follow.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class FollowProvider {

    private final FollowDao followDao;

    @Autowired
    public FollowProvider(FollowDao followDao) {
        this.followDao = followDao;
    }

    // GET 팔로워 조회 API
    public List<GetFollowerRes> getFollowers(int userIdx) throws BaseException {
        try {
            List<GetFollowerRes> getFollowerRes = followDao.getFollowers(userIdx);
            return getFollowerRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // GET 팔로잉 조회 API
    public List<GetFollowingRes> getFollowings(int userIdx) throws BaseException {
        try{
            List<GetFollowingRes> getFollowingRes = followDao.getFollowings(userIdx);
            return getFollowingRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }


}
