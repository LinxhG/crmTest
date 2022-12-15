package com.heu.crm.workbench.service;

import com.heu.crm.workbench.entity.ActivityRemark;

import java.util.List;

/**
 * @author LinXueHang
 * @date 2022/12/13 13:39
 */
public interface ActivityRemarkService {
    /**
     * 查询市场活动备注
     * @param actitvityId
     * @return
     */
    List<ActivityRemark> queryActivityRemarkForDetailsByActitvityId(String actitvityId);

    /**
     * 保存创建的市场活动备注
     * @param remark
     * @return
     */
    int saveCreateActivityRemark(ActivityRemark remark);

    /**
     * 根据备注id，删除市场活动备注
     * @param id
     * @return
     */
    int deleteActivityRemarkById(String id);

    /**
     * 更新市场活动备注
     * @param remark
     * @return
     */
    int saveEditActivityRemark(ActivityRemark remark);
}
