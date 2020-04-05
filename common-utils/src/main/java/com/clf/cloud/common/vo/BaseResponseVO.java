package com.clf.cloud.common.vo;

import com.clf.cloud.common.enums.ErrorEnum;
import com.clf.cloud.common.exception.CommonException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * @Author: clf
 * @Date: 2020-02-29
 * @Description: TODO
 */
@Data
@Builder
@AllArgsConstructor
public class BaseResponseVO<T> {
    private Integer code;
    private String msg;
    private T data;

    private BaseResponseVO() {
    }

    // 未登录异常
    public static<T> BaseResponseVO noLogin(){
        BaseResponseVO response = new BaseResponseVO();
        response.setCode(HttpStatus.UNAUTHORIZED.value());
        response.setMsg("请登录");
        return response;
    }

    public static BaseResponseVO success() {
        return success(null);
    }

    public static<T> BaseResponseVO success(T data) {
        return BaseResponseVO.builder().code(HttpStatus.OK.value()).msg("success").data(data).build();
    }

    public static BaseResponseVO serviceException(CommonException e) {
        return BaseResponseVO.builder().code(e.getCode()).msg(e.getMsg()).build();
    }

    public static BaseResponseVO error(ErrorEnum error) {
        return BaseResponseVO.builder().code(error.getCode()).msg(error.getMsg()).build();
    }

    public static BaseResponseVO errorMsg(String msg){
        return new BaseResponseVO(500, msg, null);
    }
}
