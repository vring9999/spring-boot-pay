package com.instead.pay.commercial.mapper;

import com.instead.pay.commercial.model.Commercial;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CommercialMapper {

    /**
     * 条件查询商户信息
     * @param params
     * @return
     */
    List<Commercial> queryCommercial(@Param("params")  Map<String,Object> params);
    /**
     * 登录查询
     * @param commercialName
     * @returnupdateCommercial
     */

    Commercial login(String commercialName);

    /*
     * @title findRoleNameByUser
     * @description 根据用户名查询权限角色
     * @author vring
     * @param:
     * @throws
     */
    String findRoleNameByUser(String commercialName);

    /**
     * 根据商户id查询商户信息  有没有根据名称查询的
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
