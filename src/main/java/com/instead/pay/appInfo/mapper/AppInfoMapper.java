package com.instead.pay.appInfo.mapper;

import com.instead.pay.appInfo.model.AppInfo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface AppInfoMapper {

    List<AppInfo> queryAppInfo(Map<String,Object> param);

    List<AppInfo> getAppByComId(String commercialNumber);

    void insertAppInfo(AppInfo appInfo);

    void updateAppInfo(AppInfo appInfo);

    AppInfo getAppById(String appId);

    AppInfo getAppByKey(String appkey);

    AppInfo getAppByName(String appName);

    List<Map<String,String>> getCodeName(String commercialNumber);

    void delete(String appId);

}
