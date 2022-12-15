package com.heu.crm.workbench.mapper;

import com.heu.crm.workbench.entity.ActivityRemark;

import java.util.List;

public interface ActivityRemarkMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity_remark
     *
     * @mbg.generated Tue Dec 13 11:18:01 CST 2022
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity_remark
     *
     * @mbg.generated Tue Dec 13 11:18:01 CST 2022
     */
    int insert(ActivityRemark record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity_remark
     *
     * @mbg.generated Tue Dec 13 11:18:01 CST 2022
     */
    int insertSelective(ActivityRemark record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity_remark
     *
     * @mbg.generated Tue Dec 13 11:18:01 CST 2022
     */
    ActivityRemark selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity_remark
     *
     * @mbg.generated Tue Dec 13 11:18:01 CST 2022
     */
    int updateByPrimaryKeySelective(ActivityRemark record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity_remark
     *
     * @mbg.generated Tue Dec 13 11:18:01 CST 2022
     */
    int updateByPrimaryKey(ActivityRemark record);

    /**
     * 根据市场活动id，查询市场活动备注
     * @param activityId
     * @return
     */
    List<ActivityRemark> selectActivityRemarkForDetailsByActivityId(String activityId);

    /**
     * 保存创建的市场活动备注
     * @param remark
     * @return
     */
    int insertActivityRemark(ActivityRemark remark);

    /**
     * 根据备注id，删除市场活动备注
     * @param id
     * @return
     */
    int deleteActivityRemarkById(String id);

    /**
     * 更新市场活动的备注
     * @param remark
     * @return
     */
    int updateActivityRemark(ActivityRemark remark);
}