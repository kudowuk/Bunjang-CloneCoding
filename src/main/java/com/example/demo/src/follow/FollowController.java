package com.example.demo.src.follow;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.follow.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.INVALID_USER_JWT;

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

    public FollowController(FollowProvider followProvider, FollowService followService, JwtService jwtService) {
        this.followProvider = followProvider;
        this.followService = followService;
        this.jwtService = jwtService;
    }

    // GET 팔로워 조회 API
    @GetMapping("/{userIdx}/followers") // (GET) 127.0.0.1:8000/app/follows/userIdx?=1/followers
    public BaseResponse<List<GetFollowerRes>> getFollowers(@PathVariable("userIdx") int userIdx) {
        try {
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
    public BaseResponse<List<GetFollowingRes>> getFollowings(@PathVariable("followingUserIdx") int followingUserIdx) {

        try {
            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(followingUserIdx != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            List<GetFollowingRes> getFollowingRes = followProvider.getFollowings(followingUserIdx);
            return new BaseResponse<>(getFollowingRes);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }




    // POST 즐겨찾기 등록 API
    @ResponseBody
    @PostMapping("/{userIdx}") // (POST) 127.0.0.1:8000/app/follows/userIdx?=1
    public BaseResponse<PostFollowRes> createFollow(@PathVariable("userIdx") int userIdx, @RequestBody PostFollowReq postFollowReq) {

        try{
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

            String result = "찜을 수정하였습니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }


}
