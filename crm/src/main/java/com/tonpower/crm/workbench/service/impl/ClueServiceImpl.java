package com.tonpower.crm.workbench.service.impl;

import com.tonpower.crm.utils.DateTimeUtil;
import com.tonpower.crm.utils.SqlSessionUtil;
import com.tonpower.crm.utils.UUIDUtil;
import com.tonpower.crm.vo.PaginationVo;
import com.tonpower.crm.workbench.dao.*;
import com.tonpower.crm.workbench.domain.*;
import com.tonpower.crm.workbench.service.ClueService;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: li377650260
 * @date: 2021/6/7 21:56
 */
public class ClueServiceImpl implements ClueService {
    private ClueDao clueDao = null;
    private ClueActivityRelationDao clueActivityRelationDao = null;
    private ClueRemarkDao clueRemarkDao = null;

    private CustomerDao customerDao = null;
    private CustomerRemarkDao customerRemarkDao = null;

    private ContactsDao contactsDao = null;
    private ContactsRemarkDao contactsRemarkDao = null;
    private ContactsActivityRelationDao contactsActivityRelationDao = null;

    private TranDao tranDao = null;
    private TranHistoryDao tranHistoryDao = null;
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

    @Override
    public boolean unbund(String id) {
        clueActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ClueActivityRelationDao.class);
        boolean flag = true;

        int count = clueActivityRelationDao.unbund(id);
        if (count != 1){
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean bund(String clueId, String[] activityId) {
        boolean flag = true;
        clueActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ClueActivityRelationDao.class);

        for (String aid : activityId){
            ClueActivityRelation car = new ClueActivityRelation();
            car.setId(UUIDUtil.getUUID());
            car.setActivityId(aid);
            car.setClueId(clueId);

            int count = clueActivityRelationDao.bund(car);
            if (count != 1){
                flag = false;
            }

        }

        return flag;
    }

    @Override
    public boolean convert(String clueId, Tran t, String createBy) {
        clueDao = SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);
        clueActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ClueActivityRelationDao.class);
        clueRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ClueRemarkDao.class);
        customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
        customerRemarkDao = SqlSessionUtil.getSqlSession().getMapper(CustomerRemarkDao.class);
        contactsDao = SqlSessionUtil.getSqlSession().getMapper(ContactsDao.class);
        contactsRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ContactsRemarkDao.class);
        contactsActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ContactsActivityRelationDao.class);
        tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
        tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);

        boolean flag = true;

        String id = UUIDUtil.getUUID();
        String createTime = DateTimeUtil.getSysTime();

        // 1.通过线索Id获取线索对象（当线索对象当中封装了线索的信息）
        Clue clue = clueDao.getById(clueId);

        // 2.通过线索对象提取客户信息，当该客户不存在的时候，新建客户（根据公司的名称精确匹配，判断该客户是否存在！）
        String company = clue.getCompany();
        Customer cus = customerDao.getCustomerByName(company);
        // 判断如果cus为空证明以前没有这个客户
        if (cus == null){
            cus = new Customer();
            cus.setId(UUIDUtil.getUUID());
            cus.setAddress(clue.getAddress());
            cus.setWebsite(clue.getWebsite());
            cus.setPhone(clue.getPhone());
            cus.setOwner(clue.getOwner());
            cus.setCreateBy(createBy);
            cus.setCreateTime(createTime);
            cus.setNextContactTime(clue.getNextContactTime());
            cus.setName(company);
            cus.setDescription(clue.getDescription());
            cus.setContactSummary(clue.getContactSummary());

            int count = customerDao.save(cus);
            if (count!=1){
                flag = false;
            }
        }

        // 3.通过线索对象提取联系人信息，保存联系人信息
        Contacts con = new Contacts();
        con.setSource(clue.getSource());
        con.setOwner(clue.getOwner());
        con.setNextContactTime(clue.getNextContactTime());
        con.setMphone(clue.getMphone());
        con.setJob(clue.getJob());
        con.setId(id);
        con.setFullname(clue.getFullname());
        con.setEmail(clue.getEmail());
        con.setDescription(clue.getDescription());
        con.setCustomerId(cus.getId());
        con.setCreateTime(createTime);
        con.setCreateBy(createBy);
        con.setContactSummary(clue.getContactSummary());
        con.setAppellation(clue.getAppellation());
        con.setAddress(clue.getAddress());
        int count2 = contactsDao.save(con);
        if (count2 != 1){
            flag = false;
        }

        // 4.线索备注转换到客户备注以及联系人备注
        List<ClueRemark> clueRemarkList = clueRemarkDao.getListByClueId(clueId);
        for (ClueRemark clueRemark : clueRemarkList){
            // 备注的转换主要就是要转换备注信息，所以取出线索中的备注信息
            String noteContent = clueRemark.getNoteContent();

            // 创建客户备注对象，添加备注信息
            CustomerRemark customerRemark = new CustomerRemark();
            customerRemark.setNoteContent(noteContent);
            customerRemark.setId(UUIDUtil.getUUID());
            customerRemark.setCreateBy(createBy);
            customerRemark.setCreateTime(createTime);
            customerRemark.setCustomerId(cus.getId());
            customerRemark.setEditFlag("0");
            int count3 = customerRemarkDao.save(customerRemark);
            if (count3 != 1){
                flag = false;
            }

            // 创建联系人备注对象
            ContactsRemark contactsRemark = new ContactsRemark();
            contactsRemark.setNoteContent(noteContent);
            contactsRemark.setId(UUIDUtil.getUUID());
            contactsRemark.setCreateBy(createBy);
            contactsRemark.setCreateTime(createTime);
            contactsRemark.setContactsId(con.getId());
            contactsRemark.setEditFlag("0");
            int count4 = contactsRemarkDao.save(contactsRemark);
            if (count4 != 1){
                flag = false;
            }
        }

        // 5.“线索和市场活动”的关系转换到“联系人和市场活动”的关系
        // 查询出与该条线索关联的市场活动
        List<ClueActivityRelation> clueActivityRelationList = clueActivityRelationDao.getListByClueId(clueId);
        // 遍历关联表集合，取出和线索关联的市场活动Id
        for (ClueActivityRelation clueActivityRelation: clueActivityRelationList){
            String activityId = clueActivityRelation.getActivityId();

            // 创建联系人和市场活动关联表的对象，让我们之前生成的联系人和市场活动做关联
            ContactsActivityRelation contactsActivityRelation = new ContactsActivityRelation();
            contactsActivityRelation.setId(UUIDUtil.getUUID());
            contactsActivityRelation.setActivityId(activityId);
            contactsActivityRelation.setContactsId(con.getId());
            // 将关联对象添加到关联表中
            int count5 = contactsActivityRelationDao.save(contactsActivityRelation);
            if (count5 != 1){
                flag = false;
            }
        }

        // 6.如果有创建交易需求，创建一条交易
        if (t != null){

            t.setSource(clue.getSource());
            t.setOwner(clue.getOwner());
            t.setNextContactTime(clue.getNextContactTime());
            t.setDescription(clue.getDescription());
            t.setCustomerId(cus.getId());
            t.setContactSummary(clue.getContactSummary());
            t.setContactsId(con.getId());

            // 添加交易信息
            int count6 = tranDao.save(t);
            if (count6 != 1){
                flag = false;
            }

            // 7.如果创建了交易，则创建一条该交易下的交易历史
            TranHistory th = new TranHistory();
            th.setTranId(t.getId());
            th.setStage(t.getStage());
            th.setMoney(t.getMoney());
            th.setId(UUIDUtil.getUUID());
            th.setExpectedDate(t.getExpectedDate());
            th.setCreateTime(createTime);
            th.setCreateBy(createBy);
            // 添加交易
            int count7 = tranHistoryDao.save(th);
            if (count7 != 1){
                flag = false;
            }

        }

        // 8.删除线索备注
        for (ClueRemark clueRemark : clueRemarkList){
            int count8 = clueRemarkDao.detele(clueRemark);
            if (count8 != 1){
                flag = false;
            }
        }

        // 9.删除线索和市场活动的关系
        for (ClueActivityRelation clueActivityRelation : clueActivityRelationList){
            int count9 = clueActivityRelationDao.detele(clueActivityRelation);
            if (count9 != 1){
                flag = false;
            }

        }

        // 10.删除线索
        int count9 = clueDao.detele(clueId);
        if (count9 != 1){
            flag = false;
        }

        return flag;
    }

    @Override
    public PaginationVo<Clue> pageList(Map<String, Object> map) {
        clueDao = SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);
        // 取得total
        int total = clueDao.getTotalByCondition(map);

        // 取得dataList
        List<Activity> dataList = clueDao.getDataListByCondition(map);

        // 将total和dataList封装成一个vo
        PaginationVo vo = new PaginationVo();
        vo.setDataList(dataList);
        vo.setTotal(total);
        System.out.println(vo);
        return vo;
    }
}
