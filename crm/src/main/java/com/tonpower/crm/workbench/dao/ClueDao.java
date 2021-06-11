package com.tonpower.crm.workbench.dao;

import com.tonpower.crm.workbench.domain.Clue;

public interface ClueDao {


    int save(Clue c);

    Clue detail(String id);
}
