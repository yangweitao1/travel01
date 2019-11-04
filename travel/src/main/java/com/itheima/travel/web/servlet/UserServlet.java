package com.itheima.travel.web.servlet;

import com.itheima.travel.constant.Constant;
import com.itheima.travel.domain.Result;
import com.itheima.travel.domain.User;
import com.itheima.travel.factory.BeanFactory;
import com.itheima.travel.service.UserService;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * 处理用户模块的所有请求的Servlet
 * 继承了BaseServlet，那么我们的UserServlet就能够处理多个请求
 * 前提是处理请求的方法名和传入的action参数值要一样
 */
@WebServlet("/user")
public class UserServlet extends BaseServlet {
    private UserService userService = (UserService) BeanFactory.getBean("userService");

    /**
     * 处理校验用户名是否已注册的方法
     * @param request
     * @param response
     */
    private Result checkUsername(HttpServletRequest request,HttpServletResponse response) throws IOException {
        Result result = new Result(true);//flag为true表示服务器没有出现异常
        //1.获取请求参数username
        String username = request.getParameter("username");
        try {
            //2.调用业务层的方法，根据用户名username查询用户信息
            User user = userService.findUserByUsername(username);
            //3.判断user是否为null
            if (user == null) {
                //用户名可用
                result.setData(true);//data为true表示用户名可用
            }else {
                //用户名不可用
                result.setData(false);//data为false表示用户名不可用
            }
        } catch (Exception e) {
            e.printStackTrace();
            //说明服务器异常
            result.setFlag(false);
            result.setErrorMsg(Constant.SERVER_ERROR);
        }

        //将result转换成json字符串，输出到客户端
        return result;
    }

    /**
     * 处理注册请求的方法
     * @param request
     * @param response
     */
    private Result register(HttpServletRequest request ,HttpServletResponse response) throws IOException {
        //封装响应数据到Result
        Result result = new Result(true);
        //1.获取所有请求参数封装刀User对象中
        Map<String, String[]> map = request.getParameterMap();
        User user = new User();
        try {
            BeanUtils.populate(user,map);

            System.out.println(user);
            //2.调用业务层的方法，处理注册
            userService.doRegister(user);
        } catch (Exception e) {
            e.printStackTrace();
            result.setFlag(false);//表示注册失败
        }

        //将result转换成json字符串，然后输出到客户端
        return result;
    }

    /**
     * 处理激活的方法
     * @param request
     * @param response
     */
    private Result active(HttpServletRequest request ,HttpServletResponse response) throws IOException {
        PrintWriter writer = response.getWriter();
        //1.获取请求参数code的值
        String code = request.getParameter("code");
        try {
            //2.调用业务层的方法处理激活
            int i = userService.doActive(code);
            if (i == -1) {
                //表示请勿重复激活
                writer.write("请勿重复激活");
            }else if(i == -2){
                //表示激活码错误
                writer.write("激活码错误");
            }else {
                //激活成功
                //激活成功，则跳转到登录页面
                //以斜杠开头的是绝对路径写法，不以斜杠开头的是相对路径写法
                response.sendRedirect("login.html");
            }
        } catch (Exception e) {
            e.printStackTrace();
            //服务器异常，就说明激活失败
            writer.write("激活失败，请重新激活");
        }
        return null;
    }

    /**
     * 处理登录请求的方法
     * @param request
     * @param response
     */
    public Result login(HttpServletRequest request ,HttpServletResponse response) throws IOException {
        Result result = new Result(true);
        //1.获取请求参数
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String check = request.getParameter("check");

        //获取自动登录状态
        String auto = request.getParameter("auto");

        //2.从session中获取服务器生成的验证码
        HttpSession session = request.getSession();
        String checkCode = (String) session.getAttribute(Constant.CHECKCODE_KEY);

        //校验验证码
        if (check.equalsIgnoreCase(checkCode)) {
            //验证码正确
            //调用业务层的方法，校验用户名和密码
            try {
                User user = userService.doLogin(username,password);

                //登录成功，就将user存放到session域对象
                session.setAttribute(Constant.USER_KEY,user);

                result.setData(true);//data为true表示登录成功

                //登录成功，我们再判断是否需要自动登录
                if ("at".equals(auto)) {
                    //需要自动登录
                    //也就表示下一次登录，我们不用输入用户名和密码了，就直接登录了
                    //也就是表示将用户名和密码存放到Cookie中
                    Cookie cookie = new Cookie("info", username + "#" + password);
                    //设置maxAge以及Path
                    cookie.setMaxAge(7*24*60*60);//设置最大有效期为7天
                    cookie.setPath(request.getContextPath());//有效范围为当前项目

                    //使用response添加cookie
                    response.addCookie(cookie);
                }
            } catch (Exception e) {
                e.printStackTrace();
                //说明登录失败
                result.setData(false);//data为false表示登录失败
                result.setErrorMsg(e.getMessage());//封装失败信息
            }
        }else {
            //验证码错误
            result.setData(false);
            result.setErrorMsg("验证码错误");
        }

        //将result转换成json字符串然后输出到客户端
        return result;
    }

    /**
     * 处理查询用户登录信息的方法
     * @param request
     * @param response
     */
    public Result findUserInfo(HttpServletRequest request , HttpServletResponse response) throws IOException {
        Result result = new Result(true);
        //1.从session中获取user
        User user = (User) request.getSession().getAttribute(Constant.USER_KEY);
        //2.将user封装到result的data中
        result.setData(user);

        //3.将result转换成json字符串输出到客户端
        return result;
    }

    /**
     * 处理退出登录的请求
     * @param request
     * @param response
     */
    private Result logout(HttpServletRequest request ,HttpServletResponse response) throws IOException {
        //销毁当前的session
        request.getSession().invalidate();
        //跳转到登录页面
        response.sendRedirect("login.html");

        return null;
    }
}
