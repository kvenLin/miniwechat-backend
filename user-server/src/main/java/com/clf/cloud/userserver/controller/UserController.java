package com.clf.cloud.userserver.controller;

import com.clf.cloud.userserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
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
public class UserController {

    @Autowired
    private UserService userService;

    @Value("${testValue}")
    private String testValue;

    @GetMapping("testValue")
    public String testValue() {
        return testValue;
    }

}
