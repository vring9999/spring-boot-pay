package com.instead.pay.qr.controller;

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
import com.instead.pay.qr.model.Qr;
import com.instead.pay.qr.service.QrService;
import com.instead.pay.superpay.service.SuperPayService;
import com.instead.pay.util.*;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/qr")
@Slf4j
public class QrController {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private QrService qrService;
    @Autowired
    private OrderInfoService orderInfoService;

    @Autowired
    private SuperPayService superPayService;

    @Autowired
    private CommercialService commercialService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private AppInfoMapper appInfoMapper;

    /**
     * 添加新的二维码
     *
     * @param qr
     */
    @PostMapping(value = "/insert")
    @ResponseBody
    public JSONObject insert(Qr qr) {
        log.info("/qr/insert  use param --->{}",qr);
        JSONObject jsonObject = new JSONObject();
        if (qr != null) {
            qr.setQrId(StringUtil.getUuid());
            qr.setCreateTime(new Date());
            qrService.insert(qr);
            jsonObject.put("qrId", qr.getQrId());
            jsonObject.put(ResultKey.KEY_MSG, "操作成功");
            jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.SUCCESS_CODE);
        } else {
            jsonObject.put(ResultKey.KEY_MSG, "二维码信息为空");
            jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.FAILUE_CODE);
        }
        return jsonObject;
    }

    /**
     * 启用|禁用二维码
     *
     * @param qrId
     */
    @PostMapping(value = "/updateEnableStatus")
    @ResponseBody
    public JSONObject updateEnableStatus(String qrId,int status) {
        JSONObject jsonObject = new JSONObject();
        Map<String,Object> param = new HashMap<>();
        param.put("qrId",qrId);
        param.put("enableStatus",status);
        qrService.updateEnableStatus(param);
        jsonObject.put(ResultKey.KEY_MSG, "操作成功");
        jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.SUCCESS_CODE);
        return jsonObject;
    }

    /**
     * 查询二维码
     *
     * @param filter
     */
    @PostMapping(value = "/queryQrAll")
    @ResponseBody
    public JSONObject queryQrAll(String filter, Integer limit, Integer page) {
        JSONObject jsonObject = new JSONObject();
        filter = InputInjectFilter.encodeInputString(filter);
        //filter参数转map
        Map<String, Object> param = StringUtil.formatParam(filter);
        try {
            List<Qr> qrList = new ArrayList<Qr>();
            if (StringUtil.isEmpty(page)) {
                qrList = qrService.queryQrAll(param);
            } else {
                PageHelper.startPage(page, limit);
                qrList = qrService.queryQrAll(param);
                PageInfo<Qr> pageInfo = new PageInfo<>(qrList);
                jsonObject.put("total", pageInfo.getTotal());
            }
            jsonObject.put(ResultKey.KEY_SUCC, true);
            jsonObject.put(ResultKey.KEY_LIST_DATA, qrList);
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


    /**
     * 根据Id查二维码信息
     * @return
     */
    @PostMapping(value = "/getQrById")
    @ResponseBody
    public JSONObject getQrById(String id){
        JSONObject jsonObject = new JSONObject();
        try {
            Qr qr=qrService.getQrById(id);
            jsonObject.put(ResultKey.KEY_SUCC, true);
            jsonObject.put(ResultKey.KEY_DATA, qr);
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

    /**
     * 根据Id查二维码信息
     * @return
     */
    @PostMapping(value = "/getQrByOrderId")
    @ResponseBody
    public JSONObject getQrByOrderId(String orderId){
        JSONObject jsonObject = new JSONObject();
        try {
            Map<String,Object> map = qrService.getUrlMoney(orderId);
            jsonObject.put(ResultKey.KEY_SUCC, true);
            jsonObject.put(ResultKey.KEY_DATA, map);
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


    /**
     * @return 浮动金额 二维码地址  订单id
     * @title distributionQrCode
     * @description 分配二维码
     * @author vring
     * @param:
     * @Secured("hasRole('ROLE_ADMIN')") 表示登陆人员需要具备ROLE_ADMIN权限才可以访问此接口
     */
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    @Secured("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/distributionQrCode")
    @ResponseBody
    public JSONObject distributionQrCode(OrderInfo orderInfo, HttpServletRequest request) {
        log.info("distributionQrCode param for orderInfo：{}", GsonUtil.boToString(orderInfo));
        JSONObject jsonObject = new JSONObject();
        try {
            Map<String, Object> map = Request2Map.request2Map(request);
            log.info("自动代收参数：{}",map);
            String appId = orderInfo.getApplicationName();
            AppInfo info = appInfoMapper.getAppById(appId);
            orderInfo.setCommercialNumber(info.getCommercialNumber());
            orderInfo.setApplicationType(UsedCode.APPLICATION_TYPE_IN);
            orderInfo.setOrderId(StringUtil.getOrderId(UsedCode.APPLICATION_TYPE_IN));
            orderInfo.setIsHand(UsedCode.IS_HAND_NO);
            map.put("appKey",info.getAppKey());
            orderInfo.setApplicationType(UsedCode.APPLICATION_TYPE_IN);
            jsonObject = checkParam(orderInfo,map);
            if(!jsonObject.getString(ResultKey.KEY_CODE).equals(ErrorCodeContents.SUCCESS_CODE)){
                return jsonObject;
            }
            if (StringUtil.isEmpty(appId)) {
                jsonObject.put(ResultKey.KEY_SUCC, false);
                jsonObject.put(ResultKey.KEY_MSG, "appId为空");
                jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.PARAMS_INCOMPLETE);
                return jsonObject;
            }
            return addOrder(orderInfo, orderInfo.getCommercialNumber(),false,object2 -> new JSONObject());
        } catch (Exception e) {
            jsonObject.put(ResultKey.KEY_SUCC, false);
            jsonObject.put(ResultKey.KEY_MSG, "操作失败");
            jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.FAILUE_CODE);
            log.error("{}", e);
        }
        return jsonObject;
    }


    /**
     * @return 浮动金额 二维码地址  订单id
     * @title distributionQrCode
     * @description 代付
     * @author vring
     * @param:
     * @Secured("hasRole('ROLE_ADMIN')") 表示登陆人员需要具备ROLE_ADMIN权限才可以访问此接口
     */
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    @Secured("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/replacePay")
    @ResponseBody
    public JSONObject replacePay(OrderInfo orderInfo, HttpServletRequest request) {
        log.info("replacePay param for orderInfo：{}", GsonUtil.boToString(orderInfo));
        JSONObject jsonObject = new JSONObject();
        try {
            Map<String, Object> map = Request2Map.request2Map(request);
            String appId = orderInfo.getApplicationName();
            AppInfo info = appInfoMapper.getAppById(appId);
            orderInfo.setCommercialNumber(info.getCommercialNumber());
            orderInfo.setApplicationType(UsedCode.APPLICATION_TYPE_OUT);
            orderInfo.setOrderId(StringUtil.getOrderId(UsedCode.APPLICATION_TYPE_OUT));
            orderInfo.setIsHand(UsedCode.IS_HAND_NO);
            map.put("appKey",info.getAppKey());
            jsonObject = checkParam(orderInfo,map);
            if(!jsonObject.getString(ResultKey.KEY_CODE).equals(ErrorCodeContents.SUCCESS_CODE)){
                return jsonObject;
            }
            return addOrder(orderInfo, orderInfo.getCommercialNumber(),false,object2 -> new JSONObject());
        } catch (Exception e) {
            jsonObject.put(ResultKey.KEY_SUCC, false);
            jsonObject.put(ResultKey.KEY_MSG, "代付订单发起失败");
            jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.FAILUE_CODE);
            log.error("{}", e);
        }
        return jsonObject;
    }


    public boolean checkMoney(Commercial commercial,Integer floatMoney){

        int handMoney = StringUtil.handMoney(floatMoney, commercial.getCommercialRatio()); //获取抽成
        int real = floatMoney + handMoney;
        //commerNumber:2020021911080301,banlance:479900000,checkMoney:7500,float:2500
        log.info("commerNumber:{},banlance:{},checkMoney:{},float:{}",commercial.getCommercialNumber(),commercial.getCommercialBalance(),real,handMoney);
        if(real > commercial.getCommercialBalance()){
            return false;
        }
        return true;
    }


    public JSONObject checkParam(OrderInfo orderInfo,Map<String,Object> map){
        JSONObject jsonObject = new JSONObject();
        if (StringUtil.isEmpty(orderInfo.getApplicationName())){
            jsonObject.put(ResultKey.KEY_MSG, "appId为空");
            jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.PARAMS_INCOMPLETE);
            jsonObject.put(ResultKey.KEY_SUCC, false);
            return jsonObject;
        }
/*        boolean flag1 = SignVerify.isSign1(map);
        if(!flag1) {
            jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.MD5_DEFEATED);
            jsonObject.put(ResultKey.KEY_MSG, "签名校验失败！");
            return jsonObject;
        }*/
        if(orderInfo.getPayType() == 1 && StringUtil.isEmpty(orderInfo.getBankAccount())){
            jsonObject.put(ResultKey.KEY_MSG, "支付宝账号为空");
            jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.PARAMS_INCOMPLETE);
            jsonObject.put(ResultKey.KEY_SUCC, false);
            return jsonObject;
        }
        if (StringUtil.isEmpty(orderInfo.getCommercialNumber())){
            jsonObject.put(ResultKey.KEY_MSG, "商户号为空");
            jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.PARAMS_INCOMPLETE);
            jsonObject.put(ResultKey.KEY_SUCC, false);
            return jsonObject;
        }
        OrderInfo info = orderInfoService.getOrderInfoByOutId(orderInfo.getOutId());
        if (null != info) {
            jsonObject.put(ResultKey.KEY_MSG, "订单号重复");
            jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.PARAMS_INCOMPLETE);
            jsonObject.put(ResultKey.KEY_SUCC, false);
            return jsonObject;
        }
       /* String regex = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8}$";
        if (Pattern.matches(regex, orderInfo.getBankPhone())) {
        } else {
            jsonObject.put(ResultKey.KEY_MSG, "手机号格式不正确");
            jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.REPEAT_ORDERINFO);
            jsonObject.put(ResultKey.KEY_SUCC, false);
            return jsonObject;
        }*/
        if(UsedCode.APPLICATION_TYPE_OUT == orderInfo.getApplicationType()){
            if(StringUtil.isEmpty(orderInfo.getBankAccount()) || StringUtil.isEmpty(orderInfo.getBankName()) || StringUtil.isEmpty(orderInfo.getBankUserName())){
                jsonObject.put(ResultKey.KEY_MSG, "银行卡相关信息为空");
                jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.REPEAT_ORDERINFO);
                jsonObject.put(ResultKey.KEY_SUCC, false);
                return jsonObject;
            }
        }
        OrderInfo info1 = orderInfoService.getOrderInfoByOutId(orderInfo.getOutId());
        if (null != info1) {
            jsonObject.put(ResultKey.KEY_MSG, "重复订单（外部订单号重复）");
            jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.REPEAT_ORDERINFO);
            jsonObject.put(ResultKey.KEY_SUCC, false);
            return jsonObject;
        }
        jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.SUCCESS_CODE);
        return jsonObject;
    }

    /**
     * 手动调用代收
     *
     * @param request
     * @return
     */
//    @Secured("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/distributionQrCodeByHand")
    @ResponseBody
    public JSONObject manualQrCode(OrderInfo orderInfo,HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        try {
            Map<String, Object> map = Request2Map.req2Map(request);
            log.info("distributionQrCodeByHand param for orderInfo：{}", orderInfo);
            AppInfo info = appInfoMapper.getAppById(orderInfo.getApplicationName());
            orderInfo.setCommercialNumber(info.getCommercialNumber());//商户号
            orderInfo.setApplicationType(UsedCode.APPLICATION_TYPE_IN); //代付的订单类型
            orderInfo.setIsHand(UsedCode.IS_HAND_YES);//是手动挂单
            orderInfo.setOutId(StringUtil.getRandomString(12));//外部单号
            map.put("appKey",info.getAppKey());
            jsonObject = checkParam(orderInfo,map);
            if(!jsonObject.getString(ResultKey.KEY_CODE).equals(ErrorCodeContents.SUCCESS_CODE)){
                return jsonObject;
            }
            String orderId = StringUtil.getOrderId(UsedCode.APPLICATION_TYPE_IN);
            orderInfo.setOrderId(orderId);//订单号
            return addOrder(orderInfo, orderInfo.getCommercialNumber(),true,object2 -> new JSONObject());
        } catch (Exception e) {
            jsonObject.put(ResultKey.KEY_MSG, "操作失败");
            jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.FAILUE_CODE);
            log.error("操作失败：{}", e);
        }
        return jsonObject;
    }

    /**
     * 手动调用代付
     *
     * @param request
     * @return
     */
//    @Secured("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "replaceByHand")
    @ResponseBody
    public JSONObject replaceByHand(OrderInfo orderInfo,HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        try {
            Map<String, Object> map = Request2Map.req2Map(request);
            log.info("replaceByHand param for orderInfo：{}", GsonUtil.boToString(orderInfo));
            AppInfo info = appInfoMapper.getAppById(orderInfo.getApplicationName());
            orderInfo.setCommercialNumber(info.getCommercialNumber());
            orderInfo.setApplicationType(UsedCode.APPLICATION_TYPE_OUT);
            orderInfo.setIsHand(UsedCode.IS_HAND_YES);
            orderInfo.setOutId(StringUtil.getRandomString(12));
            map.put("appKey",info.getAppKey());
            jsonObject = checkParam(orderInfo,map);
            if(!jsonObject.getString(ResultKey.KEY_CODE).equals(ErrorCodeContents.SUCCESS_CODE)){
                return jsonObject;
            }
            String orderId = StringUtil.getOrderId(UsedCode.APPLICATION_TYPE_OUT);
            orderInfo.setOrderId(orderId);
            return addOrder(orderInfo, orderInfo.getCommercialNumber(),true,object2 -> new JSONObject());
        } catch (Exception e) {
            jsonObject.put(ResultKey.KEY_MSG, "操作失败");
            jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.FAILUE_CODE);
            log.error("操作失败：{}", e);
        }
        return jsonObject;
    }



    /**
     * 添加订单封装
     *
     * @param orderInfo
     * @param function
     *  @param flag   是否为手动挂单
     * @return
     * @throws Exception
     */
    public JSONObject addOrder(OrderInfo orderInfo, String commercialNumber,boolean flag,Function<JSONObject, JSONObject> function) {
        JSONObject jsonObject = new JSONObject();
        try {
            Commercial commercial = commercialService.getCommercialByNum(commercialNumber);
            // 代付订单验证商户余额
            boolean is_in = false;
            if(UsedCode.APPLICATION_TYPE_OUT == orderInfo.getApplicationType()){
                is_in = true;
                boolean enough = checkMoney(commercial, orderInfo.getOperatorMoney());
                if(!enough){
                    log.error("商户余额不足");
                    jsonObject.put(ResultKey.KEY_MSG, "商户余额不足！");
                    jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.FAILUE_CODE);
                    jsonObject.put(ResultKey.KEY_SUCC, false);
                    return jsonObject;
                }
            }
            orderInfo.setFloatMoney( orderInfo.getOperatorMoney());
            orderInfo.setOrderStatus(UsedCode.ORDER_STATUS_WAIT);
            orderInfo.setCreateTime(new Date());
            orderInfo.setNum(1);
            orderInfo.setCallBackNum(0);
            orderInfo.setBackStatus(UsedCode.ORDER_STATUS_CANCEL);
            orderInfo.setLine(UsedCode.ORDER_STATUS_CANCEL);
            if(is_in){
                orderInfoService.insertOrderInfo_out(orderInfo);
            }else{
                orderInfoService.insertOrderInfo(orderInfo);
            }
            //代付改冻结
            if(is_in){
                //修改用户金额
                if(!InOrder(commercial,orderInfo)){
                    jsonObject.put(ResultKey.KEY_SUCC, false);
                    jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.CONNECT_TIMED_OUT);
                    jsonObject.put(ResultKey.KEY_MSG, "用户金额计算失败！");
                    return jsonObject;
                }
            }
            //请求四方   如果是手动挂单---传入系统单号  非手动挂单---传入外部单号
            String order_id = "";
            if(flag) order_id = orderInfo.getOrderId();
            else order_id = orderInfo.getOutId();
            String response = "";
            if(is_in){//代付
                response = superPayService.systemPayNew(orderInfo,order_id);
            }else{//代收
                response = superPayService.userPayNew(order_id,orderInfo);
            }
            log.info("请求四方--》响应结果：{}",response);
            JSONObject resJSON = JSONObject.parseObject(response);
            Integer code = resJSON.getInteger("code");
            String remitUrl = "";
            if(code == 1) {
                log.info("操作成功");
                JSONObject jsonObject1 = resJSON.getJSONObject("data");
                if(is_in){
                    String repay_order_no = jsonObject1.getString("repay_order_no");
                    log.info("代付返回单号：{}",repay_order_no);
                }else{
                    remitUrl = jsonObject1.getString("url");
                    jsonObject.put(ResultKey.KEY_DATA,remitUrl);
                    orderInfo.setRemitUrl(remitUrl);
                    orderInfoService.updateOrderInfo(orderInfo);
                }
                jsonObject.put(ResultKey.KEY_MSG, "操作成功");
                jsonObject.put(ResultKey.KEY_SUCC,true);
                jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.SUCCESS_CODE);
                return jsonObject;
            }else{
                log.error("订单发起请求四方操作失败");
                jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.FAILUE_CODE);
                jsonObject.put(ResultKey.KEY_MSG, response);
                jsonObject.put(ResultKey.KEY_SUCC,false);
                return jsonObject;
            }
        } catch (Exception e) {
            log.error("订单封装失败：{}", e);
            jsonObject.put(ResultKey.KEY_SUCC,false);
            jsonObject.put(ResultKey.KEY_MSG, "操作失败！");
            jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.FAILUE_CODE);
        }
        return jsonObject;
    }



    /**
     * 校验token
     *
     * @param tokenId
     * @param appId
     * @param commercialNumber
     * @param function
     * @return
     */
    private JSONObject isToken(String tokenId, String appId, String commercialNumber, Function<JSONObject, JSONObject> function) {
        JSONObject jsonObject = new JSONObject();
        try {
            String token = redisUtil.get(tokenId);
            if (token != null) {
                Claims claims = JwtUtils.parseJWT(token);
                Long time = claims.getIssuedAt().getTime();
                if (System.currentTimeMillis() - time < 60000) {
                    if (tokenId.equals(claims.getId()) && appId.equals(claims.getIssuer()) && commercialNumber.equals(claims.getSubject())) {
                        return function.apply(jsonObject);
                    } else {
                        log.error("Token校验失败:{}");
                        jsonObject.put(ResultKey.KEY_MSG, "Token校验失败");
                    }
                } else {
                    log.error("Token已过期:{}");
                    jsonObject.put(ResultKey.KEY_MSG, "Token已过期");
                }
            } else {
                log.error("Token不存在:{}");
                jsonObject.put(ResultKey.KEY_MSG, "Token不存在");
            }
        } catch (ExpiredJwtException e) {
            log.error("Token已过期:{}", e);
            jsonObject.put(ResultKey.KEY_MSG, "Token已过期");
        } catch (UnsupportedJwtException e) {
            log.error("Token格式错误: {} " + e);
            jsonObject.put(ResultKey.KEY_MSG, "Token格式错误");
        } catch (MalformedJwtException e) {
            log.error("Token没有被正确构造: {} " + e);
            jsonObject.put(ResultKey.KEY_MSG, "Token没有被正确构造");
        } catch (SignatureException e) {
            log.error("签名失败: {} " + e);
            jsonObject.put(ResultKey.KEY_MSG, "签名失败");
        } catch (IllegalArgumentException e) {
            log.error("非法参数异常: {} " + e);
            jsonObject.put(ResultKey.KEY_MSG, "非法参数异常");
        }
        return jsonObject;
    }


    /*
     * @title   回调给商户
     * @description  回调方法
     * @author vring
     * @param: map 回调内容   callBackUrl 回调地址
     * @throws
     */
    private JSONObject callBack(String emitUrl, String callBackUrl){
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
//            httpHeaders.set("Content-type", "application/json; charset=utf-8");// 设置编码
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);// 设置编码
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add(ResultKey.KEY_SUCC, "true");
            map.add(ResultKey.KEY_CODE, ErrorCodeContents.SUCCESS_CODE);
            map.add(ResultKey.KEY_DATA, emitUrl);
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, httpHeaders);
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(callBackUrl, request, String.class);
            String responseInfo = responseEntity.getBody();
            log.info("responseInfo:{}", responseInfo);
            JSONObject  jsonObject = JSON.parseObject(responseInfo);
            if (!"200".equals(jsonObject.getString("status"))) {
                log.info("商户回调失败");
                resultJsonObject.put(ResultKey.KEY_SUCC, false);
                resultJsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.FAILUE_CODE);
                resultJsonObject.put(ResultKey.KEY_MSG, "商户回调失败");
            } else {
                log.info("商户回调成功");
            }
        }  catch (ResourceAccessException r){
            log.error("回调超时，请检查回调地址：{}", r);
//            UrlResponse response1 = new UrlResponse(false, ErrorCodeContents.CONNECT_TIMED_OUT, "回调超时，请检查回调地址", "");
        }catch (RestClientException e) {
            log.error("回调失败,数据格式不正确:{}", e);
        }
        return resultJsonObject;
    }

    //计算商户金额 --下单（目前只算-代付）
    private boolean InOrder(Commercial commercial,OrderInfo orderInfo){
        try {
            //手续费
            Integer handMoney = StringUtil.handMoney(orderInfo.getOperatorMoney(),commercial.getCommercialRatio());
            //修改余额
            commercial.setCommercialBalance(commercial.getCommercialBalance()-orderInfo.getOperatorMoney()-handMoney);
            //修改冻结金额
            commercial.setFreezeMoney(commercial.getFreezeMoney()+orderInfo.getOperatorMoney()+handMoney);
            commercialService.updateCommercial(commercial);
            return true;
        } catch (Exception e) {
            log.error("{}",e);
            return false;
        }
    }

}
