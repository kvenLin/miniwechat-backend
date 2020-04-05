package com.clf.cloud.common.utils;

import com.alibaba.fastjson.JSON;
import com.clf.cloud.common.vo.BaseResponseVO;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.util.EntityUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @Author: clf
 * @Date: 2020-02-17
 * @Description: 短信验证码工具类
 */
@Slf4j
public class CodeMsgUtils {
    @Data
    private static class CodeEntity {
        String returnStatus;
        String message;
        Long remainPoint;
        Long taskID;
        Integer successCounts;
    }

    private static String appcode = "a9eb033c6f2d4ae19397741772044988";

    public static BaseResponseVO send(String mobile, String code) {
        String host = "https://cxkjsms.market.alicloudapi.com";
        String path = "/chuangxinsms/dxjk";
        String method = "POST";
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<>();
        querys.put("content", "【轻聊】你的验证码是：" + code + "，3分钟内有效！");
        querys.put("mobile", mobile);
        Map<String, String> bodys = new HashMap<>();
        try {
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            if(response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                log.error("response异常, response: { }", response.toString());
                return BaseResponseVO.errorMsg("发送短信请求异常");
            }
            String jsonResult = EntityUtils.toString(response.getEntity());
            CodeEntity codeEntity = JSON.parseObject(jsonResult, CodeEntity.class);
            if(codeEntity == null || !"Success".equals(codeEntity.getReturnStatus())) {
                log.error("发送短信返回实体异常, codeEntity: {}", codeEntity.toString());
                return BaseResponseVO.errorMsg("短信服务平台异常");
            }
        } catch (Exception e) {
            log.error("发送请求异常");
        }
        return BaseResponseVO.success();
    }

    /**
     * 生成四位数字的验证码
     * @return
     */
    public static String generateCode() {
        return String.format("%04d",new Random().nextInt(9999));
    }

}
