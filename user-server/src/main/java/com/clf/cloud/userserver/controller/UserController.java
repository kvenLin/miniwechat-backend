package com.clf.cloud.userserver.controller;

import com.clf.cloud.api.user.UserFeignApis;
import com.clf.cloud.api.vo.UsersVO;
import com.clf.cloud.common.vo.BaseResponseVO;
import com.clf.cloud.userserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: clf
 * @Date: 2020-03-12
 * @Description: TODO
 */
@RestController
@RequestMapping("/users")
@RefreshScope
public class UserController implements UserFeignApis {

    @Autowired
    private UserService userService;

    @GetMapping("/{userId}")
    @Override
    public BaseResponseVO<UsersVO> queryUserInfo(@PathVariable("userId") String userId) {
        return BaseResponseVO.success(userId);
    }
}
