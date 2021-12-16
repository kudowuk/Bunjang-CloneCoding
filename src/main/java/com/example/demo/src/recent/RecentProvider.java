package com.example.demo.src.recent;

import com.example.demo.config.BaseException;
import com.example.demo.src.recent.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class RecentProvider {

    private final RecentDao recentDao;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public RecentProvider(RecentDao recentDao) {
        this.recentDao = recentDao;
    }

    public List<GetLookedRes> getLooked(int userIdx) throws BaseException {

        List<GetLookedRes> getLookedRes = recentDao.getLooked(userIdx);
        return getLookedRes;
//        try{
//            List<GetLookedRes> getLookedRes = recentDao.getLooked(userIdx);
//            return getLookedRes;
//        }
//        catch (Exception exception) {
//            throw new BaseException(DATABASE_ERROR);
//        }
    }

    public List<GetSearchedRes> getSearched(int userIdx) throws BaseException {
        try{
            List<GetSearchedRes> getSearchedRes = recentDao.getSearched(userIdx);
            return getSearchedRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
