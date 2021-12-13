package com.example.demo.src.like;

import com.example.demo.config.BaseException;
import com.example.demo.src.like.model.GetLikeRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class LikeProvider {

    private final LikeDao likeDao;

    @Autowired
    public LikeProvider(LikeDao likeDao) {
        this.likeDao = likeDao;
    }

    public List<GetLikeRes> getLikes(int userIdx) throws BaseException {
        try{
            List<GetLikeRes> getLikeRes = likeDao.getLikes(userIdx);
            return getLikeRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }





}
