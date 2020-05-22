package com.instead.pay.util;

import com.instead.pay.util.gsonadapter.JsonDateValueProcessor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.beans.IntrospectionException;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Administrator
 * @ClassName: StringUtil
 * @Description: 字符串处理类
 * @date 2015-12-24 下午2:15:03
 */
@Component
@Slf4j
public class StringUtil {


    private static String[] chars = new String[]{"a", "b", "c", "d", "e", "f",
            "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
            "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z"};
    private static char[] forty = new char[]{'q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p', 'l', 'k', 'j', 'h', 'g',
            'f', 'd', 's', 'a', 'z', 'x', 'c', 'v', 'b', 'n', 'm', 'Q', 'W', 'E', 'R', 'T', 'Y', 'U', 'I', 'O', 'P',
			'L', 'K', 'J', 'H', 'G', 'F','D', 'S', 'A', 'Z', 'X', 'C', 'V', 'B', 'N', 'M'};


    /**
     * @param @param string
     * @return boolean
     * @throws
     * @Title: isEmpty
     * @Description: 判断字符串是否为空
     */
    public static boolean isEmpty(String string) {
        boolean result = false;
        if (string == null || "".equals(string.trim())) {
            result = true;
        }
        return result;
    }

    /**
     * 验证Object是否为空,object instanceof String
     *
     * @param object
     * @return
     */
    public static boolean isEmpty(Object object) {
        boolean result = false;
        if (object == null || "".equals(object.toString().trim())) {
            result = true;
        }
        return result;
    }

    /**
     * @return String 返回类型
     * @throws
     * @Title: getUuid
     * @Description: 获取UUID 带-标识
     */
    public static String getUuid() {
        UUID uuid = UUID.randomUUID();
        String str = uuid.toString();
        return str;
    }

    /**
     * @return
     * @Title: getUUID
     * @Description: 获取UUID 去掉-标识
     */
    public static String getUUID() {
        String str = getUuid();
        str = str.replace("-", "");
        return str;
    }

    /**
     * @return String 短UUID
     * @Title: getShortUuid
     * @Description: 获取短UUID
     */
    public static String getShortUuid() {
        StringBuffer shortBuffer = new StringBuffer();
        String uuid = getUUID();
        for (int i = 0; i < 8; i++) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, 16);
            shortBuffer.append(chars[x % 0x3E]);
        }
        return shortBuffer.toString();
    }

    /**
     * 获取随机数
     *
     * @param len 随机数长度
     * @return 返回len长度的随机数
     */
    public static StringBuffer getRandomCode(int len) {
        StringBuffer buffer = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < len; i++) {
            buffer.append(random.nextInt(10));
        }
        return buffer;
    }


    private static boolean isBack(String backNum){
        // 创建HttpClient实例
        String url = "https://ccdcapi.alipay.com/validateAndCacheCardInfo.json?_input_charset=utf-8&cardNo=";
        url+=backNum;
        url+="&cardBinCheck=true";
        StringBuilder sb = new StringBuilder();
        try {
            URL urlObject = new URL(url);
            URLConnection uc = urlObject.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
            String inputLine = null;
            while ( (inputLine = in.readLine()) != null) {
                sb.append(inputLine);
            }
            in.close();
            com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(sb.toString());
            if(jsonObject.containsKey("validated")){
                if(Boolean.parseBoolean(jsonObject.get("validated").toString())){
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public static void main(String[] args) {
        System.out.println(getRandomCode(8));
    }

    /**
     * @param @param  val
     * @param @param  length
     * @param @return 设定文件
     * @return boolean 返回类型
     * @Title: isCheckFiledLen
     * @Description: 校验字段长度
     */
    public static boolean isCheckFiledLen(String val, int length) {
        boolean result = false;
        int valLen = val.length();
        if (valLen > length) {
            result = true;
        }
        return result;
    }

    /**
     * 将字符串为"null"或空对象转化为字符串""
     *
     * @param obj
     */
    public static String doNullStr(Object obj) {
        String str = "";
        if (obj != null) {
            str = String.valueOf(obj);
            if (str.equals("null")) {
                str = "";
            }
        }
        return str.trim();
    }

    /**
     * 将字符串中的中文转化为拼音,其他字符不变
     *
     * @param inputStr
     * @return
     * @throws BadHanyuPinyinOutputFormatCombination
     */
    public static String getPingYin(String inputStr) throws BadHanyuPinyinOutputFormatCombination {
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);
        char[] input = inputStr.trim().toCharArray();
        String output = "";
        for (int i = 0; i < input.length; i++) {
            if (Character.toString(input[i]).matches("[\\u4E00-\\u9FA5]+")) {
                String[] temp = PinyinHelper.toHanyuPinyinStringArray(input[i], format);
                output += temp[0];
            } else
                output += Character.toString(input[i]);
        }
        return output;
    }

    /**
     * 格式化查询参数
     *
     * @param filter
     * @return
     */
    public static Map<String, Object> formatParam(String filter) {
        filter = InputInjectFilter.decodeInputString(filter);// HTML 反转义
        Map<String, Object> map = new HashMap<String, Object>();
        if (filter != null) {
            Pattern p = Pattern.compile("(\\w*)=([^&]*)");
            Matcher m = p.matcher(filter);
            while (m.find()) {
                if (!isEmpty(m.group(1)) && !isEmpty(m.group(2))) {
                    map.put(m.group(1), m.group(2));
                }
            }
        }
        return map;
    }

    public static Map<String, String> formatParamString(String filter) {
        filter = InputInjectFilter.decodeInputString(filter);// HTML 反转义
        Map<String, String> map = new HashMap<String, String>();
        if (filter != null) {
            Pattern p = Pattern.compile("(\\w*)=([^&]*)");
            Matcher m = p.matcher(filter);
            while (m.find()) {
                if (!isEmpty(m.group(1)) && !isEmpty(m.group(2))) {
                    map.put(m.group(1), m.group(2));
                }
            }
        }
        return map;
    }

    /**
     * 2016-11-30
     *
     * @param @param  str
     * @param @return 参数说明
     * @return boolean 返回类型
     * @Title: isNumeric
     * @Description: 判断字符串是否是数字组成
     */
    public static boolean isNumeric(String str) {
        for (int i = str.length(); --i >= 0; ) {
            int chr = str.charAt(i);
            if (chr < 48 || chr > 57)
                return false;
        }
        return true;
    }

    /**
     * @param str
     * @return
     * @Title: underlineToCamel
     * @Description: 下划线格式字符转为驼峰式字符规则
     */
    public static String underlineToCamel(String str) {
        if (isEmpty(str)) {
            return "";
        }
        int len = str.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = str.charAt(i);
            if ('_' == c) {
                if (++i < len) {
                    sb.append(Character.toUpperCase(str.charAt(i)));
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * @param str
     * @return
     * @Title: camelToUnderline
     * @Description: 驼峰式字符转为下划线格式字符规则
     */
    public static String camelToUnderline(String str) {
        if (isEmpty(str)) {
            return "";
        }
        int len = str.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = str.charAt(i);
            if (Character.isUpperCase(c)) {
                sb.append("_");
                sb.append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * @param str
     * @return
     * @Title: camel2under
     * @Description: 驼峰式字符转为下划线格式字符规则
     */
    public static String camel2under(String str) {
        String separator = "_";
        str = str.replaceAll("([a-z])([A-Z])", "$1" + separator + "$2").toLowerCase();
        return str;
    }

    /**
     * @param @param  str
     * @param @param  code
     * @param @throws UnsupportedEncodingException 参数说明
     * @return String 返回类型
     * @Title: encode
     * @Description: 根据指定编码对字符串进行转码
     */
    public static String encode(String str, String code) throws UnsupportedEncodingException {
        if (isEmpty(str)) {
            return "";
        }
        return java.net.URLEncoder.encode(str, code);
    }

    /**
     * @param @param  str
     * @param @return
     * @param @throws UnsupportedEncodingException 参数说明
     * @return String 返回类型
     * @Title: encode
     * @Description: 对字符转进行UTF-8转码
     */
    public static String encode(String str) throws UnsupportedEncodingException {
        if (isEmpty(str)) {
            return "";
        }
        return encode(str, "UTF-8");
    }

    /**
     * @param @param  str
     * @param @param  code
     * @param @return
     * @param @throws UnsupportedEncodingException 参数说明
     * @return String 返回类型
     * @Title: decode
     * @Description: 根据指定编码对字符串进行解码
     */
    public static String decode(String str, String code) throws UnsupportedEncodingException {
        if (isEmpty(str)) {
            return "";
        }
//		if (str.contains("+"))
//			str = str.replace("+", "%2B");
        return java.net.URLDecoder.decode(str, code);
    }

    /**
     * @param @param  str
     * @param @return
     * @param @throws UnsupportedEncodingException 参数说明
     * @return String 返回类型
     * @Title: decode
     * @Description: 对字符转进行UTF-8解码
     */
    public static String decode(String str) throws UnsupportedEncodingException {
        if (isEmpty(str)) {
            return "";
        }
        return decode(str, "UTF-8");
    }

    /**
     * 将磁盘的单位为byte转为便于阅读的单位
     * 1kb = 1024(b)
     * 1M = 1,048,576(b)
     * 1G = 1,073,741,824(b)
     * 1Tb = 1,099,511,627,776(b)
     * 1Pb = 1125899906842624(b)
     *
     * @param size
     * @return
     */
    public static String changeFileSizeToRead(BigDecimal size) {
        String readSize = "";
        if (size.longValue() < 1024) {
            readSize = size + " b";
        } else if (size.longValue() >= 1024 && size.longValue() < 1048576) {
            readSize = size.divide(new BigDecimal(1024)).setScale(1, RoundingMode.HALF_UP) + " Kb";
        } else if (size.longValue() >= 1048576 && size.longValue() < 1073741824) {
            readSize = size.divide(new BigDecimal(1024 * 1024)).setScale(1, RoundingMode.HALF_UP) + " Mb";
        } else if (size.longValue() >= 1073741824 && size.longValue() < 1099511627776l) {
            readSize = size.divide(new BigDecimal(1024 * 1024 * 1024)).setScale(1, RoundingMode.HALF_UP) + " Gb";
        } else if (size.longValue() >= 1099511627776l && size.longValue() < 1125899906842624l) {
            readSize = size.divide(new BigDecimal(1024 * 1024 * 1024 * 1024l)).setScale(1, RoundingMode.HALF_UP) + " Tb";
        }
        return readSize;
    }

    /**
     * 转义"_"
     *
     * @param params
     * @param filterKeys
     */
    public static void filterFormater(Map<String, Object> params, String[] filterKeys) {
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            if (ArrayUtils.contains(filterKeys, entry.getKey())) {
                String value = (String) entry.getValue();
                if (!StringUtil.isEmpty(value)) {
                    value = value.replaceAll("_", "\\\\_");
                }
                params.put(entry.getKey(), value);
            }
        }
    }

    /**
     * 转义"_"
     *
     * @param obj
     * @param filterKeys
     */
    public static Object filterFormater(Object obj, String[] filterKeys) throws Exception {
        Map<String, Object> params = objectToMap(obj);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            if (ArrayUtils.contains(filterKeys, entry.getKey())) {
                String value = (String) entry.getValue();
                if (!StringUtil.isEmpty(value)) {
                    value = value.replaceAll("_", "\\\\_");
                }
                params.put(entry.getKey(), value);
            }
        }
        obj = mapToObject(params, obj.getClass());
        return obj;
    }

    /**
     * Object转Map
     *
     * @param obj
     * @return
     * @throws IntrospectionException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static Map<String, Object> objectToMap(Object obj) throws IntrospectionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        //法一：使用reflect进行转换
        if (obj == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        Field[] declaredFields = obj.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            map.put(field.getName(), field.get(obj));
        }
        return map;

        //法二：使用Introspector进行转换
		/*
		if(obj == null) {
			return null; 
		}
        Map<String, Object> map = new HashMap<String, Object>();   
        BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());    
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();    
        for (PropertyDescriptor property : propertyDescriptors) {    
            String key = property.getName();    
            if (key.compareToIgnoreCase("class") == 0) {   
                continue;  
            }  
            Method getter = property.getReadMethod();  
            Object value = getter!=null ? getter.invoke(obj) : null;  
            map.put(key, value);  
        }    
        return map;  
        */
    }

    /**
     * Map转Object
     *
     * @param map
     * @param beanClass
     * @return
     * @throws Exception
     */
    public static Object mapToObject(Map<String, Object> map, Class<?> beanClass) throws Exception {
        //法一：使用reflect进行转换
        if (map == null) {
            return null;
        }
        Object obj = beanClass.newInstance();
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            int mod = field.getModifiers();
            if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                continue;
            }
            field.setAccessible(true);
            field.set(obj, map.get(field.getName()));
        }
        return obj;

        //法二：使用Introspector进行转换 
		/*
		if(map == null) {
			return null;    
		}
        Object obj = beanClass.newInstance();  
        BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());    
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();    
        for (PropertyDescriptor property : propertyDescriptors) {  
            Method setter = property.getWriteMethod();    
            if (setter != null) {  
                setter.invoke(obj, map.get(property.getName()));   
            }  
        }  
        return obj;
        */
    }

    public static String objectToString(Object object) {
        String str = "";
        try {
            if (object != null) {
                if (object instanceof String) {
                    str = object.toString();
                } else {
                    JsonConfig jsonConfig = new JsonConfig();
                    jsonConfig
                            .setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
                    jsonConfig.registerJsonValueProcessor(Date.class,
                            new JsonDateValueProcessor());
                    if (object.getClass().isArray()) {
                        str = JSONArray.fromObject(object, jsonConfig).toString();
                    } else if (object instanceof List) {
                        str = JSONArray.fromObject(object).toString();
                    } else {
                        str = JSONObject.fromObject(object, jsonConfig).toString();
                    }
                }
            }
        } catch (Exception e) {
            return str;
        }
        return str;
    }

    /**
     * 获取盐值
     *
     * @param length
     * @return
     */
    public static String getSalt(int length) {
        String randomStr = "zxcvbnmlkjhgfdsaqwertyuiopQWERTYUIOPASDFGHJKLZXCVBNM1234567890";
        StringBuffer strBuf = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int random = (int) (Math.round(Math.random() * (62 - 1)) + 1);
            strBuf.append(randomStr.charAt(random));
        }
        return strBuf.toString();
    }

    /**
     * 获取提成金额
     *
     * @param orderMoney 订单金额  分为单位
     * @param radio      提成比例
     * @return
     */
    public static int handMoney(int orderMoney, double radio) { //248900   0.10
        int handMoney;
        double moneyY = (double) orderMoney / 100;  //转换成元为单位
        double moneyH = moneyY * radio;   //计算提成
        BigDecimal bd = new BigDecimal(moneyH);
        moneyH = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();  //保留两位小数
        double moneyA = moneyH * 100;   //转换成分为单位
        handMoney = (int) moneyA;
        return handMoney;
    }

    /*
     * @title getOrderId
     * @description 获取我方订单id
     * @author vring
     * @param: 支付类型
     * @throws
     */
    public static String getOrderId(int applicationType) {
        boolean[] flag = new boolean[chars.length];
        char[] results = new char[3];
        for (int i = 0; i < 3; i++) {
            int index = 0;
            do {
                index = (int) (Math.random() * 26);
            } while (flag[index]);
            results[i] = forty[index];
            flag[index] = true;
        }
        String str = new String(results);
        int number = (int) ((Math.random() * 9 + 1) * 100000);
        if (applicationType == UsedCode.APPLICATION_TYPE_IN)
            str = str + "_in_";
        if (applicationType == UsedCode.APPLICATION_TYPE_OUT)
            str = str + "_out_";
        if (applicationType == UsedCode.APPLICATION_TYPE_WIT)
            str = str + "_wit_";
        if (applicationType == UsedCode.ORDER_STATUS_BO)
            str = str + "_cz_";
        String orderId = str + number;
        return orderId;
    }

    /*
     * @title getOrderId
     * @description 获取商户号
     * @author vring
     * @param: size
     * @throws
     */
    public static String getCommercialNumber(String type, int size) {
        String temp = "";
        String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()).toString();
        if (type.equals(UsedCode.ROLE_TYPE_ADMIN)) {//系统
            temp = UsedCode.SYSTEM_NUMBER;
        } else if(type.equals(UsedCode.ROLE_TYPE_USER)){//商户
            if (0 == size) {
                temp = time + "01";
            } else if (0 < size && size < 10) {
                size = size + 1;
                temp = time + "0" + size;
            } else {
                size = size + 1;
                temp = time + size;
            }
        }
        return temp;
    }

    /**
     * @return
     * @Title: getRandomNum
     * @Description: 获取随机数
     */
    public static String getRandomNum() {
        String date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String[] beforeShuffle = new String[]{"2", "3", "4", "5", "6", "7",
                "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
                "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
                "W", "X", "Y", "Z"};
        List<String> list = Arrays.asList(beforeShuffle);
        Collections.shuffle(list);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i));
        }
        String afterShuffle = sb.toString();
        String result = afterShuffle.substring(5, 9);
        String num = result + "_" + date;
        return num;
    }

    /**
     * @return
     * @Title: getFloat
     * @Description: 获取浮动金额
     */
    public static int getFloat(int money) {
        float Max = 0.5f, Min = -0.5f;
        BigDecimal db = new BigDecimal(Math.random() * (Max - Min) + Min);
        String af = db.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
        java.text.DecimalFormat myformat = new java.text.DecimalFormat("#0.00");
        double num = Double.parseDouble(af);//装换为double类型
        double temp = money / 100;
        money = (int) ((num + temp) * 100);
        return money;
    }



    /**
     * 获取用户真实IP地址，不使用request.getRemoteAddr()的原因是有可能用户使用了代理软件方式避免真实IP地址,
     * 可是，如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值
     *
     * @return ip
     */
    public static String getRealIP(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        log.info("x-forwarded-for : {}" , ip);
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            if (ip.indexOf(",") != -1) {
                ip = ip.split(",")[0];
            }
        }
        String head = "";
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
            head = "Proxy-Client-IP";
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
            head = "WL-Proxy-Client-IP";
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
            head = "HTTP_CLIENT_IP";
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            head = "HTTP_X_FORWARDED_FOR";
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
            head = "X-Real-IP";
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            head = "unknown";
        }
        log.info("{}: {}" ,head, ip);
        return ip.equals("0:0:0:0:0:0:0:1") ? "127.0.0.1" : ip;
    }

    /**
     * 获取用户所拥有的权限列表
     *
     * @return
     */
    public List<String> getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        List<String> list = new ArrayList<>();
        for (GrantedAuthority grantedAuthority : authorities) {
            log.info("权限列表：{}", grantedAuthority.getAuthority());
            list.add(grantedAuthority.getAuthority());
        }
        return list;
    }

    /**
     * 生成指定length的随机字符串（A-Z，a-z，0-9）
     * @param length
     * @return
     */
    public static String getRandomStringToBig(int length) {
        String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(str.length());
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }


    /**
     * 生成指定length的随机字符串（A-Z，a-z，0-9）
     * @param length
     * @return
     */
    public static String getRandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(str.length());
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }
    /**
     * 城市信息   运营商
     * @return
     */
    public static String getIpInfo(String ip) {

        String ask_url = "https://sp0.baidu.com/8aQDcjqpAAV3otqbppnN2DJv/api.php?query="+ip+"&co=&resource_id=6006&t=1584159110852&ie=utf8&oe=utf8&cb=op_aladdin_callback&format=json&tn=baidu&cb=jQuery11020039986690588443397_1584092965212&_=1584092966301";
        String inputLine = "";
        String read = "";
        String location = "";
        Map<String,Object> jsonToMap = new HashMap<>();
        try {
            URL url = new URL(ask_url);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            while ((read = in.readLine()) != null) {
                inputLine += read;
            }
            int index = inputLine.indexOf("{",inputLine.indexOf("{")+1);
            String info = inputLine.substring(index,inputLine.length()-4);
            jsonToMap = com.alibaba.fastjson.JSONObject.parseObject(info);
            location = (String)jsonToMap.get("location");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return location;
    }


    /**
     * 获取操作系统,浏览器及浏览器版本信息
     * @param request
     * @return
     */
    public static String getOsAndBrowserInfo(HttpServletRequest request){
        String real_ip = getRealIP(request);
        String url = "https://sp0.baidu.com/8aQDcjqpAAV3otqbppnN2DJv/api.php?query="+real_ip+"&co=&resource_id=6006&t=1584159110852&ie=utf8&oe=gbk&cb=op_aladdin_callback&format=json&tn=baidu&cb=jQuery11020039986690588443397_1584092965212&_=1584092966301";

        String  browserDetails  =   request.getHeader("User-Agent");
        String  userAgent       =   browserDetails;
        String  user            =   userAgent.toLowerCase();

        String os = "";
        String browser = "";

        //=================OS Info=======================
        if (userAgent.toLowerCase().indexOf("windows") >= 0 )
        {
            os = "Windows";
        } else if(userAgent.toLowerCase().indexOf("mac") >= 0)
        {
            os = "Mac";
        } else if(userAgent.toLowerCase().indexOf("x11") >= 0)
        {
            os = "Unix";
        } else if(userAgent.toLowerCase().indexOf("android") >= 0)
        {
            os = "Android";
        } else if(userAgent.toLowerCase().indexOf("iphone") >= 0)
        {
            os = "IPhone";
        }else{
            os = "UnKnown, More-Info: "+userAgent;
        }
        //===============Browser===========================
        if (user.contains("edge"))
        {
            browser=(userAgent.substring(userAgent.indexOf("Edge")).split(" ")[0]).replace("/", "-");
        } else if (user.contains("msie"))
        {
            String substring=userAgent.substring(userAgent.indexOf("MSIE")).split(";")[0];
            browser=substring.split(" ")[0].replace("MSIE", "IE")+"-"+substring.split(" ")[1];
        } else if (user.contains("safari") && user.contains("version"))
        {
            browser=(userAgent.substring(userAgent.indexOf("Safari")).split(" ")[0]).split("/")[0]
                    + "-" +(userAgent.substring(userAgent.indexOf("Version")).split(" ")[0]).split("/")[1];
        } else if ( user.contains("opr") || user.contains("opera"))
        {
            if(user.contains("opera")){
                browser=(userAgent.substring(userAgent.indexOf("Opera")).split(" ")[0]).split("/")[0]
                        +"-"+(userAgent.substring(userAgent.indexOf("Version")).split(" ")[0]).split("/")[1];
            }else if(user.contains("opr")){
                browser=((userAgent.substring(userAgent.indexOf("OPR")).split(" ")[0]).replace("/", "-"))
                        .replace("OPR", "Opera");
            }

        } else if (user.contains("chrome"))
        {
            browser=(userAgent.substring(userAgent.indexOf("Chrome")).split(" ")[0]).replace("/", "-");
        } else if ((user.indexOf("mozilla/7.0") > -1) || (user.indexOf("netscape6") != -1)  ||
                (user.indexOf("mozilla/4.7") != -1) || (user.indexOf("mozilla/4.78") != -1) ||
                (user.indexOf("mozilla/4.08") != -1) || (user.indexOf("mozilla/3") != -1) )
        {
            browser = "Netscape-?";

        } else if (user.contains("firefox"))
        {
            browser=(userAgent.substring(userAgent.indexOf("Firefox")).split(" ")[0]).replace("/", "-");
        } else if(user.contains("rv"))
        {
            String IEVersion = (userAgent.substring(userAgent.indexOf("rv")).split(" ")[0]).replace("rv:", "-");
            browser="IE" + IEVersion.substring(0,IEVersion.length() - 1);
        } else
        {
            browser = "UnKnown, More-Info: "+userAgent;
        }

        return os +" --- "+ browser ;
    }


}
