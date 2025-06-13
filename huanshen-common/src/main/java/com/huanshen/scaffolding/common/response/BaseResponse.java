package com.huanshen.scaffolding.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class BaseResponse implements Serializable {

    private static final long serialVersionUID = 3874949836220475118L;

    public static final String SUCCESS_RET_CODE = "000000";

    private boolean success;

    private String resultCode;

    private String resultDesc;

    private long total;

    /**
     * 业务数据
     */
    @JsonInclude()
    private Object result = null;

    /**
     * 默认构造函数
     */
    public BaseResponse() {
        super();
    }

    public static BaseResponse success(Object result) {
        return new BaseResponse(true, SUCCESS_RET_CODE, null, result);
    }

    @SuppressWarnings("all")
    public static BaseResponse fail(String errCode, String msg) {
        return new BaseResponse(false, errCode, msg, null);
    }

    /**
     * 构造函数
     *
     * @param success       是否成功
     * @param resultCode    是否成功代码
     * @param resultMessage 是否成功信息
     * @param result        业务执行结果
     */
    public BaseResponse(boolean success, String resultCode, String resultMessage, Object result) {
        super();
        this.setSuccess(success);
        this.setResultCode(resultCode);
        this.setResultDesc(resultMessage);
        this.setResult(result);
    }

    public BaseResponse(boolean success, String resultCode, String resultMessage, Object result, Long total) {
        super();
        this.setSuccess(success);
        this.setResultCode(resultCode);
        this.setResultDesc(resultMessage);
        this.setResult(result);
        this.setTotal(total);
    }
}
