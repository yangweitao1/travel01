package com.itheima.travel.web.servlet;

import com.alibaba.druid.sql.visitor.functions.Concat;
import com.itheima.travel.constant.Constant;
import com.itheima.travel.domain.PageBean;
import com.itheima.travel.domain.Result;
import com.itheima.travel.domain.Route;
import com.itheima.travel.factory.BeanFactory;
import com.itheima.travel.service.RouteService;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 包名:com.itheima.travel.web.servlet
 * 作者:Leevi
 * 日期2019-07-18  09:46
 */
@WebServlet("/route")
public class RouteServlet extends BaseServlet{
    private RouteService routeService = (RouteService) BeanFactory.getBean("routeService");

    /**
     * 获取路线详情的方法
     * @param request
     * @param response
     * @return
     */
    private Result findRouteByRid(HttpServletRequest request,HttpServletResponse response){
        Result result = new Result(true);

        //1.获取请求参数rid
        String rid = request.getParameter("rid");
        //2.调用业务层的方法，根据rid获取Route
        try {
            Route route = routeService.findRouteByRid(rid);
            result.setData(route);
        } catch (Exception e) {
            e.printStackTrace();
            result.setFlag(false);
            result.setErrorMsg(Constant.SERVER_ERROR);
        }
        return result;
    }
    /**
     * 处理分页查看某类路线的请求
     * @param request
     * @param response
     * @return
     */
    private Result findRoutePage(HttpServletRequest request,HttpServletResponse response){
        Result result = new Result(true);
        //1.获取请求参数
        String cid = request.getParameter("cid");
        Integer currentPage = Integer.valueOf(request.getParameter("currentPage"));
        Integer pageSize = Integer.valueOf(request.getParameter("pageSize"));

        //获取keyword
        String keyword = request.getParameter("keyword");
        try {
            //2.调用业务层的方法进行分页查询
            PageBean<Route> pageBean =  routeService.findRoutePage(cid,currentPage,pageSize,keyword);
            result.setData(pageBean);
        } catch (Exception e) {
            e.printStackTrace();
            result.setFlag(false);
            result.setErrorMsg(Constant.SERVER_ERROR);
        }

        return result;
    }
    /**
     * 处理黑马精选的方法
     * @param request
     * @param response
     * @return
     */
    private Result careChoose(HttpServletRequest request, HttpServletResponse response){
        Result result = new Result(true);
        try {
            //1.调用业务层的方法，查询黑马精选路线信息
            Map<String,List<Route>> map = routeService.careChoose();
            result.setData(map);
        } catch (Exception e) {
            e.printStackTrace();
            result.setFlag(false);
            result.setErrorMsg(Constant.SERVER_ERROR);
        }

        return result;
    }
}
