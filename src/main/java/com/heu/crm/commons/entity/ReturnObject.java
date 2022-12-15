package com.heu.crm.commons.entity;
/**
 * @author LinXueHang
 * @date 2022/11/18 16:25
 */

/**
 * @author LinXueHang
 * @version 1.0
 * Create by 2022/11/18 16:25
 */
public class ReturnObject {
    // 处理成功获取失败标记   成功---1，失败---0
    private String code;
    // 提示信息
    private String message;
    // 其他信息
    private Object retData;

    public ReturnObject() {
    }

    public ReturnObject(String code, String message, Object retData) {
        this.code = code;
        this.message = message;
        this.retData = retData;
    }

    @Override
    public String toString() {
        return "ReturnObject{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", retData=" + retData +
                '}';
    }

    public Object getRetData() {
        return retData;
    }

    public void setRetData(Object retData) {
        this.retData = retData;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
