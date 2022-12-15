package com.heu.crm.commons.utils;
/**
 * @author LinXueHang
 * @date 2022/11/25 20:14
 */

import java.util.UUID;

/**
 * @author LinXueHang
 * @version 1.0
 * Create by 2022/11/25 20:14
 */
public class UUIDUtils {
    /**
     * 获取UUID的值
     * @return
     */
    public static String getUUID(){
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
