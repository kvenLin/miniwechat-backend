package com.clf.cloud.common.exception;

import com.clf.cloud.common.enums.ErrorEnum;
import com.clf.cloud.common.vo.BaseResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @Author: clf
 * @Date: 2020-02-29
 * @Description: 公共异常处理
 */
@Slf4j
@RestControllerAdvice
public class BaseExceptionHandler {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)//将抛异常的请求响应状态设置成500
    @ExceptionHandler(value = Exception.class)
    public BaseResponseVO exceptionHandler(HttpServletRequest request
            , Exception e){
        if (e instanceof CommonException){
            CommonException commonException = (CommonException) e;
            log.error("CommonException: code: {}, msg: {}", commonException.getCode(), commonException.getMsg());
            return BaseResponseVO.serviceException(commonException);
        }else if (e instanceof MethodArgumentNotValidException || e instanceof BindException){
            log.info(request.getParameterMap().toString());
            Map<String, String[]> parameterMap = request.getParameterMap();
            log.error("========================输出信息参数信息====================");
            parameterMap.forEach((s, strings) -> {
                log.error("param: " + s + ", value: " + strings);
            });
            log.error("========================参数打印完毕====================");
            List<FieldError> errors = null;
            if(e instanceof BindException) {
                BindException exception = (BindException) e;
                errors = exception.getFieldErrors();
            } else {
                MethodArgumentNotValidException exception = (MethodArgumentNotValidException) e;
                errors = exception.getBindingResult().getFieldErrors();
            }
            String msg = "";
            for (FieldError error : errors) {
                if (StringUtils.isEmpty(msg)){
                    msg = error.getDefaultMessage();
                }else {
                    msg += ","+error.getDefaultMessage();
                }
            }
            log.error(msg);
            BaseResponseVO<String> result = BaseResponseVO.error(ErrorEnum.BIND_ERROR);
            result.setMsg(String.format(result.getMsg(),msg));
            return result;
        }else if (e instanceof HttpRequestMethodNotSupportedException){
            HttpRequestMethodNotSupportedException exception = (HttpRequestMethodNotSupportedException) e;
            log.error("请求方式错误,不支持:{}",exception.getMethod());
            BaseResponseVO<String> result = BaseResponseVO.error(ErrorEnum.REQUEST_METHOD_ERROR);
            result.setMsg(String.format(result.getMsg(),exception.getMethod()));
            return result;
        }else {
            e.printStackTrace();
            String requestURI = request.getRequestURI();
            log.error("请求异常的接口:{}",requestURI);
            BaseResponseVO<String> error = BaseResponseVO.error(ErrorEnum.SERVER_ERROR);
            error.setMsg(String.format(error.getMsg(),e.getMessage()));
            return error;
        }
    }

}
