package com.tonpower.crm.workbench.service.impl;

import com.tonpower.crm.utils.SqlSessionUtil;
import com.tonpower.crm.workbench.dao.CustomerDao;
import com.tonpower.crm.workbench.service.CustomerService;

import java.util.List;

/**
 * @description:
 * @author: li377650260
 * @date: 2021/6/16 16:12
 */
public class CustomerServiceImpl implements CustomerService {
    private CustomerDao customerDao = null;

    @Override
    public List<String> getCustomerName(String name) {
        customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
        List<String> list = customerDao.getCustomerName(name);
        return list;
    }
}
