package com.example.demo.src.like;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.like.model.*;
import com.example.demo.src.user.UserDao;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/app/likes")
public class LikeController {


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final LikeProvider likeProvider;
    @Autowired
    private final LikeService likeService;
    @Autowired
    private final JwtService jwtService;
    @Autowired
    private final UserDao userDao;
    @Autowired
    private final LikeDao likeDao;


    public LikeController(LikeProvider likeProvider, LikeService likeService, JwtService jwtService, UserDao userDao, LikeDao likeDao) {
        this.likeProvider = likeProvider;
        this.likeService = likeService;
        this.jwtService = jwtService;
        this.userDao = userDao;
        this.likeDao = likeDao;
    }

    // GET 즐겨찾기 조회 API
    @GetMapping("/{userIdx}") // (GET) 127.0.0.1:8000/app/likes/userIdx?=1
    public BaseResponse<List<GetLikeRes>> getLikes(@PathVariable("userIdx") int userIdx) {

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

            List<GetLikeRes> getLikeRes = likeProvider.getLikes(userIdx);
            return new BaseResponse<>(getLikeRes);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    // POST 즐겨찾기 등록 API
    @ResponseBody
    @PostMapping("/{userIdx}") // (POST) 127.0.0.1:8000/app/likes/userIdx?=1
    public BaseResponse<PostLikeRes> createLike(@PathVariable("userIdx") int userIdx, @RequestBody PostLikeReq postLikeReq) {

        try{

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

            PostLikeRes postLikeRes = likeService.createLike(userIdx, postLikeReq);
            return new BaseResponse<>(postLikeRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    // PATCH 즐겨찾기 수정 API
    @ResponseBody
    @PatchMapping("/{userIdx}/{likeIdx}") // (PATCH) 127.0.0.1:8000/app/likes/userIdx?=1/likeIdx?=1
    public BaseResponse<String> modifyLike(@PathVariable("userIdx") int userIdx, @PathVariable("likeIdx") int likeIdx, @RequestBody Like like){


        try {
            // 유저 유무 확인
            if(userDao.checkUserIdx(userIdx) == 0) {
                throw new BaseException(NOT_EXIST_USER);
            }
            // 유저 탈퇴 확인
            if(userDao.checkStatusUserIdx(userIdx) == 1) {
                throw new BaseException(BREAKAWAY_USER);
            }

            // 찜인덱스 유무 확인
            if(likeDao.checkLikeIdx(likeIdx) == 0) {
                throw new BaseException(NOT_EXIST_LIKE);
            }


            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            PatchLikeReq patchLikeReq = new PatchLikeReq(userIdx, likeIdx, like.getStatus() );
            likeService.modifyLike(patchLikeReq);

            String result = "찜을 수정하였습니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }


}
