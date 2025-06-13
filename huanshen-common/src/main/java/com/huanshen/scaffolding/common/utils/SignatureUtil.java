package com.huanshen.scaffolding.common.utils;

import cn.hutool.json.JSONUtil;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * AuthUtil
 *
 * @description 签名工具类
 */
public class SignatureUtil {
    private static final String ALGORITHM = "hmacsha256";

    public static String createSha(String apiKey, String username, String password, Long timestamp) {
        String preStr = String.format("apiKey:%s,timestamp:%s,username:%s,password:%s", apiKey, timestamp, username, password);
        Mac mac;
        try {
            mac = Mac.getInstance(ALGORITHM);
            SecretKeySpec spec = new SecretKeySpec(apiKey.getBytes(StandardCharsets.UTF_8), ALGORITHM);
            mac.init(spec);
        } catch (Exception e) {
            return null;
        }
        byte[] hexDigits = mac.doFinal(preStr.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hexDigits);
    }

    public static String createSignature(String apiKey, String username, String password, Long timestamp) {
        String sha = createSha(apiKey, username, password, timestamp);
        String authorization = JSONUtil.toJsonPrettyStr(new SignatureDto(apiKey, username, timestamp, sha));
        return Base64.getEncoder().encodeToString(authorization.getBytes(StandardCharsets.UTF_8));
    }

    public static SignatureDto decodeSignature(String signature) {
        byte[] decode = Base64.getDecoder().decode(signature.getBytes(StandardCharsets.UTF_8));
        String authorization = new String(decode, StandardCharsets.UTF_8);
        return JSONUtil.toBean(authorization, SignatureDto.class);
    }
    @Data
    @AllArgsConstructor
    public static class SignatureDto implements Serializable {
        private String apiKey;
        private String username;
        private Long timestamp;
        private String sha;
    }
}

