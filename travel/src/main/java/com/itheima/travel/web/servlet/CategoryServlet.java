package com.itheima.travel.web.servlet;

import com.itheima.travel.constant.Constant;
import com.itheima.travel.domain.Category;
import com.itheima.travel.domain.Result;
import com.itheima.travel.factory.BeanFactory;
import com.itheima.travel.service.CategoryService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 包名:com.itheima.travel.web.servlet
 * 作者:Leevi
 * 日期2019-07-18  08:45
 */
@WebServlet("/category")
public class CategoryServlet extends BaseServlet{
    private CategoryService categoryService = (CategoryService) BeanFactory.getBean("categoryService");
    /**
     * 查询所有分类信息的方法
     * @param request
     * @param response
     * @return
     */
    private Result findAll(HttpServletRequest request, HttpServletResponse response){
        Result result = new Result(true);
        try {
            //List<Category> categoryList = categoryService.findAll();
            String categoryList = categoryService.findAll();
            result.setData(categoryList);
        } catch (Exception e) {
            e.printStackTrace();
            //服务器异常
            result.setFlag(false);
            result.setErrorMsg(Constant.SERVER_ERROR);
        }
        return result;
    }
}
