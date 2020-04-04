package com.clf.cloud.api.auth;

import com.clf.cloud.common.vo.BaseResponseVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @Author: clf
 * @Date: 2020-04-04
 * @Description: TODO
 */

@FeignClient("auth-server")
public interface AuthorizationFeignApis {
    @RequestMapping(value = "/auth/check", method = RequestMethod.POST)
    BaseResponseVO check(String headerString);
}
