package com.instead.pay.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;


/**
 * @ClassName: BuildReqRandomUtils
 * @Description: TODO
 * @author: qwh
 * @date: 2019年4月15日 下午2:54:29
 */
public class BuildReqRandomUtils {
	/**
	 * 生成统一支付订单号
	 * @return
	 */
	public static String getMicOid() {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");// 设置日期格式
		Random random = new Random();
		return "MO" + df.format(new Date()) + (random.nextInt(1000) + 1000);
	}
	public static String getOutOid() {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");// 设置日期格式
		Random random = new Random();
		return "Oc" + df.format(new Date()) + (random.nextInt(1000) + 1000);
	}
	//获取通过放款单号-商户订单号
	public static String getPassForPay() {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");// 设置日期格式
		Random random = new Random();
		return "PFP_" + df.format(new Date()) + (random.nextInt(1000) + 1000);
	}
	//获取有盾订单号
	public static String getUDCreditId() {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");// 设置日期格式
		Random random = new Random();
		return "UDC_" + df.format(new Date()) + (random.nextInt(1000) + 1000);
	}
	//获取常用订单号
	public static String getCommondId(String head) {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");// 设置日期格式
		Random random = new Random();
		return StringUtils.isEmpty(head)?"COM_":head + df.format(new Date()) + (random.nextInt(1000) + 1000);
	}
	//获取公信宝订单号
	public static String getGXBSequenceNo() {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");// 设置日期格式
		Random random = new Random();
		return "GXB_"+ df.format(new Date()) + (random.nextInt(1000) + 1000);
	}
	/**
	 * 自动扣款的外部订单号
	 * @Title: getAutoRepayId 
	 * @Description: TODO
	 * @return
	 */
	public static String getAutoRepayId() {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");// 设置日期格式
		Random random = new Random();
		return "ARP_" + df.format(new Date()) + (random.nextInt(1000) + 1000);
	}
	/**
	 * 生成兑换码
	 * 
	 * @return
	 */
	public static String getExchangeCode( ) {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");// 设置日期格式
		Random random = new Random();
		return "EC" + df.format(new Date()) +createRandom(false,6) ;
	}
	/**
	 * 创建指定数量的随机字符串
	 * @param length
	 * @return
	 */
	public static String createRandom(boolean numberFlag, int length) {
		String retStr = "";
		String strTable = numberFlag ? "1234567890" : "1234567890abcdefghijkmnpqrstuvwxyz";
		int len = strTable.length();
		boolean bDone = true;
		do {
			retStr = "";
			int count = 0;
			for (int i = 0; i < length; i++) {
				double dblR = Math.random() * len;
				int intR = (int) Math.floor(dblR);
				char c = strTable.charAt(intR);
				if (('0' <= c) && (c <= '9')) {
					count++;
				}
				retStr += strTable.charAt(intR);
			}
			if (count >= 2) {
				bDone = false;
			}
		} while (bDone);
		return retStr;
	}
	
	/**
	 * 随机生成字母
	 * @param num	字母数量
	 * @return
	 */
	public static String getRandomEng(int num){
		StringBuffer sb=new StringBuffer();
        for(int i=0;i<num;i++) {

            int e = (int) Math.round(Math.random() * 26);
            int j = (int) 'A' + e;
            char ch = (char) j;
            sb.append(ch);
        }
        return sb.toString();
	}
	

	/**
	 * 生成字符级userId		规则 父级随机三个字母+后缀+随机数   子级  取父级的三个字母+(后缀数字+1)+随机数
	 * @param buyerId 父级id 可能为空
	 * @return
	 */
	public static String getUserId(String buyerId){
		StringBuffer userId=new StringBuffer();
		if(!StringUtil.isEmpty(buyerId)){	
			String eng=buyerId.substring(0,3);
			String num=buyerId.substring(4, buyerId.indexOf("_"));	//获取‘_’之前的字符
			int Num=Integer.parseInt(num)+1;
			//当前用户以子级身份注册
			userId.append(eng).append(".").append(Num).append("_").append(StringUtil.getRandomCode(10)).toString();
		}else{
			//当前用户以父级身份注册
			userId.append(getRandomEng(3)).append(".").append("1").append("_").append(StringUtil.getRandomCode(10)).toString();
		}
		return userId.toString();
	}
}
