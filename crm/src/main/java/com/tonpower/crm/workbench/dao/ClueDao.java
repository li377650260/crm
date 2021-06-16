package com.tonpower.crm.workbench.dao;

import com.tonpower.crm.workbench.domain.Activity;
import com.tonpower.crm.workbench.domain.Clue;

import java.util.List;
import java.util.Map;

public interface ClueDao {


    int save(Clue c);

    Clue detail(String id);

    Clue getById(String clueId);

    int detele(String clueId);

    int getTotalByCondition(Map<String, Object> map);

    List<Activity> getDataListByCondition(Map<String, Object> map);
}
