package com.heu.crm.workbench.service.Impl;
/**
 * @author LinXueHang
 * @date 2022/12/13 13:40
 */

import com.heu.crm.workbench.entity.ActivityRemark;
import com.heu.crm.workbench.mapper.ActivityRemarkMapper;
import com.heu.crm.workbench.service.ActivityRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author LinXueHang
 * @version 1.0
 * Create by 2022/12/13 13:40
 */
@Service("activityRemarkService")
public class ActivityRemarkServiceImpl implements ActivityRemarkService {
    @Autowired
    private ActivityRemarkMapper activityRemarkMapper;

    @Override
    public List<ActivityRemark> queryActivityRemarkForDetailsByActitvityId(String actitvityId) {
        return activityRemarkMapper.selectActivityRemarkForDetailsByActivityId(actitvityId);
    }

    @Override
    public int saveCreateActivityRemark(ActivityRemark remark) {
        return activityRemarkMapper.insertActivityRemark(remark);
    }

    @Override
    public int deleteActivityRemarkById(String id) {
        return activityRemarkMapper.deleteActivityRemarkById(id);
    }

    @Override
    public int saveEditActivityRemark(ActivityRemark remark) {
        return activityRemarkMapper.updateActivityRemark(remark);
    }
}
