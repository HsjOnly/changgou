package com.itheima.changgou.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

//@ControllerAdvice
public class BaseExceptionHanlder {

    @ExceptionHandler
    @ResponseBody
    public String exceptionHandler(Exception e) {
        return "哦豁，发生错误了...";
    }
}
