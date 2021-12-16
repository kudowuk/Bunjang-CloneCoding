package com.example.demo.src.follow;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.follow.model.*;
import com.example.demo.src.user.UserDao;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/app/follows")
public class FollowController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final FollowProvider followProvider;
    @Autowired
    private final FollowService followService;
    @Autowired
    private final JwtService jwtService;
    @Autowired
    private final UserDao userDao;
    @Autowired
    private final FollowDao followDao;

    public FollowController(FollowProvider followProvider, FollowService followService, JwtService jwtService, UserDao userDao, FollowDao followDao) {
        this.followProvider = followProvider;
        this.followService = followService;
        this.jwtService = jwtService;
        this.userDao = userDao;
        this.followDao = followDao;
    }

    // GET 팔로워 조회 API
    @GetMapping("/{userIdx}/followers") // (GET) 127.0.0.1:8000/app/follows/userIdx?=1/followers
    public BaseResponse<List<GetFollowerRes>> getFollowers(@PathVariable("userIdx") int userIdx) {
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

            List<GetFollowerRes> getFollowerRes = followProvider.getFollowers(userIdx);
            return new BaseResponse<>(getFollowerRes);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    // GET 팔로잉 조회 API
    @GetMapping("/{userIdx}/followings") // (GET) 127.0.0.1:8000/app/follows/userIdx?=1/followers
    public BaseResponse<List<GetFollowingRes>> getFollowings(@PathVariable("userIdx") int userIdx) {
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

            List<GetFollowingRes> getFollowingRes = followProvider.getFollowings(userIdx);
            return new BaseResponse<>(getFollowingRes);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }




    // POST 팔로우 등록 API
    @ResponseBody
    @PostMapping("/{userIdx}") // (POST) 127.0.0.1:8000/app/follows/userIdx?=1
    public BaseResponse<PostFollowRes> createFollow(@PathVariable("userIdx") int userIdx, @RequestBody PostFollowReq postFollowReq) {

        // 팔로우 하려는 인덱스 입력하기
        if(postFollowReq.getBrandIdx() == null && postFollowReq.getFollowingUserIdx() == null){
            return new BaseResponse<>(POST_FOLLOWS_EMPTY_TARGETIDX);
        }
        // 브랜드 인덱스 또는 유저인덱스 둘중 하나만 입력하기
        if(!(postFollowReq.getBrandIdx() == null) && !(postFollowReq.getFollowingUserIdx() == null)){
            return new BaseResponse<>(POST_FOLLOWS_ONE_TARGETIDX);
        }

        // 자신과 동일한 유저 인덱스 확인
        if(postFollowReq.getFollowingUserIdx() == userIdx){
            return new BaseResponse<>(POST_IMPOSSIBLE_SAME_USER);
        }

        try{
            // 유저 유무 확인
            if(userDao.checkUserIdx(userIdx) == 0) {
                throw new BaseException(NOT_EXIST_USER);
            }
            // 유저 탈퇴 확인
            if(userDao.checkStatusUserIdx(userIdx) == 1) {
                throw new BaseException(BREAKAWAY_USER);
            }

            // 브랜드 유무 확인
            if(followDao.checkBrandIdx(postFollowReq.getBrandIdx()) == 0) {
                throw new BaseException(NOT_EXIST_BRAND);
            }
            // 브랜드 활성화 확인
            if(followDao.checkStatusBrandIdx(postFollowReq.getBrandIdx()) == 1) {
                throw new BaseException(INACTIVE_BRAND);
            }

            // 팔로잉 하려는 유저인덱스 유무 확인
            if(followDao.checkFollowingUserIdx(postFollowReq.getFollowingUserIdx()) == 0) {
                throw new BaseException(NOT_EXIST_FOLLOW);
            }
            // 팔로잉 하려는 유저인덱스 활성화 확인
            if(followDao.checkStatusFollowingUserIdx(postFollowReq.getFollowingUserIdx()) == 1) {
                throw new BaseException(INACTIVE_FOLLOW);
            }

            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            PostFollowRes postFollowRes = followService.createFollow(userIdx, postFollowReq);
            return new BaseResponse<>(postFollowRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    // PATCH 즐겨찾기 수정 API
    @ResponseBody
    @PatchMapping("/{userIdx}/{followIdx}") // (PATCH) 127.0.0.1:8000/app/follows/userIdx?=1/followIdx?=1
    public BaseResponse<String> modifyFollow(@PathVariable("userIdx") int userIdx, @PathVariable("followIdx") int followIdx, @RequestBody Follow follow){


        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            PatchFollowReq patchFollowReq = new PatchFollowReq(userIdx, followIdx, follow.getStatus());
            followService.modifyFollow(patchFollowReq);

            String result = "팔로우를 수정하였습니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }


}
