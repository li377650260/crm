package com.tonpower.crm.settings.web.controller;

import com.tonpower.crm.settings.domain.User;
import com.tonpower.crm.settings.service.UserService;
import com.tonpower.crm.settings.service.impl.UserServiceImpl;
import com.tonpower.crm.utils.MD5Util;
import com.tonpower.crm.utils.PrintJson;
import com.tonpower.crm.utils.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("进入用户控制器");

        String path = request.getServletPath();

        if ("/settings/user/login.do".equals(path)){
            login(request,response);
        }else if ("/settings/user/xxx.do".equals(path)){

        }
    }

    public void login(HttpServletRequest request , HttpServletResponse response){
        System.out.println("进入到登录验证操作");

        String loginAct = request.getParameter("loginAct");
        String loginPwd = request.getParameter("loginPwd");
        // 将密码明文转换为密文形式
        loginPwd = MD5Util.getMD5(loginPwd);

        // 接受浏览器IP地址
        String ip = request.getRemoteAddr();
        System.out.println(ip);

        // 未来业务层开发，统一使用代理类形态的接口对象
        // 传递impl接口实现类得到代理实现类对象
        UserService service = (UserService) ServiceFactory.getService(new UserServiceImpl());

        try {
            User user = service.login(loginAct,loginPwd,ip);
            request.getSession().setAttribute("user",user);

            // 如果登录验证成功为前端返回一个信息
            // {success:true}
            PrintJson.printJsonFlag(response,true);
        }catch (Exception e){

            // 如果登录验证失败为前端返回一个信息
            // {success:true}

            String msg = e.getMessage();
            /*
             如果控制器controller需要为ajax返回多个信息，则可以使用两个方法
             1.可以将多个信息包装成map，将map通过工具解析成json串
             2.需要创建一个vo对象  将信息封装一个对象
             如果对于展现的信息未来还需要大量使用，可以进行创建vo对象方便以后使用
             如果未来对展现的信息不需要进行使用，则直接使用map即可
             */
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("succes",false);
            map.put("msg",msg);
            PrintJson.printJsonObj(response,map);
            e.printStackTrace();
        }
    }
}
