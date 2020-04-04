package com.clf.cloud.gatewayserver.filters;

import com.alibaba.fastjson.JSONObject;
import com.clf.cloud.api.auth.AuthorizationFeignApis;
import com.clf.cloud.common.utils.ToolUtils;
import com.clf.cloud.common.vo.BaseResponseVO;
import com.clf.cloud.gatewayserver.dao.AuthQueryDao;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
public class AuthFilter extends ZuulFilter {

    @Value("${totalUrls}")
    private String totalUrls;

    @Autowired
    private AuthQueryDao authQueryDao;
    @Autowired
    private AuthorizationFeignApis authorizationFeignApis;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
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
        List<String> anonUrl = authQueryDao.selectAnonUrl();
        log.info("anonUrl: {}", anonUrl);
        if (anonUrl.contains(servletPath)) {
            log.info("请求匿名接口直接放行");
            //请求匿名接口直接放行
            return null;
        }
//        BaseResponseVO responseVO = authorizationFeignApis.check(request.getHeader("Authorization"));
        //TODO, 测试使用
        BaseResponseVO responseVO = BaseResponseVO.success();
        //校验token,对请求的url进行放行
        if(responseVO.getCode() == HttpStatus.OK.value()) {
            if(servletPath.equals("/netty/link")) {
                log.info("get in /netty/link, totalUrls: {}", totalUrls);
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
