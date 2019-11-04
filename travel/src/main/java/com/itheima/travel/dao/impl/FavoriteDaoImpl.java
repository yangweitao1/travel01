package com.itheima.travel.dao.impl;

import com.itheima.travel.dao.FavoriteDao;
import com.itheima.travel.domain.Favorite;
import com.itheima.travel.domain.Route;
import com.itheima.travel.domain.User;
import com.itheima.travel.utils.DateUtil;
import com.itheima.travel.utils.JDBCUtil;
import com.itheima.travel.utils.StringUtil;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 包名:com.itheima.travel.dao.impl
 * 作者:Leevi
 * 日期2019-07-19  11:09
 */
public class FavoriteDaoImpl implements FavoriteDao{
    private JdbcTemplate template = new JdbcTemplate(JDBCUtil.getDataSource());

    @Override
    public Favorite findFavorite(String rid, User user) {
        String sql = "select * from tab_favorite where rid=? and uid=?";
        Favorite favorite = null;
        try {
            favorite = template.queryForObject(sql, new BeanPropertyRowMapper<>(Favorite.class), rid, user.getUid());
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return favorite;
    }

    @Override
    public void addFavorite(String rid, User user) {
        String sql = "insert into tab_favorite values(?,?,?)";
        template.update(sql,rid, DateUtil.getCurrentDate(),user.getUid());
    }

    @Override
    public void updateRouteCount(String rid) {
        String sql = "update tab_route set count=count+1 where rid=?";
        template.update(sql,rid);
    }

    @Override
    public int findRouteCount(String rid) {
        String sql = "select count from tab_route where rid=?";
        Integer count = template.queryForObject(sql, Integer.class, rid);
        return count;
    }

    @Override
    public Integer findFavoriteCountByUid(int uid) {
        String sql = "select count(*) from tab_favorite where uid=?";
        Integer count = null;
        try {
            count = template.queryForObject(sql, Integer.class, uid);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return count;
    }

    @Override
    public List<Map<String, Object>> findMyFavoritePageList(Integer currentPage, Integer pageSize, int uid) {
        String sql = "SELECT * FROM tab_favorite f,tab_route r WHERE f.rid=r.rid AND f.uid=? limit ?,?";
        //将查询到的每一条数据封装到一个Map中
        //那么多条数据就对应多个map，那么多个map就存放到一个List中
        List<Map<String, Object>> mapList = null;
        try {
            mapList = template.queryForList(sql, uid, (currentPage - 1) * pageSize, pageSize);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return mapList;
    }

    @Override
    public Integer findCount(String rname, String minPrice, String maxPrice) {
        //查询总路线条数
        String sql = "select count(*) from tab_route where rflag=1";

        List<Object> params = new ArrayList<>();
        //判断rname是否为空
        if (!StringUtil.isEmpty(rname)) {
            //rname不为空，就要添加rname的搜索条件
            sql += " and rname like ?";
            params.add("%"+rname+"%");
        }

        //判断minPrice是否为空
        if (!StringUtil.isEmpty(minPrice)) {
            //minPrice不为空，则考虑minPrice的条件
            sql += " and price>=?";
            params.add(minPrice);
        }

        //判断maxPrice是否为空
        if (!StringUtil.isEmpty(maxPrice)) {
            //maxPrice不为空，则考虑maxPrice的条件
            sql += " and price<=?";
            params.add(maxPrice);
        }

        Integer count = null;
        try {
            count = template.queryForObject(sql, Integer.class,params.toArray());
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return count;
    }

    @Override
    public List<Route> findFavoriteRankList(Integer currentPage, Integer pageSize, String rname, String minPrice, String maxPrice) {
        //查询当前页的数据集合，按照count字段的降序排列
        String sql = "select * from tab_route where rflag=1";

        List<Object> params = new ArrayList<>();
        //判断rname是否为空
        if (!StringUtil.isEmpty(rname)) {
            //rname不为空，就要添加rname的搜索条件
            sql += " and rname like ?";
            params.add("%"+rname+"%");
        }

        //判断minPrice是否为空
        if (!StringUtil.isEmpty(minPrice)) {
            //minPrice不为空，则考虑minPrice的条件
            sql += " and price>=?";
            params.add(minPrice);
        }

        //判断maxPrice是否为空
        if (!StringUtil.isEmpty(maxPrice)) {
            //maxPrice不为空，则考虑maxPrice的条件
            sql += " and price<=?";
            params.add(maxPrice);
        }

        //排序和分页要放到条件之后
        sql += " order by count desc limit ?,?";
        params.add((currentPage-1)*pageSize);
        params.add(pageSize);

        List<Route> routeList = null;
        try {
            routeList = template.query(sql, new BeanPropertyRowMapper<>(Route.class), params.toArray());
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return routeList;
    }
}
