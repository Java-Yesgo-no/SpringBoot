package com.cdcas.filter;

import com.alibaba.fastjson.JSON;
import com.cdcas.common.R;
import com.cdcas.pojo.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "loginCheckFilter",urlPatterns="/*")
@Slf4j
public class LoginCheckFilter implements Filter {

//    路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER=new AntPathMatcher();
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request=(HttpServletRequest)servletRequest;
        HttpServletResponse response=(HttpServletResponse)servletResponse;
        /**
         *  对登录完善的过程
         *  1.获取本次请求的URI
         *  2.判断本次请求是否需要处理
         *  3.如果不需要处理,则直接放行
         *  4.判断登录状态,如果已登录,则直接放行
         *  5.如果未登录则返回未登录结果
         */
//        1.获取本次请求的URI
        String requestURI=request.getRequestURI();

        log.info("拦截到请求:{}",requestURI);

//      定义不需要处理的请求路径
        String[] urls=new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**"

        };
//        2.判断本次请求是否需处理
        boolean check=check(requestURI,urls);

//        3.如果不需要处理，则直接放行
        if (check){
            log.info("拦截到请求{}不需要处理",requestURI);
            filterChain.doFilter(request,response);
            return;
        }
//        4.判断登录状态，如果已登录，则直接放行
        Employee employee= (Employee) request.getSession().getAttribute("employee");
         if(employee!=null){
            log.info("用户已登录,用户为:"+employee.getUsername());
            filterChain.doFilter(request,response);
            return;
        }
        log.info("用户未登录");
//        5.如果未登录则返回未登录结果，通过输出流方式向客服端页面响应数据
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
    }

    /**
     * 路径匹配，检查本次请求是否需要放行
     * @param requestURI
     * @param urls
     * @return
     */
    public boolean check(String requestURI,String[] urls){
        for (String url : urls) {
            boolean match=PATH_MATCHER.match(url,requestURI);
            if (match){
                return true;
            }
        }
        return false;
    }
}
