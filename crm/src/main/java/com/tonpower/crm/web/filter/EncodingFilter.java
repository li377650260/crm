package com.tonpower.crm.web.filter;

import javax.servlet.*;
import java.io.IOException;

/**
 * @description:
 * @author: li377650260
 * @date: 2021/6/2 0:41
 */
public class EncodingFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        // 过滤请求中的中文乱码
        servletRequest.setCharacterEncoding("UTF-8");
        // 过滤响应流中的中文乱码
        servletResponse.setContentType("text/html;charset=utf-8");

        filterChain.doFilter(servletRequest,servletResponse);
    }
}
