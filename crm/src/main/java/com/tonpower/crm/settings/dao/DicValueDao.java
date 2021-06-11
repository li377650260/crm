package com.tonpower.crm.settings.dao;

import com.tonpower.crm.settings.domain.DicValue;

import java.util.List;

/**
 * @description:
 * @author: li377650260
 * @date: 2021/6/7 22:05
 */
public interface DicValueDao {
    List<DicValue> getListByCode(String code);
}
