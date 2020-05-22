package com.instead.pay.commercial.mapper;

import com.instead.pay.commercial.model.IpWhite;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IpWhiteMapper {

    void insert(List<IpWhite> list);

    List<String> queryIp(String commercialNumber);

    void deleteIpBycommercialNumber(String commercialNumber);
}
