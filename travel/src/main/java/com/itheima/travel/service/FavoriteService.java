package com.itheima.travel.service;

import com.itheima.travel.domain.Favorite;
import com.itheima.travel.domain.PageBean;
import com.itheima.travel.domain.Route;
import com.itheima.travel.domain.User;

import java.lang.reflect.InvocationTargetException;

/**
 * 包名:com.itheima.travel.service
 * 作者:Leevi
 * 日期2019-07-19  11:09
 */
public interface FavoriteService {
    Favorite findFavorite(String rid, User user);

    int addFavorite(String rid, User user);

    PageBean<Favorite> findMyFavoritePage(Integer currentPage, Integer pageSize, User user) throws InvocationTargetException, IllegalAccessException;

    PageBean<Route> findFavoriteRankPage(Integer currentPage, Integer pageSize, String rname, String minPrice, String maxPrice);
}
