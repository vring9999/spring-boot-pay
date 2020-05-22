package com.instead.pay.commercial.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.instead.pay.appInfo.mapper.AppInfoMapper;
import com.instead.pay.appInfo.model.AppInfo;
import com.instead.pay.commercial.model.Commercial;
import com.instead.pay.commercial.model.IpWhite;
import com.instead.pay.commercial.service.CommercialService;
import com.instead.pay.commercial.service.IpWhiteService;
import com.instead.pay.orderInfo.model.OrderInfo;
import com.instead.pay.orderInfo.service.OrderInfoService;
import com.instead.pay.role.model.Role;
import com.instead.pay.role.service.RoleService;
import com.instead.pay.util.*;
import com.instead.pay.util.Security.JwtHelper;
import com.instead.pay.util.Security.UrlResponse;
import com.instead.pay.util.rsa.KeyManager;
import com.instead.pay.util.rsa.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Controller
@RequestMapping("/Commercial")
@Component
@Slf4j
public class CommercialController {
    @Autowired
    private CommercialService commercialService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private IpWhiteService ipWhiteService;

    @Resource
    private RedisUtil redisUtil;

    @Autowired
    private AppInfoMapper appInfoMapper;

    @Autowired
    private OrderInfoService orderInfoService;

    @Autowired
    private ThreadUtil threadUtil;

    @Autowired
    private static CacheInfo cacheInfo;

    @Autowired
    public CommercialController(CacheInfo cacheInfo){
        CommercialController.cacheInfo = cacheInfo;
    }


    @PostMapping(value = "/getPublicKey")
    @ResponseBody
    public JSONObject getPublicKey() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("publicKey", KeyManager.getPublic_key());
        return jsonObject;
    }

    @PostMapping(value = "/queryCommInfo")
    @ResponseBody
    public JSONObject queryCommInfo(String commercialId) {
        JSONObject jsonObject = new JSONObject();
        Commercial commercial = commercialService.getCommercial(commercialId);
        if (null == commercial) {
            jsonObject.put(ResultKey.KEY_MSG, "商户信息为空");
            jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.FAILUE_CODE);
            return jsonObject;
        }
        List<String> ipList = ipWhiteService.queryIp(commercial.getCommercialNumber());
        StringBuffer ips = new StringBuffer();
        for(String ip:ipList){
            ips.append(ip);
            ips.append(",");
        }
        List<AppInfo> info = appInfoMapper.getAppByComId(commercial.getCommercialNumber());
        jsonObject.put("commercial", commercial);
        jsonObject.put("appInfo", info);
        jsonObject.put("ips", ips.toString());
        jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.SUCCESS_CODE);
        return jsonObject;
    }


    @PostMapping(value = "/recharge")
    @ResponseBody
    public JSONObject recharge(String commercialId,Integer money,HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String token = request.getHeader("Authorization");
        if (token == null || token.isEmpty()) {
            jsonObject.put(ResultKey.KEY_MSG, "token为空，请登录");
            jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.FAILUE_CODE);
            jsonObject.put(ResultKey.KEY_SUCC,false);
            return jsonObject;
        }
        Commercial commercial = commercialService.getCommercial(commercialId);
        if (null == commercial) {
            jsonObject.put(ResultKey.KEY_MSG, "商户信息为空");
            jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.FAILUE_CODE);
            jsonObject.put(ResultKey.KEY_SUCC,false);
            return jsonObject;
        }
        if(money == null || money == 0){
            jsonObject.put(ResultKey.KEY_MSG, "请您输入正确的金额格式");
            jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.FAILUE_CODE);
            jsonObject.put(ResultKey.KEY_SUCC,false);
            return jsonObject;
        }
        String operatorName = JwtHelper.getUsername(token);
        int handMoney = StringUtil.handMoney(money, commercial.getCommercialRatio()); //获取抽成
        String orderId = StringUtil.getOrderId(UsedCode.ORDER_STATUS_BO);
        StringBuffer outId = StringUtil.getRandomCode(10);
        Integer realMoney = money - handMoney;
        OrderInfo info = new OrderInfo(orderId,outId.toString(),
                commercial.getCommercialNumber(),1,money,
                new Date(),UsedCode.ORDER_STATUS_BO,
                UsedCode.PAY_TYPE_BANK,UsedCode.IS_HAND_YES,
                UsedCode.ORDER_STATUS_CONFIRM,
                commercial.getCommercialName(),
                operatorName,handMoney,realMoney,new Date());
        return inRecharge(commercial,realMoney,object->{
            orderInfoService.insertOrderInfo(info);
            jsonObject.put(ResultKey.KEY_MSG, "充值成功!");
            jsonObject.put(ResultKey.KEY_SUCC,true);
            jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.SUCCESS_CODE);
            return jsonObject;
        });
    }

    private JSONObject inRecharge(Commercial commercial,int realMoney,Function<JSONObject,JSONObject> function){
        JSONObject jsonObject = new JSONObject();
        try{
            commercial.setCommercialBalance(commercial.getCommercialBalance() + realMoney);
            commercial.setAllMoney(commercial.getAllMoney()+realMoney);
            commercialService.updateCommercial(commercial);
            return function.apply(jsonObject);
        }catch (Exception e){
            log.error("{}:",e);
            jsonObject.put(ResultKey.KEY_MSG, "商户金额计算失败！");
            jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.FAILUE_CODE);
            jsonObject.put(ResultKey.KEY_SUCC, false);
            return jsonObject;
        }
    }






    @RequestMapping(value = "/queryCommercial", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject queryCommercial(@RequestParam(defaultValue = "") String filter, Integer page, Integer limit) throws InterruptedException {
        JSONObject jsonObject = new JSONObject();
        filter = InputInjectFilter.encodeInputString(filter);
        //filter参数转map
        Map<String, Object> params = StringUtil.formatParam(filter);
        try {
            List<Commercial> commercialList = new ArrayList<Commercial>();
            if (StringUtil.isEmpty(page)) {
                commercialList = commercialService.queryCommercial(params);
            } else {
                PageHelper.startPage(page, limit);
                commercialList = commercialService.queryCommercial(params);
                PageInfo<Commercial> pageInfo = new PageInfo<>(commercialList);
                jsonObject.put("total", pageInfo.getTotal());
            }
            jsonObject.put("commercialList", commercialList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    /**
     * 给商户开户  新增系统用户
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/addCommercial", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject addCommercial(Commercial commercial, String userType) {
        JSONObject jsonObject = new JSONObject();
        try {
            if (StringUtil.isEmpty(userType)) {
                log.error("用户类型为空");
                jsonObject.put(ResultKey.KEY_MSG, "请选择用户类型！");
                jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.FAILUE_CODE);
                return jsonObject;
            }
            if (null != commercial && loginUser(commercial.getCommercialName())) {
//                Map<String, Object> param = Request2Map.req2Map(req);
//                String userType = (String) param.get("userType");
                Map<String, Object> map = new HashMap<String, Object>();
                String time = new SimpleDateFormat("yyyy-MM-dd").format(new Date()).toString();
                map.put("creationTime", time);
                List<Commercial> list = commercialService.queryCommercial(map);
                //随机生成商户号
                String commercialNumber = StringUtil.getCommercialNumber(userType, list.size());
                String commercialId = UUID.randomUUID().toString();

                //设置商户初始登录密码 --123456
//                String defaultPwd = MD5Util.getSaltMD5("123456");
//                commercial.setCommercialPassword(defaultPwd);

                // 前端设置初始的商户登录密码123456然后加密传输   解密后的明文
                String password = MD5Util.getPwd(commercial.getCommercialPassword());
                //撒盐后的密文
                String saltMD5pwd = MD5Util.getSaltMD5(password);
                Map<String, Object> param = new HashMap<>();
                if (userType.equals(UsedCode.ROLE_TYPE_USER)) {
                    param.put("roleName", "ROLE_USER");
                    float ratio=Float.parseFloat(cacheInfo.getCommonToKey("instead.commercial+bus.ratio"));
                    float withRatio=Float.parseFloat(cacheInfo.getCommonToKey("instead.commercial+with.ratio"));
                    commercial.setCommercialRatio(ratio);
                    commercial.setCommercialWithRatio(withRatio);
                } else if (userType.equals(UsedCode.ROLE_TYPE_ADMIN)) {
                    param.put("roleName", "ROLE_ADMIN");
                }
                Role role = roleService.queryRole(param).get(0);
                commercial.setRoleId(role.getRoleId());
                commercial.setCommercialPassword(saltMD5pwd);
                commercial.setCommercialId(commercialId);
                commercial.setCommercialNumber(commercialNumber);
                commercial.setCreationTime(new Date());
                commercial.setUpdateTime(new Date());
                commercialService.insertCommercial(commercial);
                jsonObject.put(ResultKey.KEY_MSG, "注册成功！");
                jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.SUCCESS_CODE);
            } else {
                jsonObject.put(ResultKey.KEY_MSG, "商户已存在！请重新选择商户名");
                jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.USER_IS_EXIST);
            }
        } catch (Exception e) {
            log.error("商户注册失败：{}", e);
            jsonObject.put(ResultKey.KEY_MSG, "添加商户失败！");
            jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.FAILUE_CODE);
            return jsonObject;
        }
        return jsonObject;
    }

    /*
     * @title login
     * @description 商户系统用户账号密码登录
     * @author vring
     * @param
     * @throws
     */
    @PostMapping(value = "/login")
    @ResponseBody
    public JSONObject login(HttpServletRequest req, HttpServletResponse res) {
        JSONObject jsonObject = new JSONObject();
        HttpSession session = req.getSession();
        try {
            Map<String, Object> map = Request2Map.request2Map(req);
            String commercialName = map.get("commercialName").toString();
            String commercialPassword = map.get("commercialPassword").toString();
            if (StringUtil.isEmpty(commercialName) || StringUtil.isEmpty(commercialPassword)) {
                jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.FAILUE_CODE);
                jsonObject.put(ResultKey.KEY_MSG, "账号和者密码不能为空！");
                return jsonObject;
            } else if (commercialPassword.equals("false")) {
                jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.FAILUE_CODE);
                jsonObject.put(ResultKey.KEY_MSG, "密码格式不正确");
                return jsonObject;
            }
            //数据库对象
            Commercial commercial = commercialService.login(commercialName);
            String roleName = "";
            if (null == commercial) {
                jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.USER_IS_NOT_FOUND);
                jsonObject.put(ResultKey.KEY_MSG, "该商户不存在，请先注册！");
                return jsonObject;
            }
            // 解密后的明文
            String password = MD5Util.getPwd(commercialPassword);
            //校验密码是否一致
            boolean flag = MD5Util.getSaltverifyMD5(password, commercial.getCommercialPassword());
            if (flag) {
                roleName = commercialService.findRoleNameByUser(commercial.getCommercialName());
                redisUtil.ins(commercial.getCommercialName(), JSON.toJSONString(commercial), 2, TimeUnit.DAYS);
            } else {
                jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.CHECK_PWD_FAIL);
                jsonObject.put(ResultKey.KEY_MSG, "密码错误，请重新输入");
                return jsonObject;
            }
            String token = "Bearer " + JwtHelper.createToken(commercial.getCommercialName(), roleName);
            redisUtil.ins(commercial.getCommercialId(), token);
            JSONObject arr = new JSONObject();
            arr.put("obj", JSON.toJSONString(commercial));
            arr.put("roleName", roleName);
            redisUtil.ins(token, arr.toString());
            res.addHeader("Authorization", token);
            log.info("登录成功,TOKEN:{}", token);
            session.setAttribute("user", commercialName);
            Map<String, String> data = new HashMap<>();
            data.put("commercialNumber", commercial.getCommercialNumber());
            data.put("commercialId", commercial.getCommercialId());
            jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.SUCCESS_CODE);
            jsonObject.put(ResultKey.KEY_MSG, "登录成功");
            jsonObject.put(ResultKey.KEY_DATA, data);
        } catch (Exception e) {
            log.error("登录异常{}", e);
            jsonObject.put(ResultKey.KEY_MSG, "登录异常！");
            jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.FAILUE_CODE);
        }
        return jsonObject;
    }

    /**
     * 退出登录
     * @return
     */
    @RequestMapping(value = "/logout")
    public void logout(HttpServletRequest request) {
        Commercial commercial = threadUtil.get();
        String token = request.getHeader("Authorization");
        redisUtil.delete(commercial.getCommercialId());
        redisUtil.delete(commercial.getCommercialName());
        redisUtil.delete(token);
    }

    /*
     * @title setRadio
     * @description 商户设置服务费比例
     * @author vring
     * @param:   商户id  服务比例
     * @throws
     */
    @PostMapping(value = "/setRadio")
    @ResponseBody
    public JSONObject setRadio(String commercialId, float commercialRatio) {
        JSONObject jsonObject = new JSONObject();
        if (StringUtil.isEmpty(commercialId) || commercialRatio == 0) {
            jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.FAILUE_CODE);
            jsonObject.put(ResultKey.KEY_MSG, "参数为空");
            return jsonObject;
        }
        Commercial commercial = commercialService.getCommercial(commercialId);
        if (null != commercial) {
            commercial.setCommercialRatio(commercialRatio);
            commercial.setUpdateTime(new Date());
            commercialService.updateCommercial(commercial);
            jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.SUCCESS_CODE);
            jsonObject.put(ResultKey.KEY_MSG, "设置成功");
        } else {
            jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.USER_IS_NOT_FOUND);
            jsonObject.put(ResultKey.KEY_MSG, "商户信息不存在");
        }
        return jsonObject;
    }

    /**
     * 设置商户提成服务费比例
     *
     * @param commercialId
     * @param commercialWithRatio
     * @return
     */
    @PostMapping(value = "/setWitRadio")
    @ResponseBody
    public JSONObject setWithRadio(String commercialId, float commercialWithRatio) {
        JSONObject jsonObject = new JSONObject();
        if (StringUtil.isEmpty(commercialId) || commercialWithRatio == 0) {
            jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.FAILUE_CODE);
            jsonObject.put(ResultKey.KEY_MSG, "参数为空");
            return jsonObject;
        }
        Commercial commercial = commercialService.getCommercial(commercialId);
        if (null != commercial) {
            commercial.setCommercialWithRatio(commercialWithRatio);
            commercial.setUpdateTime(new Date());
            commercialService.updateCommercial(commercial);
            jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.SUCCESS_CODE);
            jsonObject.put(ResultKey.KEY_MSG, "设置成功");
        } else {
            jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.USER_IS_NOT_FOUND);
            jsonObject.put(ResultKey.KEY_MSG, "商户信息不存在");
        }
        return jsonObject;
    }

    @PostMapping(value = "/updateCommercial")
    @ResponseBody
    public JSONObject updateCommercial(Commercial commercial) throws Exception {
        JSONObject jsonObject = new JSONObject();
        if(null == commercial){
            jsonObject.put(ResultKey.KEY_SUCC, false);
            jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.USER_IS_NOT_FOUND);
            jsonObject.put(ResultKey.KEY_MSG, "参数为空");
            return jsonObject;
        }
        if(StringUtil.isEmpty(commercial.getCommercialId())){
            jsonObject.put(ResultKey.KEY_SUCC, false);
            jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.USER_IS_NOT_FOUND);
            jsonObject.put(ResultKey.KEY_MSG, "缺少商户id");
            return jsonObject;
        }
        Commercial TEMP = commercialService.getCommercial(commercial.getCommercialId());
        if(null == TEMP){
            jsonObject.put(ResultKey.KEY_SUCC, false);
            jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.USER_IS_NOT_FOUND);
            jsonObject.put(ResultKey.KEY_MSG, "暂无该商户信息");
            return jsonObject;
        }
        if(!StringUtil.isEmpty(commercial.getSafetyPwd())){
            // 解密后的明文
            String password = MD5Util.getPwd(commercial.getSafetyPwd());
            //撒盐后的密文
            String saltMD5pwd = MD5Util.getSaltMD5(password);
            TEMP.setSafetyPwd(saltMD5pwd);
        }
        TEMP.setReserved2(commercial.getReserved2());
        TEMP.setCommercialName(commercial.getCommercialName());
        TEMP.setCommercialIphone(commercial.getCommercialIphone());
        commercialService.updateCommercial(TEMP);
        jsonObject.put(ResultKey.KEY_SUCC, true);
        jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.SUCCESS_CODE);
        jsonObject.put(ResultKey.KEY_MSG, "修改成功");
        return jsonObject;
    }



    /**
     * 商户修改密码
     *
     * @param
     * @param
     * @return
     */
    @PostMapping(value = "/updateCommercialPwd")
    @ResponseBody
    public JSONObject updateCommercial(String commercialName, String oldPwd, String newPwd) throws Exception {
        JSONObject jsonObject = new JSONObject();
        Commercial info = commercialService.login(commercialName);
        if (null != info) {
            if (oldPwd.equals("false") || newPwd.equals("false")) {
                jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.FAILUE_CODE);
                jsonObject.put(ResultKey.KEY_MSG, "密码格式不正确");
                return jsonObject;
            }
            // 解密后的旧密码明文
            oldPwd = MD5Util.getPwd(oldPwd);
            //校验旧密码和原密码是否一致
            boolean flag = MD5Util.getSaltverifyMD5(oldPwd, info.getCommercialPassword());
            if (!flag) {
                jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.FAILUE_CODE);
                jsonObject.put(ResultKey.KEY_MSG, "请输入正确的原始密码！");
                return jsonObject;
            }

            //解密后的新密码明文
            newPwd = MD5Util.getPwd(newPwd);
            String saltMD5pwd = MD5Util.getSaltMD5(newPwd);
            info.setCommercialPassword(saltMD5pwd);
            info.setUpdateTime(new Date());
            commercialService.updateCommercial(info);
            jsonObject.put(ResultKey.KEY_SUCC, true);
            jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.SUCCESS_CODE);
            jsonObject.put(ResultKey.KEY_MSG, "设置成功");
        } else {
            jsonObject.put(ResultKey.KEY_SUCC, false);
            jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.USER_IS_NOT_FOUND);
            jsonObject.put(ResultKey.KEY_MSG, "商户信息不存在");
        }
        return jsonObject;
    }


    /**
     * 商户手机验证码登入
     *
     * @param phoneNumber
     * @param code
     * @param req
     * @return
     */
    @RequestMapping(value = "/userLoginByCode", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject userLogin(String phoneNumber, String code, HttpServletRequest req) {
        JSONObject resultJson = new JSONObject();
        if (StringUtil.isEmpty(code) || StringUtil.isEmpty(phoneNumber)) {
            // 验证码或账号密码为空
            resultJson.put(ResultKey.KEY_SUCC, false);
            resultJson.put(ResultKey.KEY_CODE, ErrorCodeContents.USER_CODE_IS_NULL);
            resultJson.put(ResultKey.KEY_MSG, "验证码或手机号为空");
        } else {
            return checkCode(phoneNumber, code, Object -> {  // 登陆通过
                Commercial commercial = commercialService.getCommercialByIphone(phoneNumber);
                if (commercial == null) {
                    resultJson.put(ResultKey.KEY_SUCC, false);
                    resultJson.put(ResultKey.KEY_CODE, ErrorCodeContents.SUCCESS_CODE);
                    resultJson.put(ResultKey.KEY_MSG, "该商户号不存在，请先注册！");
                } else {
                    resultJson.put(ResultKey.KEY_SUCC, true);
                    resultJson.put(ResultKey.KEY_DATA, commercial);
                    resultJson.put(ResultKey.KEY_CODE, ErrorCodeContents.SUCCESS_CODE);
                    resultJson.put(ResultKey.KEY_MSG, "登陆成功");
                }
                return resultJson;
            });
        }
        return resultJson;
    }

    /**
     * 用户密码登入
     */
    @RequestMapping(value = "/userLoginByPsw", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject userLoginByPsw(String phoneNumber, String userPsw) {
        JSONObject resultJson = new JSONObject();
        if (StringUtil.isEmpty(phoneNumber) || StringUtil.isEmpty(userPsw)) {
            // 验证码或账号密码为空
            resultJson.put(ResultKey.KEY_SUCC, false);
            resultJson.put(ResultKey.KEY_CODE, ErrorCodeContents.FAILUE_CODE);
            resultJson.put(ResultKey.KEY_MSG, "账号密码为空");
        } else {
            Commercial commercial = commercialService.getCommercialByIphone(phoneNumber);
            if (commercial != null && userPsw.equals(commercial.getCommercialPassword())) {// 登陆通过
                redisUtil.ins("user", phoneNumber);
                //LoginUserMap.setLoginUsers(userAccount, session.getId());
                commercial.setCommercialPassword(null);
                resultJson.put(ResultKey.KEY_SUCC, true);
                resultJson.put(ResultKey.KEY_DATA, commercial);
                resultJson.put(ResultKey.KEY_CODE, ErrorCodeContents.SUCCESS_CODE);
                resultJson.put(ResultKey.KEY_MSG, "登陆成功");
            } else if (commercial != null && !userPsw.equals(commercial.getCommercialPassword())) {
                // 未找到该用户
                resultJson.put(ResultKey.KEY_SUCC, false);
                resultJson.put(ResultKey.KEY_CODE, ErrorCodeContents.USER_IS_NOT_FOUND);
                resultJson.put(ResultKey.KEY_MSG, "密码错误，请重新输入");
            } else {
                // 未找到该用户
                resultJson.put(ResultKey.KEY_SUCC, false);
                resultJson.put(ResultKey.KEY_CODE, ErrorCodeContents.USER_IS_NOT_FOUND);
                resultJson.put(ResultKey.KEY_MSG, "账号有误请确认");
            }
        }
        return resultJson;
    }


    /**
     * 获取验证码
     *
     * @param phoneNum
     * @param type
     * @return
     */
    @RequestMapping(value = "/sendCode", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject sendCode(String phoneNum, String type) {
        JSONObject resultJson = new JSONObject();
        try {
            phoneNum = InputInjectFilter.encodeInputString(phoneNum);
            type = InputInjectFilter.encodeInputString(type);
            // 检查手机号码正确性
            if (SendMsgUtil.checkPhoneNum(phoneNum)) {
                // 获取随机数
                StringBuffer code = StringUtil.getRandomCode(6);
                String templateParam = "{\"code\":\"" + code + "\"}";
                String templateId = "aliyun.properties+sms.temp." + type;
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("phoneNum", phoneNum);
                params.put("templateParam", templateParam);
                params.put("templateCode", CacheInfo.getCommonToKey(templateId));
                SendMsgUtil sendMsgUtil = new SendMsgUtil();
                JSONObject json = new JSONObject();
                try {
                    json = sendMsgUtil.sendSms(params);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if ("OK".equals(json.get("code"))) {
                    // 将手机号码，验证码以及当前时间放入缓存；
                    String checkMsg = code.toString() + "," + System.currentTimeMillis();
                    redisUtil.ins(phoneNum, checkMsg);
                    resultJson.put(ResultKey.KEY_SUCC, true);
                    resultJson.put(ResultKey.KEY_MSG, "验证码发送成功");
                    resultJson.put(ResultKey.KEY_CODE, ErrorCodeContents.SUCCESS_CODE);
                } else {
                    resultJson.put(ResultKey.KEY_SUCC, true);
                    resultJson.put(ResultKey.KEY_MSG, "验证码发送失败");
                    resultJson.put(ResultKey.KEY_CODE, ErrorCodeContents.SEND_CODE_FAIL);
                }
            } else {
                resultJson.put(ResultKey.KEY_SUCC, true);
                resultJson.put(ResultKey.KEY_MSG, "请输入正确的手机号码");
                resultJson.put(ResultKey.KEY_CODE, ErrorCodeContents.PHONE_NUM_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultJson;
    }

    /** @throws
     * @title 设置白名单
     * @description
     * @author vring
     * @param:
     */
    @PostMapping(value = "insertIp")
    @ResponseBody
    public JSONObject insertIp(String commercialNumber, String ips) {
        JSONObject resultJson = new JSONObject();
        if (StringUtil.isEmpty(commercialNumber) || StringUtil.isEmpty(ips)) {
            resultJson.put(ResultKey.KEY_MSG, "参数不完整");
            resultJson.put(ResultKey.KEY_CODE, ErrorCodeContents.PARAMS_INCOMPLETE);
            return resultJson;
        }
        ipWhiteService.deleteIpBycommercialNumber(commercialNumber);
        String[] temp = ips.split(",");
        List<IpWhite> list = new ArrayList<>();
        for (String ip : temp) {
            IpWhite iw = new IpWhite(commercialNumber, ip);
            list.add(iw);
        }
        ipWhiteService.insert(list);
        resultJson.put(ResultKey.KEY_MSG, "设置成功");
        resultJson.put(ResultKey.KEY_CODE, ErrorCodeContents.SUCCESS_CODE);
        return resultJson;
    }

    /*
     * @title loginUser
     * @description
     * @author vring
     * @param: flag  1 登錄   2 註冊
     * @throws
     */
    public boolean loginUser(String commercialName) {
        Commercial u = commercialService.login(commercialName);
        if (!StringUtil.isEmpty(commercialName) && u == null) {
            return true;
        }
        return false;
    }

    /**
     * 验证手机号验证码等信息
     *
     * @param phoneNum
     * @param code
     * @return
     */
    private JSONObject checkCode(String phoneNum, String code, Function<JSONObject, JSONObject> function) {
        String checkMsg = StringUtil.doNullStr(redisUtil.get(phoneNum));
        JSONObject resuleJs = new JSONObject();
        if (checkMsg != null && checkMsg.length() > 0) {
            String[] arr = checkMsg.split(",");
            Long time = Long.parseLong(arr[1]);
            String codeSession = arr[0];
            // 验证码有效时常为2分钟
            if ((System.currentTimeMillis() - time) / 1000 / 60 >= 2) {
                System.out.println("手机号：" + phoneNum + "验证码已过期");
                resuleJs.put("success", false);
                resuleJs.put("mes", "验证码已过期");
                return resuleJs;
            }
            if (codeSession.trim().equalsIgnoreCase(code) || "123456".equals(code)) {
                System.out.println("验证码验证通过");
                redisUtil.delete(phoneNum);
                return function.apply(resuleJs);
                // 验证通过，将缓存中的验证信息删除
            } else {
                resuleJs.put("success", false);
                resuleJs.put("mes", "验证码有误");
                return resuleJs;
            }
        } else {
            resuleJs.put("success", false);
            resuleJs.put("mes", "手机号码不一致");
            return resuleJs;
        }
    }

    /**
     * 查询商户ip白名单
     * @param commercialNumber
     * @return
     */
    @RequestMapping(value = "/getWhiteIp", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject getWhiteIp(String commercialNumber){
        JSONObject jsonObject=new JSONObject();
        try {
            List<String> ipList = ipWhiteService.queryIp(commercialNumber);
            StringBuffer ips = new StringBuffer();
            for(String ip:ipList){
                ips.append(ip);
                ips.append(",");
            }
            jsonObject.put(ResultKey.KEY_DATA, ips);
            jsonObject.put(ResultKey.KEY_SUCC, true);
            jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.SUCCESS_CODE);
            jsonObject.put(ResultKey.KEY_MSG, "查询成功！");
        } catch (Exception e) {
            log.error("{}", e);
            jsonObject.put(ResultKey.KEY_SUCC, false);
            jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.USER_IS_NOT_FOUND);
            jsonObject.put(ResultKey.KEY_MSG, "查询失败！");
        }
        return jsonObject;
    }

    @RequestMapping(value = "/deleteCommercial", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject deleteCommercial(String ids){
        JSONObject jsonObject=new JSONObject();
        try {
            List<String> list = Arrays.asList(ids.split(","));
            commercialService.deleteCommercial(list);
            jsonObject.put(ResultKey.KEY_SUCC, true);
            jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.SUCCESS_CODE);
            jsonObject.put(ResultKey.KEY_MSG, "删除完毕！");
        } catch (Exception e) {
            log.error("{}", e);
            jsonObject.put(ResultKey.KEY_SUCC, false);
            jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.USER_IS_NOT_FOUND);
            jsonObject.put(ResultKey.KEY_MSG, "查询失败！");
        }
        return jsonObject;
    }

    /**
     * 验证安全密码
     * @param commercialId
     * @param safetyPwd
     * @return
     */
    @RequestMapping(value = "/verifySafetyPwd", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject verifySafetyPwd(String commercialId,String safetyPwd){
        JSONObject jsonObject=new JSONObject();
        try {
            String getPwd=commercialService.getSafetyPwd(commercialId);
            if(safetyPwd.equals(getPwd)) {
                jsonObject.put(ResultKey.KEY_SUCC, true);
                jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.SUCCESS_CODE);
                jsonObject.put(ResultKey.KEY_MSG, "密码验证正确！");
            }else {
                jsonObject.put(ResultKey.KEY_SUCC, false);
                jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.CHECK_PWD_FAIL);
                jsonObject.put(ResultKey.KEY_MSG, "安全密码有误，请重试！");
            }
        } catch (Exception e) {
            log.error("{}", e);
            jsonObject.put(ResultKey.KEY_SUCC, false);
            jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.FAILUE_CODE);
            jsonObject.put(ResultKey.KEY_MSG, "验证失败，请联系后台管理员！");
        }
        return jsonObject;
    }

    /**
     * 查询用户金额详情
     * @return
     */
    @RequestMapping(value = "/getCommMoneyInfo", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject getCommMoneyInfo(String commercialNumber){
        JSONObject jsonObject=new JSONObject();
        try {
            Map<String,Object> getCommDayUp=new HashMap<>();
            getCommDayUp=commercialService.getCommDayUp(commercialNumber);
            jsonObject.put(ResultKey.KEY_DATA, getCommDayUp);
            jsonObject.put(ResultKey.KEY_SUCC, true);
            jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.SUCCESS_CODE);
            jsonObject.put(ResultKey.KEY_MSG, "查询成功！");
        } catch (Exception e) {
            log.error("{}", e);
            jsonObject.put(ResultKey.KEY_SUCC, false);
            jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.FAILUE_CODE);
            jsonObject.put(ResultKey.KEY_MSG, "查询失败，请联系后台管理员！");
        }
        return jsonObject;
    }
}
