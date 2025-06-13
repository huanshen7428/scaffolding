package com.huanshen.scaffolding.security.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "token")
@Data
public class TokenConfigProperties {

    /**
     * token过期时间，单位分钟
     */
    private Integer tokenExpireTime;

    /**
     * 更新令牌时间，单位分钟
     */
    private Integer refreshCheckTime;


    /**
     * token加密密钥
     */
    private String secretKey;
}
