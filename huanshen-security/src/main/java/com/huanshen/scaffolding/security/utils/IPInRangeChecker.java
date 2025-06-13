package com.huanshen.scaffolding.security.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.util.SubnetUtils;
import org.apache.commons.net.util.SubnetUtils.SubnetInfo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class IPInRangeChecker {
    private static final Pattern ADDRESS_PATTERN = Pattern.compile("(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})");
    private static final Pattern CIDR_PATTERN = Pattern.compile("(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})/(\\d{1,2})");


    /**
     * 判断给定的IP地址是否在指定的CIDR格式的IP段内
     *
     * @param ipAddress    待检查的IP地址（如："192.168.1.100"）
     * @param cidrNotation CIDR格式的IP段（如："192.168.1.0/24"）
     * @return 如果IP地址在指定的IP段内，则返回true，否则返回false
     * @throws IllegalArgumentException 如果输入的参数格式不正确
     */
    public static boolean isIpAddressInRange(String ipAddress, String cidrNotation) throws IllegalArgumentException {
        //如果地址段是一个ip地址，则进行equals对比
        Matcher matcher = ADDRESS_PATTERN.matcher(cidrNotation);
        if (matcher.matches()) {
            return ipAddress.equals(cidrNotation);
        }
        SubnetUtils utils;
        try {
            utils = new SubnetUtils(cidrNotation);
        } catch (IllegalArgumentException e) {
            log.warn("地址段格式非法,{}", cidrNotation);
            return false;
        }
        SubnetInfo info = utils.getInfo();

        return info.isInRange(ipAddress);
    }
}
