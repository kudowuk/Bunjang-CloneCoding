package com.example.demo.src.address;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.address.model.*;
import com.example.demo.src.user.UserDao;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.config.BaseResponseStatus.PATCH_ADDRESSES_EMPTY_LONGITUDE;
import static com.example.demo.utils.ValidationRegex.*;

@RestController
@RequestMapping("/app/users")
public class AddressController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final AddressProvider addressProvider;
    @Autowired
    private final AddressService addressService;
    @Autowired
    private final JwtService jwtService;
    @Autowired
    private final UserDao userDao;

    public AddressController(AddressProvider addressProvider, AddressService addressService, JwtService jwtService, UserDao userDao){
        this.addressProvider = addressProvider;
        this.addressService = addressService;
        this.jwtService = jwtService;
        this.userDao = userDao;
    }

    // GET 전체 주소 조회 API(번개장터 배송지 설정)
    @ResponseBody
    @GetMapping("/{userIdx}/addresses") // (GET) 127.0.0.1:9000/app/userIdx?=1/addresses/
    public BaseResponse<List<GetAddressRes>> getAddresses(@PathVariable("userIdx") int userIdx) {

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

            List<GetAddressRes> getAddressRes = addressProvider.getAddresses(userIdx);
            return new BaseResponse<>(getAddressRes);

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    // GET 특정 주소 조회 API(번개장터 배송지 수정 화면)
    @ResponseBody
    @GetMapping("/{userIdx}/addresses/{addressIdx}") // (GET) 127.0.0.1:9000/app/userIdx?=1/addresses/addressIdx?=1/
    public BaseResponse<GetAddressRes> getAddress(@PathVariable("userIdx") int userIdx, @PathVariable("addressIdx") int addressIdx) {
        try {
            // 유저 유무 확인
            if(userDao.checkUserIdx(userIdx) == 0) {
                throw new BaseException(NOT_EXIST_USER);
            }
            // 유저 탈퇴 확인
            if(userDao.checkStatusUserIdx(userIdx) == 1) {
                throw new BaseException(BREAKAWAY_USER);
            }

            // 주소 유무 확인
            if(userDao.checkAddressIdx(addressIdx) == 0) {
                throw new BaseException(NOT_EXIST_ADDRESS);
            }
            // 주소 삭제 확인
            if(userDao.checkStatusAddressIdx(addressIdx) == 1) {
                throw new BaseException(DELETED_ADDRESS);
            }

            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            GetAddressRes getAddressRes = addressProvider.getAddress(userIdx, addressIdx);
            return new BaseResponse<>(getAddressRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    // POST 주소 생성 API(배송지 추가)
    @ResponseBody
    @PostMapping("/{userIdx}/addresses")
    public BaseResponse<PostAddressRes> createAddress(@PathVariable("userIdx") int userIdx, @RequestBody PostAddressReq postAddressReq) {

        // 받는분 입력하기
        if(postAddressReq.getRecipient() == null || postAddressReq.getRecipient().isEmpty()) {
            return new BaseResponse<>(POST_ADDRESSES_EMPTY_RECIPIENT);
        }

        // 휴대전화 입력하기
        if(postAddressReq.getPhone() == null || postAddressReq.getPhone().isEmpty()) {
            return new BaseResponse<>(POST_ADDRESSES_EMPTY_PHONE);
        }
        // 휴대전화 정규 표현
        if(!isRegexPhone(postAddressReq.getPhone())){
            return new BaseResponse<>(POST_ADDRESSES_INVALID_PHONE);
        }

        // 위도 입력하기
        if(postAddressReq.getLatitude() == null || postAddressReq.getLatitude().isEmpty()) {
            return new BaseResponse<>(POST_ADDRESSES_EMPTY_LATITUDE);
        }
        // 국내 위도 정규표현
        if(!isRegexLatitude(postAddressReq.getLatitude())){
            return new BaseResponse<>(POST_ADDRESSES_INVALID_LATITUDE);
        }

        // 경도 입력하기
        if(postAddressReq.getLongitude() == null || postAddressReq.getLongitude().isEmpty()) {
            return new BaseResponse<>(POST_ADDRESSES_EMPTY_LONGITUDE);
        }
        // 국내 경도 정규표현
        if(!isRegexLongitude(postAddressReq.getLongitude())){
            return new BaseResponse<>(POST_ADDRESSES_INVALID_LONGITUDE);
        }

        // 도로명 주소 입력하기
        if(postAddressReq.getRoadName() == null || postAddressReq.getRoadName().isEmpty()) {
                return new BaseResponse<>(POST_ADDRESSES_EMPTY_ROADNAME);
        }

        // 배송지 상태(구분) 입력하기
        if(postAddressReq.getStatus() == null || postAddressReq.getStatus().isEmpty()) {
            return new BaseResponse<>(POST_ADDRESSES_EMPTY_STATUS);
        }
        // 주소 일반 배송지 및 기본 배송지 구분하기
        if(!postAddressReq.getStatus().equals("Y") && !postAddressReq.getStatus().equals("M")) {
            return new BaseResponse<>(POST_ADDRESSES_INVALID_STATUS);
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

            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            PostAddressRes postAddressRes = addressService.createAddress(userIdx, postAddressReq);
            return new BaseResponse<>(postAddressRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    // PATCH 특정 주소 수정 API(배송지 수정 화면)
    @ResponseBody
    @PatchMapping("/{userIdx}/addresses/{addressIdx}")
    public BaseResponse<String> modifyAddress(@PathVariable("userIdx") int userIdx, @PathVariable("addressIdx") int addressIdx, @RequestBody Address address){

        // 수정할 받는분 입력하기
        if(address.getRecipient() == null || address.getRecipient().isEmpty()) {
            return new BaseResponse<>(PATCH_ADDRESSES_EMPTY_RECIPIENT);
        }

        // 수정할 휴대전화 입력하기
        if(address.getPhone() == null || address.getPhone().isEmpty()) {
            return new BaseResponse<>(PATCH_ADDRESSES_EMPTY_RECIPIENT);
        }
        // 휴대전화 정규 표현
        if(!isRegexPhone(address.getPhone())){
            return new BaseResponse<>(PATCH_ADDRESSES_INVALID_LATITUDE);
        }

        // 수정할 위도 입력하기
        if(address.getLatitude() == null || address.getLatitude().isEmpty()) {
            return new BaseResponse<>(PATCH_ADDRESSES_EMPTY_LATITUDE);
        }
        // 국내 위도 정규표현
        if(!isRegexLatitude(address.getLatitude())){
            return new BaseResponse<>(PATCH_ADDRESSES_INVALID_LATITUDE);
        }

        // 수정할 경도 입력하기
        if(address.getLongitude() == null || address.getLongitude().isEmpty()) {
            return new BaseResponse<>(PATCH_ADDRESSES_EMPTY_LONGITUDE);
        }
        // 국내 경도 정규표현
        if(!isRegexLongitude(address.getLongitude())){
            return new BaseResponse<>(PATCH_ADDRESSES_INVALID_LONGITUDE);
        }

        // 수정할 도로명 주소 입력하기
        if(address.getRoadName() == null || address.getRoadName().isEmpty()) {
            return new BaseResponse<>(PATCH_ADDRESSES_EMPTY_ROADNAME);
        }

        // 수정할 배송지 상태(구분) 입력하기
        if(address.getStatus() == null || address.getStatus().isEmpty()) {
            return new BaseResponse<>(PATCH_ADDRESSES_EMPTY_STATUS);
        }
        // 주소 일반 배송지 및 기본 배송지 구분하기
        if(!address.getStatus().equals("Y") && !address.getStatus().equals("M")) {
            return new BaseResponse<>(PATCH_ADDRESSES_INVALID_STATUS);
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

            // 주소 유무 확인
            if(userDao.checkAddressIdx(addressIdx) == 0) {
                throw new BaseException(NOT_EXIST_ADDRESS);
            }
            // 주소 삭제 확인
            if(userDao.checkStatusAddressIdx(addressIdx) == 1) {
                throw new BaseException(DELETED_ADDRESS);
            }


            //jwt에서 idx 추출.
            int userIdxByJwt = jwtService.getUserIdx();
            //userIdx와 접근한 유저가 같은지 확인
            if(userIdx != userIdxByJwt) {
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            PatchAddressReq patchAddressReq = new PatchAddressReq(addressIdx, userIdx, address.getRecipient(), address.getPhone(), address.getLatitude(), address.getLongitude(), address.getRoadName(), address.getDetailedAddress(), address.getRequestMsg(), address.getStatus() );
            addressService.modifyAddress(patchAddressReq);

            String result = "주소가 수정되었습니다.";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }



}
