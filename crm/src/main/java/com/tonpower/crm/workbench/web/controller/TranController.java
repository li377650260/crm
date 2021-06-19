package com.tonpower.crm.workbench.web.controller;

import com.tonpower.crm.settings.domain.User;
import com.tonpower.crm.settings.service.UserService;
import com.tonpower.crm.settings.service.impl.UserServiceImpl;
import com.tonpower.crm.utils.DateTimeUtil;
import com.tonpower.crm.utils.PrintJson;
import com.tonpower.crm.utils.ServiceFactory;
import com.tonpower.crm.utils.UUIDUtil;
import com.tonpower.crm.vo.PaginationVo;
import com.tonpower.crm.workbench.domain.*;
import com.tonpower.crm.workbench.service.ActivityService;
import com.tonpower.crm.workbench.service.ClueService;
import com.tonpower.crm.workbench.service.CustomerService;
import com.tonpower.crm.workbench.service.TranService;
import com.tonpower.crm.workbench.service.impl.ActivityServiceImpl;
import com.tonpower.crm.workbench.service.impl.ClueServiceImpl;
import com.tonpower.crm.workbench.service.impl.CustomerServiceImpl;
import com.tonpower.crm.workbench.service.impl.TranServiceImpl;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TranController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("进入交易控制器");

        String path = request.getServletPath();

        if ("/workbench/transaction/add.do".equals(path)){
            add(request,response);
        }else if ("/workbench/transaction/getCustomerName.do".equals(path)){
            getCustomerName(request,response);
        }else if ("/workbench/transaction/save.do".equals(path)){
            save(request,response);
        }else if ("/workbench/transaction/detail.do".equals(path)){
            detail(request,response);
        }else if ("/workbench/transaction/getHistoryByTranId.do".equals(path)){
            getHistoryByTranId(request,response);
        }else if ("/workbench/transaction/changeStage.do".equals(path)){
            changeStage(request,response);
        }else if ("/workbench/transaction/getCharts.do".equals(path)){
            getCharts(request,response);
        }
    }

    private void getCharts(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行交易图标绘制的操作");

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());
        Map<String,Object> map = ts.getCharts();

        PrintJson.printJsonObj(response,map);
    }

    private void changeStage(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行阶段改变的操作");

        String id = request.getParameter("id");
        String stage = request.getParameter("stage");
        String money = request.getParameter("money");
        String expectedDate = request.getParameter("expectedDate");
        String editBy = ((User)request.getSession().getAttribute("user")).getName();
        String editTime = DateTimeUtil.getSysTime();

        Tran t = new Tran();
        t.setId(id);
        t.setEditBy(editBy);
        t.setEditTime(editTime);
        t.setExpectedDate(expectedDate);
        t.setMoney(money);
        t.setStage(stage);

        Map<String,String> pMap = (Map<String, String>) request.getServletContext().getAttribute("pMap");
        t.setPossibility(pMap.get(stage));

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());
        Map<String,Object> map = new HashMap<>();

        boolean flag = ts.changeStage(t);
        map.put("success",flag);
        map.put("t",t);

        PrintJson.printJsonObj(response,map);

    }

    private void getHistoryByTranId(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("进入到交易历史的信息列表展示");
        Map<String,String> pMap = (Map<String, String>) request.getServletContext().getAttribute("pMap");
        String tranId = request.getParameter("tranId");

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());
        List<TranHistory> list = ts.getHistoryByTranId(tranId);

        for (TranHistory history : list){
            String stage = history.getStage();
            history.setPossibility(pMap.get(stage));

        }
        PrintJson.printJsonObj(response,list);
    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("跳转到详细信息页");

        String id = request.getParameter("id");
        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());

        Tran t = ts.detail(id);

        // 处理可能性
        String stage = t.getStage();
        ServletContext application = request.getServletContext();
        Map<String,String> pMap = (Map<String, String>) application.getAttribute("pMap");
        String possibility = pMap.get(stage);
        t.setPossibility(possibility);
        request.setAttribute("t",t);
//        request.setAttribute("possibility",possibility);
        request.getRequestDispatcher("/workbench/transaction/detail.jsp").forward(request,response);
    }

    private void save(HttpServletRequest request, HttpServletResponse response) throws IOException {

        System.out.println("执行交易添加的操作");

         String id = UUIDUtil.getUUID();
         String owner = request.getParameter("owner");
         String money = request.getParameter("money");
         String name = request.getParameter("name");
         String expectedDate = request.getParameter("expectedDate");
         String customerName = request.getParameter("customerName");
         String stage = request.getParameter("stage");
         String type = request.getParameter("type");
         String source = request.getParameter("source");
         String activityId = request.getParameter("activityId");
         String contactsId = request.getParameter("contactsId");
         String  createBy = ((User)request.getSession().getAttribute("user")).getName();
         String createTime = DateTimeUtil.getSysTime();
         String description = request.getParameter("description");
         String contactSummary = request.getParameter("contactSummary");
         String nextContactTime = request.getParameter("nextContactTime");

         Tran t = new Tran();
         t.setId(id);
         t.setOwner(owner);
         t.setName(name);
         t.setMoney(money);
         t.setExpectedDate(expectedDate);
         t.setStage(stage);
         t.setType(type);
         t.setCreateBy(createBy);
         t.setActivityId(activityId);
         t.setCreateTime(createTime);
         t.setSource(source);
         t.setNextContactTime(nextContactTime);
         t.setDescription(description);
         t.setContactSummary(contactSummary);
         t.setContactsId(contactsId);

         TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());
         boolean flag = ts.save(t,customerName);

         if (flag){
             response.sendRedirect(request.getContextPath() + "/workbench/transaction/index.jsp");
         }
    }

    private void getCustomerName(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到取得客户名称列表");

        String name = request.getParameter("name");

        CustomerService cs = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());
        List<String> sList = cs.getCustomerName(name);

        PrintJson.printJsonObj(response,sList);

    }

    private void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("进入到跳转到添加交易的页面");

        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> uList = us.getUserList();

        request.setAttribute("uList",uList);
        request.getRequestDispatcher("/workbench/transaction/save.jsp").forward(request,response);

    }

}
