package com.itheima.travel.dao;

import com.itheima.travel.domain.Favorite;
import com.itheima.travel.domain.Route;
import com.itheima.travel.domain.User;

import java.util.List;
import java.util.Map;

/**
 * 包名:com.itheima.travel.dao
 * 作者:Leevi
 * 日期2019-07-19  11:09
 */
public interface FavoriteDao {
    Favorite findFavorite(String rid, User user);

    void addFavorite(String rid, User user);

    void updateRouteCount(String rid);

    int findRouteCount(String rid);

    Integer findFavoriteCountByUid(int uid);

    List<Map<String, Object>> findMyFavoritePageList(Integer currentPage, Integer pageSize, int uid);

    Integer findCount(String rname, String minPrice, String maxPrice);

    List<Route> findFavoriteRankList(Integer currentPage, Integer pageSize, String rname, String minPrice, String maxPrice);
}
