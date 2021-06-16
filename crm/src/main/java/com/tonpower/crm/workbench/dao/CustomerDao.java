package com.tonpower.crm.workbench.dao;

import com.tonpower.crm.workbench.domain.Customer;

public interface CustomerDao {

    Customer getCustomerByName(String company);

    int save(Customer cus);
}
