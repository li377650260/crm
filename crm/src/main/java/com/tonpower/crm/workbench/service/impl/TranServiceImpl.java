package com.tonpower.crm.workbench.service.impl;

import com.tonpower.crm.utils.SqlSessionUtil;
import com.tonpower.crm.utils.UUIDUtil;
import com.tonpower.crm.workbench.dao.CustomerDao;
import com.tonpower.crm.workbench.dao.TranDao;
import com.tonpower.crm.workbench.dao.TranHistoryDao;
import com.tonpower.crm.workbench.domain.Customer;
import com.tonpower.crm.workbench.domain.Tran;
import com.tonpower.crm.workbench.domain.TranHistory;
import com.tonpower.crm.workbench.service.TranService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: li377650260
 * @date: 2021/6/16 14:45
 */
public class TranServiceImpl implements TranService {
    private TranDao  tranDao = null;
    private TranHistoryDao tranHistoryDao = null;
    private CustomerDao customerDao = null;

    @Override
    public boolean save(Tran t, String customerName) {
        tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
        customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
        tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);
        /*

        在交易的实体类对象中缺少一项必要的属性就是customerId（客户的主键）
        可以通过参数客户名字来决定客户的主键
        在客户表中进行精准查找，如果有这个客户则取出ID，如果没有这个客户则新建一条客户信息，取出ID封装交易对象中
        在交易添加完成后，需要创建一条交易历史
        */
        boolean flag = true;

        Customer customer = customerDao.getCustomerByName(customerName);
        if (customer == null){

            customer = new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setName(customerName);
            customer.setCreateBy(t.getCreateBy());
            customer.setCreateTime(t.getCreateTime());
            customer.setContactSummary(t.getContactSummary());
            customer.setOwner(t.getOwner());
            customer.setNextContactTime(t.getNextContactTime());
            // 添加客户
            int count1 = customerDao.save(customer);
            if (count1 != 1){
                flag = false;
            }

        }
        t.setCustomerId(customer.getId());

        // 添加交易
        int count2 = tranDao.save(t);
        if (count2 != 1){
            flag = false;
        }

        // 添加交易历史
        TranHistory th = new TranHistory();
        th.setId(UUIDUtil.getUUID());
        th.setTranId(t.getId());
        th.setStage(t.getStage());
        th.setCreateBy(t.getCreateBy());
        th.setCreateTime(t.getCreateTime());
        th.setMoney(t.getMoney());
        th.setExpectedDate(t.getExpectedDate());
        int count3 = tranHistoryDao.save(th);
        if (count3 != 1){
            flag = false;
        }

        return flag;
    }

    @Override
    public Tran detail(String id) {
        tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);

        Tran t= tranDao.detail(id);
        return t;
    }

    @Override
    public List<TranHistory> getHistoryByTranId(String tranId) {
        tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);
        List<TranHistory> list = tranHistoryDao.getHistoryByTranId(tranId);

        return list;
    }

    @Override
    public boolean changeStage(Tran t) {
        boolean flag = true;
        tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
        tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);

        int count1 = tranDao.changStage(t);
        if (count1 != 1){
            flag = false;
        }

        TranHistory th = new TranHistory();
        th.setId(UUIDUtil.getUUID());
        th.setCreateTime(t.getEditTime());
        th.setCreateBy(t.getEditBy());
        th.setMoney(t.getMoney());
        th.setStage(t.getStage());
        th.setExpectedDate(t.getExpectedDate());

        int count2 = tranHistoryDao.save(th);
        if (count2 != 1){
            flag = false;
        }

        return flag;
    }

    @Override
    public Map<String, Object> getCharts() {
        tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);

        // 取得交易的总条数
        int total = tranDao.getTotal();

        // 取得dataList
        List<Map<String,Object>> dataList = tranDao.getCharts();

        // 将total和dataList保存到map中
        Map<String,Object> map = new HashMap<>();
        map.put("total",total);
        map.put("dataList",dataList);

        return map;
    }
}
