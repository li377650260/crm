package com.tonpower.crm.workbench.dao;

import com.tonpower.crm.workbench.domain.Tran;

import java.util.List;
import java.util.Map;

public interface TranDao {

    int save(Tran t);

    Tran detail(String id);

    int changStage(Tran t);

    int getTotal();

    List<Map<String, Object>> getCharts();
}
