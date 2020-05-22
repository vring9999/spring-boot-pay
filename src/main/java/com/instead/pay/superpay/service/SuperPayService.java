package com.instead.pay.superpay.service;


import com.instead.pay.commercial.model.Commercial;
import com.instead.pay.orderInfo.model.OrderInfo;

public interface SuperPayService {
	/**
	 * 
	 * @param payType    还款方式 1. 支付宝 2. 银行卡
	 * @param money      CNY
	 * @param outTradeNo 外部订单号
	 * @return
	 */
	String userPay(String payType, String money, String outTradeNo,OrderInfo orderInfo) throws Exception;

	String systemPay(Commercial commercial, String money, String outTradeNo) throws Exception;

	String systemPay(OrderInfo orderInfo, String money, String outTradeNo) throws Exception;

	String userPay_form(Commercial commercial , String payType, String money, String outTradeNo) throws Exception;

	String userPayNew(String outTradeNo,OrderInfo orderInfo) throws Exception;

	String systemPayNew(OrderInfo orderInfo,String outTradeNo ) throws Exception;

	String queryOrder(String outTradeNo);

}
