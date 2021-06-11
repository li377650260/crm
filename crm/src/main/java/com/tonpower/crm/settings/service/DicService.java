package com.tonpower.crm.settings.service;

import com.tonpower.crm.settings.domain.DicValue;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: li377650260
 * @date: 2021/6/7 22:09
 */
public interface DicService {
    Map<String, List<DicValue>> getAll();
}
