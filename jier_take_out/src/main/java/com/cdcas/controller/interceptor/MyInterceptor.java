package com.cdcas.controller.interceptor;

import com.alibaba.fastjson.JSON;
import com.cdcas.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class MyInterceptor implements HandlerInterceptor {
    public static  final AntPathMatcher ANT_PATH_MATCHER=new AntPathMatcher();
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        /**
         *  对登录完善的过程
         *  4.判断登录状态,如果已登录,则直接放行
         *  5.如果未登录则返回未登录结果
         */


//        4.判断登录状态,如果已登录,则直接放行

        if (request.getSession().getAttribute("employee")!=null){
            return true;
        }

//        5.如果未登录则返回未登录结果，通过输出流方式向客服端页面响应数据
        response.sendRedirect("/backend/page/login/login.html");
        return true;
    }

}
