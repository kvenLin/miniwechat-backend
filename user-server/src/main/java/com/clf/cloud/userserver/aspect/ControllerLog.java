package com.clf.cloud.userserver.aspect;

import com.clf.cloud.common.vo.BaseResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * controller层日志
 * @author clf
 */
@Aspect
@Slf4j
@Component
public class ControllerLog {

    @Pointcut("execution(public * com.clf.cloud.userserver.controller.*.*(..))")
    public void controller(){
    }

    @Before("controller()")
    public void before(JoinPoint joinPoint){
        Signature signature = joinPoint.getSignature();
        String method = signature.getDeclaringTypeName()+"."+signature.getName();
        log.warn("-----------------------------------------------------");
        log.warn("当前执行controller的方法:  "+method);
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            log.warn("参数: "+ arg);
        }
    }

    @AfterReturning(pointcut = "controller()",returning = "ret")
    public void after(Object ret){
        BaseResponseVO result = (BaseResponseVO) ret;
        if (result!=null&&result.getCode()!=200){
            log.error(result.getMsg());
        }
        log.warn("controller返回参数："+result);
        log.warn("-----------------------------------------------------");
    }
}
