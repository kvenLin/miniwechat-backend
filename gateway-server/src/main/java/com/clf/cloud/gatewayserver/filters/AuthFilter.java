package com.clf.cloud.gatewayserver.filters;

import com.alibaba.fastjson.JSONObject;
import com.clf.cloud.api.auth.AuthorizationFeignApis;
import com.clf.cloud.common.enums.ErrorEnum;
import com.clf.cloud.common.utils.ClientUtils;
import com.clf.cloud.common.utils.ToolUtils;
import com.clf.cloud.common.vo.BaseResponseVO;
import com.clf.cloud.gatewayserver.dao.AuthQueryDao;
import com.clf.cloud.gatewayserver.redis.RedisService;
import com.clf.cloud.gatewayserver.redis.key.ClientIpAuthKey;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Author: clf
 * @Date: 2020-04-04
 * @Description: TODO
 */
@Slf4j
@Component
@RefreshScope
public class AuthFilter extends ZuulFilter {

    @Value("${totalUrls}")
    private String totalUrls;

    @Autowired
    private AuthQueryDao authQueryDao;
    @Autowired
    private AuthorizationFeignApis authorizationFeignApis;
    @Autowired
    private RedisService redisService;

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        log.info("in AuthFilter.....");
        // ThreadLocal
        RequestContext ctx = RequestContext.getCurrentContext();

        // 获取当前请求和返回值
        HttpServletRequest request = ctx.getRequest();
        HttpServletResponse response = ctx.getResponse();

        // 提前设置请求继续，如果失败则修改此内容
        ctx.setSendZuulResponse(true);
        ctx.setResponseStatusCode(200);

        String servletPath = request.getServletPath();
        log.info("servletPath: {}", servletPath);
        List<String> anonUrl = authQueryDao.selectAnonUrl();
        log.info("anonUrl: {}", anonUrl);
        if (anonUrl.contains(servletPath)) {
            log.info("请求匿名接口直接放行");
            //请求匿名接口直接放行
            return null;
        }
        String token = request.getHeader("Authorization");
        BaseResponseVO responseVO = BaseResponseVO.error(ErrorEnum.AUTH_ERROR);
        if(StringUtils.isNotEmpty(token)) {
             responseVO = authorizationFeignApis.check(token);
        }
        //TODO, 测试使用
//        BaseResponseVO responseVO = BaseResponseVO.success();
        //校验token,对请求的url进行放行
        if(responseVO.getStatus() == HttpStatus.OK.value()) {
            if(servletPath.equals("/netty/link")) {
                String clientIp = ClientUtils.getClientIpAddress(request);
                redisService.set(ClientIpAuthKey.getByClientIP, clientIp, "authConnect");
                log.info("request url: /netty/link, totalUrls: {}", totalUrls);
                //获取netty服务的url信息
                String[] split = totalUrls.split(",");
                int i = ToolUtils.randomNum(split.length);
                renderJson(ctx, response, BaseResponseVO.success(split[i]));
            }
        } else {
            renderJson(ctx, response, BaseResponseVO.noLogin());
        }
        return null;
    }

    /**
     * 渲染json对象
     */
    public static void renderJson(RequestContext ctx, HttpServletResponse response, Object jsonObject) {
        // 设置终止请求
        response.setHeader("Content-Type", "application/json;charset=UTF-8");
        ctx.setSendZuulResponse(false);
        ctx.setResponseBody(JSONObject.toJSONString(jsonObject));
    }
}
