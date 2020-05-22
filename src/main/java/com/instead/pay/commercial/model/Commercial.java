package com.instead.pay.commercial.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 商户信息
 */
@Data
@AllArgsConstructor
@Component
@NoArgsConstructor
public class Commercial {

    /**
     * 商户id
     */
    private String commercialId;
    /**
     * 商户名
     */
    private String commercialName;
    /**
     * 商户手机号
     */
    private String commercialIphone;
    /**
     * 商户号
     */
    private String commercialNumber;
    /**
     * 商户密码
     */
    private String commercialPassword;
    /**
     * 商户余额
     */
    private int commercialBalance;
    /**
     * 系统代收代付抽成比例
     */
    private float commercialRatio;
    /**
     * 商户提现抽成比例
     */
    private float commercialWithRatio;
    /**
     * 修改时间
     */
    private Date updateTime;
    /**
     * 创建时间
     */
    private Date creationTime;
    /**
     * 权限
     */
    private String roleId;
    /**
     * 商户标识（商户名）
     */
    private String reserved2;

    /**
     * 冻结金额
     */
    private Integer freezeMoney;
    /**
     * 总金额
     */
    private Integer allMoney;
    /**
     * 总提现金额
     */
    private Integer allWitMoney;
    /**
     * 安全密码
     */
    private String safetyPwd;


    public Integer getFreezeMoney() {
        return freezeMoney == null?0:freezeMoney;
    }

    public Integer getAllMoney() {
        return allMoney== null?0:allMoney;
    }

    public Integer getAllWitMoney() {
        return allWitMoney== null?0:allWitMoney;
    }
}
