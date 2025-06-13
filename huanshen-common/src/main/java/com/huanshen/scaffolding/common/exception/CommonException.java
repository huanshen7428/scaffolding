package com.huanshen.scaffolding.common.exception;

import lombok.Getter;

@Getter
public class CommonException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private Integer httpCode;
    /**
     * 错误码
     */
    private String errorCode;

    private String desc;

    /**
     * 空构造方法，避免反序列化问题
     */
    public CommonException() {
    }

    public CommonException(ExceptionEnums exception, String desc) {
        this.httpCode = exception.getHttpCode();
        this.desc = desc;
        this.errorCode = exception.getErrorCode();
    }

    public CommonException(ExceptionEnums status) {
        this.httpCode = status.getHttpCode();
        this.errorCode = status.getErrorCode();
    }

}
