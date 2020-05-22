package com.instead.pay.percentage.service;

import com.github.pagehelper.PageInfo;
import com.instead.pay.percentage.model.Percentage;
import com.instead.pay.util.pageHelper.PageRequest;
import com.instead.pay.util.pageHelper.PageResult;

import java.util.List;
import java.util.Map;

public interface PercentageService {

    /**
     * 分页查询接口
     * 这里统一封装了分页请求和结果，避免直接引入具体框架的分页对象, 如MyBatis或JPA的分页对象
     * 从而避免因为替换ORM框架而导致服务层、控制层的分页接口也需要变动的情况，替换ORM框架也不会
     * 影响服务层以上的分页接口，起到了解耦的作用
     * @return PageResult 自定义，统一分页查询结果
     */
//    PageInfo<Percentage> getPageInfo(Integer page, Integer limit, Map<String,Object> param);

    List<Percentage> selectAll(Map<String,Object> param);

    void insert(Percentage percentage);

}
