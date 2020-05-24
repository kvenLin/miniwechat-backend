package com.clf.cloud.api.user;

import com.clf.cloud.api.vo.UsersVO;
import com.clf.cloud.common.vo.BaseResponseVO;
import feign.Param;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("user-server")
public interface UserFeignApis {
    @RequestMapping(value = "/users/queryUserInfo", method = RequestMethod.GET)
    BaseResponseVO<UsersVO> queryUserInfo(@Param("username") String username);


}
