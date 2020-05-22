package com.instead.pay.orderInfo.service;

import com.instead.pay.orderInfo.model.OrderInfo;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface OrderInfoService {

    List<OrderInfo> queryOrderInfo(Map<String,Object> param);

    List<OrderInfo> queryOrderInfoCash(Map<String,Object> param);

    List<OrderInfo> queryOrderInfoByCz(Map<String,Object> param);

    List<Integer> getOrderAllMoney();

    OrderInfo getOrderInfoById(String orderId);

    OrderInfo getOrderInfoByOutId(String outId);

    void insertOrderInfo(OrderInfo orderInfo);

    void updateOrderInfo(OrderInfo orderInfo);

    boolean checkIp(HttpServletRequest request, HttpServletResponse response, String commercialNumber ) throws IOException;

    Map<String,Object> getHome();


    void updateUrl(Map<String,Object> param);

    void insertOrderInfo_out(OrderInfo orderInfo);


    void updateBatch(List<Map<String, Object>> uplist);


    List<Map<String,Object>> getOrderStatic(Map<String,Object> params);
}
