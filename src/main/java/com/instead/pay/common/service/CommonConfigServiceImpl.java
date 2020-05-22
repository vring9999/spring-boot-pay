package com.instead.pay.common.service;

import com.instead.pay.common.mapper.CommonConfigMapper;
import com.instead.pay.common.model.CommonConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("CommonConfigService")
public class CommonConfigServiceImpl implements CommonConfigService {
    @Autowired
    private CommonConfigMapper commonConfigMapper;

    @Override
    public List<CommonConfig> queryCommons(Map<String, Object> params) {
        return commonConfigMapper.queryCommons(params);
    }

    @Override
    public void addCommon(CommonConfig common) {
        commonConfigMapper.addCommon(common);
    }

    @Override
    public void updCommon(CommonConfig common) {
        commonConfigMapper.updCommon(common);
    }

    @Override
    public void delCommon(String id) {
        commonConfigMapper.delCommon(id);
    }

    @Override
    public CommonConfig getCommonsById(String id) { return commonConfigMapper.getCommonsById(id); }
}
