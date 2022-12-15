package com.heu.crm.workbench.service;

import com.heu.crm.workbench.entity.Activity;
import org.apache.ibatis.annotations.Delete;

import java.util.List;
import java.util.Map;

/**
 * @author LinXueHang
 * @date 2022/11/25 16:51
 */
public interface ActivityService {

    /**
     * 保存创建的市场活动
     * @param activity
     * @return
     */
    int saveCreateActivity(Activity activity);

    /**
     * 根据条件分页查询市场活动的列表
     * @param map
     * @return
     */
    List<Activity> queryActivityByConditionsForPage(Map<String, Object> map);

    /**
     * 根据条件查询市场活动总条数
     * @param map
     * @return
     */
    int queryCountOfActivityByCondition(Map<String, Object> map);

    /**
     * 根据ids，批量删除市场活动
     * @param ids
     * @return
     */
    int deleteActivityByIds(String[] ids);

    /**
     * 根据id查询市场活动
     * @param id
     * @return
     */
    Activity queryActivityById(String id);

    /**
     * 保存编辑后的市场活动
     * @param activity
     * @return
     */
    int saveEditActivity(Activity activity);

    /**
     * 查询所有的市场活动
     * @return
     */
    List<Activity> queryAllActivities();

    /**
     * 批量保存创建的市场活动
     * @param activityList
     * @return
     */
    int saveCreateActivityByList(List<Activity> activityList);

    /**
     * 根据id查询市场活动的明细信息
     * @param id
     * @return
     */
    Activity queryActivityForDetail(String id);
}
