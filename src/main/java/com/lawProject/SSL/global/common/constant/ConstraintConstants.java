package com.lawProject.SSL.global.common.constant;

public class ConstraintConstants {
    /**
     * User
     */


    /**
     * Security
     */
    public static final String DEFAULT_LOGIN_REQUEST_URL = "/login";
    public static final String HTTP_METHOD = "POST";
    public static final String CONTENT_TYPE = "application/json";
    public static final String USERNAME_KEY="email";
    public static final String PASSWORD_KEY="password";

    /**
     * JWT
     */
    public static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    public static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    public static final String USERNAME_CLAIM = "username";
    public static final String ROLE_CLAIM = "role";
    public static final String BEARER = "Bearer ";

    /**
     * LawSuit
     */
    public static final String CIVIL = "101";
    public static final String CRIMINAL = "201";

}

