package com.huanshen.scaffolding.general.config;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.huanshen.scaffolding.common.response.BaseResponse;
import com.huanshen.scaffolding.common.exception.CommonException;
import com.huanshen.scaffolding.common.exception.ExceptionEnums;
import com.huanshen.scaffolding.common.multilanguage.MultiLanguageUtil;
import com.huanshen.scaffolding.common.page.util.ServletUtils;
import com.huanshen.scaffolding.security.constant.SecurityConsts;
import com.huanshen.scaffolding.security.domain.LoginUser;
import com.huanshen.scaffolding.security.domain.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;

@RestControllerAdvice(basePackages = {"com.huanshen.scaffolding.partner", "com.huanshen.scaffolding.owner", "com.huanshen.scaffolding.general"})
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 鉴权异常
     */
    @ExceptionHandler(AuthorizationException.class)
    public BaseResponse error(AuthorizationException e, HttpServletResponse response) {
        log.error("AuthorizationException", e);
        response.setStatus(HttpStatus.FORBIDDEN.value());
        String message = getMessage(ExceptionEnums.NO_PERMISSION.getErrorCode());
        return BaseResponse.fail(ExceptionEnums.NO_PERMISSION.getErrorCode(), message);
    }

    /**
     * 入参校验异常
     *
     * @param ex
     * @param response
     * @return
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public BaseResponse methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException ex, HttpServletResponse response) {
        log.error("MethodArgumentNotValidException", ex);
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        String field = Objects.requireNonNull(ex.getBindingResult().getFieldError()).getField();
        String errorMsg = "";
        List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();
        if (CollUtil.isNotEmpty(allErrors)) {
            errorMsg = allErrors.get(0).getDefaultMessage();
        }

        String message = getMessage(ExceptionEnums.PARAMETER_INVALID_EXCEPTION.getErrorCode());
        message = formatMessage(message, field);
        message = message + " " + errorMsg;
        return BaseResponse.fail(ExceptionEnums.PARAMETER_INVALID_EXCEPTION.getErrorCode(), message);
    }

    /**
     * 请求方法不支持
     *
     * @param ex
     * @param response
     * @return
     */
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public BaseResponse methodArgumentNotValidExceptionHandler(HttpRequestMethodNotSupportedException ex, HttpServletResponse response) {
        log.error("HttpRequestMethodNotSupportedException", ex);
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        String message = getMessage(ExceptionEnums.METHOD_NOT_SUPPORT.getErrorCode());
        return BaseResponse.fail(ExceptionEnums.METHOD_NOT_SUPPORT.getErrorCode(), message);
    }

    /**
     * 入参的json序列化异常
     *
     * @param ex
     * @param response
     * @return
     */
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public BaseResponse httpMessageNotReadableExceptionHandler(HttpMessageNotReadableException ex, HttpServletResponse response) {
        log.error("HttpMessageNotReadableException", ex);
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        String message = getMessage(ExceptionEnums.PARAMETER_INVALID_EXCEPTION.getErrorCode());
        return BaseResponse.fail(ExceptionEnums.PARAMETER_INVALID_EXCEPTION.getErrorCode(), message);
    }

    /**
     * @param ex
     * @param response
     * @return
     */
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public BaseResponse missingServletRequestParameterExceptionHandler(MissingServletRequestParameterException ex, HttpServletResponse response) {
        log.error("MissingServletRequestParameterException", ex);
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        String message = getMessage(ExceptionEnums.PARAMETER_INVALID_EXCEPTION.getErrorCode());
        return BaseResponse.fail(ExceptionEnums.PARAMETER_INVALID_EXCEPTION.getErrorCode(), message);
    }


    @ExceptionHandler(value = CommonException.class)
    public BaseResponse commonException(CommonException exception, HttpServletResponse response) {
        log.error("commonException", exception);
        response.setStatus(exception.getHttpCode() == null ? HttpStatus.INTERNAL_SERVER_ERROR.value() : exception.getHttpCode());

        String errorCode = exception.getErrorCode();
        String desc = exception.getDesc();

        String message = formatMessage(getMessage(errorCode), desc);
        return BaseResponse.fail(exception.getErrorCode(), message);
    }

    @ExceptionHandler(value = Exception.class)
    public BaseResponse exceptionHandler(Exception ex, HttpServletResponse response) {
        log.error("exceptionHandler", ex);

        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        /* 业务指定输出MessageKey异常消息 */
        String errorCode = ExceptionEnums.SYSTEM_ERROR.getErrorCode();
        return BaseResponse.fail(errorCode, getMessage(ExceptionEnums.SYSTEM_ERROR.getErrorCode()));
    }

    private String getLang() {
        String lang = null;
        LoginUser loginUser = UserContext.getCurrentUser();
        if (loginUser != null) {
            return loginUser.getLang();
        }
        HttpServletRequest request = ServletUtils.getRequest();
        if (request == null) {
            return null;
        }
        return request.getHeader(SecurityConsts.REQUEST_HEADER_LANG);
    }

    private String getMessage(String errorCode) {
        return MultiLanguageUtil.getMessage(errorCode, getLang());
    }

    private String formatMessage(String message, String param) {
        if (StrUtil.isBlank(param)) {
            return message;
        }
        if (StrUtil.isBlank(message)) {
            return null;
        }
        return message.contains("%s") ?
                String.format(message, param) :
                String.format("%s, [%s]", message, param);
    }

}
