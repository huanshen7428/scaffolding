package com.huanshen.scaffolding.security.config;

import com.huanshen.scaffolding.security.utils.AesCipherUtils;
import com.huanshen.scaffolding.security.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * 初始化相关配置
 *
 * @date 2023-11-24 16:05
 */
@Configuration
@ConditionalOnProperty("encryptAESKey")
public class SecurityConfigInit {

    @Autowired
    private TokenConfigProperties tokenConfigProperties;

    @Value("${encryptAESKey}")
    private String encryptAESKey;

    /**
     * 初始化工具类
     */
    @PostConstruct
    public void init() {
        JwtUtils.INS.setConfig(tokenConfigProperties);
        AesCipherUtils.INS.setEncryptAESKey(encryptAESKey);
    }

}
