package com.clf.cloud.authorizationserver.controller;

import com.clf.cloud.api.auth.AuthorizationFeignApis;
import com.clf.cloud.api.user.UserFeignApis;
import com.clf.cloud.api.vo.UsersVO;
import com.clf.cloud.common.utils.JwtTokenUtils;
import com.clf.cloud.common.vo.BaseResponseVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @Author: clf
 * @Date: 2020-04-04
 * @Description: TODO
 */
@RestController
@RequestMapping("/auth")
public class AuthorizationController implements AuthorizationFeignApis {

    @Autowired
    private UserFeignApis userFeignApis;

    @PostMapping("/check")
    @Override
    public BaseResponseVO check(String authToken) {
        if(StringUtils.isNotEmpty(authToken)) {
            JwtTokenUtils jwtTokenUtil = new JwtTokenUtils();
            boolean flag = jwtTokenUtil.isTokenExpired(authToken);
            if (!flag) {
                // 2、解析出JWT中的payload -> userid - randomkey
//                String randomkey = jwtTokenUtil.getMd5KeyFromToken(authToken);
                String username = jwtTokenUtil.getUsernameFromToken(authToken);
                // 3、是否需要验签,以及验签的算法

                // 4、判断userid是否有效
                BaseResponseVO<UsersVO> userInfo = userFeignApis.queryUserInfo(username);
                if (userInfo.getStatus() == HttpStatus.OK.value() &&
                        username.equals(userInfo.getData().getId())) {
                    return BaseResponseVO.success();
                }
            }
        }
        return BaseResponseVO.noLogin();
    }
}
