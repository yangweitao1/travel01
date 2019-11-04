package com.itheima.travel.dao;

import com.itheima.travel.domain.Category;

import java.util.List;

/**
 * 包名:com.itheima.travel.dao
 * 作者:Leevi
 * 日期2019-07-18  08:47
 */
public interface CategoryDao {

    List<Category> findAll();
}
