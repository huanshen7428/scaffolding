package com.huanshen.scaffolding.security.utils;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.huanshen.scaffolding.common.response.BaseResponse;
import com.huanshen.scaffolding.common.exception.ExceptionEnums;
import com.huanshen.scaffolding.common.multilanguage.MultiLanguageUtil;
import com.huanshen.scaffolding.common.page.util.ServletUtils;
import com.huanshen.scaffolding.security.domain.LoginUser;
import com.huanshen.scaffolding.security.domain.UserContext;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * ResponseUtil
 *
 * @description
 * @date 2024-06-04 20:10
 */
public class ResponseUtil {
    private ResponseUtil() {
    }

    /**
     * 输出流中写入错误码与错误提醒，继续向下执行
     * @param enums
     * @param params
     */
    public static void response(ExceptionEnums enums, Object... params) {
        response(enums.getHttpCode(), enums.getErrorCode(), enums.getErrorCode(), params);
    }

    public static void response(Integer httpCode, String errorCode, String msgKey, Object... params) {
        HttpServletResponse httpServletResponse = ServletUtils.getResponse();
        if (httpServletResponse == null) {
            return;
        }
        httpServletResponse.setStatus(httpCode);
        httpServletResponse.setContentType("application/json; charset=utf-8");
        try (PrintWriter out = httpServletResponse.getWriter()) {
            BaseResponse baseResponse = createResp(errorCode, msgKey, params);
            out.append(JSON.toJSONString(baseResponse));
        } catch (IOException ignored) {
        }
    }

    public static BaseResponse createResp(String errorCode, String msgKey, Object... params) {
        String lang = null;
        LoginUser loginUser = UserContext.getCurrentUser();
        if (loginUser != null) {
            lang = loginUser.getLang();
        }
        String message = MultiLanguageUtil.getMessage(msgKey, lang);

        message = formatMessage(message, params);

        return BaseResponse.fail(errorCode, message);
    }

    public static String formatMessage(String message, Object... params) {
        if (params == null) {
            return message;
        }
        String param = Stream.of(params).map(String::valueOf).collect(Collectors.joining(","));
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
