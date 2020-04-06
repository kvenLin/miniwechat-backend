package com.clf.cloud.userserver.service;

import com.clf.cloud.common.vo.BaseResponseVO;

public interface MsgService {
    /**
     * 发送短信验证码
     * @param ip
     * @param mobile
     * @return
     */
    BaseResponseVO sendMsg(String ip, String mobile);
}
