package com.instead.pay.commercial.service;

import com.instead.pay.commercial.mapper.CommercialMapper;
import com.instead.pay.commercial.model.Commercial;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author vring
 * @ClassName CommercialServiceImpl.java
 * @Description
 * @createTime 2019/12/11 11:55
 */
@Service("commercialService")
public class CommercialServiceImpl implements CommercialService{

    @Autowired
    private CommercialMapper commercialMapper;

    @Override
    public List<Commercial> queryCommercial(Map<String, Object> params) {
        return commercialMapper.queryCommercial(params);
    }

    @Override
    public Commercial login(String commercialName) {
        return commercialMapper.login(commercialName);
    }

    @Override
    public String findRoleNameByUser(String commercialName) {
        return commercialMapper.findRoleNameByUser(commercialName);
    }

    @Override
    public Commercial getCommercial(String CommercialId) {
        return commercialMapper.getCommercial(CommercialId);
    }

    @Override
    public Commercial getCommercialByNum(String commercialNum) {
        return commercialMapper.getCommercialByNum(commercialNum);
    }

    @Override
    public Commercial getCommercialByIphone(String commercialIphone) {
        return commercialMapper.getCommercialByIphone(commercialIphone);
    }

    @Override
    public void insertCommercial(Commercial commercial) {
        commercialMapper.insertCommercial(commercial);
    }

    @Override
    public void updateCommercial(Commercial commercial) {
        commercialMapper.updateCommercial(commercial);
    }

    @Override
    public void deleteCommercial(List<String> list) {
            commercialMapper.deleteCommercial(list);
    }

    @Override
    public List<String> queryTokenUrl(String commercialNumber) {
        return commercialMapper.queryTokenUrl(commercialNumber);
    }

    @Override
    public String getSafetyPwd(String commercialId) {
        return commercialMapper.getSafetyPwd(commercialId);
    }

    @Override
    public Map<String,Object> getCommDayUp(String commercialNumber) {
        return commercialMapper.getCommDayUp(commercialNumber);
    }

}
