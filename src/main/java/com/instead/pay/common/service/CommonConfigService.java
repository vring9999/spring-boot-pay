package com.instead.pay.common.service;

import com.instead.pay.common.model.CommonConfig;

import java.util.List;
import java.util.Map;

public interface CommonConfigService {
    List<CommonConfig> queryCommons(Map<String,Object> params);

    void addCommon(CommonConfig common);

    void updCommon(CommonConfig common);

    void delCommon(String id);

    CommonConfig getCommonsById(String id);
}
