package com.sky.handler;

import com.sky.constant.MessageConstant;
import com.sky.exception.BaseException;
import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理器，处理项目中抛出的业务异常
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 捕获业务异常
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result exceptionHandler(BaseException ex){
        log.error("异常信息：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }
    @ExceptionHandler(DuplicateKeyException.class)
    public Result handleDuplicateKey(DuplicateKeyException ex) {
        // 获取根因（SQLIntegrityConstraintViolationException）
        Throwable rootCause = ex.getRootCause();
        if (rootCause instanceof SQLIntegrityConstraintViolationException) {
            String message = rootCause.getMessage();
            // 提取重复的用户名（如 "Duplicate entry '233' for key 'employee.idx_username'"）
            if (message.contains("Duplicate entry")) {
                String[] parts = message.split("'"); // 按单引号分割
                if (parts.length >= 2) {
                    String duplicateValue = parts[1]; // 取出 '233'
                    String errorMsg = duplicateValue + MessageConstant.USERNAME_ALREADY_EXISTS;
                    return Result.error(errorMsg); // 返回 "233用户名已存在"
                }
            }
        }
        // 默认错误提示
        return Result.error(MessageConstant.USERNAME_ALREADY_EXISTS);
    }



    @ExceptionHandler(Exception.class)
    public Result ex(Exception ex){
        ex.printStackTrace();
        return Result.error("操作出错！请联系管理员");
    }

}
