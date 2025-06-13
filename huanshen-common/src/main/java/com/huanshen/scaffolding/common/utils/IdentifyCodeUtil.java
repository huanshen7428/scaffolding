package com.huanshen.scaffolding.common.utils;

public class IdentifyCodeUtil {

    private IdentifyCodeUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static String createRandom(int length) {
        StringBuilder retStr = new StringBuilder();
        String strTable = "1234567890";
        int len = strTable.length();
        boolean bDone = true;
        do {
            int count = 0;
            for (int i = 0; i < length; i++) {
                double dblR = Math.random() * len;//NOSONAR
                int intR = (int) Math.floor(dblR);
                char c = strTable.charAt(intR);
                if (('0' <= c) && (c <= '9')) {
                    count++;
                }
                retStr = retStr.append(strTable.charAt(intR));
            }
            if (count >= 2) {
                bDone = false;
            }
        } while (bDone);

        return retStr.toString();
    }


}
