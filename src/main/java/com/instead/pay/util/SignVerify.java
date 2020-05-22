package com.instead.pay.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.instead.pay.exception.TokenException;
import com.instead.pay.util.rsa.Md5Utils;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;

@Component
@Slf4j
public class SignVerify {

    @Autowired
    private static CacheInfo cacheInfo;

    /*static String appKey = "808007661587526222";
    static String privateKey = "690645d78ada7bff9b202e71ff783806";*/

    @Autowired
    public SignVerify(CacheInfo cacheInfo) throws Exception {
        SignVerify.cacheInfo = cacheInfo;
//        appKey = com.instead.pay.util.StringUtil.doNullStr(cacheInfo.getCommonToKey("superpay.properties+appKey"));
//        privateKey = com.instead.pay.util.StringUtil.doNullStr(cacheInfo.getCommonToKey("superpay.properties+privateKey"));
    }

/*    public static JSONObject isSign(Map<String, Object> map) {
        JSONObject object = isSign(map);

        if (object.getIntValue("code") == 0) {
            return function.apply(object);
        }
        return object;
    }*/

    public static JSONObject isSign(Map<String, Object> map, Function<JSONObject, JSONObject> function) {
        //返回参数
        JSONObject jsonObject = new JSONObject();
        String sign = "";
        String appKey="";
        if (map.get("time") != null) {
            Long ts = System.currentTimeMillis();
            Long time=Math.abs(ts - Long.parseLong(map.get("time").toString()));
            if (time > 100000) {
                jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.TIMEOUT);
                jsonObject.put(ResultKey.KEY_MSG, "时间超过10秒，签名时效!");
                log.error("时间超过10秒，签名时效！");
                log.error("当前时间：{}", ts);
                log.error("请求时间：{}", Long.parseLong(map.get("time").toString()));
                log.error("验证时间相差：{}", time);
                return jsonObject;
            } else {
                //map.put("key",DeployCode.getInstance().getSecretKey());
                List<String> keys = new ArrayList<>(map.keySet());
                sign = map.get("sign").toString();
                appKey=map.get("appKey").toString();
                keys.remove("sign");
                keys.remove("appKey");
                Collections.sort(keys);
                StringBuilder sb = new StringBuilder();
                keys.forEach(key-> sb.append(key).append('=').append(map.get(key)).append('&'));
                sb.append("appKey").append("=").append(appKey);
                //sb.append("key="+DeployCode.getInstance().getSecretKey());
                log.info("isSign use param  isSign:{}",sb.toString());
                try{
                    String MD5 = DigestUtils.md5DigestAsHex(sb.toString().getBytes());
                    log.info("sign:{},MD5:{}"  ,sign,MD5);
                    if (MD5.equals(sign)) {
                        log.info("校验成功！校验时间:{}",System.currentTimeMillis());
                        jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.SUCCESS_CODE);
                        return function.apply(jsonObject);
                    } else {
                        jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.MD5_DEFEATED);
                        jsonObject.put(ResultKey.KEY_MSG, "MD5校验失败！");
                        log.error("校验失败！");
                    }
                }catch (Exception e){
                          log.error("{}",e);
                }
            }
        } else {
            jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.TIME_IS_NULL);
            jsonObject.put(ResultKey.KEY_MSG, "时间戳为空！");
            log.error("时间戳为空！");
        }
        return jsonObject;
    }

    public static boolean isSign1(Map<String, Object> map) {
        log.info("初始Map数据：{}",map);
        //返回参数
        String sign = "";
        String appKey="";
        if (map.get("time") != null) {
            Long ts = System.currentTimeMillis();
            Long time=Math.abs(ts - Long.parseLong(map.get("time").toString()));
            /*if (time > 100000) {
                log.error("时间超过10秒，签名时效！");
                log.error("当前时间：{}", ts);
                log.error("请求时间：{}", Long.parseLong(map.get("time").toString()));
                log.error("验证时间相差：{}", time);
                return false;
            } else {*/
                //map.put("key",DeployCode.getInstance().getSecretKey());
                List<String> keys = new ArrayList<>(map.keySet());
                sign = map.get("sign").toString();
                appKey=map.get("appKey").toString();
                keys.remove("sign");
                keys.remove("appKey");
                Collections.sort(keys);
                StringBuilder sb = new StringBuilder();
                keys.forEach(key-> sb.append(key).append('=').append(map.get(key)).append('&'));
                sb.append("appKey").append("=").append(appKey);
                //sb.append("key="+DeployCode.getInstance().getSecretKey());
                log.info("isSign use param  isSign:{}",sb.toString());
                try{
                    String MD5 = DigestUtils.md5DigestAsHex(sb.toString().getBytes());
                    log.info("sign:{},MD5:{}"  ,sign,MD5);
                    if (MD5.equals(sign)) {
                        log.info("校验成功！校验时间:{}",System.currentTimeMillis());
                        return true;
                    } else {
                        log.error("校验失败！");
                        return false;
                    }
                }catch (Exception e){
                    log.error("{}",e);
                    return false;
                }
            //}
        } else {
            log.error("时间戳为空！");
            return false;
        }
    }


    public static boolean checkSign(Map<String, Object> map) throws Exception {
//        Map<String, Object> map = Request2Map.req2Map(request);
        //进行校验的签名
        String check_sign = (String) map.get("sign");
        log.info("进行校验的签名：{}",check_sign);
        if(StringUtil.isEmpty(check_sign)){
            log.error("签名为空");
            return false;
        }
        String sign_type = (String) map.get("sign_type");
        String out_order_no = (String) map.get("out_order_no");
        String serial_no = (String) map.get("serial_no");
        String status = (String) map.get("status");
        String pay_amount = (String) map.get("pay_amount");
        log.info("affirmOrder use params-->sign_type:{},serial_no:{},out_order_no:{},status:{},pay_amount:{}",
                                           sign_type,serial_no,out_order_no,status,pay_amount);
        //sign  :   MD5('MD5'+私钥(private_key)+签名类型+外部订单号+流水号+交易状态)
        String sign = Md5Utils.md5(new StringBuffer().append("MD5").append(StringUtil.doNullStr(cacheInfo.getCommonToKey("superpay.properties+privateKey"))).append(sign_type)
                .append(out_order_no).append(serial_no).append(status).toString(), "UTF-8");
        log.info("our sign:{}",sign);
        if(check_sign.equals(sign)){
            return true;
        }else{
            log.error("签名校验失败");
            return false;
        }
    }


    public static void main(String[] args) throws Exception {
        Map<String,String> params = new HashMap<>();
        params.put("app_id", "t2fdff");
        params.put("mer_order_no", "e2fdff");
        params.put("return_url", "12fdff");
        params.put("notify_url", "h2fdff");
        try {
            params.put("money", AmountUtils.changeF2Y("5988"));
        } catch (Exception e) {
            e.printStackTrace();
//            return "金额格式错误";
        }
        params.put("pay_info", "字符");//还款人姓名
        params.put("pay_type", 2+"");
        params.put("appKey", "809173598h");
        params.put("nonce_str", StringUtil.getUUID());
        String sign = getSign(params);
        System.out.println(sign);
    }

    public static String getSign(Map<String,String> params){
        String sign = "";
        try{
            String Key = cacheInfo.getCommonToKey("superpay.properties+privateKey");
            List<String> keys = new ArrayList<>(params.keySet());
//        char[] myCharArray = mystr.ToCharArray(); //转为字符数组
//        Array.Sort(myCharArray); //升序排序
//        string new_str = new String(myCharArray); //转为字符串
            Collections.sort(keys);
            StringBuilder sb = new StringBuilder();
            keys.forEach(key-> sb.append(key).append('=').append(params.get(key)).append('&'));
            sb.append("key").append("=").append(Key);
            System.out.println(sb.toString());
            String str = Md5Utils.md5(sb.toString(), "UTF-8");
            sign = str.toUpperCase();
        }catch (Exception e){
            log.error("签名失败：{}",e);
        }
        return sign;
    }

    public static boolean checkSignNew(Map<String, String> map){
        //进行校验的签名
        String check_sign = map.get("sign");
        map.remove("sign");
        log.info("进行校验的签名：{}",check_sign);
        if(StringUtil.isEmpty(check_sign)){
            log.error("签名为空");
            return false;
        }
        StringBuffer sf = new StringBuffer();
        for(Map.Entry<String, String> key : map.entrySet()){
            sf.append(key+"="+map.get(key)+",");
        }
        log.info("请求参数：{}",sf);
        String sign = getSign(map);
        log.info("our sign:{}",sign);
        if(check_sign.equals(sign)){
            return true;
        }else{
            log.error("签名校验失败");
            return false;
        }
    }

    /**
     * MD5签名加密(map含time字段)
     * @param map
     * @return
     */
    public static String getMD5Encrypt(Map<String,String> map){
        String sign="";
        String appKey="";
        List<String> keys = new ArrayList<>(map.keySet());
        Collections.sort(keys);
        appKey=map.get("appKey");
        keys.remove("appKey");
        StringBuilder sb = new StringBuilder();
        keys.forEach(key-> sb.append(key).append('=').append(map.get(key)).append('&'));
        sb.append("appKey").append("=").append(appKey);
        sign = DigestUtils.md5DigestAsHex(sb.toString().getBytes());
        return sign;
    }
}
