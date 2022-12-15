package com.heu.crm.settings.web.controller;
/**
 * @author LinXueHang
 * @date 2022/11/17 14:47
 */

import com.heu.crm.commons.constant.Contants;
import com.heu.crm.commons.entity.ReturnObject;
import com.heu.crm.commons.utils.DateUtils;
import com.heu.crm.settings.entity.User;
import com.heu.crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author LinXueHang
 * @version 1.0
 * Create by 2022/11/17 14:47
 */
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * url 要和 controller 方法处理完请求之后，响应信息返回的页面的资源目录保持一致
     */
    @RequestMapping("/settings/qx/user/toLogin.do")
    public String toLogin(){
       // 请求转发到登录页面
       return "settings/qx/user/login";
    }

    @RequestMapping("/settings/qx/user/login.do")
    @ResponseBody
    public Object login(String loginAct, String loginPwd, String isRemPwd, HttpServletRequest request,
                        HttpServletResponse response, HttpSession session){
        // 封装参数
        Map<String, Object> map = new HashMap<>();
        map.put("loginAct", loginAct);
        map.put("loginPwd", loginPwd);
        // 调用service层方法，查询用户
        User user = userService.queryUserByLoginActAndPwd(map);
        // 根据查询结果，生成相应信息
        ReturnObject retObject = new ReturnObject(Contants.RETURN_OBJECT_CODE_FAIL, null, null);
        if (user == null){
            // 登录失败，用户名或密码错误
            retObject.setMessage("用户名或密码错误!");
        } else {// 进一步判断账号是否合法
            // 当前时间
            String nowStr = DateUtils.formatDataTime(new Date());
            if (nowStr.compareTo(user.getExpireTime()) > 0){
                // 登陆失败，账号已过期
                retObject.setMessage("账号已过期!");
            } else if ("0".equals(user.getLockState())){
                // 登录失败，状态被锁定
                retObject.setMessage("状态被锁定!");
            } else if (!user.getAllowIps().contains(request.getRemoteAddr())){
                // 登陆失败，ip受限
                retObject.setMessage("ip受限!");
            } else {
                // 登陆成功
                retObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                // 把user对象保存到作用域中
                session.setAttribute(Contants.CURRENT_USER_KEY, user);
                // 如果需要记住密码，则往外写 cookie
                if ("true".equals(isRemPwd)){
                    Cookie loginActCookie = new Cookie("loginAct", user.getLoginAct());
                    loginActCookie.setMaxAge(10*24*60*60);
                    response.addCookie(loginActCookie);
                    Cookie loginPwdCookie = new Cookie("loginPwd", user.getLoginPwd());
                    loginPwdCookie.setMaxAge(10*24*60*60);
                    response.addCookie(loginPwdCookie);
                } else {
                    // 把没有过期的 cookie 删除
                    Cookie loginActCookie = new Cookie("loginAct", "1");
                    loginActCookie.setMaxAge(0);
                    response.addCookie(loginActCookie);
                    Cookie loginPwdCookie = new Cookie("loginPwd", "1");
                    loginPwdCookie.setMaxAge(0);
                    response.addCookie(loginPwdCookie);
                }
            }
        }
        return retObject;
    }

    @RequestMapping("/settings/qx/user/logout.do")
    public String logout(HttpServletResponse response, HttpSession session){
        // 清空cookie
        Cookie loginActCookie = new Cookie("loginAct", "1");
        loginActCookie.setMaxAge(0);
        response.addCookie(loginActCookie);
        Cookie loginPwdCookie = new Cookie("loginPwd", "1");
        loginPwdCookie.setMaxAge(0);
        response.addCookie(loginPwdCookie);
        // 销毁session
        session.invalidate();
        // 请求转发到登录页面
        // 借助springMVC来重定向，springMVC默认当前项目名为crm，底层还是调用response.sendRedirect("/crm/");
        return "redirect:/";
    }
}

