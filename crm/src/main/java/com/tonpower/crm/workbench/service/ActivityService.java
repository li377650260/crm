package com.tonpower.crm.workbench.service;

import com.tonpower.crm.vo.PaginationVo;
import com.tonpower.crm.workbench.domain.Activity;
import com.tonpower.crm.workbench.domain.ActivityRemark;

import java.util.List;
import java.util.Map;

public interface ActivityService {
    Boolean save(Activity activity);

    PaginationVo<Activity> pageList(Map<String, Object> map);

    boolean delete(String[] ids);

    Map<String, Object> getUserListAndActivity(String id);

    Boolean update(Activity activity);

    Activity detail(String id);

    List<ActivityRemark> getRemarkListByAid(String activityId);

    boolean deleteRemark(String id);

    boolean saveReamark(ActivityRemark ar);

    boolean updateRemark(ActivityRemark ar);
}
