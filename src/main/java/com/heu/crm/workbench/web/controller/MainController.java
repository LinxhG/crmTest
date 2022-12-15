package com.heu.crm.workbench.web.controller;
/**
 * @author LinXueHang
 * @date 2022/11/20 19:43
 */

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author LinXueHang
 * @version 1.0
 * Create by 2022/11/20 19:43
 */
@Controller
public class MainController {

    @RequestMapping("/workbench/main/index.do")
    public String index(){
        // 跳转到main/index.jsp
        return "workbench/main/index";
    }

}
