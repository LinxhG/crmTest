package com.heu.crm.settings.mapper;

import com.heu.crm.settings.entity.User;

import java.util.List;
import java.util.Map;

public interface UserMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_user
     *
     * @mbg.generated Fri Nov 18 14:29:38 CST 2022
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_user
     *
     * @mbg.generated Fri Nov 18 14:29:38 CST 2022
     */
    int insert(User record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_user
     *
     * @mbg.generated Fri Nov 18 14:29:38 CST 2022
     */
    int insertSelective(User record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_user
     *
     * @mbg.generated Fri Nov 18 14:29:38 CST 2022
     */
    User selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_user
     *
     * @mbg.generated Fri Nov 18 14:29:38 CST 2022
     */
    int updateByPrimaryKeySelective(User record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_user
     *
     * @mbg.generated Fri Nov 18 14:29:38 CST 2022
     */
    int updateByPrimaryKey(User record);

    /**
     * 根据账号和密码来查询用户
     * @return
     */
    User selectUserByLoginActAndPwd(Map<String, Object> map);

    /**
     * 查询所有用户
     */
    List<User> selectAllUsers();
}