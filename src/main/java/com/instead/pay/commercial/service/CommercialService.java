package com.instead.pay.commercial.service;

import com.instead.pay.commercial.model.Commercial;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


public interface CommercialService {

    /**
     * 条件查询商户信息
     * @param params
     * @return
     */
    List<Commercial> queryCommercial(Map<String,Object> params);

    /**
     * 登录查询
     * @param commercialName
     * @return
     */
    Commercial login(String commercialName);

    /*
     * @title findRoleNameByUser
     * @description 根据用户名查询权限角色
     * @author vring
     * @param:
     * @throws
     */
    String findRoleNameByUser(String systemUserName);

    /**
     * 根据商户id查询商户信息
     * @param CommercialId
     * @return
     */
    Commercial getCommercial(String CommercialId);

    /**
     * 根据商户号查询商户信息
     * @param commercialNum
     * @return
     */
    Commercial getCommercialByNum(String commercialNum);

    /**
     * 根据手机号查询商户信息
     * @param commercialIphone
     * @return
     */
    Commercial getCommercialByIphone(String commercialIphone);

    /**
     * 添加商户信息
     * @param commercial
     */
    void insertCommercial(Commercial commercial);

    /**
     * 修改商户信息
     * @param commercial
     */
    void updateCommercial(Commercial commercial);

    /**
     * 删除商户信息
     * @param list
     */
    void deleteCommercial(List<String> list);


    List<String> queryTokenUrl(String commercialNumber);

    String getSafetyPwd(String commercialId);

    Map<String,Object> getCommDayUp(String commercialNumber);
}
