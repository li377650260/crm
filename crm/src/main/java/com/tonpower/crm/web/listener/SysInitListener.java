package com.tonpower.crm.web.listener;

import com.tonpower.crm.settings.domain.DicValue;
import com.tonpower.crm.settings.service.DicService;
import com.tonpower.crm.settings.service.impl.DicServiceImpl;
import com.tonpower.crm.utils.PrintJson;
import com.tonpower.crm.utils.ServiceFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.*;

/**
 * @description:
 * @author: li377650260
 * @date: 2021/6/8 15:18
 */
public class SysInitListener implements ServletContextListener {
    @Override
//    event:该参数可以取得监听的对象，可以通过该参数来取得上下文域对象
    public void contextInitialized(ServletContextEvent event) {

        System.out.println("上下文域对象被创建了");

        ServletContext application = event.getServletContext();

        // 调用业务层取得数据字典中的数据
        DicService ds = (DicService) ServiceFactory.getService(new DicServiceImpl());
        Map<String, List<DicValue>> map = new HashMap<>();
        map = ds.getAll();

        // 将map对象解析成为上下文域对象中的键值对，遍历map
        Set<String> set = map.keySet();
        for (String key:set){
            application.setAttribute(key,map.get(key));
        }


        // ------------------------------------------------------

        // 数据字典完毕处理完毕后，开始解析属性文件
        /*
        处理属性文件的步骤：
            解析文件将属性文件中的键值对解析为Java中的键值对关系（map）
            Map<String(stage),String(possibility)> map
         */

        // 解析属性文件
        ResourceBundle rb = ResourceBundle.getBundle("Stage2Possibility");
        Map<String,String> pMap = new HashMap<>();
        // 枚举类：值一般是固定的不会轻易改变的
        Enumeration em = rb.getKeys();

        while (em.hasMoreElements()){
            String key = (String) em.nextElement();
            String value =  rb.getString(key);

            pMap.put(key,value);
        }

        application.setAttribute("pMap",pMap);
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {

        System.out.println("上下文域对象被销毁了");

    }
}
