package com.lawProject.SSL.global.util.constant;

public class SwaggerConstants {
    /**
     * swagger
     */
    public static final String[] SWAGGER_APPOINTED_PATHS = {
            "/**"
    };
    public static final String DEFINITION_TITLE = "Vacation Projdct API 명세서";
    public static final String DEFINITION_DESCRIPTION = "\uD83D\uDE80 Realtime Congestion Based Location Recommendation Service - SSL Server의 API 명세서입니다.";
    public static final String DEFINITION_VERSION = "v1";

    public static final String SECURITY_SCHEME_NAME = "bearer-key";
    public static final String SECURITY_SCHEME = "bearer";
    public static final String SECURITY_SCHEME_BEARER_FORMAT = "JWT";
    public static final String SECURITY_SCHEME_DESCRIPTION = "JWT 토큰 키를 입력해주세요";

    /**
     * Common
     */
    public static final String PAGING = "pageable";
    public static final String PAGING_DESCRIPTION = "";

    public static final String SORT_KEY = "sortKey";
    public static final String SORT_KEY_DESCRIPTION = "최신순(기본값): createdAt, 인기순: popular, 고가순: highPrice, 저가순: lowestPrice";

    /**
     * Home
     */
    public static final String TAG_HOME = "Home";
    public static final String TAG_HOME_DESCRIPTION = "Home API";
    public static final String HOME_SUMMARY = "메인 페이지";
    public static final String HOME_DESCRIPTION = "메인 페이지 정보를 가져옵니다.";


}