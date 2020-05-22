package com.instead.pay.qr.service;

import com.instead.pay.qr.mapper.QrMapper;
import com.instead.pay.qr.model.Qr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author vring
 * @ClassName qrServiceImpl.java
 * @Description
 * @createTime 2019/12/17 14:54
 */
@Service
public class QrServiceImpl implements QrService {

    @Autowired
    private QrMapper qrMapper;
    @Override
    public Qr selectQrCode(Map<String,Object> map) {
        return qrMapper.selectQrCode(map);
    }

    @Override
    public void insert(Qr qr) {
        qrMapper.insert(qr);
    }

    @Override
    public void updateEnableStatus(Map<String,Object> map) {
        qrMapper.updateEnableStatus(map);
    }

    @Override
    public void updateNumber(Qr qr) {
        qrMapper.updateNumber(qr);
    }

    @Override
    public int selectMaxNumber() {
        return qrMapper.selectMaxNumber();
    }

    @Override
    public List<Qr> queryQrAll(Map<String, Object> map) {
        return qrMapper.queryQrAll(map);
    }

    @Override
    public void updateQr(Qr qr) {
        qrMapper.updateQr(qr);
    }

    @Override
    public Qr getQrById(String id) { return qrMapper.getQrById(id); }

    @Override
    public Map<String, Object> getUrlMoney(String orderId) {
        return qrMapper.getUrlMoney(orderId);
    }
}
