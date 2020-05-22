package com.instead.pay.util;

import org.springframework.stereotype.Component;

/**
 * @author vring
 * @ClassName UsedCode.java
 * @Description
 * @createTime 2019/12/3 16:56
 */
@Component
public class UsedCode {

    /**
     * @fieldName: PAY_TYPE_ALIPAY
     * @fieldType: String
     * @Description: 支付类型为支付宝
     */
    public static final int PAY_TYPE_ALIPAY = 1;
    /**
     * @fieldName: PAY_TYPE_WECHAT
     * @fieldType: String
     * @Description: 支付类型为微信
     */
    public static final int PAY_TYPE_WECHAT = 3;
    /**
     * @fieldName: PAY_TYPE_BANK
     * @fieldType: String
     * @Description: 支付类型为银行卡
     */
    public static final int PAY_TYPE_BANK = 2;

    /**
     * @fieldName: ORDER_STATUS_CANCEL
     * @fieldType: String
     * @Description: 订单状态--关闭0   未回调   不走线下承兑
     */
    public static final int ORDER_STATUS_CANCEL = 0;
    /**
     * @fieldName: ORDER_STATUS_WAIT
     * @fieldType: String
     * @Description: 订单状态--已确认1已承兑   商户回调成功
     */
    public static final int ORDER_STATUS_CONFIRM = 1;
    /**
     * @fieldName: ORDER_STATUS_CONFIRM
     * @fieldType: String
     * @Description: 订单状态--待确认2待承兑  回调失败
     */
    public static final int ORDER_STATUS_WAIT = 2;
    /**
     * @fieldName: ORDER_STATUS_CONFIRM
     * @fieldType: String
     * @Description: 订单状态--过期
     */
    public static final int ORDER_STATUS_BE = 3;

    /**
     * @fieldName: ORDER_STATUS_CONFIRM
     * @fieldType: String
     * @Description: 订单状态--驳回    订单类型---充值
     */
    public static final int ORDER_STATUS_BO = 4;

    /**
     * @fieldName: ORDER_STATUS_WAIT
     * @fieldType: String
     * @Description: 應用類型 1代收   走线下承兑
     */
    public static final int APPLICATION_TYPE_IN = 1;
    /**
     * @fieldName: ORDER_STATUS_CONFIRM
     * @fieldType: String
     * @Description:  應用類型 2代付
     */
    public static final int APPLICATION_TYPE_OUT = 2;

    /**
     * @fieldName: ORDER_STATUS_CONFIRM
     * @fieldType: String
     * @Description:  應用類型 3提现
     */
    public static final int APPLICATION_TYPE_WIT = 3;

    /**
     * @fieldName: IS_HAND_YES
     * @fieldType: int  1是
     * @Description:  是否为自动发起代收代付(即手动挂单)
     */
    public static final int IS_HAND_YES = 1;

    /**
     * @fieldName: DIST_TYPE_SYSTEM
     * @fieldType: int  2否
     *      * @Description:  是否为自动发起代收代付(即自动挂单)
     */
    public static final int IS_HAND_NO = 2;
    /**
     * @fieldName: SYSTEM_NUMBER
     * @fieldType: String
     * @Description:  系统专用商户号
     */
    public static final String SYSTEM_NUMBER = "0000000000";

    /**
     * @fieldName: ROLE_ADMIN
     * @fieldType: String
     * @Description:  权限角色
     */
    public static final String ROLE_ADMIN = "ROLE_ADMIN";

    /**
     * @fieldName: ROLE_ADMIN
     * @fieldType: String
     * @Description:  权限角色
     */
    public static final String ROLE_USER = "ROLE_USER";

    /**
     * @fieldName: ROLE_TYPE_ADMIN
     * @fieldType: String
     * @Description:  权限类型为系统用户
     */
    public static final String ROLE_TYPE_ADMIN = "0";

    /**
     * @fieldName: ROLE_TYPE_USER
     * @fieldType: String
     * @Description:  权限类型为普通商户
     */
    public static final String ROLE_TYPE_USER = "1";


    /**
     * 秘钥配置
     */
    public static final Integer SECRET_KEY = 2;
    /**
     * 秘钥显示
     */
    public static final String GET_SECRET="**********";
}
