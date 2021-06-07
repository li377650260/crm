package com.tonpower.settings.test;

import com.tonpower.crm.settings.dao.UserDao;
import com.tonpower.crm.settings.domain.User;
import com.tonpower.crm.utils.DateTimeUtil;
import com.tonpower.crm.utils.MD5Util;
import com.tonpower.crm.utils.SqlSessionUtil;

import java.awt.*;
import java.util.List;

/**
 * @description:
 * @author: li377650260
 * @date: 2021/6/1 17:46
 */
public class Test1 {
    public static void main(String[] args) {
        // 验证失效时间

        // 失效时间
//        String expireTime = "2021-10-11 10:10:10";
//
//        // 当前时间
//        String nowTime = DateTimeUtil.getSysTime();
//        int count = expireTime.compareTo(nowTime);
//        System.out.println(count);

        // 验证锁定状态
        /*String lockState = "0";
        if ("0".equals(lockState)){
            System.out.println("账号已锁定");
        }*/

        // 验证IP信息
        /*String ip = "192.168.1.1";
        // 允许访问的ip地址群
        String allowIps = "192.168.1.1,192.165.1.1";
        if (allowIps.contains(ip)){
            System.out.println("有效的ip地址，允许访问");
        }else {
            System.out.println("ip地址受限，清练习管理员");
        }*/
        String pwd = "123";
        pwd = MD5Util.getMD5(pwd);
        System.out.println(pwd);
        // 创建时间，当前系统时间
//        String  createTime = DateTimeUtil.getSysTime();
        // 从当前用户信息获取创建人
//        String  createBy = ((User)request.getSession().getAttribute("user")).getName();

//        id
//        owner
//        name
//        startDate
//        endDate
//        cost
//        description
//        createTime
//        createBy
//        $.ajax({
//             url:"",
//             data:{
//
//             },
//             type:"",
//             dataType:"json",
//             success:function () {
//
//             },
//        })
    }
}
