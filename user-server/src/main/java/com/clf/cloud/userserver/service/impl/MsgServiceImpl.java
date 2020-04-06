package com.clf.cloud.userserver.service.impl;

import com.clf.cloud.common.utils.CodeMsgUtils;
import com.clf.cloud.common.vo.BaseResponseVO;
import com.clf.cloud.userserver.redis.RedisService;
import com.clf.cloud.userserver.redis.key.MsgCodeKey;
import com.clf.cloud.userserver.service.MsgService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: clf
 * @Date: 2020-02-17
 * @Description: TODO
 */
@Service
@Slf4j
public class MsgServiceImpl implements MsgService {

    @Autowired
    private RedisService redisService;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public BaseResponseVO sendMsg(String ip, String mobile) {
        //使用redis对IP和手机号进行限制发送, 三分钟才能重新发送验证码, 避免同一ip非法刷接口操作
        String msgCode = redisService.get(MsgCodeKey.getByClientIP, ip, String.class);
        if(StringUtils.isNotEmpty(msgCode)) {
            return BaseResponseVO.errorMsg("该IP对应验证码已发送,1分钟内不能重复发送");
        }
        msgCode = redisService.get(MsgCodeKey.getByMobile, mobile, String.class);
        if(StringUtils.isNotEmpty(msgCode)) {
            return BaseResponseVO.errorMsg("对应该手机号的验证码已发送,1分钟内不能重复发送");
        }
        String code = CodeMsgUtils.generateCode();
        BaseResponseVO result = CodeMsgUtils.send(mobile, code);
        if(result.getCode() == HttpStatus.SC_OK) {
            log.warn("发送验证成功, verifyCode: " + code);
            //redis保存ip-code & mobile-code 并设置有效时长3分钟
            redisService.set(MsgCodeKey.getByClientIP, ip, code);
            redisService.set(MsgCodeKey.getByMobile, mobile, code);
        }
        return result;
    }
}
