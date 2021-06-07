package com.tonpower.crm.web.filter;

import com.tonpower.crm.settings.domain.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @description:
 * @author: li377650260
 * @date: 2021/6/2 17:12
 */
public class LoginFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        HttpSession session = request.getSession();
        User u = (User) session.getAttribute("user");
        String uri = request.getRequestURI(); // 【/网站名/资源文件名】
        // 判断如果本次请求的资源文件与登录有关则放行，或者是欢迎页面也放行
        if (uri.indexOf("login") != -1|| "/myweb/".equals(uri)){
            filterChain.doFilter(servletRequest,servletResponse);
            return;
        }
        if (u != null){
            filterChain.doFilter(servletRequest,servletResponse);
        }else {
            response.sendRedirect(request.getContextPath() + "/login.html");
        }
    }
}
