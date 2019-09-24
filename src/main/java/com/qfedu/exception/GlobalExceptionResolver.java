package com.qfedu.exception;

import com.qfedu.common.JsonResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 */

//@ControllerAdvice // 必须使用该注解
//@ResponseBody
@RestControllerAdvice
public class GlobalExceptionResolver {

    // @ExceptionHandler 修饰处理异常的方法
    // 注解的参数表示要处理的异常，注解修饰的方法，要使用异常作为参数
    @ExceptionHandler(Exception.class)
    public JsonResult exception(Exception e){

        return new JsonResult(1,e.getMessage());
    }
}
