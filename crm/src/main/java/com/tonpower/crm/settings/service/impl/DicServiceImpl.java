package com.tonpower.crm.settings.service.impl;

import com.tonpower.crm.settings.dao.DicTypeDao;
import com.tonpower.crm.settings.dao.DicValueDao;
import com.tonpower.crm.settings.domain.DicType;
import com.tonpower.crm.settings.domain.DicValue;
import com.tonpower.crm.settings.service.DicService;
import com.tonpower.crm.utils.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: li377650260
 * @date: 2021/6/7 22:10
 */
public class DicServiceImpl implements DicService {
    private DicTypeDao dicTypeDao = null;
    private DicValueDao dicValueDao = null;

    @Override
    public Map<String, List<DicValue>> getAll() {
        dicTypeDao = SqlSessionUtil.getSqlSession().getMapper(DicTypeDao.class);
        dicValueDao = SqlSessionUtil.getSqlSession().getMapper(DicValueDao.class);

        Map<String,List<DicValue>> map = new HashMap<>();
        // 将字典类型列表取出
        List<DicType> dicTypeList = dicTypeDao.getTypeList();

        // 将字典类型列表遍历然后作为参数查询数据字典值的列表
        for (DicType dicType : dicTypeList){
            String code = dicType.getCode();

            // 根据每一个字典类型来取得字典值的列表
            List<DicValue> dicValueList = dicValueDao.getListByCode(code);
            map.put(code,dicValueList);
        }
        return map;
    }
}
