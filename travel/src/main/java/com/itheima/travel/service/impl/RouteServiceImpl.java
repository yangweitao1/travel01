package com.itheima.travel.service.impl;

import com.itheima.travel.dao.RouteDao;
import com.itheima.travel.domain.*;
import com.itheima.travel.factory.BeanFactory;
import com.itheima.travel.service.RouteService;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 包名:com.itheima.travel.service.impl
 * 作者:Leevi
 * 日期2019-07-18  09:46
 */
public class RouteServiceImpl implements RouteService{
    private RouteDao routeDao = (RouteDao) BeanFactory.getBean("routeDao");

    @Override
    public Map<String, List<Route>> careChoose() {
        //1.调用dao层的方法，查询人气旅游路线集合
        List<Route> popList = routeDao.findPopList();
        //2.调用dao层的方法，查询最新旅游路线集合
        List<Route> newestList = routeDao.findNewestList();
        //3.调用dao层的方法，查询主题旅游路线集合
        List<Route> themeList = routeDao.findThemeList();
        //4.将上述路线集合封装到Map中
        Map<String,List<Route>> map = new HashMap<>();
        map.put("popList",popList);
        map.put("newestList",newestList);
        map.put("themeList",themeList);
        return map;
    }

    @Override
    public PageBean<Route> findRoutePage(String cid, Integer currentPage, Integer pageSize, String keyword) {
        //1.创建PageBean对象
        PageBean<Route> pageBean = new PageBean<>();
        //2.往PageBean中设置currentPage
        pageBean.setCurrentPage(currentPage);
        //3.往PageBean中设置pageSize
        pageBean.setPageSize(pageSize);
        //4.调用dao层的方法获取当前分类下的总数据条数
        Integer totalSize = routeDao.findRouteCountByCid(cid,keyword);
        //设置totalSize
        pageBean.setTotalSize(totalSize);

        //5.计算出totalPage，并且设置totalPage
        //Integer totalPage = totalSize % pageSize == 0 ? totalSize / pageSize : totalSize / pageSize + 1;
        //pageBean.setTotalPage(totalPage);

        //6.调用dao层的方法,获取当前页的数据集合
        List<Route> routeList = routeDao.findRouteList(cid,currentPage,pageSize,keyword);
        pageBean.setList(routeList);
        return pageBean;
    }

    @Override
    public Route findRouteByRid(String rid) throws InvocationTargetException, IllegalAccessException {
        //1.调用dao层的方法，根据rid连接route、category、seller三张表进行查询
        Map<String, Object> map = routeDao.findRouteByRid(rid);

        Route route = new Route();
        //使用BeanUtils将map中的数据封装到Route中
        BeanUtils.populate(route,map);

        //1.设置category
        Category category = new Category();
        //将map中与category对应的数据封装到category中
        BeanUtils.populate(category,map);
        route.setCategory(category);

        //2. 设置seller
        Seller seller = new Seller();
        BeanUtils.populate(seller,map);
        route.setSeller(seller);

        //2.调用dao层的方法根据rid查询route_img表，获取路线的图片集合
        List<RouteImg> routeImgList = routeDao.findRouteImgListByRid(rid);
        route.setRouteImgList(routeImgList);
        return route;
    }
}
