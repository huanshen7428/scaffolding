package com.huanshen.scaffolding.common.multilanguage;

import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.Map;

/**
 * Created on 2023/6/28.
 *
 */
@Slf4j
public class MultiLanguageUtil {

    private static Map<String, BsI18nResource> MULTI_LANGUAGE_CACHE;

    public static void setMultiLanguageCache(Map<String, BsI18nResource> multiLanguageCache) {
        MULTI_LANGUAGE_CACHE = Collections.unmodifiableMap(multiLanguageCache);
    }

    /**
     * 功能 : 根据语言类型获取指定消息的国际化消息内容
     * <p>
     * 方法名称: getMessage
     *
     * @param key  : 多语言key
     * @param lang : 语言类型
     * @return : java.lang.String
     * 异常类型
     */
    public static String getMessage(String key, String lang) {
        if (key == null || key.isEmpty()) {
            log.error("获取多语言文本异常,入参不可为空. key:{}, lang:{}", key, lang);
        }
        if (MULTI_LANGUAGE_CACHE == null) {
            log.error("获取多语言文本异常，无数据");
            return null;
        }
        BsI18nResource bsI18nResource = MULTI_LANGUAGE_CACHE.get(key);
        if (bsI18nResource == null) {
            log.error("BsI18nResource[{}] not found", key);
            return null;
        }
        if ("en".equals(lang)) {
            return bsI18nResource.getEnUs();
        } else {
            return bsI18nResource.getZhCn();
        }
    }
}
