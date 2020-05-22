package com.instead.pay.qr.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Data   //GETTER   SETTER
@Component         //实例化
@AllArgsConstructor     //有参构造
@NoArgsConstructor      //无参构造
public class Qr{
    private String qrId;
    private String qrUrl;
    private Date createTime;
    private int enableStatus;
    /*
    *账号
     */
    private String bankAccount;
    private String receiptName;
    /*
     * 1 支付宝二维码   2微信二维码   3银行卡   4支付宝账号   5微信账号
     */
    private int receiptType;
    private String remark;
    private String remark1;
    private int qrNumber;
    /**
     * 商户号id
     */
    private String commercialNumber;

}
