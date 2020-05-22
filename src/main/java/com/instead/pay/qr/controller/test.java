package com.instead.pay.qr.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.instead.pay.util.*;
import com.instead.pay.util.rsa.Md5Utils;

public class test {

	private static final String SYS_PAY_URL = "https://api.hyperlinkpay.com/api/v1/open/order";
	private static final String USER_PAY_URL = "https://api.hyperlinkpay.com/api/v1/open/remit";
	private static String app_key = "808007661587526222";
	private static String privateKey = "690645d78ada7bff9b202e71ff783806";

	public static void main(String[] args) {

		for(int i = 0 ; i < 5 ; i++){
			if(i == 2){
				break;
			}
			System.out.println("i========"+i);
		}







//        userPay(BuildReqRandomUtils.getOutOid(),"10000");
//out_order_no=20200331195347436&serial_no=202003311953511702&status=success&sign_type=MD5&pay_amount=200.00&coin_count=28.5714&sign=907b896ea65f6d4773186d5d59ba31d0
	}

	private static String sysPay(String outTradeNo, String amount) {


		JSONObject params = new JSONObject();
		params.put("app_key", app_key);
		params.put("out_order_no", outTradeNo);
		params.put("receive_url", "http://www.jinanfu.com.cn/ledi/bPayController/superPayCallBack");
		params.put("dissent_receive_url", "http://www.jinanfu.com.cn/ledi/bPayController/superPayCallBack");
		try {
			params.put("pay_amount", AmountUtils.changeF2Y(amount));
		} catch (Exception e) {
			e.printStackTrace();
			return "金额格式错误";
		}
		params.put("payee_name", "张付");
		params.put("payee_account", "6217001820012112326");
		params.put("bank_name", "中国建设银行");
		params.put("bank_address", "无");
		params.put("timestamp", getTimestamp());
		params.put("noncestr", StringUtil.getUUID());
		params.put("payee_phone", "17584847552");

		params.put("sign",
				Md5Utils.md5(new StringBuffer()
						.append("MD5")
						.append(privateKey)
						.append(params.get("timestamp"))
						.append(params.get("noncestr"))
						.append(params.get("receive_url"))
						.toString(), "UTF-8"));

		String respone = null;
		try {
			respone = HttpUtil.syncPostJson(SYS_PAY_URL, params.toJSONString());
			JSONObject resJSON = JSONObject.parseObject(respone);
			System.out.println("返回结果："+resJSON.toJSONString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return respone;
	}


	private static void getBackInfo(){
		Map<String,String> jsonObject = new HashMap<>();
		jsonObject.put("orderId","muv_in_690115");
		jsonObject.put("outId","MO202004282032241750");
		jsonObject.put("operatorMoney","251250");
		jsonObject.put("orderStatus","1");
		jsonObject.put("createTime","Tue Apr 28 20:32:08 GMT 2020");
		jsonObject.put("confirmTime","Tue Apr 28 20:39:27 GMT 2020");
		jsonObject.put("time",String.valueOf(System.currentTimeMillis()));
		jsonObject.put("appKey","WKJegkMAWodW");
		String sign = SignVerify.getMD5Encrypt(jsonObject);
		jsonObject.put("sign",sign);
		jsonObject.put(ResultKey.KEY_SUCC, "true");
		jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.SUCCESS_CODE);
		jsonObject.put(ResultKey.KEY_MSG, "确认成功");
		String data= JSONObject.toJSONString(jsonObject);
		System.out.println(data);
	}
	
	

	private static String userPay(String outTradeNo, String amount) {

		JSONObject params = new JSONObject();
		params.put("app_key", app_key);
		params.put("out_order_no", outTradeNo);
		params.put("receive_url", "http://www.jinanfu.com.cn/ledi/bPayController/superPayCallBack");
		params.put("dissent_receive_url", "http://www.jinanfu.com.cn/ledi/bPayController/superPayCallBack");
		try {
			params.put("pay_amount", AmountUtils.changeF2Y(amount));
		} catch (Exception e) {
			e.printStackTrace();
			return "金额格式错误";
		}
		params.put("remit_name", "张三");
		params.put("remit_phone", "13148755484");
		params.put("remit_type", "2");
		params.put("timestamp", getTimestamp());
		params.put("noncestr", StringUtil.getUUID());

		params.put("sign",
				Md5Utils.md5(new StringBuffer().append("MD5").append(privateKey).append(params.get("timestamp"))
						.append(params.get("noncestr")).append(params.get("receive_url")).toString(), "UTF-8"));

		String respone = null;
		try {
			respone = HttpUtil.syncPostJson(USER_PAY_URL, params.toJSONString());
			System.out.println("返回数据："+JSONObject.parse(respone));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return respone;
	}


	private static long getTimestamp() {
		return System.currentTimeMillis() / 1000;
	}

}
