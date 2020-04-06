package com.clf.cloud.userserver.bo;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author: clf
 * @Date: 2020-02-18
 * @Description: 注册表单
 */
@Data
public class RegisterBO {
    @NotNull(message = "用户名不能为空")
    private String username;
    @NotNull(message = "密码不能为空")
    private String password;
    @NotNull(message = "手机号不能为空")
    private String mobile;
    @NotNull(message = "验证码不能为空")
    private String verifyCode;
    private String cid;
}
