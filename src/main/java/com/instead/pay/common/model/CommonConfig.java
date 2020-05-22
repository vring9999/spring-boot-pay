package com.instead.pay.common.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@Component
@NoArgsConstructor
public class CommonConfig {

    /**
     * 主键
     */
    private String id;
    /**
     * 配置名称
     */
    private String cfgName;
    /**
     * 属性名
     */
    private String cfgKey;
    /**
     * 属性值
     */
    private String cfgValue;
    /**
     * 备注
     */
    private String cfgRemark;
    /**
     * 配置类型  1：普通配置     2：秘钥配置
     */
    private Integer cfgType;
}
