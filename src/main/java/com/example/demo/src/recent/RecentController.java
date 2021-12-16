package com.example.demo.src.recent;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.product.ProductDao;
import com.example.demo.src.recent.model.*;
import com.example.demo.src.user.UserDao;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/app/recent")
public class RecentController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final RecentProvider recentProvider;
    @Autowired
    private final RecentService recentService;
    @Autowired
    private final JwtService jwtService;
    @Autowired
    private final UserDao userDao;
    @Autowired
    private final ProductDao productDao;
    @Autowired
    private final RecentDao recentDao;

    public RecentController(RecentProvider recentProvider, RecentService recentService, JwtService jwtService, UserDao userDao, ProductDao productDao, RecentDao recentDao) {
        this.recentProvider = recentProvider;
        this.recentService = recentService;
        this.jwtService = jwtService;
        this.userDao = userDao;
        this.productDao = productDao;
        this.recentDao = recentDao;
    }

    // GET 최근 본 상품 조회 API
    @ResponseBody
    @GetMapping("/looked")
    public BaseResponse<List<GetLookedRes>> getLooked() {
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();

            // 유저 유무 확인
            if(userDao.checkUserIdx(userIdxByJwt) == 0) {
                throw new BaseException(NOT_EXIST_USER);
            }
            // 유저 탈퇴 확인
            if(userDao.checkStatusUserIdx(userIdxByJwt) == 1) {
                throw new BaseException(BREAKAWAY_USER);
            }


            List<GetLookedRes> getLookedRes = recentProvider.getLooked(userIdxByJwt);
            return new BaseResponse<>(getLookedRes);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    // GET 최근 검색어 조회 API
    @ResponseBody
    @GetMapping("/searched")
    public BaseResponse<List<GetSearchedRes>> getRecents() {
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();

            // 유저 유무 확인
            if(userDao.checkUserIdx(userIdxByJwt) == 0) {
                throw new BaseException(NOT_EXIST_USER);
            }
            // 유저 탈퇴 확인
            if(userDao.checkStatusUserIdx(userIdxByJwt) == 1) {
                throw new BaseException(BREAKAWAY_USER);
            }

            List<GetSearchedRes> getSearchedRes = recentProvider.getSearched(userIdxByJwt);
            return new BaseResponse<>(getSearchedRes);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    // POST 최근 본 상품 등록 API
    @ResponseBody
    @PostMapping("/looked")
    public BaseResponse<PostLookedRes> createLooked(@RequestBody PostLookedReq postLookedReq) {

        try{
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();

            // 유저 유무 확인
            if(userDao.checkUserIdx(userIdxByJwt) == 0) {
                throw new BaseException(NOT_EXIST_USER);
            }
            // 유저 탈퇴 확인
            if(userDao.checkStatusUserIdx(userIdxByJwt) == 1) {
                throw new BaseException(BREAKAWAY_USER);
            }

            // 상품 유무 확인
            if(productDao.checkProductIdx(postLookedReq.getProductIdx()) == 0) {
                throw new BaseException(NOT_EXIST_PRODUCT);
            }
            // 최근 본 상품에 이미 들어 있는지 확인
            if(recentDao.checkDuplicateProductIdx(postLookedReq.getProductIdx()) == 1) {
                throw new BaseException(DUPLICATE_LOOKED);
            }


            PostLookedRes postLookedRes = recentService.createLooked(userIdxByJwt, postLookedReq);
            return new BaseResponse<>(postLookedRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    // POST 최근 검색어 등록 API
    @ResponseBody
    @PostMapping("/searched")
    public BaseResponse<PostSearchedRes> createSearched(@RequestBody PostSearchedReq postSearchedReq) {

        try{
            // jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();

            // 유저 유무 확인
            if(userDao.checkUserIdx(userIdxByJwt) == 0) {
                throw new BaseException(NOT_EXIST_USER);
            }
            // 유저 탈퇴 확인
            if(userDao.checkStatusUserIdx(userIdxByJwt) == 1) {
                throw new BaseException(BREAKAWAY_USER);
            }

            // 최근 검색어에 이미 들어 있는지 확인
            if(recentDao.checkDuplicateWord(userIdxByJwt, postSearchedReq.getWord()) == 1) {
                throw new BaseException(DUPLICATE_SEARCHED);
            }

            PostSearchedRes postSearchedRes = recentService.createSearched(userIdxByJwt, postSearchedReq);
            return new BaseResponse<>(postSearchedRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @DeleteMapping("/looked/{lookedIdx}")
    public BaseResponse<String> deleteLooked(@PathVariable("lookedIdx") int lookedIdx) {
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();

            // 유저 유무 확인
            if(userDao.checkUserIdx(userIdxByJwt) == 0) {
                throw new BaseException(NOT_EXIST_USER);
            }
            // 유저 탈퇴 확인
            if(userDao.checkStatusUserIdx(userIdxByJwt) == 1) {
                throw new BaseException(BREAKAWAY_USER);
            }

            // 최근 본 상품 인덱스 유무 확인
            if(recentDao.checkLookedIdx(lookedIdx) == 0) {
                throw new BaseException(NOT_EXIST_LOOKED);
            }
            // 최근 본 상품에 이미 삭제가 되어있는지 확인(상태가 'N')
            if(recentDao.checkStatusLookedIdx(lookedIdx) == 1) {
                throw new BaseException(ALREADY_DELETED_LOOKED);
            }

            recentService.deleteLooked(userIdxByJwt, lookedIdx);
            String result = "최근 본상품을 삭제하였습니다.";

            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @DeleteMapping("/searched/{searchedIdx}")
    public BaseResponse<String> deleteSearched(@PathVariable("searchedIdx") int searchedIdx) {
        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();


            // 유저 유무 확인
            if(userDao.checkUserIdx(userIdxByJwt) == 0) {
                throw new BaseException(NOT_EXIST_USER);
            }
            // 유저 탈퇴 확인
            if(userDao.checkStatusUserIdx(userIdxByJwt) == 1) {
                throw new BaseException(BREAKAWAY_USER);
            }

            // 최근 본 상품 인덱스 유무 확인
            if(recentDao.checkSearchedIdx(searchedIdx) == 0) {
                throw new BaseException(NOT_EXIST_SEARCHED);
            }
            // 최근 본 상품에 이미 삭제가 되어있는지 확인(상태가 'N')
            if(recentDao.checkStatusSearchedIdx(searchedIdx) == 1) {
                throw new BaseException(ALREADY_DELETED_SEARCHED);
            }

            recentService.deleteSearched(userIdxByJwt, searchedIdx);

            String result = "최근 검색어를 삭제하였습니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }

    }

}
