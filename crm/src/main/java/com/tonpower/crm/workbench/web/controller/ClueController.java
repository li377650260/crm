package com.tonpower.crm.workbench.web.controller;

import com.tonpower.crm.settings.domain.User;
import com.tonpower.crm.settings.service.UserService;
import com.tonpower.crm.settings.service.impl.UserServiceImpl;
import com.tonpower.crm.utils.DateTimeUtil;
import com.tonpower.crm.utils.PrintJson;
import com.tonpower.crm.utils.ServiceFactory;
import com.tonpower.crm.utils.UUIDUtil;
import com.tonpower.crm.vo.PaginationVo;
import com.tonpower.crm.workbench.domain.Activity;
import com.tonpower.crm.workbench.domain.ActivityRemark;
import com.tonpower.crm.workbench.service.ActivityService;
import com.tonpower.crm.workbench.service.impl.ActivityServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClueController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("进入市场活动控制器");

        String path = request.getServletPath();

        if ("/workbench/activity/xxx.do".equals(path)){

        }else if ("/workbench/activity/xxx.do".equals(path)){

        }
}
}
