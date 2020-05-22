package com.instead.pay.qr.mapper;

import com.instead.pay.qr.model.Qr;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface QrMapper {
    /**
     * @title
     * @description
     * @author vring
     * @param:
     * @throws 
     */
    Qr selectQrCode(Map<String,Object> map);
    /*
     * @title insert
     * @description
     * @author vring
     * @param:
     * @throws
     */
    void insert(Qr qr);


    void updateEnableStatus(Map<String,Object> map);

    void updateNumber(Qr qr);

    int selectMaxNumber();

    List<Qr> queryQrAll(Map<String,Object> map);

    void updateQr(Qr qr);

    Qr getQrById(String id);

    Map<String, Object> getUrlMoney(String orderId);
}
