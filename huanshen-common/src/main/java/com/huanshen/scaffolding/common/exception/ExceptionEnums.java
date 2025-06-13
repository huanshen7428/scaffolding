package com.huanshen.scaffolding.common.exception;


import lombok.Getter;

@Getter
public enum ExceptionEnums {
    /**
     * 通用系统异常
     */
    SYSTEM_ERROR("5000"),

    //*********************************系统异常，前后端代码及数据问题导致的bug*******************************************
    /**
     * 入参错误
     */
    PARAMETER_INVALID_EXCEPTION("5001"),
    /**
     * 请求头AUTHORIZATION为空
     */
    EMPTY_AUTHORIZATION("5003"),
    /**
     * 查询的数据不存在
     */
    DATA_MISSING("5004"),
    /**
     * 数据状态异常
     */
    WRONG_DATA_STATUS("5005"),
    /**
     * 权限不足
     */
    NO_PERMISSION("5006"),

    /**
     * 数据存在重复
     */
    DATA_DUPLICATE("5007"),

    /**
     * 请求参数缺失
     */
    PARAMETER_EMPTY_EXCEPTION("5008"),
    /**
     * 密码格式不匹配要求
     */
    PASSWORD_NOT_MATCH("5009"),

    /**
     * 请求方法不匹配
     */
    METHOD_NOT_SUPPORT("5010"),

    /**
     * 远程服务访问失败
     */
    REMOTE_SERVICE_ACCESS_FAIL("5011"),
    /**
     * 远程服务执行失败
     */
    REMOTE_SERVICE_EXEC_FAIL("5012"),


    //*********************************业务异常，用户违规操作产生的错误提示*******************************************
    /**
     * token无效。 401会导致页面跳转到登录页
     */
    INVALID_TOKEN(401, "4000"),
    /**
     * 用户不存在
     */
    USER_NOT_EXIST("4001"),
    /**
     * 密码错误
     */
    WRONG_ACCOUNT_PASSWD("4002"),
    /**
     * 用户已过期
     */
    ACCOUNT_EXPIRED("4003"),
    /**
     * 文件格式不匹配
     */
    FILE_TYPE_INVALID_EXCEPTION("4004"),
    /**
     * 入参冲突
     */
    PARAMETER_DUPLICATE_EXCEPTION("4005"),
    /**
     * 验证码错误
     */
    WRONG_VERIFY_CODE("4006"),
    /**
     * 操作过于频繁
     */
    FREQUENCY_LIMIT_EXCEPTION("4007"),
    /**
     * 密码不能与原密码一致
     */
    REPEAT_PASSWORD_EXCEPTION("4008"),
    /**
     * 账号被占用
     */
    ACCOUNT_OCCUPIED("4009"),

    /**
     * 角色被占用
     */
    ROLE_OCCUPIED("4010"),

    /**
     * 邮箱被占用
     */
    EMAIL_OCCUPIED("4011"),

    /**
     * 账号状态异常
     */
    ACCOUNT_WRONG_STATUS("4012"),


    ;
    private final Integer httpCode;
    private final String errorCode;

    ExceptionEnums(Integer httpCode, String errorCode) {
        this.httpCode = httpCode;
        this.errorCode = errorCode;
    }

    ExceptionEnums(String errorCode) {
        this.httpCode = 500;
        this.errorCode = errorCode;
    }

}
