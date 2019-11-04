package com.itheima.travel.dao.impl;

import com.itheima.travel.dao.RouteDao;
import com.itheima.travel.domain.Route;
import com.itheima.travel.domain.RouteImg;
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
 * 日期2019-07-18  09:47
 */
public class RouteDaoImpl implements RouteDao{
    private JdbcTemplate template = new JdbcTemplate(JDBCUtil.getDataSource());
    @Override
    public List<Route> findPopList() {
        String sql = "SELECT * FROM tab_route WHERE rflag=1 ORDER BY COUNT DESC LIMIT 0,4";
        List<Route> popList = null;
        try {
            popList = template.query(sql, new BeanPropertyRowMapper<>(Route.class));
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return popList;
    }

    @Override
    public List<Route> findNewestList() {
        String sql = "SELECT * FROM tab_route WHERE rflag=1 ORDER BY rdate DESC LIMIT 0,4";
        List<Route> newestList = null;
        try {
            newestList = template.query(sql, new BeanPropertyRowMapper<>(Route.class));
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return newestList;
    }

    @Override
    public List<Route> findThemeList() {
        String sql = "SELECT * FROM tab_route WHERE rflag=1 AND isThemeTour=1 LIMIT 0,4";
        List<Route> themeList = null;
        try {
            themeList = template.query(sql, new BeanPropertyRowMapper<>(Route.class));
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return themeList;
    }

    @Override
    public Integer findRouteCountByCid(String cid, String keyword) {
        String sql = "select count(*) from tab_route where rflag=1";

        List<Object> params = new ArrayList<>();//将要设置的参数放置到params集合中
        //如果keyword不为空，那么我们的SQL语句，要添加一个条件根据路线名称和keyword进行模糊查询"rname like ?"
        if (!StringUtil.isEmpty(keyword)) {
            sql += " and rname like ?";
            //添加第二个参数
            params.add("%"+keyword+"%");
        }

        //如果cid不为空，那我们要考虑cid的条件
        if (!StringUtil.isEmpty(cid)) {
            sql += " and cid = ?";
            //添加第二个参数
            params.add(cid);
        }
        Integer totalSize = null;
        try {
            totalSize = template.queryForObject(sql, Integer.class,params.toArray());
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return totalSize;
    }

    @Override
    public List<Route> findRouteList(String cid, Integer currentPage, Integer pageSize, String keyword) {
        //分页查询某类路线
        String sql = "select * from tab_route where rflag=1 ";
        List<Object> params = new ArrayList<>();//将要设置的参数放置到params集合中
        //如果keyword不为空，那么我们的SQL语句，要添加一个条件根据路线名称和keyword进行模糊查询"rname like ?"
        if (!StringUtil.isEmpty(keyword)) {
            sql += " and rname like ?";
            //添加第二个参数
            params.add("%"+keyword+"%");
        }

        //如果cid不为空，那我们要考虑cid的条件
        if (!StringUtil.isEmpty(cid)) {
            sql += " and cid = ?";
            //添加第二个参数
            params.add(cid);
        }
        //拼接limit进行分页
        sql += "  limit ?,?";
        //limit后面的俩参数表示:1. 跳过的数据条数   2.查询的数据条数
        //跳过的数据条数=当前页之前的所有数据条数   ，这个公式 (currentPage-1)*每页条数
        params.add((currentPage - 1) * pageSize);
        params.add(pageSize);
        List<Route> routeList = null;
        try {
            routeList = template.query(sql, new BeanPropertyRowMapper<>(Route.class), params.toArray());
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return routeList;
    }

    @Override
    public Map<String, Object> findRouteByRid(String rid) {
        String sql = "SELECT * FROM tab_route r,tab_category c,tab_seller s WHERE r.cid=c.cid AND r.sid=s.sid AND rid=?";
        //使用queryForObject方法将查询到的一条数据封装到JavaBean中，要求是:结果集的字段名和JavaBean的属性名对应，如果能对应就能封装好，不能对应就封装不好
        //Route route = template.queryForObject(sql, new BeanPropertyRowMapper<>(Route.class), rid);
        //有一个方法能够封装结果集中的所有字段，封装到Map对象中，字段名就是map的key，字段值就是map的value
        Map<String, Object> map = null;
        try {
            map = template.queryForMap(sql, rid);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return map;
    }

    @Override
    public List<RouteImg> findRouteImgListByRid(String rid) {
        String sql = "SELECT * FROM tab_route_img WHERE rid=?";
        List<RouteImg> routeImgList = null;
        try {
            routeImgList = template.query(sql, new BeanPropertyRowMapper<>(RouteImg.class), rid);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return routeImgList;
    }
}
