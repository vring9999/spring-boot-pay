package com.instead.pay.orderInfo.model;

import com.instead.pay.util.desen.Desensitized;
import com.instead.pay.util.desen.SensitiveTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 订单表
 */
@Data   //GETTER   SETTER
@Component         //实例化
@AllArgsConstructor     //有参构造
@NoArgsConstructor      //无参构造
public class OrderInfo {



    public OrderInfo(String orderId, String outId, String commercialNumber, Integer num,Integer operatorMoney,String makerName, Date createTime, int applicationType, int payType, int isHand, int orderStatus, String remark,String operatorName,String qrId,String applicationName) {
        this.orderId = orderId;
        this.outId = outId;
        this.commercialNumber = commercialNumber;
        this.num = num;
        this.operatorMoney = operatorMoney;
        this.makerName = makerName;
        this.createTime = createTime;
        this.applicationType = applicationType;
        this.payType = payType;
        this.isHand = isHand;
        this.orderStatus = orderStatus;
        this.remark = remark;
        this.operatorName = operatorName;
        this.applicationName = applicationName;
        this.qrId = qrId;
    }

    /**
     * 我方服务费
     */
    private Integer outRatio;
    /**
     * 回调状态
     */
    private Integer backStatus;
    /**
     * 回调信息
     */
    private String backInfo;

    /**
     * 内部单号
     */
    private String orderId;
    /**
     * 外部单号
     */
    private String outId;
    /**
     * 商户号
     */
    private String commercialNumber;
    /**
     * 操作金额
     */
    private Integer operatorMoney;
    /**
     * 浮动金额
     */
    private Integer floatMoney;
    /**
     * 扣除手續費後的金額
     */
    private Integer deductedMoney;
    /**
     * 二维码地址
     */
    private String qrId;
    /**
     * 打款人姓名
     */
    private String makerName;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 确认时间
     */
    private Date confirmTime;
    /**
     * 操作人
     */
    private String operatorName;
    /**
     * 应用名称
     */
    private String applicationName;
    /**
     * 数量
     */
    private Integer num;
    /**
     * 类型
     */
    private Integer applicationType;
    /**
     * 支付方式   1 支付宝   2微信   3银行卡
     */
    private Integer payType;

    /**
     * 是否为手动  1是  2否
     */
    private Integer isHand;

    /**
     * 订单状态  0 已取消  1已确认  2待确认
     */
    private Integer orderStatus;
    /**
     * 备用   订单标识
     */
    private String remark;

    /**
     * 回调接口
     */
    private String callBackUrl;

    /**
     * 回调次数
     */
    private Integer callBackNum;

    /**
     * 比特币
     */
    private float bitcoin;

    /**
     * 银行卡号
     */
    private String bankAccount;
    /**
     * 收款银行
     */
    private String bankName;

    /**
     * 收款银行开户行分行
     */
    private String bankAddress;

    /**
     * 收款人名字
     */
    private String bankUserName;
    /**
     * 收款人联系方式
     */
//    @Desensitized(type = SensitiveTypeEnum.bankPhone)
    private String bankPhone;


    /**
     * 流水号
     */
    private String serialNo;
    /**
     * 流水号
     */
    private String remark4;

    /**
     * 支付地址
     */
    private String remitUrl;

    private String commerCialName;

    private String appName;

    /**
     * 是否线下承兑  1是  0否
     */
    private Integer line;


    public OrderInfo(String orderId, String outId, String commercialNumber, Integer num,
                     Integer operatorMoney,String makerName, Date createTime, int applicationType,
                     int payType, int isHand, int orderStatus, String operatorName,String applicationName,
                     String bankAccount, String bankName, String bankAddress, String bankUserName,String bankPhone) {
        this.orderId = orderId;
        this.outId = outId;
        this.commercialNumber = commercialNumber;
        this.num = num;
        this.operatorMoney = operatorMoney;
        this.makerName = makerName;
        this.createTime = createTime;
        this.applicationType = applicationType;
        this.payType = payType;
        this.isHand = isHand;
        this.orderStatus = orderStatus;
        this.operatorName = operatorName;
        this.applicationName = applicationName;

        this.bankAccount = bankAccount;
        this.bankName = bankName;
        this.bankAddress = bankAddress;
        this.bankUserName = bankUserName;
        this.bankPhone = bankPhone;
    }


    public OrderInfo(String orderId, String outId, String commercialNumber, int num , int operatorMoney, String makerName,
                     Date createTime, int applicationType, int payType, int isHand, int orderStatus, String commercialName,
                     String qrId) {
        this.orderId = orderId;
        this.outId = outId;
        this.commercialNumber = commercialNumber;
        this.num = num;
        this.operatorMoney = operatorMoney;
        this.makerName = makerName;
        this.createTime = createTime;
        this.applicationType = applicationType;
        this.payType = payType;
        this.isHand = isHand;
        this.orderStatus = orderStatus;
        this.makerName = commercialName;
        this.qrId = qrId;
    }
    //充值构造器
    public OrderInfo(String orderId, String outId, String commercialNumber, int num , int operatorMoney,
                     Date createTime, int applicationType, int payType, int isHand, int orderStatus,
                     String commercialName,String operatorName,Integer outRatio,Integer deductedMoney,
                     Date confirmTime) {
        this.orderId = orderId;
        this.outId = outId;
        this.commercialNumber = commercialNumber;
        this.num = num;
        this.operatorMoney = operatorMoney;
        this.operatorName = operatorName;
        this.createTime = createTime;
        this.applicationType = applicationType;
        this.payType = payType;
        this.isHand = isHand;
        this.orderStatus = orderStatus;
        this.makerName = commercialName;
        this.outRatio = outRatio;
        this.deductedMoney = deductedMoney;
        this.confirmTime = confirmTime;
    }


}
