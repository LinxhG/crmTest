package com.heu.crm.web.controller;
/**
 * @author LinXueHang
 * @date 2022/11/17 13:59
 */

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author LinXueHang
 * @version 1.0
 * Create by 2022/11/17 13:59
 */
@Controller
public class IndexController {

    /**
     * 理论上，给Controller分配url：http://127.0.0.1:8080/crm/
     * 为了简便，协议 协议://ip:port/应用名称 必须省去，用 / 代表应用根目录下的 /
     * @return
     */
    @RequestMapping("/")
    public String index(){
        // 请求转发
        return "index";
    }

}
