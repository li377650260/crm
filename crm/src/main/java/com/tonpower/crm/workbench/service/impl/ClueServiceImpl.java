package com.tonpower.crm.workbench.service.impl;

import com.tonpower.crm.utils.SqlSessionUtil;
import com.tonpower.crm.workbench.dao.ClueDao;
import com.tonpower.crm.workbench.domain.Clue;
import com.tonpower.crm.workbench.service.ClueService;
import org.apache.ibatis.session.SqlSessionFactory;

/**
 * @description:
 * @author: li377650260
 * @date: 2021/6/7 21:56
 */
public class ClueServiceImpl implements ClueService {
    private ClueDao clueDao = null;

    @Override
    public boolean save(Clue c) {
        clueDao = SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);
        boolean flag = true;

        int count = clueDao.save(c);
        if (count != 1){
            flag = false;
        }
        return flag;
    }

    @Override
    public Clue detail(String id) {
        clueDao = SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);
        Clue c = clueDao.detail(id);
        return c;
    }
}
