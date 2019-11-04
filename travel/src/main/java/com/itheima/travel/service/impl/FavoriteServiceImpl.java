package com.itheima.travel.service.impl;

import com.itheima.travel.dao.FavoriteDao;
import com.itheima.travel.domain.Favorite;
import com.itheima.travel.domain.PageBean;
import com.itheima.travel.domain.Route;
import com.itheima.travel.domain.User;
import com.itheima.travel.factory.BeanFactory;
import com.itheima.travel.service.FavoriteService;
import com.itheima.travel.utils.JDBCUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.sql.DataSource;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 包名:com.itheima.travel.service.impl
 * 作者:Leevi
 * 日期2019-07-19  11:09
 */
public class FavoriteServiceImpl implements FavoriteService{
    private FavoriteDao favoriteDao = (FavoriteDao) BeanFactory.getBean("favoriteDao");

    @Override
    public Favorite findFavorite(String rid, User user) {
        return favoriteDao.findFavorite(rid,user);
    }

    @Override
    public int addFavorite(String rid, User user) {
        int count = 0;
        Connection conn = null;
        try {
            //获取JdbcTemplate对象使用的连接
            //1.开启事务同步
            TransactionSynchronizationManager.initSynchronization();
            //2.获取数据源对象
            DataSource dataSource = JDBCUtil.getDataSource();
            //3.通过JdbcTemplate框架中的DataSourceUtils从数据源获取连接
            conn = DataSourceUtils.getConnection(dataSource);//底层使用的是ThreadLocal

            //执行业务逻辑之前开启事务:connection.setAutoCommit(false)
            conn.setAutoCommit(false);
            //1.调用dao层的方法，往收藏表添加一条数据
            favoriteDao.addFavorite(rid, user);

            //int num = 10 / 0;

            //2.调用dao层的方法修改路线的count字段
            favoriteDao.updateRouteCount(rid);
            //3.调用dao层的方法查询路线的总收藏次数
            count = favoriteDao.findRouteCount(rid);

            //执行完业务逻辑没有出现异常，提交事务:connection.commit()
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
            //执行业务逻辑的过程中，出现异常，回滚事务:connection.rollback()
            try {
                conn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }finally {
            //清理资源
            //1.清除事务同步
            TransactionSynchronizationManager.clearSynchronization();
            //2.将conn的autoCommit属性还原成true
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return count;
    }

    @Override
    public PageBean<Favorite> findMyFavoritePage(Integer currentPage, Integer pageSize, User user) throws InvocationTargetException, IllegalAccessException {
        //1.创建PageBean对象
        PageBean<Favorite> pageBean = new PageBean<>();
        //2.设置数据
        pageBean.setCurrentPage(currentPage);
        pageBean.setPageSize(pageSize);
        //调用dao层的方法到数据库查询出当前用户的总收藏路线条数
        Integer totalSize = favoriteDao.findFavoriteCountByUid(user.getUid());
        pageBean.setTotalSize(totalSize);

        //调用dao层的方法获取当前页的收藏数据集合，并且设置
        List<Map<String, Object>> mapList = favoriteDao.findMyFavoritePageList(currentPage,pageSize,user.getUid());

        //我们设置到pageBean 里面去的List，应该是List<Favorite>
        //将List<Map<String, Object>>   ----->    List<Favorite>
        //将List<Map<String, Object>>每一个map转换成一个favorite,然后再将每一个favorite存放到List中
        List<Favorite> favoriteList = new ArrayList<>();
        for (Map<String, Object> map : mapList) {
            //1.创建一个Favorite
            Favorite favorite = new Favorite();
            //2.将map中与favorite对应的数据设置到favorite中
            BeanUtils.populate(favorite,map);
            //3.还缺少route、user
            favorite.setUser(user);

            Route route = new Route();
            //将map中与route对应的数据封装到route
            BeanUtils.populate(route,map);
            favorite.setRoute(route);

            favoriteList.add(favorite);
        }
        pageBean.setList(favoriteList);

        return pageBean;
    }

    @Override
    public PageBean<Route> findFavoriteRankPage(Integer currentPage, Integer pageSize, String rname, String minPrice, String maxPrice) {
        //1.创建PageBean
        PageBean<Route> pageBean = new PageBean<>();
        //2.设置数据
        pageBean.setCurrentPage(currentPage);
        pageBean.setPageSize(pageSize);

        //调用dao层的方法获取totalSize
        Integer totalSize = favoriteDao.findCount(rname,minPrice,maxPrice);
        pageBean.setTotalSize(totalSize);

        //调用dao层的方法，获取当前页的数据集合
        List<Route> routeList = favoriteDao.findFavoriteRankList(currentPage,pageSize,rname,minPrice,maxPrice);
        pageBean.setList(routeList);
        return pageBean;
    }
}
