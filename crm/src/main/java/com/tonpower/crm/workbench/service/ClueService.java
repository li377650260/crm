package com.tonpower.crm.workbench.service;

import com.tonpower.crm.workbench.domain.Clue;

public interface ClueService {
    boolean save(Clue c);

    Clue detail(String id);
}
