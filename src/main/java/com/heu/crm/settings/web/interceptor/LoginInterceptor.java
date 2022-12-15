package com.heu.crm.settings.web.interceptor;
/**
 * @author LinXueHang
 * @date 2022/11/20 15:03
 */


import com.heu.crm.commons.constant.Contants;
import com.heu.crm.settings.entity.User;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author LinXueHang
 * @version 1.0
 * Create by 2022/11/20 15:03
 */
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 如果用户没有登录成功，则跳转到登录页面
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(Contants.CURRENT_USER_KEY);
        if (user == null){
            // 请求转发可以直接 /，重定向还需要在 / 后面加上项目的名字
            // 对比UserController.logout的return "redirect:/";
            // response.sendRedirect("/crm/");
            // 动态获取项目名
            response.sendRedirect(request.getContextPath());
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
