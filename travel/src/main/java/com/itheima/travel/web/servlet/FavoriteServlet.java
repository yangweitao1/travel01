package com.itheima.travel.web.servlet;

import com.itheima.travel.constant.Constant;
import com.itheima.travel.domain.*;
import com.itheima.travel.factory.BeanFactory;
import com.itheima.travel.service.FavoriteService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 包名:com.itheima.travel.web.servlet
 * 作者:Leevi
 * 日期2019-07-19  11:08
 */
@WebServlet("/favorite")
public class FavoriteServlet extends BaseServlet{
    private FavoriteService favoriteService = (FavoriteService) BeanFactory.getBean("favoriteService");

    /**
     * 处理查看收藏排行榜的请求
     * @param request
     * @param response
     * @return
     */
    private Result favoriteRank(HttpServletRequest request, HttpServletResponse response){
        Result result = new Result(true);
        Integer currentPage = Integer.valueOf(request.getParameter("currentPage"));
        Integer pageSize = Integer.valueOf(request.getParameter("pageSize"));

        //获取rname、minPrice、maxPrice
        String rname = request.getParameter("rname");
        String minPrice = request.getParameter("minPrice");
        String maxPrice = request.getParameter("maxPrice");

        //调用业务层的方法处理请求
        try {
            PageBean<Route> pageBean = favoriteService.findFavoriteRankPage(currentPage,pageSize,rname,minPrice,maxPrice);
            result.setData(pageBean);
        } catch (Exception e) {
            e.printStackTrace();
            result.setFlag(false);
            result.setErrorMsg(Constant.SERVER_ERROR);
        }
        return result;
    }
    /**
     * 处理查看我的收藏记录
     * @param request
     * @param response
     */
    private Result findMyFavorite(HttpServletRequest request, HttpServletResponse response){
        Result result = new Result(true);
        //1.获取请求参数
        Integer currentPage = Integer.valueOf(request.getParameter("currentPage"));
        Integer pageSize = Integer.valueOf(request.getParameter("pageSize"));
        //2.获取当前的用户信息
        User user = (User) request.getSession().getAttribute(Constant.USER_KEY);
        //3.调用业务层的方法，获取当前页的收藏记录
        try {
            PageBean<Favorite> pageBean = favoriteService.findMyFavoritePage(currentPage,pageSize,user);
            result.setData(pageBean);
        } catch (Exception e) {
            e.printStackTrace();
            result.setFlag(false);
            result.setErrorMsg(Constant.SERVER_ERROR);
        }
        return result;
    }
    /**
     * 添加收藏的方法
     * @param request
     * @param response
     * @return
     */
    private Result addFavorite(HttpServletRequest request, HttpServletResponse response){
        Result result = new Result(true);
        //1.获取请求参数rid
        String rid = request.getParameter("rid");
        //2.判断当前是否已登录
        User user = (User) request.getSession().getAttribute(Constant.USER_KEY);
        if (user != null) {
            //已登录
            //调用业务层的方法添加收藏
            int count = favoriteService.addFavorite(rid,user);
            result.setData(count);
        }else {
            //未登录
            //设置一个标示
            result.setData(-1);
        }

        return result;
    }
    /**
     * 判断当前路线是否已收藏
     * @param request
     * @param response
     * @return
     */
    private Result isFavorite(HttpServletRequest request, HttpServletResponse response){
        Result result = new Result(true);
        //1.获取请参数rid
        String rid = request.getParameter("rid");
        try {
            //2.判断当前是否已登录
            User user = (User) request.getSession().getAttribute(Constant.USER_KEY);

            if (user != null) {
                //当前已登录
                //调用业务层的方法判断是否已收藏
                Favorite favorite = favoriteService.findFavorite(rid,user);
                //判断是否已收藏
                if (favorite != null) {
                    //已收藏
                    result.setData(true);//这个true表示已收藏
                }else {
                    //未收藏
                    result.setData(false);//这个false目标是未收藏
                }
            }else {
                //当前未登录
                //应该显示未收藏状态
                result.setData(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setFlag(false);
            result.setErrorMsg(Constant.SERVER_ERROR);
        }
        return result;
    }
}
