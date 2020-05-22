package com.instead.pay;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.instead.pay.common.model.CommonConfig;
import com.instead.pay.common.service.CommonConfigService;
import com.instead.pay.util.*;
import com.instead.pay.util.rsa.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 启动运行代码
 */
@Component
@Slf4j
public class ApplicationRunnerImpl implements ApplicationRunner {

    @Autowired
    private CommonConfigService commonConfigService;

    @Override
    public void run(ApplicationArguments args) {
        log.info("初始化配置信息");
        queryCommon();
    }

    /**
     * 初始化配置信息
     */
    private void queryCommon() {
        List<CommonConfig> commons = commonConfigService.queryCommons(null);
        commons.forEach(comm -> CacheInfo.commonInfo.put(comm.getCfgName()+"+"+comm.getCfgKey(),comm.getCfgValue()));
    }
}