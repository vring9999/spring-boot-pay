package com.instead.pay.percentage.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.instead.pay.percentage.model.Percentage;
import com.instead.pay.percentage.service.PercentageService;
import com.instead.pay.util.*;
import com.instead.pay.util.Security.JwtHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * @author vring
 * @ClassName PercentageController.java
 * @Description TODO
 * @createTime 2019/12/6 9:43
 */
@Controller
//@RestController
@RequestMapping("/percentage")
@Slf4j
public class PercentageController {
    @Autowired
    private PercentageService percentageService;
    @Autowired
    private JwtHelper jwtHelper;
    @Autowired
    private ExportExcel exportExcel;


//    @GetMapping(value = "selectAll/{filter}/{page}/{limit}")
//    @ResponseBody
//    public JSONObject selectAll(@PathVariable String filter, @PathVariable Integer page, @PathVariable("limit")Integer limit) {

    /**
     * @author vring
     * @param filter  查询条件
     * @param page  页数
     * @param limit  条数
     * @return jsonObject
     */
    @PostMapping(value = "/selectAll")
    @ResponseBody
    public JSONObject selectAll(@RequestParam(defaultValue="")String filter, Integer page, Integer limit) {
        JSONObject jsonObject = new JSONObject();

        filter = InputInjectFilter.encodeInputString(filter);
        //filter参数转map
        Map<String, Object> param = StringUtil.formatParam(filter);
        List<Percentage> sysMenus = new ArrayList<Percentage>();
        if(StringUtil.isEmpty(page)){
            sysMenus = percentageService.selectAll(param);
        }else{
            PageHelper.startPage(page, limit);
            sysMenus = percentageService.selectAll(param);
            PageInfo<Percentage> pageInfo = new PageInfo<>(sysMenus);
            jsonObject.put("total",pageInfo.getTotal());
        }
        jsonObject.put(ResultKey.KEY_DATA, sysMenus);
        jsonObject.put(ResultKey.KEY_MSG, "操作成功");
        jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.SUCCESS_CODE);
        return jsonObject;
    }

    @PostMapping(value = "/exportExcel")
    @ResponseBody
    public JSONObject exportExcel(String filter, HttpServletResponse response) throws IOException {
        JSONObject jsonObject = new JSONObject();
        filter = InputInjectFilter.encodeInputString(filter);
        Map<String, Object> params = StringUtil.formatParam(filter);
        try {
            String name = DateUtil.getCurrentTime("yyyyMMddHHmmss")+ ".xls";
            List<Percentage> excelList = percentageService.selectAll(params);
            List<String> listTile = new ArrayList<>();
            listTile.add("订单号");
            listTile.add("商户号");
            listTile.add("服务费");
            listTile.add("时间");
            listTile.add("操作类型");
            List<String> listCloumn = new ArrayList<>();
            listCloumn.add("orderId");
            listCloumn.add("commercialNumber");
            listCloumn.add("collectionMoney");
            listCloumn.add("createTime");
            listCloumn.add("collectionType");
            if(excelList.size() != 0){
                exportExcel.exportExcel(name,excelList,listTile,listCloumn,response);
                jsonObject.put(ResultKey.KEY_CODE,ErrorCodeContents.SUCCESS_CODE);
                jsonObject.put(ResultKey.KEY_MSG,"导出成功");
            }else {
                jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.FAILUE_CODE);
                jsonObject.put(ResultKey.KEY_MSG, "请勿导出空数据表格");
            }
        } catch (IOException e) {
            log.error("导出数据表格失败{}",e);
            jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.FAILUE_CODE);
            jsonObject.put(ResultKey.KEY_MSG, "导出失败");
        }
        return jsonObject;
    }

}
