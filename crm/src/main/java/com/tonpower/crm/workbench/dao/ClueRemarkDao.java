package com.tonpower.crm.workbench.dao;

import com.tonpower.crm.workbench.domain.ClueRemark;

import java.util.List;

public interface ClueRemarkDao {

    List<ClueRemark> getListByClueId(String clueId);

    int detele(ClueRemark clueRemark);
}
