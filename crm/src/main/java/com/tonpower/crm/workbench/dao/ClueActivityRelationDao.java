package com.tonpower.crm.workbench.dao;

import com.tonpower.crm.workbench.domain.ClueActivityRelation;

import java.util.List;

public interface ClueActivityRelationDao {


    int unbund(String id);

    int bund(ClueActivityRelation car);

    List<ClueActivityRelation> getListByClueId(String clueId);

    int detele(ClueActivityRelation clueActivityRelation);
}
