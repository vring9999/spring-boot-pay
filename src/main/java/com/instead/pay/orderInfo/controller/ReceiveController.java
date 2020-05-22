package com.instead.pay.orderInfo.controller;

import com.alibaba.fastjson.JSONObject;
import com.instead.pay.orderInfo.service.OrderInfoService;
import com.instead.pay.util.ErrorCodeContents;
import com.instead.pay.util.ResultKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * @author vring
 * @ClassName ReceiveController.java
 * @Description TODO
 * @createTime 2020/4/22 13:54
 */
@Controller
@RequestMapping("/receive")
@Component
@Slf4j
public class ReceiveController {

    @Autowired
    private OrderInfoService orderInfoService;


    @RequestMapping(value = "/callback", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject getHome(){
        JSONObject jsonObject = new JSONObject();
        try {
            Map<String,Object> map=orderInfoService.getHome();
            jsonObject.put(ResultKey.KEY_SUCC, true);
            jsonObject.put(ResultKey.KEY_DATA, map);
            jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.SUCCESS_CODE);
        } catch (Exception e) {
            log.error("{}", e);
            jsonObject.put(ResultKey.KEY_SUCC, false);
            jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.FAILUE_CODE);
        }
        return jsonObject;
    }
}
