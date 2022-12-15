package com.heu.crm.workbench.service.Impl;
/**
 * @author LinXueHang
 * @date 2022/11/25 16:50
 */

import com.heu.crm.workbench.entity.Activity;
import com.heu.crm.workbench.mapper.ActivityMapper;
import com.heu.crm.workbench.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author LinXueHang
 * @version 1.0
 * Create by 2022/11/25 16:50
 */
@Service("activityService")
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private ActivityMapper activityMapper;

    @Override
    public int saveCreateActivity(Activity activity){
        return activityMapper.insertActivity(activity);
    }

    @Override
    public List<Activity> queryActivityByConditionsForPage(Map<String, Object> map) {
        return activityMapper.selectActivityByConditionsForPage(map);
    }

    @Override
    public int queryCountOfActivityByCondition(Map<String, Object> map) {
        return activityMapper.selectCountOfActivityByCondition(map);
    }

    @Override
    public int deleteActivityByIds(String[] ids) {
        return activityMapper.deleteActivityByIds(ids);
    }

    @Override
    public Activity queryActivityById(String id) {
        return activityMapper.selectActivityById(id);
    }

    @Override
    public int saveEditActivity(Activity activity) {
        return activityMapper.updateActivity(activity);
    }

    @Override
    public List<Activity> queryAllActivities() {
        return activityMapper.selectAllActivities();
    }

    @Override
    public int saveCreateActivityByList(List<Activity> activityList) {
        return activityMapper.insertActivityByList(activityList);
    }

    @Override
    public Activity queryActivityForDetail(String id) {
        return activityMapper.selectActivityForDetailsById(id);
    }
}
