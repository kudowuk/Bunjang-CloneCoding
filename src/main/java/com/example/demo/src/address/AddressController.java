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

    // GET 특정 주소 조회 API(번개장터 배송지 수정)
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

    @ResponseBody
    @PostMapping("/{userIdx}/addresses")
    public BaseResponse<PostAddressRes> createAddress(@PathVariable("userIdx") int userIdx, @RequestBody PostAddressReq postAddressReq) {

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

    @ResponseBody
    @PatchMapping("/{userIdx}/addresses/{addressIdx}")
    public BaseResponse<String> modifyAddress(@PathVariable("userIdx") int userIdx, @PathVariable("addressIdx") int addressIdx, @RequestBody Address address){

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

            PatchAddressReq patchAddressReq = new PatchAddressReq(userIdx, addressIdx, address.getRecipient(), address.getPhone(), address.getLatitude(), address.getLongitude(), address.getRoadName(), address.getDetailedAddress(), address.getRequestMsg(), address.getStatus() );
            addressService.modifyAddress(patchAddressReq);

            String result = "";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }



}
