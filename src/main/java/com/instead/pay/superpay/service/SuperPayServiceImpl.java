package com.instead.pay.superpay.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.instead.pay.commercial.model.Commercial;
import com.instead.pay.orderInfo.model.OrderInfo;
import com.instead.pay.util.*;
import com.instead.pay.util.rsa.Md5Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.NameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSONObject;

@Service
@Slf4j
public class SuperPayServiceImpl implements SuperPayService {

	static String appKey ;
//			= "808007661587526222";
//	static String privateKey ;
//		= "690645d78ada7bff9b202e71ff783806";
//	private static final String SYS_PAY_URL = "https://api.hyperlinkpay.com/api/v1/open/order";
//	private static final String USER_PAY_URL = "https://api.hyperlinkpay.com/api/v1/open/remit";
//	static String SYS_PAY_URL = "";
//	static String USER_PAY_URL = "";

	@Autowired
	private static CacheInfo cacheInfo;

	@Autowired
	public SuperPayServiceImpl(CacheInfo cacheInfo) throws Exception {
		SuperPayServiceImpl.cacheInfo = cacheInfo;
//		appKey = StringUtil.doNullStr(cacheInfo.getCommonToKey("superpay.properties+appKey"));
//		privateKey = StringUtil.doNullStr(cacheInfo.getCommonToKey("superpay.properties+privateKey"));
//		SYS_PAY_URL = StringUtil.doNullStr(cacheInfo.getCommonToKey("superpay.properties+systemPayUrl"));
//		USER_PAY_URL = StringUtil.doNullStr(cacheInfo.getCommonToKey("superpay.properties+userPayUrl"));
	}


	@Override
	public String userPay_form(Commercial commercial , String payType, String money, String outTradeNo) throws Exception {

		String temp = "";
		String callBackUrlPre = "";
		String notifyUrl = "";
		try {
			callBackUrlPre = cacheInfo.getCommonToKey("system.properties+web.base.url");
			notifyUrl = callBackUrlPre + "/orderInfo/callbackForOrder";
			temp = AmountUtils.changeF2Y(money);
		} catch (Exception e) {
			log.error("金额异常：{}",e);
			return "金额格式错误";
		}
		String timestamp = getTimestamp()+"";
		String noncestr = StringUtil.getUUID();
		String sign = Md5Utils.md5(new StringBuffer().append("MD5").append(StringUtil.doNullStr(cacheInfo.getCommonToKey("superpay.properties+privateKey"))).append(timestamp)
				.append(noncestr).append(notifyUrl).toString(), "UTF-8");
		log.info("四方请求参数：callBackUrlPre：{},notifyUrl：{},temp:{},sign:{}",callBackUrlPre,notifyUrl,temp,sign);
		NameValuePair[] data = {
					new NameValuePair("app_key", StringUtil.doNullStr(cacheInfo.getCommonToKey("superpay.properties+appKey"))),
					new NameValuePair("out_order_no", outTradeNo),
					new NameValuePair("receive_url", notifyUrl),
					new NameValuePair("dissent_receive_url", notifyUrl),
					new NameValuePair("pay_amount", temp),
					new NameValuePair("remit_name", commercial.getCommercialName()),
					new NameValuePair("remit_phone", commercial.getCommercialIphone()),
					new NameValuePair("remit_type", payType),
					new NameValuePair("timestamp", timestamp),
					new NameValuePair("noncestr", noncestr),
					new NameValuePair("sign", sign)
            };
		String respone = null;
		try {
			respone = HttpUtil.syncPostForm(StringUtil.doNullStr(cacheInfo.getCommonToKey("superpay.properties+userPayUrl")),data);
			log.info("四方响应结果：{}",respone);
		}catch (Exception e) {
			log.error("{}",e);
		}
		return respone;
	}


	/**
	 *
	 * @param outTradeNo 外部订单号
	 * @return
	 * @throws Exception
	 */
	@Override
	public String userPayNew(String outTradeNo,OrderInfo orderInfo) throws Exception {
		String callBackUrlPre = cacheInfo.getCommonToKey("system.properties+web.base.url");
		String notifyUrl = callBackUrlPre + "/orderInfo/callbackForOrder";
		Map<String,String> params = new HashMap<>();
		params.put("app_id", StringUtil.doNullStr(cacheInfo.getCommonToKey("superpay.properties+app_id")));
		params.put("mer_order_no", outTradeNo);
		params.put("return_url", notifyUrl);
		params.put("notify_url", notifyUrl);
		try {
			params.put("money", AmountUtils.changeF2Y(String.valueOf(orderInfo.getOperatorMoney())));
		} catch (Exception e) {
			e.printStackTrace();
			return "金额格式错误";
		}
		params.put("pay_info", orderInfo.getRemark());//还款人姓名
		params.put("pay_type", 4+"");
		params.put("nonce_str", StringUtil.getUUID());
		String sign = SignVerify.getSign(params);
		params.put("sign",sign);
		String respone = null;
		try {
			String url = StringUtil.doNullStr(cacheInfo.getCommonToKey("superpay.properties+userPayUrl"));
			respone = HttpUtil.syncPostFormNew(url, params);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return respone;
	}


	@Override
	public String systemPayNew(OrderInfo orderInfo,String outTradeNo) throws Exception {
		String callBackUrlPre = cacheInfo.getCommonToKey("system.properties+web.base.url");
		String notifyUrl = callBackUrlPre + "/orderInfo/callbackForOrder";

		Map<String,String> params = new HashMap<>();
		params.put("app_id", StringUtil.doNullStr(cacheInfo.getCommonToKey("superpay.properties+app_id")));
		params.put("mer_order_no", outTradeNo);
		params.put("return_url", notifyUrl);
		params.put("notify_url", notifyUrl);
		try {
			params.put("money", AmountUtils.changeF2Y(String.valueOf(orderInfo.getOperatorMoney())));
		} catch (Exception e) {
			e.printStackTrace();
			return "金额格式错误";
		}
		params.put("account_name", orderInfo.getBankUserName());//账户姓名
//		params.put("bank_account",orderInfo.getBankAccount());
		params.put("bank_name",orderInfo.getBankName());
		if(!StringUtil.isEmpty(orderInfo.getRemark())){
			params.put("note",orderInfo.getRemark());
		}
/*		if(orderInfo.getPayType() == 1){ //支付宝
			params.put("pay_type", 3+"");
			params.put("alipay_account",orderInfo.getBankAccount());
		}else if(orderInfo.getPayType() == 2){*/
            params.put("pay_type", 4+"");
            params.put("bank_account",orderInfo.getBankAccount());
//		}
		params.put("nonce_str", StringUtil.getUUID());
		String sign = SignVerify.getSign(params);
		params.put("sign",sign);
		JSONArray arr = new JSONArray();
		arr.add(params);
		log.info("代付请求参数：{}",arr.toString());
		String respone = null;
		try {
			respone = HttpUtil.syncPostFormNew(StringUtil.doNullStr(cacheInfo.getCommonToKey("superpay.properties+systemPayUrl")), params);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return respone;
	}

	@Override
	public String queryOrder(String outTradeNo) {
		String 	respone = "";
		try {
			Map<String,String> params = new HashMap<>();
			params.put("app_id", StringUtil.doNullStr(cacheInfo.getCommonToKey("superpay.properties+app_id")));
			params.put("nonce_str", StringUtil.getUUID());
			params.put("mer_order_no", outTradeNo);
			String sign = SignVerify.getSign(params);
			params.put("sign",sign);
			respone = HttpUtil.syncPostFormNew(StringUtil.doNullStr(cacheInfo.getCommonToKey("superpay.properties+systemPayUrl")), params);
		} catch (Exception e) {
			log.error("查询失败：{}",e);
		}
		return respone;
	}


	/**
	 *
	 * @param payType    还款方式 1. 支付宝 2. 银行卡
	 * @param money      CNY
	 * @param outTradeNo 外部订单号
	 * @return
	 * @throws Exception
	 */
	@Override
	public String userPay(String payType, String money, String outTradeNo,OrderInfo orderInfo) throws Exception {
		String callBackUrlPre = cacheInfo.getCommonToKey("system.properties+web.base.url");
		String notifyUrl = callBackUrlPre + "/orderInfo/callbackForOrder";
		JSONObject params = new JSONObject();
		params.put("app_key", StringUtil.doNullStr(cacheInfo.getCommonToKey("superpay.properties+appKey")));
		params.put("out_order_no", outTradeNo);
		params.put("receive_url", notifyUrl);
		params.put("dissent_receive_url", notifyUrl);
		try {
			params.put("pay_amount", AmountUtils.changeF2Y(money));
		} catch (Exception e) {
			e.printStackTrace();
			return "金额格式错误";
		}
		params.put("remit_name", orderInfo.getRemark());//还款人姓名
		params.put("remit_phone", orderInfo.getBankPhone());
		params.put("remit_type", payType);
		params.put("timestamp", getTimestamp());
		params.put("noncestr", StringUtil.getUUID());

		params.put("sign",
				Md5Utils.md5(new StringBuffer().append("MD5").append(StringUtil.doNullStr(cacheInfo.getCommonToKey("superpay.properties+privateKey"))).append(params.get("timestamp"))
						.append(params.get("noncestr")).append(params.get("receive_url")).toString(), "UTF-8"));
		String respone = null;
		try {
			String url = StringUtil.doNullStr(cacheInfo.getCommonToKey("superpay.properties+userPayUrl"));
			respone = HttpUtil.syncPostJson(url, params.toJSONString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return respone;
	}

	@Override
	public String systemPay(Commercial commercial, String money, String outTradeNo) throws Exception {
		String callBackUrlPre = cacheInfo.getCommonToKey("system.properties+web.base.url");
		String notifyUrl = callBackUrlPre + "/orderInfo/callbackForOrder";
		JSONObject params = new JSONObject();
		params.put("app_key",StringUtil.doNullStr(cacheInfo.getCommonToKey("superpay.properties+appKey")));
		params.put("out_order_no", outTradeNo);
		params.put("receive_url", notifyUrl);
		params.put("dissent_receive_url", notifyUrl);
		try {
			params.put("pay_amount", AmountUtils.changeF2Y(money));
		} catch (Exception e) {
			e.printStackTrace();
			return "金额格式错误";
		}
		params.put("payee_name", commercial.getCommercialName());
//		params.put("payee_account", mUserInfo.getBankCard());
//		params.put("bank_name", mUserInfo.getBankName());
//		params.put("bank_address",StringUtil.isEmpty( mUserInfo.getBankType())?"无":mUserInfo.getBankType());
		params.put("timestamp", getTimestamp());
		params.put("noncestr", StringUtil.getUUID());
		params.put("payee_phone", commercial.getCommercialIphone());

		params.put("sign",
				Md5Utils.md5(new StringBuffer().append("MD5").append(StringUtil.doNullStr(cacheInfo.getCommonToKey("superpay.properties+privateKey"))).append(params.get("timestamp"))
						.append(params.get("noncestr")).append(params.get("receive_url")).toString(), "UTF-8"));

		String respone = null;
		try {
			respone = HttpUtil.syncPostJson(StringUtil.doNullStr(cacheInfo.getCommonToKey("superpay.properties+systemPayUrl")), params.toJSONString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return respone;
	}

	@Override
	public String systemPay(OrderInfo orderInfo, String money, String outTradeNo ) throws Exception {
		String callBackUrlPre = cacheInfo.getCommonToKey("system.properties+web.base.url");
		String notifyUrl = callBackUrlPre + "/orderInfo/callbackForOrder";
		JSONObject params = new JSONObject();
		params.put("app_key",StringUtil.doNullStr(cacheInfo.getCommonToKey("superpay.properties+appKey")));
		params.put("out_order_no", outTradeNo);
		params.put("receive_url", notifyUrl);
		params.put("dissent_receive_url", notifyUrl);
		try {
			params.put("pay_amount", AmountUtils.changeF2Y(money));
		} catch (Exception e) {
			e.printStackTrace();
			return "金额格式错误";
		}
		params.put("payee_name", orderInfo.getBankUserName());  //收款人姓名
		params.put("payee_account", orderInfo.getBankAccount());
		params.put("bank_name", orderInfo.getBankName());
		params.put("bank_address",StringUtil.isEmpty( orderInfo.getBankAddress())?"无":orderInfo.getBankAddress());
		params.put("timestamp", getTimestamp());
		params.put("noncestr", StringUtil.getUUID());
		params.put("payee_phone", orderInfo.getBankPhone());

		params.put("sign",
				Md5Utils.md5(new StringBuffer().append("MD5").append(StringUtil.doNullStr(cacheInfo.getCommonToKey("superpay.properties+privateKey"))).append(params.get("timestamp"))
						.append(params.get("noncestr")).append(params.get("receive_url")).toString(), "UTF-8"));

		String respone = null;
		try {
			respone = HttpUtil.syncPostJson(StringUtil.doNullStr(cacheInfo.getCommonToKey("superpay.properties+systemPayUrl")), params.toJSONString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return respone;
	}

	private static long getTimestamp() {
		return System.currentTimeMillis() / 1000;
	}
}
