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
import com.tonpower.crm.workbench.domain.Tran;
import com.tonpower.crm.workbench.service.ActivityService;
import com.tonpower.crm.workbench.service.ClueService;
import com.tonpower.crm.workbench.service.impl.ActivityServiceImpl;
import com.tonpower.crm.workbench.service.impl.ClueServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClueController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("进入线索控制器");

        String path = request.getServletPath();

        if ("/workbench/clue/getUserList.do".equals(path)){
            getUserList(request,response);
        }else if ("/workbench/clue/save.do".equals(path)){
            save(request,response);
        }else if ("/workbench/clue/detail.do".equals(path)){
            detail(request,response);
        }else if ("/workbench/clue/getActivityListByClueId.do".equals(path)){
            getActivityListByClueId(request,response);
        }else if ("/workbench/clue/unbund.do".equals(path)){
            unbund(request,response);
        }else if ("/workbench/clue/getActivityByNameAndNotByClueId.do".equals(path)){
            getActivityByNameAndNotByClueId(request,response);
        }else if ("/workbench/clue/bund.do".equals(path)){
            bund(request,response);
        }else if ("/workbench/clue/getActivityByName.do".equals(path)){
            getActivityByName(request,response);
        }else if ("/workbench/clue/convert.do".equals(path)){
            convert(request,response);
        }else if ("/workbench/clue/pageList.do".equals(path)){
            pageList(request,response);
        }
}

    private void pageList(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("进入到查询线索信息列表的操作");
        String name = request.getParameter("name");
        String company = request.getParameter("company");
        String phone = request.getParameter("phone");
        String mphone = request.getParameter("mphone");
        String source = request.getParameter("source");
        String owner = request.getParameter("owner");
        String clueState = request.getParameter("clueState");

        String pageNo = request.getParameter("pageNo");
        // 每页展现的记录数
        String pageSizeStr = request.getParameter("pageSize");
        // 计算略过的记录数
        int pageSize = Integer.valueOf(pageSizeStr);
        int skipCount = (Integer.valueOf(pageNo) - 1) * Integer.valueOf(pageSize);

        Map<String,Object> map = new HashMap<String,Object>();
        map.put("name",name);
        map.put("owner",owner);
        map.put("company",company);
        map.put("phone",phone);
        map.put("mphone",mphone);
        map.put("source",source);
        map.put("clueState",clueState);
        map.put("pageSize",pageSize);
        map.put("skipCount",skipCount);

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        /*
        前端需要：市场活动信息集合，查询总列表数
        业务层的到这两条信息后需要返回map或者vo对象
        当前需求选择返回vo对象来制作分页功能
        选择使用泛型封装一个通用的vo类
         */
        PaginationVo<Clue> vo =  cs.pageList(map);
        System.out.println(vo);
        PrintJson.printJsonObj(response,vo);

    }

    private void convert(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("进入到线索转换操作");

        String clueId = request.getParameter("clueId");
        // 接受是否创建交易的标记
        String flag = request.getParameter("flag");
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        Tran t = null;
        if ("a".equals(flag)){

            t = new Tran();
            // 接受交易表单中的参数
            String money = request.getParameter("money");
            String name = request.getParameter("name");
            String expectedDate = request.getParameter("expectedDate");
            String stage = request.getParameter("stage");
            String activityId = request.getParameter("activityId");
            String id = UUIDUtil.getUUID();
            String createTime = DateTimeUtil.getSysTime();


            t.setId(id);
            t.setActivityId(activityId);
            t.setCreateBy(createBy);
            t.setStage(stage);
            t.setCreateTime(createTime);
            t.setExpectedDate(expectedDate);
            t.setMoney(money);
            t.setName(name);
        }
        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean flag1 = cs.convert(clueId,t,createBy);
        if (flag1){
            response.sendRedirect(request.getContextPath()+"/workbench/clue/index.jsp");
        }

    }
    private void getActivityByName(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行查询市场活动列表");

        String name = request.getParameter("aname");
        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        List<Activity> list = as.getActivityByName(name);

        PrintJson.printJsonObj(response,list);
    }

    private void bund(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行关联市场活动的操作");

        String clueId = request.getParameter("clueId");
        String activityId[] = request.getParameterValues("aId");

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean flag = cs.bund(clueId,activityId);

        PrintJson.printJsonFlag(response,flag);

    }

    private void getActivityByNameAndNotByClueId(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行添加关联的操作");

        String clueId = request.getParameter("clueId");
        String aname = request.getParameter("aname");
        Map<String,String> map = new HashMap<>();
        map.put("clueId",clueId);
        map.put("aname",aname);

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        List<Activity> list = as.getActivityByNameAndNotByClueId(map);
        System.out.println(list);
        PrintJson.printJsonObj(response,list);

    }

    private void unbund(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行解除关联操作");
        String id = request.getParameter("id");

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean flag = cs.unbund(id);
        PrintJson.printJsonFlag(response,flag);
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
        System.out.println(c.toString());
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
