package com.heu.crm.commons.constant;
/**
 * @author LinXueHang
 * @date 2022/11/19 11:21
 */


import com.heu.crm.settings.entity.User;

/**
 * @author LinXueHang
 * @version 1.0
 * Create by 2022/11/19 11:21
 */
public class Contants {

    // 保存 ReturnObject 类中的 code 值
    public static final String RETURN_OBJECT_CODE_SUCCESS = "1";// 成功
    public static final String RETURN_OBJECT_CODE_FAIL = "0";// 失败
    // 保存当前用户的 key
    public static final String CURRENT_USER_KEY = "sessionUser";
    // 备注的修改标记
    public static final String REMARK_EDIT_FLAG_NO_EDITED = "0";// 没有修改过
    public static final String REMARK_EDIT_FLAG_HAS_EDITED = "1";// 已经被修改过
}
