package com.clf.cloud.common.enums;

import lombok.Getter;

/**
 * @Author: clf
 * @Date: 2020-02-29
 * @Description: 错误枚举
 */
@Getter
public enum ErrorEnum {
    NOT_FOUND(404, "请求有问题，请联系管理员"),
    SERVER_ERROR(500,"服务器未知错误:%s" ),
    MOBILE_FORMAT_ERROR(501, "手机号格式错误"),
    MOBILE_EXISTED(502, "手机号已被注册"),
    BIND_ERROR(504, "%s"),
    REQUEST_METHOD_ERROR(505, "不支持%s的请求方式"),
    USERNAME_EXISTED(507, "用户名已存在"),
    VERIFY_CODE_EXPIRED(508, "验证码过期"),
    VERIFY_CODE_ERROR(509, "验证码错误"),
    MD5_ERROR(510, "MD5加密异常"),
    PARAM_CANT_NULL(511, "参数不能为空"),
    USER_NOT_FOUND(512, "用户不存在"),
    PASSWORD_WRONG(513, "用户密码错误"),
    PARSE_ERROR(514, "日期格式转换错误");
    private Integer code;
    private String msg;

    ErrorEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
