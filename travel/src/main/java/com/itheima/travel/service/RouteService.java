package com.itheima.travel.service;

import com.itheima.travel.domain.PageBean;
import com.itheima.travel.domain.Route;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

/**
 * 包名:com.itheima.travel.service
 * 作者:Leevi
 * 日期2019-07-18  09:46
 */
public interface RouteService {
    Map<String,List<Route>> careChoose();

    PageBean<Route> findRoutePage(String cid, Integer currentPage, Integer pageSize, String keyword);

    Route findRouteByRid(String rid) throws InvocationTargetException, IllegalAccessException;
}
