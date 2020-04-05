package com.clf.cloud.common.utils;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: clf
 * @Date: 19-1-18
 * @Description:
 * 客户端工具类
 */
public class ClientUtils {

    /**
     * 获取客户端请求的ip
     * @param request
     * @return
     */
    public static String getClientIpAddress(HttpServletRequest request) {
        String clientIp = request.getHeader("x-forwarded-for");
        if(clientIp == null || clientIp.length() == 0 || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getHeader("Proxy-Client-IP");
        }
        if(clientIp == null || clientIp.length() == 0 || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getHeader("WL-Proxy-Client-IP");
        }
        if(clientIp == null || clientIp.length() == 0 || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getRemoteAddr();
        }
        return clientIp;
    }

    public static boolean isRightMobileNum(String mobile) {
        if(StringUtils.isEmpty(mobile) || mobile.length() != 11) {
            return false;
        }
        String regex = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0,5-9]))\\d{8}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(mobile);
        if(m.matches()) {
            return true;
        }
        return false;
    }

    public static String formatPhone(String phone) {
        phone = phone.replace("-", "");
        phone = phone.replace("+86", "");
        phone = phone.replace(" ", "");
        return phone;
    }
}
