package com.instead.pay.common.mapper;

import com.instead.pay.common.model.CommonConfig;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CommonConfigMapper {

    List<CommonConfig> queryCommons(Map<String,Object> params);

    void addCommon(CommonConfig common);

    void updCommon(CommonConfig common);

    void delCommon(String id);

    CommonConfig getCommonsById(String id);
}
