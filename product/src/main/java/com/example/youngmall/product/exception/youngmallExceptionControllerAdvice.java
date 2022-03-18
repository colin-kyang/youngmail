package com.example.youngmall.product.exception;

import com.example.common.exception.BizCodeEnum;
import com.example.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

//@ResponseBody
//@ControllerAdvice
@Slf4j
@RestControllerAdvice(basePackages ="com.example.youngmall.product.controller")
public class youngmallExceptionControllerAdvice {

    //方法参数错误
    @ExceptionHandler(value= MethodArgumentNotValidException.class)
    public R handleValidException(MethodArgumentNotValidException e)
    {
        HashMap<String,String> respond=new HashMap<>();
        BindingResult result = e.getBindingResult();
        result.getFieldErrors().forEach((item)->{
            respond.put(item.getField(),item.getDefaultMessage());
        });

        return  R.error(BizCodeEnum.VALID_EXCEPTION.getCode(),BizCodeEnum.VALID_EXCEPTION.getMsg()).put("data",respond);
    }

    //其他错误
    @ExceptionHandler(value=Throwable.class)
    public R handException(Throwable throwable)
    {
        log.error("错误",throwable);
        return R.error(BizCodeEnum.UNKNOWN_EXCETION.getCode(),BizCodeEnum.UNKNOWN_EXCETION.getMsg());
    }
}
