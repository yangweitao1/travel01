package com.itheima.travel.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.itheima.travel.domain.Category;

import java.io.IOException;
import java.util.List;

/**
 * 包名:com.itheima.travel.service
 * 作者:Leevi
 * 日期2019-07-18  08:46
 */
public interface CategoryService {
    //List<Category> findAll() throws IOException;
    String findAll() throws IOException;
}
