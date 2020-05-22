package com.instead.pay.commercial.service;

import com.instead.pay.commercial.mapper.IpWhiteMapper;
import com.instead.pay.commercial.model.IpWhite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author vring
 * @ClassName IpWhiteServiceImpl.java
 * @Description TODO
 * @createTime 2019/12/18 15:27
 */
@Service
public class IpWhiteServiceImpl implements IpWhiteService {

    @Autowired
    private IpWhiteMapper ipWhiteMapper;
    @Override
    public void insert(List<IpWhite> list) {
        ipWhiteMapper.insert(list);
    }

    @Override
    public List<String> queryIp(String commercialNumber) {
        return ipWhiteMapper.queryIp(commercialNumber);
    }

    @Override
    public void deleteIpBycommercialNumber(String commercialNumber) {
        ipWhiteMapper.deleteIpBycommercialNumber(commercialNumber);
    }
}
