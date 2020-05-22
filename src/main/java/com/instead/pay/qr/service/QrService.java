package com.instead.pay.qr.service;

import com.instead.pay.qr.model.Qr;

import java.util.List;
import java.util.Map;

public interface QrService {
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
