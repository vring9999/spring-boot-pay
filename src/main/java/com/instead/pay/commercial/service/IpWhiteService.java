package com.instead.pay.commercial.service;

import com.instead.pay.commercial.model.IpWhite;

import java.util.List;

public interface IpWhiteService {
    void insert(List<IpWhite> list);

    List<String> queryIp(String commercialNumber);

    void deleteIpBycommercialNumber(String commercialNumber);

}
