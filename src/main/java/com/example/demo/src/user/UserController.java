package com.example.demo.src.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.*;

@RestController
@RequestMapping("/app/users")
public class UserController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final UserProvider userProvider;
    @Autowired
    private final UserService userService;
    @Autowired
    private final JwtService jwtService;
    @Autowired
    private final UserDao userDao;


    public UserController(UserProvider userProvider, UserService userService, JwtService jwtService, UserDao userDao){
        this.userProvider = userProvider;
        this.userService = userService;
        this.jwtService = jwtService;
        this.userDao = userDao;
    }

    /**
     * 회원 1명 조회 API
     * [GET] /users/:userIdx
     * @return BaseResponse<GetUserRes>
     */
    // Path-variable
    @ResponseBody
    @GetMapping("/{userIdx}") // (GET) 127.0.0.1:9000/app/users/:userIdx
    public BaseResponse<GetUserRes> getUser(@PathVariable("userIdx") int userIdx) {
        // Get Users
        try{
            // 유저 유무 확인
            if(userDao.checkUserIdx(userIdx) == 0) {
                throw new BaseException(NOT_EXIST_USER);
            }
            // 유저 탈퇴 확인
            if(userDao.checkStatusUserIdx(userIdx) == 1) {
                throw new BaseException(BREAKAWAY_USER);
            }

            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            GetUserRes getUserRes = userProvider.getUser(userIdx);
            return new BaseResponse<>(getUserRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

    }

    /**
     * 회원가입 API
     * [POST] /users
     * @return BaseResponse<PostUserRes>
     */
    // Body
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostUserRes> createUser(@RequestBody PostUserReq postUserReq) {
        // TODO: email 관련한 짧은 validation 예시입니다. 그 외 더 부가적으로 추가해주세요!

        // 이메일 입력하기
        if(postUserReq.getEmail() == null || postUserReq.getEmail().equals("")){
            return new BaseResponse<>(POST_USERS_EMPTY_EMAIL);
        }
        // 이메일 정규표현
        if(!isRegexEmail(postUserReq.getEmail())){
            return new BaseResponse<>(POST_USERS_INVALID_EMAIL);
        }

        // 비밀번호 입력하기
        if(postUserReq.getPassword() == null || postUserReq.getPassword().equals("")){
            return new BaseResponse<>(POST_USERS_EMPTY_PASSWORD);
        }
        // 비밀번호 정규표현
        if(!isRegexPassword(postUserReq.getPassword())){
            return new BaseResponse<>(POST_USERS_INVALID_PASSWORD);
        }

        // 상점명 입력하기
        if(postUserReq.getStoreName() == null || postUserReq.getStoreName().equals("")){
            return new BaseResponse<>(POST_USERS_EMPTY_EMAIL);
        }
        // 상점명 정규표현
        if(!isRegexStoreName(postUserReq.getStoreName())) {
            return new BaseResponse<>(POST_USERS_INVALID_STORENAME);
        }

//        // 휴대전화 입력하기
//        if(postUserReq.getPhone() == null || postUserReq.getPhone().equals("")){
//            return new BaseResponse<>(POST_USERS_EMPTY_PHONE);
//        }
//        // 휴대폰 정규표현
//        if(!isRegexPhone(postUserReq.getPhone())){
//            return new BaseResponse<>(POST_USERS_INVALID_PHONE);
//        }

        // 유저타입 입력하기
        if(postUserReq.getUserType() == null || postUserReq.getUserType().equals("")){
            return new BaseResponse<>(POST_USERS_EMPTY_USERTYPE);
        }
        // 유저 타입 E:이메일로 가입, K:카카오로 가입
        if(!postUserReq.getUserType().equals("E") && !postUserReq.getUserType().equals("K")){
            return new BaseResponse<>(POST_USERS_INVALID_USERTYPE);
        }

        try{
            PostUserRes postUserRes = userService.createUser(postUserReq);
            return new BaseResponse<>(postUserRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    /**
     * 로그인 API
     * [POST] /users/logIn
     * @return BaseResponse<PostLoginRes>
     */
    @ResponseBody
    @PostMapping("/logIn")
    public BaseResponse<PostLoginRes> logIn(@RequestBody PostLoginReq postLoginReq){
        try{

            // 이메일 입력하기
            if(postLoginReq.getEmail() == null || postLoginReq.getEmail().equals("")){
                return new BaseResponse<>(POST_USERS_EMPTY_EMAIL);
            }
            // 이메일 정규표현
            if(!isRegexEmail(postLoginReq.getEmail())){
                return new BaseResponse<>(POST_USERS_INVALID_EMAIL);
            }

            // 비밀번호 입력하기
            if(postLoginReq.getPassword() == null || postLoginReq.getPassword().equals("")){
                return new BaseResponse<>(POST_USERS_EMPTY_PASSWORD);
            }


            // TODO: 로그인 값들에 대한 형식적인 validatin 처리해주셔야합니다!
            // TODO: 유저의 status ex) 비활성화된 유저, 탈퇴한 유저 등을 관리해주고 있다면 해당 부분에 대한 validation 처리도 해주셔야합니다.
            PostLoginRes postLoginRes = userProvider.logIn(postLoginReq);
            return new BaseResponse<>(postLoginRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 상점정보변경 API
     * [PATCH] /users/:userIdx
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/{userIdx}")
    public BaseResponse<String> modifyStore(@PathVariable("userIdx") int userIdx, @RequestBody Store store){

        // 상점명 입력하기
        if(store.getStoreName() == null || store.getStoreName().equals("")){
            return new BaseResponse<>(PATCH_USERS_EMPTY_STORENAME);
        }
        // 상점명 정규표현
        if(!isRegexStoreName(store.getStoreName())) {
            return new BaseResponse<>(PATCH_USERS_INVALID_STORENAME);
        }

        // 상점주소 입력하기
        if(store.getContactableTime() == null || store.getContactableTime().equals("")){
            return new BaseResponse<>(PATCH_USERS_EMPTY_STOREADDRESS);
        }

        // 연락가능시간 입력하기
        if(store.getContactableTime() == null || store.getContactableTime().equals("")){
            return new BaseResponse<>(PATCH_USERS_EMPTY_CONTACTABLETIME);
        }


        try {

            // 유저 유무 확인
            if(userDao.checkUserIdx(userIdx) == 0) {
                throw new BaseException(NOT_EXIST_USER);
            }
            // 유저 탈퇴 확인
            if(userDao.checkStatusUserIdx(userIdx) == 1) {
                throw new BaseException(BREAKAWAY_USER);
            }


            //jwt에서 idx 추출
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            userService.modifyStore(userIdx, store);
            String result = "상점정보를 수정하였습니다.";
        return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @GetMapping("/{userIdx}/autoLogin")
    public BaseResponse<GetAutoLoginRes> GetAutoLogin(@PathVariable int userIdx) {
        try {
            // 유저 유무 확인
            if(userDao.checkUserIdx(userIdx) == 0) {
                throw new BaseException(NOT_EXIST_USER);
            }
            // 유저 탈퇴 확인
            if(userDao.checkStatusUserIdx(userIdx) == 1) {
                throw new BaseException(BREAKAWAY_USER);
            }

            //jwt에서 idx 추출
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if (userIdx != userIdxByJwt) {
                return new BaseResponse(INVALID_USER_JWT);
            }

            return new BaseResponse<>(new GetAutoLoginRes("Success Auto Login"));
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }


}
