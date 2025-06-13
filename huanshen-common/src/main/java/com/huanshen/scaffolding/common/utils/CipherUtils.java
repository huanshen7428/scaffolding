package com.huanshen.scaffolding.common.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * Created on 2024/7/31.
 *
 */
public class CipherUtils {


    public static byte[] getCipherMd5(String key) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update("hs-cipher0/".getBytes());
            md.update("bless-cipher1/".getBytes());
            md.update("3735928559/".getBytes());
            md.update(key.getBytes());
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    public static String getCipherBase64(String key) {
        byte[] cipherBytes = getCipherMd5(key);
        if (cipherBytes != null) {
            return Base64.getEncoder().encodeToString(cipherBytes);
        } else {
            return null;
        }
    }
}
