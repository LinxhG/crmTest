package com.heu.crm.workbench.web.controller;
/**
 * @author LinXueHang
 * @date 2022/12/13 21:06
 */

import com.heu.crm.commons.constant.Contants;
import com.heu.crm.commons.entity.ReturnObject;
import com.heu.crm.commons.utils.DateUtils;
import com.heu.crm.commons.utils.UUIDUtils;
import com.heu.crm.settings.entity.User;
import com.heu.crm.workbench.entity.ActivityRemark;
import com.heu.crm.workbench.service.ActivityRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 * @author LinXueHang
 * @version 1.0
 * Create by 2022/12/13 21:06
 */
@Controller
public class ActivityRemarkController {

    @Autowired
    private ActivityRemarkService activityRemarkService;

    @RequestMapping("/workbench/activity/saveCreateActivityRemark.do")
    @ResponseBody
    public Object saveCreateActivityRemark(ActivityRemark remark, HttpSession session){
        // 返回对象
        ReturnObject retObject = new ReturnObject(Contants.RETURN_OBJECT_CODE_FAIL, null, null);
        // 获取当前用户
        User currentUser = (User) session.getAttribute(Contants.CURRENT_USER_KEY);
        // 获取参数
        remark.setId(UUIDUtils.getUUID());
        remark.setCreateTime(DateUtils.formatDataTime(new Date()));
        remark.setCreateBy(currentUser.getId());
        remark.setEditFlag(Contants.REMARK_EDIT_FLAG_NO_EDITED);
        try {
            // 调用service方法，保存创建的市场活动
            int retCount = activityRemarkService.saveCreateActivityRemark(remark);
            if (retCount > 0) {
                retObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                retObject.setRetData(remark);
            } else {
                retObject.setMessage("系统忙，请稍后重试...");
            }
        } catch (Exception e) {
            e.printStackTrace();
            retObject.setMessage("系统忙，请稍后重试...");
        }
        return retObject;
    }

    @RequestMapping("/workbench/activity/deleteActivityRemarkById.do")
    @ResponseBody
    public Object deleteActivityRemarkById(String id){
        // 返回信息
        ReturnObject retObject = new ReturnObject(Contants.RETURN_OBJECT_CODE_FAIL, null, null);
        try {
            // 调用service方法，删除备注信息
            int refCount = activityRemarkService.deleteActivityRemarkById(id);
            if (refCount > 0){
                retObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            } else {
                retObject.setMessage("系统忙，请稍后重试...");
            }
        } catch (Exception e) {
            e.printStackTrace();
            retObject.setMessage("系统忙，请稍后重试...");
        }
        return retObject;
    }

    @RequestMapping("/workbench/activity/saveEditActivityRemark.do")
    @ResponseBody
    public Object saveEditActivityRemark(ActivityRemark remark, HttpSession session){
        // 获取当前用户
        User currentUser = (User) session.getAttribute(Contants.CURRENT_USER_KEY);
        // 返回对象
        ReturnObject retObject = new ReturnObject(Contants.RETURN_OBJECT_CODE_FAIL, null, null);
        // 收集参数
        remark.setEditBy(currentUser.getId());
        remark.setEditTime(DateUtils.formatDataTime(new Date()));
        remark.setEditFlag(Contants.REMARK_EDIT_FLAG_HAS_EDITED);
        try {
            // 调用service方法，更新市场活动备注
            int refCount = activityRemarkService.saveEditActivityRemark(remark);
            if (refCount > 0){
                retObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                retObject.setRetData(remark);
            } else {
                retObject.setMessage("系统忙，请稍后重试...");
            }
        } catch (Exception e) {
            e.printStackTrace();
            retObject.setMessage("系统忙，请稍后重试...");
        }
        return retObject;
    }
}
