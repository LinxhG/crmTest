package com.heu.crm.workbench.web.controller;
/**
 * @author LinXueHang
 * @date 2022/11/19 10:49
 */

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author LinXueHang
 * @version 1.0
 * Create by 2022/11/19 10:49
 */
@Controller
public class WorkBenchIndexController {

    @RequestMapping("/workbench/index.do")
    public String index(){
        // 跳转到业务主界面
        return "workbench/index";
    }

}
