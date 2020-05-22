package com.instead.pay.orderInfo.mapper;

import com.instead.pay.orderInfo.model.OrderInfo;
import org.springframework.core.annotation.Order;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface OrderInfoMapper {

    List<OrderInfo> queryOrderInfo(Map<String,Object> param);

    List<OrderInfo> queryOrderInfoCash(Map<String,Object> param);

    List<OrderInfo> queryOrderInfoByCz(Map<String,Object> param);

    List<Integer> getOrderAllMoney();

    OrderInfo getOrderInfoById(String orderId);

    OrderInfo getOrderInfoByOutId(String outId);

    void insertOrderInfo(OrderInfo orderInfo);

    void updateOrderInfo(OrderInfo orderInfo);

    Map<String,Object> getHome();

    void updateUrl(Map<String,Object> param);


    void insertOrderInfo_out(OrderInfo orderInfo);

    void updateBatch(List<Map<String, Object>> uplist);

    List<Map<String,Object>> getOrderStatic(Map<String,Object> params);
}
