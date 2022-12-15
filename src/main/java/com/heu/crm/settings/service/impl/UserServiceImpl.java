package com.heu.crm.settings.service.impl;
/**
 * @author LinXueHang
 * @date 2022/11/18 15:00
 */

import com.heu.crm.settings.entity.User;
import com.heu.crm.settings.mapper.UserMapper;
import com.heu.crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author LinXueHang
 * @version 1.0
 * Create by 2022/11/18 15:00
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User queryUserByLoginActAndPwd(Map<String, Object> map) {
        return userMapper.selectUserByLoginActAndPwd(map);
    }

    @Override
    public List<User> queryAllUsers() {
        return userMapper.selectAllUsers();
    }

}
