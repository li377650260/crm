package com.tonpower.crm.workbench.dao;

import com.tonpower.crm.workbench.domain.TranHistory;

import java.util.List;

public interface TranHistoryDao {

    int save(TranHistory th);

    List<TranHistory> getHistoryByTranId(String tranId);
}
