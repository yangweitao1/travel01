package com.itheima.travel.web.filter;

import com.itheima.travel.constant.Constant;
import com.itheima.travel.domain.User;
import com.itheima.travel.factory.BeanFactory;
import com.itheima.travel.service.UserService;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/*")
public class AutoLoginFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        //首先判断当前是否已登录，如果当前已登录，则不需要考虑自动登录，直接放行
        HttpServletRequest request = (HttpServletRequest) req;
        HttpSession session = request.getSession();

        User user = (User) session.getAttribute(Constant.USER_KEY);
        if (user == null) {
            //当前未登录，进行自动登录
            //获取cookie中保存的用户名和密码
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("info")) {
                        String info = cookie.getValue();
                        //获取用户名和密码
                        String username = info.split("#")[0];
                        String password = info.split("#")[1];
                        //进行登录，其实也就是获取user
                        //调用业务层的方法
                        UserService userService = (UserService) BeanFactory.getBean("userService");
                        user = userService.doLogin(username, password);

                        //将user存放session中
                        session.setAttribute(Constant.USER_KEY,user);
                    }
                }
            }

        }
        //放行
        chain.doFilter(req, resp);
    }

    public void init(FilterConfig config) throws ServletException {

    }

}
