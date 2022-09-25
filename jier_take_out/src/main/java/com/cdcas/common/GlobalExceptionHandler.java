package com.cdcas.common;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理
 *
 */
@ControllerAdvice(annotations ={RestController.class,Controller.class} )
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

        /**
         * 数据库数据不唯一异常处理方法
         * @return
         */
        @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
        public R<String> exceptionHandler(@NotNull SQLIntegrityConstraintViolationException ex){
            log.error(ex.getMessage());
            if(ex.getMessage().contains("Duplicate entry")){
                String[] split= ex.getMessage().split(" ");
                String msg=split[2]+"已存在";
               return R.error(msg);
            }
            return R.error("未知错误");
        }

        /**
         * 菜品分类业务处理方法
         * @return
         */
        @ExceptionHandler(CustomException.class)
        public R<String> exceptionHandler(@NotNull CustomException ex){
            log.error(ex.getMessage());
            return R.error(ex.getMessage());
        }
}
