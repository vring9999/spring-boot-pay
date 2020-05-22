package com.instead.pay.orderInfo.service;

import com.instead.pay.commercial.mapper.CommercialMapper;
import com.instead.pay.orderInfo.mapper.OrderInfoMapper;
import com.instead.pay.orderInfo.model.OrderInfo;
import com.instead.pay.util.ErrorCodeContents;
import com.instead.pay.util.GsonUtil;
import com.instead.pay.util.Security.UrlResponse;
import com.instead.pay.util.StringUtil;
import com.instead.pay.util.UsedCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import springfox.documentation.spring.web.readers.operation.OperationResponseClassReader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author vring
 * @ClassName OrderInfoServiceImpl.java
 * @Description TODO
 * @createTime 2019/12/18 14:18
 */
@Service
@Slf4j
public class OrderInfoServiceImpl implements OrderInfoService{

    @Value("#{'${PASS_IP}'.split(',')}")
    private String[] PASS_IP;

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    private CommercialMapper commercialMapper;

    @Override
    public List<OrderInfo> queryOrderInfo(Map<String, Object> param) {
        return orderInfoMapper.queryOrderInfo(param);
    }

    @Override
    public List<OrderInfo> queryOrderInfoCash(Map<String, Object> param) {
        return orderInfoMapper.queryOrderInfoCash(param);
    }

    @Override
    public List<OrderInfo> queryOrderInfoByCz(Map<String, Object> param) {
        return orderInfoMapper.queryOrderInfoByCz(param);
    }

    @Override
    public List<Integer> getOrderAllMoney() {
        return orderInfoMapper.getOrderAllMoney();
    }

    @Override
    public OrderInfo getOrderInfoById(String orderId) {
        return orderInfoMapper.getOrderInfoById(orderId);
    }

    @Override
    public OrderInfo getOrderInfoByOutId(String outId) {
        return orderInfoMapper.getOrderInfoByOutId(outId);
    }

    @Override
    public void insertOrderInfo(OrderInfo orderInfo) {
        orderInfoMapper.insertOrderInfo(orderInfo);
    }

    @Override
    public void updateOrderInfo(OrderInfo orderInfo) {
        orderInfoMapper.updateOrderInfo(orderInfo);
    }

    @Override
    public boolean checkIp(HttpServletRequest request, HttpServletResponse response, String commercialNumber ) throws IOException {
        //获取请求ip
        String ip = StringUtil.getRealIP(request);
        if (ArrayUtils.contains(PASS_IP,ip)) {
            return true;
        }
        if (!StringUtil.isEmpty(commercialNumber) && !UsedCode.SYSTEM_NUMBER.equals(commercialNumber)) {
            List<String> list = commercialMapper.queryTokenUrl(commercialNumber);
//                    commercialService.queryTokenUrl(commercialNumber);
            if (!list.contains(ip)) {
                log.error("非法ip访问:{}", ip);
                UrlResponse urlResponse = new UrlResponse(false, ErrorCodeContents.NOT_IN_WHITELIST,"当前请求IP不在白名单",ip);
                response.getWriter().write(GsonUtil.GSON.toJson(urlResponse));
                return false;
            }
        }
        return true;
    }

    @Override
    public Map<String, Object> getHome() { return orderInfoMapper.getHome(); }

    @Override
    public void updateUrl(Map<String,Object> param) {
        orderInfoMapper.updateUrl(param);
    }

    @Override
    public void insertOrderInfo_out(OrderInfo orderInfo) {
        orderInfoMapper.insertOrderInfo_out(orderInfo);
    }

    @Override
    public void updateBatch(List<Map<String, Object>> uplist) {
        orderInfoMapper.updateBatch(uplist);
    }

    @Override
    public List<Map<String, Object>> getOrderStatic(Map<String, Object> params) {
        return orderInfoMapper.getOrderStatic(params);
    }

}
