package com.example.demo.config;

import lombok.Getter;

/**
 * 에러 코드 관리
 */
@Getter
public enum BaseResponseStatus {
    /**
     * 1000 : 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),


    /**
     * 2000 : Request 오류
     */
    // Common
    REQUEST_ERROR(false, 2000, "입력값을 확인해주세요."),
    EMPTY_JWT(false, 2001, "JWT를 입력해주세요."),
    INVALID_JWT(false, 2002, "유효하지 않은 JWT입니다."),
    INVALID_USER_JWT(false,2003,"권한이 없는 유저의 접근입니다."),

    // users
    USERS_EMPTY_USER_ID(false, 2010, "유저 아이디 값을 확인해주세요."),

    // [POST] /users
    POST_USERS_EMPTY_EMAIL(false, 2015, "이메일을 입력해주세요."),
    POST_USERS_INVALID_EMAIL(false, 2016, "이메일 형식을 확인해주세요."),
    POST_USERS_EXISTS_EMAIL(false,2017,"중복된 이메일입니다."),
    POST_USERS_EMPTY_PASSWORD(false, 2018, "비밀번호를 입력해주세요."),
    POST_USERS_INVALID_PASSWORD(false, 2019, "비밀번호를 '숫자', '문자', '특수문자' 각 1개 이상 입력해주세요.(최소 8자, 최대 16자)"),
    POST_USERS_EMPTY_STORENAME(false, 2020, "상점명을 입력해주세요."),
    POST_USERS_INVALID_STORENAME(false, 2021, "상점명을 한글, 영어, 숫자만 입력해주세요.(최소 2자, 최대 10자)"),
    POST_USERS_EXISTS_STORENAME(false,2022,"중복된 상점명입니다."),
    POST_USERS_EMPTY_PHONE(false, 2023, "휴대전화를 입력해주세요."),
    POST_USERS_INVALID_PHONE(false, 2024, "휴대전화형식에 맞게 숫자만 입력하세요."),
    POST_USERS_EMPTY_USERTYPE(false, 2025, "유저타입을 입력해주세요."),
    POST_USERS_INVALID_USERTYPE(false, 2026, "유저타입을 'E':이메일 또는 'K':카카오 중 한 글자만 입력해주세요."),

    // [PATCH] /users
    PATCH_USERS_EMPTY_STORENAME(false, 2030, "수정할 상점명을 입력해주세요."),
    PATCH_USERS_INVALID_STORENAME(false, 2031, "수정할 상점명을 한글, 영어, 숫자만 입력해주세요(최소 2자 최대 10자)"),
    PATCH_USERS_EMPTY_STOREADDRESS(false, 2032, "수정할 상점 주소를 입력해주세요."),
    PATCH_USERS_EMPTY_CONTACTABLETIME(false, 2033, "수정할 연락가능시간을 입력해주세요."),

    // [POST] /addresses
    POST_ADDRESSES_EMPTY_RECIPIENT(false, 2040, "받는분 성함을 입력해주세요."),
    POST_ADDRESSES_EMPTY_PHONE(false, 2041, "휴대전화를 입력해주세요."),
    POST_ADDRESSES_INVALID_PHONE(false, 2042, "휴대전화형식에 맞게 숫자만 입력하세요."),
    POST_ADDRESSES_EMPTY_LATITUDE(false, 2043, "국내 위도를 입력해주세요."),
    POST_ADDRESSES_INVALID_LATITUDE(false, 2044, "알맞은 국내 위도를 입력해주세요."),
    POST_ADDRESSES_EMPTY_LONGITUDE(false, 2045, "국내 경도를 입력해주세요."),
    POST_ADDRESSES_INVALID_LONGITUDE(false, 2046, "알맞은 국내 경도를 입력해주세요."),
    POST_ADDRESSES_EMPTY_ROADNAME(false, 2047, "도로명 주소를 입력해주세요."),
    POST_ADDRESSES_EMPTY_STATUS(false, 2048, "배송지 주소 상태(구분)를 입력해주세요."),
    POST_ADDRESSES_INVALID_STATUS(false, 2049, "'Y':활성화 또는 'M'(Main):기본배송지 중 한 글자만 입력해주세요."),

    // [PATCH] /addresses
    PATCH_ADDRESSES_EMPTY_RECIPIENT(false, 2050, "수정할 받는분 성함을 입력해주세요."),
    PATCH_ADDRESSES_EMPTY_PHONE(false, 2051, "수정할 휴대전화를 입력해주세요."),
    PATCH_ADDRESSES_INVALID_PHONE(false, 2052, "휴대전화형식에 맞게 숫자만 입력하세요."),
    PATCH_ADDRESSES_EMPTY_LATITUDE(false, 2053, "수정할 국내 위도를 입력해주세요."),
    PATCH_ADDRESSES_INVALID_LATITUDE(false, 2054, "알맞은 국내 위도를 입력해주세요."),
    PATCH_ADDRESSES_EMPTY_LONGITUDE(false, 2055, "수정할 국내 경도를 입력해주세요."),
    PATCH_ADDRESSES_INVALID_LONGITUDE(false, 2056, "알맞은 국내 경도를 입력해주세요."),
    PATCH_ADDRESSES_EMPTY_ROADNAME(false, 2057, "수정할 도로명 주소를 입력해주세요."),
    PATCH_ADDRESSES_EMPTY_STATUS(false, 2058, "수정할 배송지 주소 상태(구분)를 입력해주세요."),
    PATCH_ADDRESSES_INVALID_STATUS(false, 2059, "'Y':활성화 또는 'M'(Main):기본배송지 중 한 글자만 입력해주세요."),

    // [POST] /products
    POST_PRODUCTS_EMPTY_PRODUCTNAME(false, 2060, "상품명을 입력해주세요."),
    POST_PRODUCTS_EMPTY_SUBCATEGORYIDX(false, 2061, "서브카테고리 인덱스를 입력해주세요."),
    POST_PRODUCTS_LENGTH_CONTENT(false, 2062, "상품 설명은 2000자 이내로 작성해주세요."),
    POST_PRODUCTS_EMPTY_FREESHIPPING(false, 2063, "배송비 여부를 입력해주세요."),
    POST_PRODUCTS_INVALID_FREESHIPPING(false, 2064, "배송비에 'Y':포함 또는 'N':비포함 한 글자만 입력해주세요."),
    POST_PRODUCTS_EMPTY_NEGOTIABLE(false, 2065, "협의 여부를 입력해주세요."),
    POST_PRODUCTS_INVALID_NEGOTIABLE(false, 2066, "협의에 'Y':가능 또는 'N':불가능 한 글자만 입력해주세요."),
    POST_PRODUCTS_EMPTY_QUANTITY(false, 2067, "개수를 입력해주세요."),
    POST_PRODUCTS_RANGE_QUANTITY(false, 2068, "개수의 범위는 1~999개 입니다."),
    POST_PRODUCTS_EMPTY_CONDITIONS(false, 2069, "상품 상태를 입력해주세요."),
    POST_PRODUCTS_INVALID_CONDITIONS(false, 2070, "상품 상태에 'U'(Used):중고상품 또는 'N'(New): 새상품 중 한 글자만 입력해주세요."),
    POST_PRODUCTS_EMPTY_CHANGES(false, 2071, "교환 여부를 입력해주세요."),
    POST_PRODUCTS_INVALID_CHANGES(false, 2072, "교환에 'Y':가능 또는 'N':불가능 한 글자만 입력해주세요."),
    POST_PRODUCTS_EMPTY_IMGLIST(false, 2073, "이미지 리스트를 입력해주세요. 최소 1개이상 등록해야합니다."),
    POST_PRODUCTS_EMPTY_IMAGEURL(false, 2074, "이미지URL을 입력해주세요."),
    POST_PRODUCTS_LENGTH_IMAGEURL(false, 2075, "이미지URL을 1000자이하로 입력해주세요."),
    POST_PRODUCTS_LENGTH_TAGNAME(false, 2076, "태그이름은 9자이하로 입력해주세요."),
    POST_PRODUCTS_MAX_IMAGELIST(false, 2077, "이미지는 최대 12개만 입력할 수 있어요."),
    POST_PRODUCTS_MAX_TAGLIST(false, 2078, "태그는 최대 5개만 입력할 수 있어요."),
    /**
     * 3000 : Response 오류
     */
    // Common
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),

    // [POST] /users
    DUPLICATED_EMAIL(false, 3013, "중복된 이메일입니다."),
    FAILED_TO_LOGIN(false,3014,"없는 아이디거나 비밀번호가 틀렸습니다."),

    // [GET], [PATCH] /users
    NOT_EXIST_USER(false, 3015, "없는 유저입니다. 다시 확인해주세요."),
    BREAKAWAY_USER(false, 3016, "탈퇴한 유저입니다."),

    // [GET], [PATCH] /address
    NOT_EXIST_ADDRESS(false, 3031, "없는 주소입니다. 다시 확인해주세요."),
    DELETED_ADDRESS(false, 3032, "삭제한 주소입니다."),

    // [GET], [PATCH] /products
    NOT_EXIST_SUBCATEGORY(false, 3031, "없는 서브카테고리입니다. 다시 확인해주세요."),
    INACTIVE_SUBCATEGORY(false, 3032, "비활성화된 서브카테고리입니다."),
    NOT_EXIST_AREA(false, 3033, "없는 거래지역입니다. 다시 확인해주세요."),
    INACTIVE_AREA(false, 3034, "비활성화된 거래지역입니다."),
    NOT_EXIST_PRODUCT(false, 3035, "없는 상품입니다. 다시 확인해주세요."),
    INACTIVE_PRODUCT(false, 3036, "비활성화된 상품입니다."),
    SOLD_OR_BOOKED_PRODUCT(false, 3037, "이미 예약되었거나 팔린 상품입니다."),

    // [POST] /purchases
    NOT_EXIST_PURCHASE(false, 3038, "구매한 내역이 없습니다."),
    INACTIVE_PURCHASE(false, 3039, "거래가 정상적으로 성사되지 않은 거래 입니다."),



    /**
     * 4000 : Database, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다."),

    // [PATCH] /users/{userIdx}
    MODIFY_FAIL_USERNAME(false,4010,"유저네임 수정 실패"),

    // [PATCH] /users/{userIdx}/addresses/{addressIdx}
    MODIFY_FAIL_ADDRESS(false,4013,"배송자 수정 실패"),

    // [PATCH] /reviews/{userIdx}/{purchaseIdx}/{reviewIdx}
    MODIFY_FAIL_REVIEW(false,4014,"상점후기 수정 실패"),

    // [PATCH] /likes/{userIdx}/{likedIdx}
    MODIFY_FAIL_LIKE(false,4015,"즐겨찾기 수정 실패"),
    
    

    PASSWORD_ENCRYPTION_ERROR(false, 4021, "비밀번호 암호화에 실패하였습니다."),
    PASSWORD_DECRYPTION_ERROR(false, 4022, "비밀번호 복호화에 실패하였습니다.");


    // 5000 : 필요시 만들어서 쓰세요
    // 6000 : 필요시 만들어서 쓰세요


    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
