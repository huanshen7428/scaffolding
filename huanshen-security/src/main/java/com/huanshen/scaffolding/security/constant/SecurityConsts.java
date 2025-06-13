package com.huanshen.scaffolding.security.constant;

public class SecurityConsts {

    /**
     * request请求头属性:鉴权
     */
    public static final String REQUEST_HEADER_AUTH = "Authorization";
    /**
     * 客户端账户，basic鉴权
     */
    public static final String REQUEST_HEADER_USERNAME = "username";
    public static final String REQUEST_HEADER_PASSWORD = "password";
    /**
     * 客户端账户，oauth鉴权
     */
    public static final String REQUEST_HEADER_SIGNATURE = "signature";

    /**
     * request请求头属性：语种
     */
    public static final String REQUEST_HEADER_LANG = "Lang";

    /**
     * JWT-account
     */
    public static final String ACCOUNT = "account";

    /**
     * JWT-currentTimeMillis
     */
    public final static String CURRENT_TIME_MILLIS = "currentTimeMillis";

    /**
     * 登录token 缓存中的前缀
     */
    public static final String LOGIN_TOKEN_KEY = "login_token:";


}
