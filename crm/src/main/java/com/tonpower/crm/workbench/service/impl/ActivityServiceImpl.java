package com.tonpower.crm.workbench.service.impl;

import com.tonpower.crm.settings.dao.UserDao;
import com.tonpower.crm.settings.domain.User;
import com.tonpower.crm.utils.SqlSessionUtil;
import com.tonpower.crm.vo.PaginationVo;
import com.tonpower.crm.workbench.dao.ActivityDao;
import com.tonpower.crm.workbench.dao.ActivityRemarkDao;
import com.tonpower.crm.workbench.domain.Activity;
import com.tonpower.crm.workbench.domain.ActivityRemark;
import com.tonpower.crm.workbench.service.ActivityService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: li377650260
 * @date: 2021/6/3 14:18
 */
public class ActivityServiceImpl implements ActivityService {
    private ActivityDao dao = null;
    private ActivityRemarkDao remarkDao = null;
    private UserDao userDao = null;
    @Override
    public Boolean save(Activity activity) {
        dao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
        Boolean flag = false;
        int count =  dao.save(activity);

        if (count == 1){
            flag = true;
        }
        return flag;
    }

    @Override
    public PaginationVo<Activity> pageList(Map<String, Object> map) {
        dao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
        // 取得total
        int total = dao.getTotalByCondition(map);

        // 取得dataList
        List<Activity> dataList = dao.getDataListByCondition(map);

        // 将total和dataList封装成一个vo
        PaginationVo vo = new PaginationVo();
        vo.setDataList(dataList);
        vo.setTotal(total);
        System.out.println(vo);
        return vo;
    }

    @Override
    public boolean delete(String[] ids) {
        dao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
        remarkDao = SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);

        boolean flag = true;
        // 查询出需要删除的备注的条数
        int count1 = remarkDao.getCountByAids(ids);
        // 删除备注返回收到影响的条数
        int count2 = remarkDao.deleteByAids(ids);

        if (count1 != count2){
            flag = false;
        }
        // 删除市场活动信息
        int count3 = dao.delete(ids);
        if (count3 != ids.length){
            flag = false;
        }

        return flag;
    }

    @Override
    public Map<String, Object> getUserListAndActivity(String id) {
        userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);
        dao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);

        List<User> list = userDao.getUserList();

        Activity activity = dao.getById(id);

        Map<String,Object> map = new HashMap<>();
        map.put("userList",list);
        map.put("activity",activity);

        return map;
    }

    @Override
    public Boolean update(Activity activity) {
        dao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
        Boolean flag = false;
        int count =  dao.update(activity);

        if (count == 1){
            flag = true;
        }
        return flag;
    }

    @Override
    public Activity detail(String id) {
        dao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);

        Activity a = dao.detail(id);

        return a;
    }

    @Override
    public List<ActivityRemark> getRemarkListByAid(String activityId) {
        remarkDao = SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);
        List<ActivityRemark> list = remarkDao.getRemarkListByAid(activityId);
        return list;
    }

    @Override
    public boolean deleteRemark(String id) {
        boolean flag = true;
        remarkDao = SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);

        int count = remarkDao.deleteRemark(id);
        if (count != 1 ){
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean saveReamark(ActivityRemark ar) {
        boolean flag = true;
        remarkDao = SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);

        int count = remarkDao.saveRemark(ar);
        if (count != 1){
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean updateRemark(ActivityRemark ar) {
        boolean flag = true;
        remarkDao = SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);

        int count = remarkDao.updateRemark(ar);
        if (count != 1){
            flag = false;
        }
        return flag;
    }

    @Override
    public List<Activity> getActivityListByClueId(String clueId) {
        dao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
        List<Activity> list = dao.getActivityListByClueId(clueId);

        return list;
    }

}
