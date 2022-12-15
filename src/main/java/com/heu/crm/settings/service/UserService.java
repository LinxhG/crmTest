package com.heu.crm.settings.service;

import com.heu.crm.settings.entity.User;

import java.util.List;
import java.util.Map;

/**
 * @author LinXueHang
 * @date 2022/11/18 14:56
 */
public interface UserService {

    /**
     * 根据账号和密码来查询用户
     * @param map
     * @return
     */
    User queryUserByLoginActAndPwd(Map<String, Object> map);

    /**
     * 查询所有用户（未被锁定）
     * @return
     */
    List<User> queryAllUsers();
}
