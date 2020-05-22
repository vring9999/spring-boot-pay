package com.instead.pay.appInfo.controller;

import com.alibaba.fastjson.JSONObject;
import com.instead.pay.appInfo.mapper.AppInfoMapper;
import com.instead.pay.appInfo.model.AppInfo;
import com.instead.pay.util.ErrorCodeContents;
import com.instead.pay.util.ResultKey;
import com.instead.pay.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/app")
@Component
@Slf4j
public class AppInfoController {

    @Autowired
    private AppInfoMapper appInfoMapper;


    /**
     * 查询该商户下所有的app
     * @param commercialNumber
     * @return
     */
    @RequestMapping(value = "/getAppByComId", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject getAppInfoByComId(String commercialNumber){
        JSONObject jsonObject=new JSONObject();
        try {
            List<AppInfo> getAppList=appInfoMapper.getAppByComId(commercialNumber);
            jsonObject.put(ResultKey.KEY_LIST_DATA, getAppList);
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

    /**
     * yingyongmingcehgn
     * @param commercialNumber
     * @return
     */
    @RequestMapping(value = "/getCodeName", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject getCodeName(String commercialNumber){
        JSONObject jsonObject=new JSONObject();
        try {
            List<Map<String,String>> getCodeName=appInfoMapper.getCodeName(commercialNumber);
            jsonObject.put(ResultKey.KEY_LIST_DATA, getCodeName);
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




    /**
     * 添加app
     * @param appInfo
     * @return
     */
    @RequestMapping(value = "/addAppInfo", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject addAppInfo(AppInfo appInfo){
        log.info("addAppInfo user param--->{}",appInfo);
        JSONObject jsonObject=new JSONObject();
        try {
            AppInfo oldApp=appInfoMapper.getAppByName(appInfo.getAppName());
            if(oldApp==null){
                appInfo.setAppId(getAppId());
                appInfo.setAppKey(StringUtil.getRandomString(12));
                appInfo.setCreateTime(new Date());
                appInfoMapper.insertAppInfo(appInfo);
                jsonObject.put(ResultKey.KEY_SUCC, true);
                jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.SUCCESS_CODE);
                jsonObject.put(ResultKey.KEY_MSG, "添加成功！");
            }else{
                log.error("该app已存在");
                jsonObject.put(ResultKey.KEY_SUCC, false);
                jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.USER_IS_NOT_FOUND);
                jsonObject.put(ResultKey.KEY_MSG, "该app已存在！");
            }
        } catch (Exception e) {
            log.error("{}", e);
            jsonObject.put(ResultKey.KEY_SUCC, false);
            jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.USER_IS_NOT_FOUND);
            jsonObject.put(ResultKey.KEY_MSG, "添加APP失败！");
        }
        return jsonObject;
    }

    /**
     * 修改App信息
     * @param appInfo
     * @return
     */
    @RequestMapping(value = "/updAppInfo", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject updAppInfo(AppInfo appInfo){
        log.info("updAppInfo user param--->{}",appInfo);
        JSONObject jsonObject=new JSONObject();
        try {
            appInfo.setOperatorTime(new Date());
            appInfoMapper.updateAppInfo(appInfo);
            jsonObject.put(ResultKey.KEY_SUCC, true);
            jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.SUCCESS_CODE);
            jsonObject.put(ResultKey.KEY_MSG, "修改成功！");
        } catch (Exception e) {
            log.error("{}", e);
            jsonObject.put(ResultKey.KEY_SUCC, false);
            jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.FAILUE_CODE);
            jsonObject.put(ResultKey.KEY_MSG, "修改APP信息失败！");
        }
        return jsonObject;
    }


    /**
     * 修改AppKey
     * @param appId
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject delete(String appId){
        log.info("delete appId param--->{}",appId);
        JSONObject jsonObject=new JSONObject();
        try {
            appInfoMapper.delete(appId);
            jsonObject.put(ResultKey.KEY_SUCC, true);
            jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.SUCCESS_CODE);
            jsonObject.put(ResultKey.KEY_MSG, "删除完毕");
        } catch (Exception e) {
            log.error("{}", e);
            jsonObject.put(ResultKey.KEY_SUCC, false);
            jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.USER_IS_NOT_FOUND);
            jsonObject.put(ResultKey.KEY_MSG, "修改AppKey失败，请稍后再试！");
        }
        return jsonObject;
    }

    /**
     * 修改AppKey
     * @param appId
     * @return
     */
    @RequestMapping(value = "/updAppKey", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject updAppKey(String appId){
        log.info("updAppKey appId param--->{}",appId);
        JSONObject jsonObject=new JSONObject();
        try {
            AppInfo oldApp=appInfoMapper.getAppById(appId);
            if(oldApp!=null){
                String appKey=StringUtil.getRandomString(12);
                oldApp.setAppKey(appKey);
                oldApp.setOperatorTime(new Date());
                appInfoMapper.updateAppInfo(oldApp);
                jsonObject.put(ResultKey.KEY_SUCC, true);
                jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.SUCCESS_CODE);
                jsonObject.put(ResultKey.KEY_MSG, "AppKey修改成功！");
                log.info("AppKey修改成功当前AppKey为 --->{}",appKey);
            }else{
                log.error("该App不存在");
                jsonObject.put(ResultKey.KEY_SUCC, false);
                jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.USER_IS_NOT_FOUND);
                jsonObject.put(ResultKey.KEY_MSG, "该App不存在，请确认！");
            }
        } catch (Exception e) {
            log.error("{}", e);
            jsonObject.put(ResultKey.KEY_SUCC, false);
            jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.USER_IS_NOT_FOUND);
            jsonObject.put(ResultKey.KEY_MSG, "修改AppKey失败，请稍后再试！");
        }
        return jsonObject;
    }

    /**
     * appId生成规则
     * @return
     */
    private String getAppId(){
        String appId="";
        StringBuffer sb=new StringBuffer();
        while (true){
            sb.append(StringUtil.getRandomStringToBig(3)).append("_").append(StringUtil.getRandomCode(5));
            appId=sb.toString();
            AppInfo app=appInfoMapper.getAppById(appId);
            if(app==null){  //表示appId不存在
                break;
            }
        }
        return appId;
    }
}
