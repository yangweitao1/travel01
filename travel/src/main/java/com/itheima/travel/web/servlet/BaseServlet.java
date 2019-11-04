package com.itheima.travel.web.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itheima.travel.domain.Result;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
public class BaseServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        //1.获取参数action的值
        String action = request.getParameter("action");
        //2.判断action的值，调用不同的方法
        //目标:根据action参数值调用对应的方法，action的值其实就跟要调用的方法的方法名一样
        //已知方法名、以及方法的参数类型，获取方法并且调用，使用反射技术
        Class clazz = this.getClass();
        try {
            Method method = clazz.getDeclaredMethod(action, HttpServletRequest.class, HttpServletResponse.class);
            //暴力反射
            method.setAccessible(true);
            //调用方法,执行处理请求的方法
            Result result = (Result) method.invoke(this, request, response);
            //判断如果result不为null，就要解析json输出响应信息
            if (result != null) {
                //执行完处理请求的方法之后，要调用parseJson()方法
                parseJson(response,result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 这个方法是将result对象转换成json字符串然后输出到客户端
     * @param response
     * @param result
     * @throws IOException
     */
    private void parseJson(HttpServletResponse response, Result result) throws IOException {
        String jsonStr = new ObjectMapper().writeValueAsString(result);
        response.getWriter().write(jsonStr);
    }
}
