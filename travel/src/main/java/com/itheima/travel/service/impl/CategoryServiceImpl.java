package com.itheima.travel.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itheima.travel.constant.Constant;
import com.itheima.travel.dao.CategoryDao;
import com.itheima.travel.domain.Category;
import com.itheima.travel.factory.BeanFactory;
import com.itheima.travel.service.CategoryService;
import com.itheima.travel.utils.JedisUtil;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.List;

/**
 * 包名:com.itheima.travel.service.impl
 * 作者:Leevi
 * 日期2019-07-18  08:46
 */
public class CategoryServiceImpl implements CategoryService{
    private CategoryDao categoryDao = (CategoryDao) BeanFactory.getBean("categoryDao");

    @Override
    public String findAll() throws IOException {
        //1.从redis服务器获取所有分类信息
        Jedis jedis = JedisUtil.getJedis();
        String jsonStr = jedis.get(Constant.CATEGORY_ALL_KEY);

        ObjectMapper mapper = new ObjectMapper();
        //2.判断redis中是否有所有的分类信息的数据
        if (jsonStr == null) {
            //说明redis中没有所有分类信息的数据
            //2.1 从mysql中获取
            List<Category> categoryList = categoryDao.findAll();
            //2.2 将categoryList转换成json字符串
            jsonStr = mapper.writeValueAsString(categoryList);
            //2.3 将jsonStr存放到redis服务器中
            jedis.set(Constant.CATEGORY_ALL_KEY,jsonStr);
        }
        //关闭资源
        jedis.close();
        return jsonStr;
    }

    /*@Override
    public List<Category> findAll() throws IOException {
        //1.从redis服务器获取所有分类信息
        Jedis jedis = JedisUtil.getJedis();
        String jsonStr = jedis.get(Constant.CATEGORY_ALL_KEY);

        ObjectMapper mapper = new ObjectMapper();
        //2.判断redis中是否有所有的分类信息的数据
        if (jsonStr == null) {
            //说明redis中没有所有分类信息的数据
            //2.1 从mysql中获取
            List<Category> categoryList = categoryDao.findAll();
            //2.2 将categoryList转换成json字符串
            jsonStr = mapper.writeValueAsString(categoryList);
            //2.3 将jsonStr存放到redis服务器中
            jedis.set(Constant.CATEGORY_ALL_KEY,jsonStr);
        }

        //3. 将jsonStr字符串转换成List<Category>
        List<Category> categoryList = mapper.readValue(jsonStr, new TypeReference<List<Category>>() {
        });

        //关闭资源
        jedis.close();
        return categoryList;
    }*/

    //直接从mysql中获取所有分类信息
    /*@Override
    public List<Category> findAll() {
        return categoryDao.findAll();
    }*/
}
