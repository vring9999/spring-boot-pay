package com.instead.pay.appInfo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * app信息
 */
@Data   //GETTER   SETTER
@Component         //实例化
@AllArgsConstructor     //有参构造
@NoArgsConstructor      //无参构造
public class AppInfo {
    /**
     * APP ID
     */
    private String appId;
    /**
     * 商户号
     */
    private String commercialNumber;
    /**
     * app名称
     */
    private String appName;
    /**
     * 应用logo
     */
    private String appImg;
    /**
     * 白名单
     */
    private String appWhiteList;
    /**
     * 回调地址
     */
    private String appBackUrl;
    /**
     * AppKey
     */
    private String appKey;
    /**
     * 是否上架  0：上架  1：下架
     */
    private int appIsPut;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 操作人
     */
    private String operatorName;
    /**
     * 操作时间
     */
    private Date operatorTime;
    /**
     * 预留字段1
     */
    private String remark1;
    /**
     * 预留字段2
     */
    private String remark2;
}
