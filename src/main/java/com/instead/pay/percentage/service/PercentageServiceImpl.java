package com.instead.pay.percentage.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.instead.pay.percentage.mapper.PercentageMapper;
import com.instead.pay.percentage.model.Percentage;
import com.instead.pay.util.pageHelper.PageRequest;
import com.instead.pay.util.pageHelper.PageResult;
import com.instead.pay.util.pageHelper.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author vring
 * @ClassName PercentageServiceImpl.java
 * @Description
 * @createTime 2019/12/6 9:57
 */
@Service
public class PercentageServiceImpl implements PercentageService {

    @Autowired
    private PercentageMapper percentageMapper;

    @Override
    public List<Percentage> selectAll(Map<String, Object> param) {
        List<Percentage> sysMenus = percentageMapper.selectAll(param);
        return sysMenus;
    }

    @Override
    public void insert(Percentage percentage) {
        percentageMapper.insert(percentage);
    }


}
