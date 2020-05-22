package com.instead.pay.orderInfo.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.instead.pay.appInfo.mapper.AppInfoMapper;
import com.instead.pay.appInfo.model.AppInfo;
import com.instead.pay.commercial.model.Commercial;
import com.instead.pay.commercial.service.CommercialService;
import com.instead.pay.orderInfo.model.OrderInfo;
import com.instead.pay.orderInfo.service.OrderInfoService;
import com.instead.pay.percentage.model.Percentage;
import com.instead.pay.percentage.service.PercentageService;
import com.instead.pay.superpay.service.SuperPayService;
import com.instead.pay.util.*;
import com.instead.pay.util.Security.JwtHelper;
import com.instead.pay.util.rsa.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Controller
@RequestMapping("/orderInfo")
@Component
@Slf4j
public class OrderInfoController {
    @Autowired
    private OrderInfoService orderInfoService;

    @Autowired
    private PercentageService percentageService;

    @Autowired
    private CommercialService commercialService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AppInfoMapper appInfoMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private SuperPayService superPayService;

    @Autowired
    private ThreadUtil threadUtil;

    @Autowired
    private static CacheInfo cacheInfo;

    @Autowired
    public OrderInfoController(CacheInfo cacheInfo){
        OrderInfoController.cacheInfo = cacheInfo;
    }

    @RequestMapping(value = "/getHome", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject getHome(){
        JSONObject jsonObject = new JSONObject();
        try {
            Map<String,Object> map=orderInfoService.getHome();
            jsonObject.put(ResultKey.KEY_SUCC, true);
            jsonObject.put(ResultKey.KEY_DATA, map);
            jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.SUCCESS_CODE);
            jsonObject.put(ResultKey.KEY_MSG, "首页查询成功！");
        } catch (Exception e) {
            log.error("{}", e);
            jsonObject.put(ResultKey.KEY_SUCC, false);
            jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.FAILUE_CODE);
            jsonObject.put(ResultKey.KEY_MSG, "首页查询失败，请稍后再试");
        }
        return jsonObject;
    }



    /**
     * 复制map对象
     * @explain 将paramsMap中的键值对全部拷贝到resultMap中；
     * paramsMap中的内容不会影响到resultMap（深拷贝）
     * @param paramsMap
     *     被拷贝对象
     * @param resultMap
     *     拷贝后的对象
     */
    public static void mapCopy(Map paramsMap, Map resultMap) {
        if (resultMap == null) resultMap = new HashMap();
        if (paramsMap == null) return;

        Iterator it = paramsMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            Object key = entry.getKey();
            resultMap.put(key, paramsMap.get(key) != null ? paramsMap.get(key) : "");

        }
    }

    /**
     * 管理员修改已过期订单为待承兑订单
     * @param ids
     * @return
     */
    @RequestMapping(value = "/updateOrderStatus", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject updateOrderStatus(String ids){
        JSONObject jsonObject = new JSONObject();
        try{
            List<String> list = Arrays.asList(ids.split(","));//订单id集合
            //订单状态
            Map<String,Object> map = new HashMap<>();
            map.put("orderStatus",UsedCode.ORDER_STATUS_WAIT);
            List<Map<String,Object>> uplist = new ArrayList<>();
            for(int i = 0 ;i < list.size();i++){
                String id = list.get(i);
                OrderInfo info = orderInfoService.getOrderInfoById(id);
                if(UsedCode.ORDER_STATUS_BE == info.getOrderStatus()){
                    Map<String,Object> TEMP = new HashMap<>();
                    TEMP.put("orderId",id);
                    mapCopy(map,TEMP);
                    uplist.add(TEMP);
                }
            }
            orderInfoService.updateBatch(uplist);
            jsonObject.put(ResultKey.KEY_SUCC, true);
            jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.SUCCESS_CODE);
            jsonObject.put(ResultKey.KEY_MSG, "修改完毕！");
        } catch (Exception e) {
            log.error("{}", e);
            jsonObject.put(ResultKey.KEY_SUCC, false);
            jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.FAILUE_CODE);
            jsonObject.put(ResultKey.KEY_MSG, "修改失败");
        }
        return jsonObject;
    }



/*    *//**
     * 管理员操作修改订单
     * @param filter
     * @return
     *//*
    @RequestMapping(value = "/updateOrder", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject updateOrder(String filter,String ids){
        JSONObject jsonObject = new JSONObject();
        try {
            filter = InputInjectFilter.encodeInputString(filter);
            //filter参数转map
            Map<String, Object> params = StringUtil.formatParam(filter);
            List<String> list = Arrays.asList(ids.split(","));//订单id集合
            //订单状态  回调状态   回调地址  订单标识
            Map<String,Object> map = new HashMap<>();
            if(!StringUtil.isEmpty((String) params.get("backStatus"))){
                map.put("backStatus",params.get("backStatus"));
            }
            if(!StringUtil.isEmpty((String) params.get("orderStatus"))){
                int orderStatus = Integer.parseInt((String) params.get("orderStatus"));
                map.put("orderStatus",orderStatus);
            }
            if(!StringUtil.isEmpty((String) params.get("remark"))){
                map.put("remark",params.get("remark"));
            }
            if(!StringUtil.isEmpty((String) params.get("callBackUrl"))){
                map.put("callBackUrl",params.get("callBackUrl"));
            }
            List<Map<String,Object>> uplist = new ArrayList<>();
            for(int i = 0 ;i < list.size();i++){
                String id = list.get(i);
                Map<String,Object> TEMP = new HashMap<>();
                TEMP.put("orderId",id);
                mapCopy(map,TEMP);
                uplist.add(TEMP);
            }
            orderInfoService.updateBatch(uplist);
            jsonObject.put(ResultKey.KEY_SUCC, true);
            jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.SUCCESS_CODE);
            jsonObject.put(ResultKey.KEY_MSG, "修改完毕！");
        } catch (Exception e) {
            log.error("{}", e);
            jsonObject.put(ResultKey.KEY_SUCC, false);
            jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.FAILUE_CODE);
            jsonObject.put(ResultKey.KEY_MSG, "修改失败");
        }
        return jsonObject;
    }*/

    /**
     * 订单统计查询
     * @param req
     * @return
     */
    @RequestMapping(value = "/getOrderStatic", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject getOrderStatic(HttpServletRequest req){
        JSONObject jsonObject = new JSONObject();
        try {
            Map<String, Object> map = Request2Map.req2Map(req);
            List<Map<String,Object>> list=orderInfoService.getOrderStatic(map);
            jsonObject.put(ResultKey.KEY_LIST_DATA, list);
            jsonObject.put(ResultKey.KEY_SUCC, true);
            jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.SUCCESS_CODE);
            jsonObject.put(ResultKey.KEY_MSG, "查询成功！");
            return jsonObject;
        } catch (Exception e) {
            log.error("{}",e);
            jsonObject.put(ResultKey.KEY_SUCC, false);
            jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.APP_NONENTITY);
            jsonObject.put(ResultKey.KEY_MSG, "查询失败，请稍后再试！");
            return jsonObject;
        }
    }

    /**
     * app支付获取token
     *
     * @param appId commercialNumber
     * @param commercialNumber
     * @return jsonObjectl
     */
    @RequestMapping(value = "/getToken", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject getToken(String appId, String commercialNumber) {
        log.info("getToken user params-->appId:{},commercialNumber:{}", appId, commercialNumber);
        JSONObject jsonObject = new JSONObject();
        try {
            AppInfo appInfo = appInfoMapper.getAppById(appId);
            if (appInfo != null) {
                if (commercialNumber.equals(appInfo.getCommercialNumber())) {
                    String tokenId = StringUtil.getUuid();
                    String token = JwtUtils.createJWT(tokenId, appId, commercialNumber, 60000);
                    redisUtil.ins(tokenId, token, 1, TimeUnit.MINUTES);
                    log.info("TokenId : {}", tokenId);
                    jsonObject.put("TokenId", tokenId);
                    jsonObject.put(ResultKey.KEY_SUCC, true);
                    jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.SUCCESS_CODE);
                    jsonObject.put(ResultKey.KEY_MSG, "获取TokenId成功！");
                } else {
                    jsonObject.put(ResultKey.KEY_SUCC, false);
                    jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.COMMERCIAL_NO_APP);
                    jsonObject.put(ResultKey.KEY_MSG, "该商户号未绑定该App！");
                }
            } else {
                jsonObject.put(ResultKey.KEY_SUCC, false);
                jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.APP_NONENTITY);
                jsonObject.put(ResultKey.KEY_MSG, "该App不存在！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    /**
     * 条件查询订单信息集
     *
     * @param filter
     * @return jsonObject
     */
    @RequestMapping(value = "/queryOrderInfo", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject queryOrderInfo(@RequestParam(defaultValue = "") String filter, Integer page, Integer limit, HttpServletRequest request ) {
        JSONObject jsonObject = new JSONObject();
        filter = InputInjectFilter.encodeInputString(filter);
        //filter参数转map
        Map<String, Object> param = StringUtil.formatParam(filter);
        try {
            String pcnumber = (String) param.get("commercialNumber");
            String token = request.getHeader("Authorization");
            String jsonObject1 = redisUtil.get(token);
            JSONObject jsonObject2 = JSONObject.parseObject(jsonObject1);
            String data = (String) jsonObject2.get("obj");
            Commercial commercial = GsonUtil.stringToBo(data, Commercial.class);
            String loginNumber = commercial.getCommercialNumber();
//            String loginNumber = threadUtil.get().getCommercialNumber();
            log.info("订单查询----登录商户：{}，查找商户：{}",loginNumber,pcnumber);
            //如果是根据商户号查询   当前商户只能查询自己的   系统管理员才能查询所有商户的订单
            if(!StringUtil.isEmpty(pcnumber) && !StringUtil.isEmpty(loginNumber)){
                if(!UsedCode.SYSTEM_NUMBER.equals(loginNumber) && !loginNumber.equals(pcnumber)){
                    jsonObject.put(ResultKey.KEY_SUCC, false);
                    jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.PERMISSION_DENIED);
                    jsonObject.put(ResultKey.KEY_MSG, "权限不足");
                    return jsonObject;
                }
            }
            //如果不是根据商户号查询 并且当前登录的不是系统管理员 查询的会携带当前登录的商户号进行查询
            if(StringUtil.isEmpty(pcnumber) && !UsedCode.SYSTEM_NUMBER.equals(loginNumber)){
                param.put("commercialNumber",loginNumber);
            }
            if(UsedCode.SYSTEM_NUMBER.equals(pcnumber)){
                param.remove("commercialNumber");
            }
            log.info("订单查询参数：{}",param);
            List<OrderInfo> OrderInfoList = new ArrayList<OrderInfo>();
            if (StringUtil.isEmpty(page)) {
                OrderInfoList = orderInfoService.queryOrderInfo(param);
            } else {
                PageHelper.startPage(page, limit);
                if(!StringUtil.isEmpty(param.get("applicationType")) &&
                        Integer.parseInt((String)param.get("applicationType")) == UsedCode.APPLICATION_TYPE_WIT){
                    OrderInfoList = orderInfoService.queryOrderInfoCash(param); //提现
                }else if(!StringUtil.isEmpty(param.get("applicationType")) &&
                        Integer.parseInt((String)param.get("applicationType")) == UsedCode.ORDER_STATUS_BO){
                    OrderInfoList = orderInfoService.queryOrderInfoByCz(param); //充值
                }else{
                    OrderInfoList = orderInfoService.queryOrderInfo(param); //代收付
                }
                PageInfo<OrderInfo> pageInfo = new PageInfo<>(OrderInfoList);
                jsonObject.put("total", pageInfo.getTotal());
            }
            jsonObject.put(ResultKey.KEY_SUCC, true);
            jsonObject.put(ResultKey.KEY_LIST_DATA, OrderInfoList);
            jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.SUCCESS_CODE);
            jsonObject.put(ResultKey.KEY_MSG, "查询成功！");
        } catch (Exception e) {
            log.error("{}", e);
            jsonObject.put(ResultKey.KEY_SUCC, false);
            jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.FAILUE_CODE);
            jsonObject.put(ResultKey.KEY_MSG, "查询失败");
        }
        return jsonObject;
    }


    @RequestMapping(value = "/callbackForOrder", method = RequestMethod.POST)
    public String callbackForOrder(HttpServletRequest req) {
        JSONObject jsonObject = new JSONObject();
        Map<String, String> map = Request2Map.req2MapNew(req);
        log.info("四方回调请求参数：{}",GsonUtil.boToString(map));
        try {
            boolean flag = SignVerify.checkSignNew(map);
            if(!flag){
                return "error";
            }
            String orderId = map.get("mer_order_no");
            log.info("四方回调给我们的订单号：{}",orderId);
            OrderInfo orderInfo = orderInfoService.getOrderInfoById(orderId);
            if (null == orderInfo) {
                log.info("未找到订单信息");
                return "success";
            }
            if(orderInfo.getOrderStatus() == UsedCode.ORDER_STATUS_CONFIRM){
                log.info("订单重复确认");
                return "success";
            }
            //四方流水号
            String serialNo = map.get("sys_order_no");
            orderInfo.setSerialNo(serialNo);
            Integer status = Integer.parseInt(map.get("status"));
            if(1 == status || 2 == status){ //1-开始匹配（审核成功）  已打款
                orderInfo.setOrderStatus(UsedCode.ORDER_STATUS_CONFIRM);
            }else if(3 == status){ //3-交易成功
                log.info("交易成功");
                orderInfo.setOrderStatus(UsedCode.ORDER_STATUS_CONFIRM);
            }else if(4 == status){ //4-取消（审核失败或其他异常情况）
                log.info("回调显示订单取消");
                orderInfo.setOrderStatus(UsedCode.ORDER_STATUS_CANCEL);   //修改订单状态
                orderInfoService.updateOrderInfo(orderInfo);
                return "success";
            }
            AppInfo info = appInfoMapper.getAppById(orderInfo.getApplicationName());
            if(orderInfo.getApplicationType()==UsedCode.APPLICATION_TYPE_IN){           //代收
                affirmPut(orderInfo, info.getAppKey(),orderInfo.getOrderStatus(),object -> jsonObject);
            }else if(orderInfo.getApplicationType()==UsedCode.APPLICATION_TYPE_OUT){    //代付
                affirmPay(orderInfo, info.getAppKey(),orderInfo.getOrderStatus(),object -> jsonObject);
            }else if(orderInfo.getApplicationType()==UsedCode.APPLICATION_TYPE_WIT){    //提现
                affirmWit(orderInfo,object -> jsonObject);
            }
        } catch (ResourceAccessException r) {
            log.error("回调超时，请检查回调地址：{}", r);
        } catch (Exception e) {
            log.error("回调失败，请稍后再试！{}", e);
        }
        return "success";
    }

    /**
     * @title syncCallBck
     * @description   手动补发回调给商户  已承兑才能手动回调
     * @author vring
     * @param:
     * @throws
     */
    @RequestMapping(value = "/syncCallBck", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject syncCallBck(String orderId){
        OrderInfo info = orderInfoService.getOrderInfoById(orderId);
        JSONObject jsonObject = new JSONObject();
        try{
            if(info.getOrderStatus() == UsedCode.ORDER_STATUS_CONFIRM){
                String obj = info.getBackInfo();
                Map<String,String> map = GsonUtil.stringToMap(obj);
                log.info("手动回调补发数据：{}",map);
                String reason = HttpUtil.syncPostForm(info.getCallBackUrl(),map);
                log.info("补发结果：{}",reason);
                if("success".equals(reason)){
                    info.setBackStatus(UsedCode.ORDER_STATUS_CONFIRM);
                    orderInfoService.updateOrderInfo(info);
                    jsonObject.put(ResultKey.KEY_SUCC, true);
                    jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.SUCCESS_CODE);
                    jsonObject.put(ResultKey.KEY_MSG, "补发成功");
                    jsonObject.put(ResultKey.KEY_DATA, reason);
                }else {
                    jsonObject.put(ResultKey.KEY_SUCC, false);
                    jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.FAILUE_CODE);
                    jsonObject.put(ResultKey.KEY_MSG, "补发失败");
                    jsonObject.put(ResultKey.KEY_DATA, reason);
                }
            }else{
                jsonObject.put(ResultKey.KEY_SUCC, false);
                jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.FAILUE_CODE);
                jsonObject.put(ResultKey.KEY_MSG, "订单未承兑,请先承兑");
            }
        }catch (Exception e){
            log.info("手动补发失败{}",e);
            jsonObject.put(ResultKey.KEY_SUCC, false);
            jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.FAILUE_CODE);
            jsonObject.put(ResultKey.KEY_MSG, "手动补发失败");
        }
        return jsonObject;
    }


    /**
     * @description   查询订单信息
     * @author vring
     * @param:
     * @throws
     */
    @RequestMapping(value = "/order_query", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject order_query(String orderId){
        String response = superPayService.queryOrder(orderId);
        log.info("请求四方--》响应结果：{}",response);
        JSONObject resJSON = JSONObject.parseObject(response);
        JSONObject jsonObject = JSONObject.parseObject(response);
        Integer code = resJSON.getInteger("code");
        if(code == 1) {
            JSONObject jsonObject1 = resJSON.getJSONObject("data");
            jsonObject.put(ResultKey.KEY_MSG, "操作成功");
            jsonObject.put(ResultKey.KEY_SUCC,true);
            jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.SUCCESS_CODE);
            jsonObject.put(ResultKey.KEY_DATA,jsonObject1);
            return jsonObject;
        }else if(code == 0) {
            jsonObject.put(ResultKey.KEY_SUCC, true);
            jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.SUCCESS_CODE);
            jsonObject.put(ResultKey.KEY_MSG, "未匹配");
        }else{
            jsonObject.put(ResultKey.KEY_SUCC, true);
            jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.SUCCESS_CODE);
            jsonObject.put(ResultKey.KEY_MSG, "支付中");
        }
        return jsonObject;
    }



    /*
     * @title affirmOrder
     * @description
     * @author vring
     * @param orderId 訂單id
     * @param: affirmType 0取消訂單     1確認訂單
     * @throws
     */
    @RequestMapping(value = "/affirmOrder", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject affirmOrder(String orderId,Integer status,String reason, HttpServletRequest req) {
        log.info("affirmOrder use params-->orderId:{}", orderId);
        JSONObject jsonObject = new JSONObject();
        try {
            //判权
//            JSONObject jsonObject = roleRight();
//            if(!jsonObject.isEmpty()) return jsonObject;
            OrderInfo orderInfo = orderInfoService.getOrderInfoById(orderId);
            log.info("数据库订单信息：{}", orderInfo);
            if (null == orderInfo) {
                jsonObject.put(ResultKey.KEY_SUCC, false);
                jsonObject.put(ResultKey.KEY_MSG, "未找到该订单信息！");
                jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.NOT_FOUND_ORDERINFO);
                return jsonObject;
            }
            if(orderInfo.getOrderStatus() == UsedCode.ORDER_STATUS_CONFIRM){
                jsonObject.put(ResultKey.KEY_SUCC, false);
                jsonObject.put(ResultKey.KEY_MSG, "请勿重复确认订单！");
                jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.NOT_FOUND_ORDERINFO);
                return jsonObject;
            }
            AppInfo info = appInfoMapper.getAppById(orderInfo.getApplicationName());//applicationName就是appid
            //待承兑
            if(orderInfo.getOrderStatus() == UsedCode.ORDER_STATUS_WAIT) {
                if (orderInfo.getApplicationType() == UsedCode.APPLICATION_TYPE_IN) {           //代收
                    affirmPut(orderInfo, info.getAppKey(),5, object -> jsonObject);
                } else if (orderInfo.getApplicationType() == UsedCode.APPLICATION_TYPE_OUT) {    //代付
                    affirmPay(orderInfo, info.getAppKey(), 5,object -> jsonObject);
                }
            }else if(orderInfo.getOrderStatus() == UsedCode.ORDER_STATUS_BE){
                jsonObject.put(ResultKey.KEY_SUCC, true);
                jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.SUCCESS_CODE);
                jsonObject.put(ResultKey.KEY_MSG, "订单已过期！");
                return jsonObject;
            }
            if(orderInfo.getOrderStatus() == UsedCode.ORDER_STATUS_BO &&StringUtil.isEmpty(orderInfo.getRemark4())){
                log.info("提现驳回需要理由！");
                jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.FAILUE_ACCOUNT_BALANCE);
                jsonObject.put(ResultKey.KEY_MSG, "提现驳回需要理由！");
                return jsonObject;
            }
            if(orderInfo.getApplicationType()==UsedCode.APPLICATION_TYPE_WIT){    //提现
                if(orderInfo.getOrderStatus()==UsedCode.ORDER_STATUS_BO){
                    orderInfo.setRemark4(reason);
                }else{
                    affirmWit(orderInfo, object -> jsonObject);
                }
            }
            jsonObject.put(ResultKey.KEY_SUCC, true);
            jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.SUCCESS_CODE);
            jsonObject.put(ResultKey.KEY_MSG, "确认订单成功！");
        } catch (ResourceAccessException r) {
            log.error("{}", r);
            jsonObject.put(ResultKey.KEY_SUCC, false);
            jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.CONNECT_TIMED_OUT);
            jsonObject.put(ResultKey.KEY_MSG, "回调超时，请检查回调地址");
        } catch (Exception e) {
            log.error("{}", e);
            jsonObject.put(ResultKey.KEY_SUCC, false);
            jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.FAILUE_CODE);
            jsonObject.put(ResultKey.KEY_MSG, "确认失败，请稍后再试！");
        }
        return jsonObject;
    }


    /**
     * 商户申请提现
     * @return
     */
    @RequestMapping(value = "/applyWit", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject applyWit(HttpServletRequest request){
        JSONObject jsonObject = new JSONObject();
        try {
            //获取初始值
            Map<String, Object> map = Request2Map.req2Map(request);
            log.info("提现参数：{}",map);
            Commercial commercial = commercialService.getCommercialByNum(map.get("commercialNumber").toString());
            String pwd=commercialService.getSafetyPwd(commercial.getCommercialId());
            if(!StringUtil.isEmpty(pwd)){
                // 解密后的明文
                String password = MD5Util.getPwd(pwd);
                //校验密码是否一致
                boolean flag = MD5Util.getSaltverifyMD5(password, commercial.getSafetyPwd());
                if (!flag) {
                    jsonObject.put(ResultKey.KEY_SUCC, false);
                    jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.CHECK_PWD_FAIL);
                    jsonObject.put(ResultKey.KEY_MSG, "密码错误，请重新输入");
                    return jsonObject;
                }
            }
            log.info("商户信息：{}", commercial);
            String orderId=StringUtil.getOrderId(UsedCode.APPLICATION_TYPE_WIT);
            //生成订单信息
            OrderInfo orderInfo=getOrder(orderId,commercial.getCommercialNumber(),map);
            //抽成比例
            double radio = commercial.getCommercialWithRatio();
            if (radio == 0) {
                jsonObject.put(ResultKey.KEY_SUCC, false);
                jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.FAILUE_CODE);
                jsonObject.put(ResultKey.KEY_MSG, "该商户尚未设置提现服务费收取比例！");
                return jsonObject;
            }
            //获取用户可提现金额
            Integer mayCash=getMayWitMoney(commercial);
            orderInfo.setFloatMoney(orderInfo.getOperatorMoney());
            int endMoney=mayCash-orderInfo.getOperatorMoney();
            if (endMoney < 0) {
                log.info("提现申请失败,商户余额不足");
                jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.FAILUE_ACCOUNT_BALANCE);
                jsonObject.put(ResultKey.KEY_MSG, "提现申请失败,商户余额不足");
                return jsonObject;
            }
            //获取系统最大提现金额 当提现金超出限制时 开启线下提现
            Integer maxMoney = Integer.parseInt(cacheInfo.getCommonToKey("system.properties+max.cash"));
            if(orderInfo.getOperatorMoney() < maxMoney){
                Integer  code =  toSupPay(orderInfo);
                if(code == 1){
                    orderInfo.setLine(UsedCode.ORDER_STATUS_CANCEL);
                }
                else {
                    jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.FAILUE_CODE);
                    jsonObject.put(ResultKey.KEY_MSG, "操作失败,访问四方失败！");
                    jsonObject.put(ResultKey.KEY_SUCC,false);
                    return jsonObject;
                }
            }else {
                orderInfo.setLine(UsedCode.APPLICATION_TYPE_IN);
            }
            orderInfoService.insertOrderInfo_out(orderInfo);
            //修改用户金额
            if(!inWithOrder(commercial,orderInfo)){
                jsonObject.put(ResultKey.KEY_SUCC, false);
                jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.CONNECT_TIMED_OUT);
                jsonObject.put(ResultKey.KEY_MSG, "用户金额计算失败！");
                return jsonObject;
            }
            jsonObject.put(ResultKey.KEY_SUCC, true);
            jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.SUCCESS_CODE);
            jsonObject.put(ResultKey.KEY_MSG, "申请成功，当前状态-->待审核！");
        } catch (Exception e) {
            log.error("{}", e);
            jsonObject.put(ResultKey.KEY_SUCC, false);
            jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.FAILUE_CODE);
            jsonObject.put(ResultKey.KEY_MSG, "申请失败，请稍后再试！");
        }
        return jsonObject;
    }

    /**
     * 计算可提现金额
     * @param commercialNumber
     * @return
     */
    @RequestMapping(value = "/mayCashWit", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject mayCashWit(String commercialNumber){
        JSONObject jsonObject = new JSONObject();
        try {
            Commercial commercial = commercialService.getCommercialByNum(commercialNumber);
            log.info("商户信息：{}", commercial);
            Integer mayCash=getMayWitMoney(commercial);
            jsonObject.put(ResultKey.KEY_DATA, mayCash);
            jsonObject.put(ResultKey.KEY_SUCC, true);
            jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.SUCCESS_CODE);
            jsonObject.put(ResultKey.KEY_MSG, "计算成功！");
        } catch (Exception e) {
            log.error("{}", e);
            jsonObject.put(ResultKey.KEY_SUCC, false);
            jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.FAILUE_CODE);
            jsonObject.put(ResultKey.KEY_MSG, "计算可提现金额失败，请稍后再试！");
        }
        return jsonObject;
    }

    /**
     * 计算可提现金额
     * @param commercial
     * @return
     */
    private Integer getMayWitMoney(Commercial commercial){
        double radio = commercial.getCommercialWithRatio();//抽成比例
        Integer mayCash=StringUtil.handMoney(commercial.getCommercialBalance(),radio);
        return commercial.getCommercialBalance()-mayCash;
    }
    /**
     * 访问四方的代付
     * @param orderInfo
     * @return
     * @throws Exception
     */
    private Integer toSupPay(OrderInfo orderInfo) throws Exception {
//        JSONObject jsonObject = new JSONObject();
        //请求四方   如果是手动挂单---传入系统单号  非手动挂单---传入外部单号
        String order_id = orderInfo.getOrderId();
        String response = "";
        response = superPayService.systemPayNew(orderInfo, order_id);
        log.info("响应结果：{}",response);
        JSONObject resJSON = JSONObject.parseObject(response);
        Integer code = resJSON.getInteger("code");
        if(code == 1) {
            log.info("请求成功");
        }else{
            code = 500;
            log.error("提现错误：{}",resJSON.getString("message"));
        }
        return code;
    }

    /**
     * 封装提现基础数据
     * @param orderId
     * @param commercialNumber
     * @param map
     * @return
     */
    private OrderInfo getOrder(String orderId,String commercialNumber,Map<String, Object> map){
        OrderInfo Info = new OrderInfo(orderId,"",
                commercialNumber,
                1,
                Integer.parseInt(map.get("operatorMoney").toString()),
                "", new Date(),
                UsedCode.APPLICATION_TYPE_WIT,
                Integer.parseInt(map.get("payType").toString()),
                UsedCode.IS_HAND_YES,
                UsedCode.ORDER_STATUS_WAIT,"","",
                map.get("bankAccount").toString(), map.get("bankName").toString(), map.get("bankAddress").toString(),
                map.get("bankUserName").toString(), map.get("bankPhone").toString());
        return Info;
    }


    private JSONObject roleRight(){
        JSONObject jsonObject = new JSONObject();
        //判权
        String roleName = JwtHelper.getUserRole(redisUtil.get(threadUtil.get().getCommercialId()));
        if(UsedCode.ROLE_USER.equals(roleName)){
            log.error("越权操作！--> {}",threadUtil.get().getCommercialName());
            jsonObject.put(ResultKey.KEY_SUCC, false);
            jsonObject.put(ResultKey.KEY_MSG, "无权限进行此项操作！");
            jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.PERMISSION_DENIED);
        }
        return jsonObject;
    }


    private JSONObject callBack_NEW(OrderInfo orderInfo, String callBackUrl,String appKey){
        log.info("开始回调--》回调地址：{}", callBackUrl);
        JSONObject resultJsonObject = new JSONObject();
        if(StringUtil.isEmpty(callBackUrl)){
            resultJsonObject.put(ResultKey.KEY_SUCC, false);
            resultJsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.FAILUE_CODE);
            resultJsonObject.put(ResultKey.KEY_MSG, "回调失败,回调地址为空");
            return resultJsonObject;
        }
        try {
            Map<String,String> jsonObject = new HashMap<>();
            jsonObject.put("orderId",String.valueOf(orderInfo.getOrderId()));
            jsonObject.put("outId",String.valueOf(orderInfo.getOutId()));
            jsonObject.put("operatorMoney",String.valueOf(orderInfo.getOperatorMoney()));
            jsonObject.put("orderStatus",String.valueOf(orderInfo.getOrderStatus()));
            jsonObject.put("createTime",String.valueOf(orderInfo.getCreateTime()));
            jsonObject.put("confirmTime",String.valueOf(orderInfo.getConfirmTime()));
            jsonObject.put("time",String.valueOf(System.currentTimeMillis()));
            jsonObject.put("appKey",appKey);
            String sign = SignVerify.getMD5Encrypt(jsonObject);
            jsonObject.put("sign",sign);
            jsonObject.put(ResultKey.KEY_SUCC, "true");
            jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.SUCCESS_CODE);
            jsonObject.put(ResultKey.KEY_MSG, "确认成功");
/*            String reason = HttpUtil.syncPostForm(callBackUrl,jsonObject);
            log.info("--------商户回调返回结果:{}",reason);
            if("success".equals(reason)){
                resultJsonObject.put("data",jsonObject);
                resultJsonObject.put(ResultKey.KEY_CODE,ErrorCodeContents.SUCCESS_CODE);
            }*/
            String reasons = "";
            //回调失败  重复回调5次
            int num = 5;
            for(int i = 0 ; i < num ; i++){
                reasons = HttpUtil.syncPostForm(callBackUrl,jsonObject);
                log.info("--------商户回调返回结果:{}",reasons);
                if("success".equals(reasons)){
                    resultJsonObject.put("data",jsonObject);
                    resultJsonObject.put(ResultKey.KEY_CODE,ErrorCodeContents.SUCCESS_CODE);
                    break;
                }
            }
            log.info("商户回调成功");
        }catch (Exception e) {
            log.info("callback_new failed-->{}",e);
            resultJsonObject.put(ResultKey.KEY_CODE,ErrorCodeContents.FAILUE_CODE);
        }
        return resultJsonObject;
    }
    /*
     * @title   回调给商户
     * @description  回调方法
     * @author vring
     * @param: map 回调内容   callBackUrl 回调地址
     * @throws
     */
    /*private JSONObject callBack(OrderInfo orderInfo, String callBackUrl){
        log.info("开始回调--》回调地址：{}", callBackUrl);
        JSONObject resultJsonObject = new JSONObject();
        if(StringUtil.isEmpty(callBackUrl)){
            resultJsonObject.put(ResultKey.KEY_SUCC, false);
            resultJsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.FAILUE_CODE);
            resultJsonObject.put(ResultKey.KEY_MSG, "回调失败,回调地址为空");
            return resultJsonObject;
        }
        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.set("Accept-Charset", "utf-8");
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);// 设置编码
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add(ResultKey.KEY_SUCC, "true");
            map.add(ResultKey.KEY_CODE, ErrorCodeContents.SUCCESS_CODE);
            map.add(ResultKey.KEY_MSG, "确认成功");

            map.add("pay_url", orderInfo.getRemitUrl());
            map.add("orderInfo",StringUtil.objectToString(orderInfo));
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, httpHeaders);
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(callBackUrl, request, String.class);
            String responseInfo = responseEntity.getBody();
            log.info("responseInfo:{}", responseInfo);
            JSONObject  jsonObject = JSON.parseObject(responseInfo);
            if (!"200".equals(jsonObject.getString("code"))) {
                log.info("商户回调失败");
                resultJsonObject.put(ResultKey.KEY_SUCC, false);
                resultJsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.FAILUE_CODE);
                resultJsonObject.put(ResultKey.KEY_MSG, "商户回调失败");
            } else {
                log.info("商户回调成功");
            }
        }  catch (ResourceAccessException r){
            log.error("回调超时，请检查回调地址：{}", r);
            resultJsonObject.put(ResultKey.KEY_SUCC, false);
            resultJsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.CONNECT_TIMED_OUT);
            resultJsonObject.put(ResultKey.KEY_MSG, "回调超时，请检查回调地址");
        }catch (RestClientException e) {
            log.error("回调失败,数据格式不正确:{}", e);
            resultJsonObject.put(ResultKey.KEY_SUCC, false);
            resultJsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.FAILUE_CODE);
            resultJsonObject.put(ResultKey.KEY_MSG, "回调失败,数据格式不正确");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultJsonObject;
    }*/


    public String getHand(OrderInfo orderInfo,String appKey){
        Map<String,String> jsonObject1 = new HashMap<>();
        jsonObject1.put("orderId",String.valueOf(orderInfo.getOrderId()));
        jsonObject1.put("outId",String.valueOf(orderInfo.getOutId()));
        jsonObject1.put("operatorMoney",String.valueOf(orderInfo.getOperatorMoney()));
        jsonObject1.put("orderStatus",String.valueOf(orderInfo.getOrderStatus()));
        jsonObject1.put("createTime",String.valueOf(orderInfo.getCreateTime()));
        jsonObject1.put("confirmTime",String.valueOf(orderInfo.getConfirmTime()));
        jsonObject1.put("time",String.valueOf(System.currentTimeMillis()));
        jsonObject1.put("appKey",appKey);
        String sign = SignVerify.getMD5Encrypt(jsonObject1);
        log.info("回调给商户时的我方签名：{}",sign);
        jsonObject1.put("sign",sign);
        jsonObject1.put(ResultKey.KEY_SUCC, "true");
        jsonObject1.put(ResultKey.KEY_CODE, ErrorCodeContents.SUCCESS_CODE);
        jsonObject1.put(ResultKey.KEY_MSG, "确认成功");
        String data = JSONObject.toJSONString(jsonObject1);
        return data;
    }


    /**
     * 处理 代收 订单确认
     * @param orderInfo
     * @param function
     * @return
     */
    private JSONObject affirmPut(OrderInfo orderInfo, String appKey,Integer status,Function<JSONObject, JSONObject> function) {
        //返回参数
        JSONObject jsonObject = new JSONObject();
        try {
            Commercial commercial = commercialService.getCommercialByNum(orderInfo.getCommercialNumber());
            int handMoney = StringUtil.handMoney(orderInfo.getOperatorMoney(), commercial.getCommercialRatio()); //获取抽成

            Percentage percentage = new Percentage(StringUtil.getUuid(), orderInfo.getOrderId(), orderInfo.getCommercialNumber(),handMoney, new Date(), UsedCode.APPLICATION_TYPE_IN);
            int yMoney = orderInfo.getOperatorMoney() - handMoney;
            log.info("商户：{},商户余额：{},服务费比例：{},浮动金额：{},系统抽成：{},订单余额:{}",commercial.getCommercialNumber(), commercial.getCommercialBalance(),commercial.getCommercialRatio(),orderInfo.getFloatMoney(),handMoney,yMoney);

            orderInfo.setDeductedMoney(yMoney);
//            orderInfo.setOrderStatus(UsedCode.ORDER_STATUS_CONFIRM);    //已确认
            orderInfo.setConfirmTime(new Date());
            orderInfo.setOutRatio(handMoney); //我方服务费
            orderInfo.setLine(UsedCode.ORDER_STATUS_CANCEL);
            if(orderInfo.getIsHand() == UsedCode.IS_HAND_NO && status == 1){//手动挂单不需要回调
                //回调给商户
                jsonObject = callBack_NEW(orderInfo, orderInfo.getCallBackUrl(),appKey);
                if(!ErrorCodeContents.SUCCESS_CODE.equals(jsonObject.get("errorCode"))){
                    log.error("商户回调失败");
                    orderInfo.setBackStatus(UsedCode.ORDER_STATUS_WAIT);
                    orderInfoService.updateOrderInfo(orderInfo);
                    return jsonObject;
                }else{
                    log.info("存储的回调信息：{}",JSONObject.toJSONString(jsonObject.get("data")));
                    orderInfo.setBackInfo(JSONObject.toJSONString(jsonObject.get("data")));
                    orderInfo.setBackStatus(UsedCode.ORDER_STATUS_CONFIRM);
                }
            }
            //线下承兑+手动回调
            if(status == 5){
                String data = getHand(orderInfo,appKey);
                orderInfo.setBackInfo(data);
                orderInfo.setLine(UsedCode.APPLICATION_TYPE_IN);
                orderInfo.setOrderStatus(UsedCode.ORDER_STATUS_CONFIRM);
                orderInfo.setBackStatus(UsedCode.ORDER_STATUS_CANCEL);
            }
            orderInfoService.updateOrderInfo(orderInfo);
            percentageService.insert(percentage);
            //修改用户金额
            if(!closeCommMoney(commercial,orderInfo)){
                jsonObject.put(ResultKey.KEY_SUCC, false);
                jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.CONNECT_TIMED_OUT);
                jsonObject.put(ResultKey.KEY_MSG, "用户金额结算失败！");
                return jsonObject;
            }
            jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.SUCCESS_CODE);
            return function.apply(jsonObject);
        } catch (Exception e) {
            log.error("{}", e);
            jsonObject.put(ResultKey.KEY_SUCC, false);
            jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.FAILUE_CODE);
            jsonObject.put(ResultKey.KEY_MSG, "确认失败，请稍后再试！");
        }
        return jsonObject;
    }

    /**
     * 处理 代付 订单确认
     * @param orderInfo
     * @param function
     * @return
     */
    private JSONObject affirmPay(OrderInfo orderInfo,String appKey ,Integer confirm,Function<JSONObject, JSONObject> function) {
        //返回参数
        JSONObject jsonObject = new JSONObject();
        try {
            Commercial commercial = commercialService.getCommercialByNum(orderInfo.getCommercialNumber());
            log.info("商户信息：{}", commercial);
            double radio = commercial.getCommercialRatio();//抽成比例
            if (radio == 0) {
                jsonObject.put(ResultKey.KEY_SUCC, false);
                jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.FAILUE_CODE);
                jsonObject.put(ResultKey.KEY_MSG, "该商户尚未设置服务费收取比例！");
                return jsonObject;
            }
            int handMoney = StringUtil.handMoney(orderInfo.getOperatorMoney(), radio); //获取抽成
            int realMoney = orderInfo.getOperatorMoney() + handMoney;//商户实际支付
            log.info("商户：{},商户余额：{},服务费比例：{},系统抽成：{}" +
                    ",商户实际支付:{}",commercial.getCommercialNumber(), commercial.getCommercialBalance(),commercial.getCommercialRatio(),orderInfo.getFloatMoney(),handMoney,realMoney);
            Percentage percentage = new Percentage(StringUtil.getUuid(), orderInfo.getOrderId(), orderInfo.getCommercialNumber(), handMoney, new Date(), UsedCode.APPLICATION_TYPE_OUT);
            orderInfo.setDeductedMoney(realMoney);
            orderInfo.setConfirmTime(new Date());
            orderInfo.setOutRatio(handMoney); //我方服务费
            orderInfo.setLine(UsedCode.ORDER_STATUS_CANCEL);
            if(orderInfo.getIsHand() == UsedCode.IS_HAND_NO){//手动挂单不需要回调
                jsonObject = callBack_NEW(orderInfo,orderInfo.getCallBackUrl(),appKey);
                //回调失败
                if(!ErrorCodeContents.SUCCESS_CODE.equals(jsonObject.get("errorCode"))){
                    orderInfo.setBackStatus(UsedCode.ORDER_STATUS_WAIT);
                    orderInfoService.updateOrderInfo(orderInfo);
                    return jsonObject;
                }else{
                    //回调成功
                    orderInfo.setBackInfo(JSONObject.toJSONString(jsonObject.get("data")));
                    orderInfo.setBackStatus(UsedCode.ORDER_STATUS_CONFIRM);
                }
            }
            //线下承兑+手动回调 (手动确认代付)
            if(confirm == 5){
                String data = getHand(orderInfo,appKey);
                orderInfo.setBackInfo(data);
                orderInfo.setLine(UsedCode.APPLICATION_TYPE_IN);
                orderInfo.setOrderStatus(UsedCode.ORDER_STATUS_CONFIRM);
                orderInfo.setBackStatus(UsedCode.ORDER_STATUS_CANCEL);
            }
            orderInfoService.updateOrderInfo(orderInfo);
            percentageService.insert(percentage);
            //修改用户金额
            if(!closeCommMoney(commercial,orderInfo)){
                jsonObject.put(ResultKey.KEY_SUCC, false);
                jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.CONNECT_TIMED_OUT);
                jsonObject.put(ResultKey.KEY_MSG, "用户金额结算失败！");
                return jsonObject;
            }
            jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.SUCCESS_CODE);
            return function.apply(jsonObject);
        } catch (Exception e) {
            log.error("{}", e);
            jsonObject.put(ResultKey.KEY_SUCC, false);
            jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.FAILUE_CODE);
            jsonObject.put(ResultKey.KEY_MSG, "确认失败，请稍后再试！");
        }
        return jsonObject;
    }


    /**
     * 处理 提现 订单确认
     * @param orderInfo
     * @param function
     * @return
     */
    private JSONObject affirmWit(OrderInfo orderInfo,Function<JSONObject, JSONObject> function) {
        //返回参数
        JSONObject jsonObject = new JSONObject();
        try {
            Commercial commercial = commercialService.getCommercialByNum(orderInfo.getCommercialNumber());
            //获取 服务费金额
            int handMoney = StringUtil.handMoney(orderInfo.getOperatorMoney(),commercial.getCommercialWithRatio());
            //int realMoney = orderInfo.getFloatMoney() - handMoney;//商户实际支付
            log.info("商户：{},商户余额：{},服务费比例：{},系统抽成：{},商户实际提现:{}",commercial.getCommercialNumber(), commercial.getCommercialBalance(),commercial.getCommercialWithRatio(),handMoney,orderInfo.getOperatorMoney());
            //最终金额 = 现有的 - （ 操作金额 + 服务费 ）
            orderInfo.setDeductedMoney(orderInfo.getOperatorMoney()+handMoney);
            Percentage percentage = new Percentage(StringUtil.getUuid(), orderInfo.getOrderId(), orderInfo.getCommercialNumber(), orderInfo.getDeductedMoney(), new Date(), UsedCode.APPLICATION_TYPE_WIT);

            orderInfo.setConfirmTime(new Date());
            orderInfo.setMakerName(orderInfo.getRemark());
            orderInfo.setOutRatio(handMoney);//我方服务费
            orderInfo.setLine(UsedCode.ORDER_STATUS_CANCEL);
            orderInfoService.updateOrderInfo(orderInfo);
            percentageService.insert(percentage);
            //修改用户金额
            if(!closeCommMoney(commercial,orderInfo)){
                jsonObject.put(ResultKey.KEY_SUCC, false);
                jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.CONNECT_TIMED_OUT);
                jsonObject.put(ResultKey.KEY_MSG, "用户金额结算失败！");
                return jsonObject;
            }
            jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.SUCCESS_CODE);
            return function.apply(jsonObject);
        } catch (Exception e) {
            log.error("{}", e);
            jsonObject.put(ResultKey.KEY_SUCC, false);
            jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.FAILUE_CODE);
            jsonObject.put(ResultKey.KEY_MSG, "确认失败，请稍后再试！");
        }
        return jsonObject;
    }

    /**
     * 用户结算金额       （未计算充值信息）
     * @param commercial    用户信息
     * @param orderInfo     订单信息
     * @return
     */
    private boolean closeCommMoney(Commercial commercial,OrderInfo orderInfo){
        try {
            Integer handMoney = 0;
            log.info("用户原始信息：{}",commercial);
            if(orderInfo.getApplicationType()==UsedCode.APPLICATION_TYPE_IN){   //入金-代收
                //总余额修改
                handMoney = StringUtil.handMoney(orderInfo.getOperatorMoney(),commercial.getCommercialRatio());
                //总金额
                commercial.setAllMoney(commercial.getAllMoney()+orderInfo.getOperatorMoney());
                //余额
                commercial.setCommercialBalance(commercial.getCommercialBalance()+orderInfo.getOperatorMoney()-handMoney);
            }else{      //出金
                if(orderInfo.getApplicationType()==UsedCode.APPLICATION_TYPE_OUT){  //代付
                    handMoney = StringUtil.handMoney(orderInfo.getOperatorMoney(),commercial.getCommercialRatio());
                }else{  //提现
                    handMoney = StringUtil.handMoney(orderInfo.getOperatorMoney(),commercial.getCommercialWithRatio());
                    commercial.setAllWitMoney(commercial.getAllWitMoney()+orderInfo.getOperatorMoney());
                }
                commercial.setFreezeMoney(commercial.getFreezeMoney()-orderInfo.getOperatorMoney()-handMoney);
                //commercial.setCommercialBalance(commercial.getCommercialBalance()+orderInfo.getOperatorMoney()-handMoney);
            }
            log.info("用户修改后信息：{}",commercial);
            commercialService.updateCommercial(commercial);
            return true;
        } catch (Exception e) {
            log.error("{}",e);
            return false;
        }
    }

    /**
     * 商户提现控制金额
     * @param commercial
     * @param orderInfo
     * @return
     */
    private boolean inWithOrder(Commercial commercial,OrderInfo orderInfo){
        try {
            log.info("商户原始信息：{}",commercial);
            //计算金额关系（冻结金额，余额）
            //手续费
            int handMoney = StringUtil.handMoney(orderInfo.getOperatorMoney(),commercial.getCommercialWithRatio());
            //实际金额 = 操作金额 + 手续费
            int allMoney=orderInfo.getOperatorMoney()+handMoney;
            //修改冻结金额
            commercial.setFreezeMoney(commercial.getFreezeMoney()+allMoney);
            //修改余额
            commercial.setCommercialBalance(commercial.getCommercialBalance()-allMoney);
            log.info("商户计算后信息：{}",commercial);
            commercialService.updateCommercial(commercial);
            return true;
        } catch (Exception e) {
            log.error("{}",e);
            return false;
        }
    }
}
