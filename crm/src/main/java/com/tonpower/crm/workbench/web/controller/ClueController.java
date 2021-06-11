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
import com.tonpower.crm.workbench.domain.Clue;
import com.tonpower.crm.workbench.service.ActivityService;
import com.tonpower.crm.workbench.service.ClueService;
import com.tonpower.crm.workbench.service.impl.ActivityServiceImpl;
import com.tonpower.crm.workbench.service.impl.ClueServiceImpl;

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

        if ("/workbench/clue/getUserList.do".equals(path)){
            getUserList(request,response);
        }else if ("/workbench/clue/save.do".equals(path)){
            save(request,response);
        }else if ("/workbench/clue/detail.do".equals(path)){
            detail(request,response);
        }else if ("/workbench/clue/getActivityListByClueId.do".equals(path)){
            getActivityListByClueId(request,response);
        }
}

    private void getActivityListByClueId(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到查询关联市场活动列表");

        String clueId = request.getParameter("clueId");
        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());


        List<Activity> aList = as.getActivityListByClueId(clueId);
        PrintJson.printJsonObj(response,aList);
    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入查询详细信息的操作");

        String id = request.getParameter("id");
        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        Clue c = cs.detail(id);

        request.setAttribute("c",c);
        request.getRequestDispatcher("/workbench/clue/detail.jsp").forward(request,response);

    }

    private void save(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到线索添加的操作");
        String id = UUIDUtil.getUUID();
        String fullname = request.getParameter("fullname");
        String appellation = request.getParameter("appellation");
        String owner = request.getParameter("owner");
        String company = request.getParameter("company");
        String job = request.getParameter("job");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String website = request.getParameter("website");
        String mphone = request.getParameter("mphone");
        String state = request.getParameter("state");
        String source = request.getParameter("source");
        String description = request.getParameter("description");
        String contactSummary = request.getParameter("contactSummary");
        String nextContacTime = request.getParameter("nextContacTime");
        String address = request.getParameter("address");
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        String createTime = DateTimeUtil.getSysTime();

        Clue c = new Clue();
        c.setId(id);
        c.setAppellation(appellation);
        c.setAddress(address);
        c.setCompany(company);
        c.setCreateBy(createBy);
        c.setCreateTime(createTime);
        c.setDescription(description);
        c.setContactSummary(contactSummary);
        c.setEmail(email);
        c.setFullname(fullname);
        c.setJob(job);
        c.setPhone(phone);
        c.setWebsite(website);
        c.setMphone(mphone);
        c.setSource(source);
        c.setState(state);
        c.setOwner(owner);
        c.setNextContactTime(nextContacTime);

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean flag = cs.save(c);

        PrintJson.printJsonFlag(response,flag);
    }

    private void getUserList(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("进入取得用户信息列表的操作");

        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());

        List<User> userList =   us.getUserList();
        PrintJson.printJsonObj(response,userList);
    }
}
