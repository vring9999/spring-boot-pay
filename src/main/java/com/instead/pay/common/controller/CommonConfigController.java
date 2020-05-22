package com.instead.pay.common.controller;


import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.instead.pay.commercial.controller.CommercialController;
import com.instead.pay.common.model.CommonConfig;
import com.instead.pay.common.service.CommonConfigService;
import com.instead.pay.util.*;
import com.instead.pay.util.rsa.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/Common")
@Component
@Slf4j
public class CommonConfigController {

    @Autowired
    private CommonConfigService commonConfigService;

    @Autowired
    private static CacheInfo cacheInfo;

    @Autowired
    public CommonConfigController(CacheInfo cacheInfo){
        CommonConfigController.cacheInfo = cacheInfo;
    }


    /**
     * 查询配置信息
     * @param filter
     * @param page
     * @param limit
     * @return
     */
    @RequestMapping(value = "/queryCommon", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject queryCommon(String filter, Integer page, Integer limit){
        JSONObject jsonObject=new JSONObject();
        try {
            filter = InputInjectFilter.encodeInputString(filter);
            //filter参数转map
            Map<String, Object> params = StringUtil.formatParam(filter);
            List<CommonConfig> commons;
            if(!StringUtil.isEmpty(page)&&!StringUtil.isEmpty(limit)){
                PageHelper.startPage(page, limit);
                commons=commonConfigService.queryCommons(params);
                PageInfo<CommonConfig> pageInfo = new PageInfo<>(commons);
                jsonObject.put("total", pageInfo.getTotal());
            }else{
                commons=commonConfigService.queryCommons(params);
            }
            for(CommonConfig com:commons){
                if(com.getCfgType()==UsedCode.SECRET_KEY){  //如果是秘钥--解密
                    com.setCfgValue(UsedCode.GET_SECRET);
                }
            }
            jsonObject.put(ResultKey.KEY_DATA, commons);
            jsonObject.put(ResultKey.KEY_SUCC, true);
            jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.SUCCESS_CODE);
            jsonObject.put(ResultKey.KEY_MSG, "查询成功！");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("{}",e);
            jsonObject.put(ResultKey.KEY_SUCC, false);
            jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.FAILUE_CODE);
            jsonObject.put(ResultKey.KEY_MSG, "查询失败，请稍后再试！");
        }
        return jsonObject;
    }

    /**
     * 添加配置信息
     * @param commonConfig
     * @return
     */
    @PostMapping(value = "/addCommon")
    @ResponseBody
    public JSONObject addCommon(CommonConfig commonConfig){
        JSONObject jsonObject=new JSONObject();
        try {
            if(commonConfig.getCfgType()==UsedCode.SECRET_KEY){  //如果是秘钥--解密
                commonConfig.setCfgValue(MD5Util.getPwd(commonConfig.getCfgValue()));
            }
            commonConfig.setId(StringUtil.getUuid());
            commonConfigService.addCommon(commonConfig);
//            updCacheToComm();
            CacheInfo.commonInfo.put(commonConfig.getCfgName()+"+"+commonConfig.getCfgKey(),commonConfig.getCfgValue());
            jsonObject.put(ResultKey.KEY_SUCC, true);
            jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.SUCCESS_CODE);
            jsonObject.put(ResultKey.KEY_MSG, "添加成功！");
        } catch (Exception e) {
            log.error("{}",e);
            jsonObject.put(ResultKey.KEY_SUCC, false);
            jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.FAILUE_CODE);
            jsonObject.put(ResultKey.KEY_MSG, "添加失败，请稍后再试！");
        }
        return jsonObject;
    }


    /**
     * 修改配置信息
     * @param commonConfig
     * @return
     */
    @PostMapping(value = "/updCommon")
    @ResponseBody
    public JSONObject updCommon(CommonConfig commonConfig){
        JSONObject jsonObject=new JSONObject();
        try {
            if(commonConfig.getCfgValue().equals(UsedCode.GET_SECRET)){
                CommonConfig comm=commonConfigService.getCommonsById(commonConfig.getId());
                commonConfig.setCfgValue(comm.getCfgValue());
            }
/*            if(commonConfig.getCfgType()==UsedCode.SECRET_KEY
                    &&!commonConfig.getCfgValue().equals(UsedCode.GET_SECRET)){  //如果是秘钥--解密
                commonConfig.setCfgValue(MD5Util.getPwd(commonConfig.getCfgValue()));
            }else if(commonConfig.getCfgType()!=UsedCode.SECRET_KEY&&commonConfig.getCfgValue().equals(UsedCode.GET_SECRET)){  //如果不是秘钥并且返回的值是“**********”
                CommonConfig comm=commonConfigService.getCommonsById(commonConfig.getId());
                commonConfig.setCfgValue(comm.getCfgValue());
            }*/
            commonConfigService.updCommon(commonConfig);
            updCacheToComm();
            jsonObject.put(ResultKey.KEY_SUCC, true);
            jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.SUCCESS_CODE);
            jsonObject.put(ResultKey.KEY_MSG, "修改成功！");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("{}",e);
            jsonObject.put(ResultKey.KEY_SUCC, false);
            jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.FAILUE_CODE);
            jsonObject.put(ResultKey.KEY_MSG, "修改失败，请稍后确认参数值！");
        }
        return jsonObject;
    }

    /**
     * 删除配置信息
     * @param id
     * @return
     */
    @PostMapping(value = "/delCommon")
    @ResponseBody
    public JSONObject delCommon(String id) {
        JSONObject jsonObject = new JSONObject();
        try {
            CommonConfig commonConfig=commonConfigService.getCommonsById(id);
            String key=commonConfig.getCfgName()+"+"+commonConfig.getCfgKey();
            CacheInfo.commonInfo.remove(key);
            commonConfigService.delCommon(id);
//            updCacheToComm();
            jsonObject.put(ResultKey.KEY_SUCC, true);
            jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.SUCCESS_CODE);
            jsonObject.put(ResultKey.KEY_MSG, "删除成功！");
        } catch (Exception e) {
            log.error("{}", e);
            jsonObject.put(ResultKey.KEY_SUCC, false);
            jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.FAILUE_CODE);
            jsonObject.put(ResultKey.KEY_MSG, "删除失败，请稍后再试！");
        }
        return jsonObject;
    }

    /**
     * 更新配置信息，配置信息更改后缓存信息也要相应修改
     * @throws Exception
     */
    private void updCacheToComm() throws Exception {
        log.info("更新配置信息！");
        cacheInfo.commonInfo.clear();   //清空原数据
        List<CommonConfig> commons = commonConfigService.queryCommons(null);
        commons.forEach(comm -> CacheInfo.commonInfo.put(comm.getCfgName()+"+"+comm.getCfgKey(),comm.getCfgValue()));
        log.info("当前配置信息为：{}",CacheInfo.commonInfo);
    }
}
