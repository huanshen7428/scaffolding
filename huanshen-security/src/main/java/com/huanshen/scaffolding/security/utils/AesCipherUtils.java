package com.huanshen.scaffolding.security.utils;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;

/**
 * AesCipherUtils
 *
 * @description
 * @date 2023-11-24 16:10
 */
public enum AesCipherUtils {
    INS;

    private String encryptAESKey;

    private AES aes;

    public void setEncryptAESKey(String encryptAESKey) {
        this.encryptAESKey = encryptAESKey;
    }

    /**
     * 功能 : 获取加密后的私钥
     * <p>
     * 方法名称: getAes
     * 方法入参: []
     * 返回类型: cn.hutool.crypto.symmetric.AES
     * 异常类型
     *
     */
    private AES getAes() {
        if (aes == null) {
            byte[] key = cn.hutool.core.codec.Base64.decode(encryptAESKey);
            aes = SecureUtil.aes(key);
        }
        return aes;
    }

    /**
     * 功能 : 加密字符串
     * <p>
     * 方法名称: enCrypto
     * 方法入参: [str]
     * 返回类型: java.lang.String
     * 异常类型
     *
     */
    public String enCrypto(String str) {
        return getAes().encryptBase64(str);
    }

    /**
     * 功能 : 解密字符串
     * <p>
     * 方法名称: deCrypto
     * 方法入参: [str]
     * 返回类型: java.lang.String
     * 异常类型
     *
     */
    public String deCrypto(String str) {
        return getAes().decryptStr(cn.hutool.core.codec.Base64.decode(str));
    }

}
