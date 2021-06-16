package com.tonpower.crm.workbench.service;

import com.tonpower.crm.vo.PaginationVo;
import com.tonpower.crm.workbench.domain.Clue;
import com.tonpower.crm.workbench.domain.Tran;

import java.util.Map;

public interface ClueService {
    boolean save(Clue c);

    Clue detail(String id);

    boolean unbund(String id);

    boolean bund(String clueId, String[] activityId);

    boolean convert(String clueId, Tran t, String createBy);

    PaginationVo<Clue> pageList(Map<String, Object> map);
}
