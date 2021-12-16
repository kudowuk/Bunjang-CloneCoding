package com.example.demo.src.recent;

import com.example.demo.config.BaseException;
import com.example.demo.src.recent.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
@Transactional(rollbackFor = BaseException.class)
public class RecentService {

    private final RecentDao recentDao;
    private final RecentProvider recentProvider;

    @Autowired
    public RecentService(RecentDao recentDao, RecentProvider recentProvider) {
        this.recentDao = recentDao;
        this.recentProvider = recentProvider;
    }

    // POST 최근 본 상품 등록 API
    public PostLookedRes createLooked(int userIdx, PostLookedReq postLookedReq) throws BaseException {
        try {
            int lookedIdx = recentDao.createLooked(userIdx, postLookedReq);
            return new PostLookedRes(lookedIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // POST 최근 검색어 등록 API
    public PostSearchedRes createSearched(int userIdx, PostSearchedReq postSearchedReq) throws BaseException {
        try {
            int searchedIdx = recentDao.createSearched(userIdx, postSearchedReq);
            return new PostSearchedRes(searchedIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // DELETE 최근 본 상품 삭제 API
    public void deleteLooked(int userIdx, int lookedIdx) throws BaseException{
        try {
            recentDao.deleteLooked(userIdx, lookedIdx);
        }catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // DELETE 최근 검색어 삭제 API
    public void deleteSearched(int userIdx, int searchedIdx) throws BaseException{
        try {
            recentDao.deleteSearched(userIdx, searchedIdx);
        }catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }


}
