package com.instead.pay.filter;
 
import com.alibaba.fastjson.serializer.ValueFilter;
 
import java.lang.reflect.Field;

import com.instead.pay.util.desen.Desensitized;
import com.instead.pay.util.desen.DesensitizedUtils;
import com.instead.pay.util.desen.SensitiveTypeEnum;
import lombok.extern.slf4j.Slf4j;
 
/**
 * 在fastjson中使用此过滤器进行脱敏操作
 */
@Slf4j
public class ValueDesensitizeFilter implements ValueFilter {
 
 
    @Override
    public Object process(Object object, String name, Object value) {
        if (null == value || !(value instanceof String) || ((String) value).length() == 0) {
            return value;
        }
        try {
            Field field = object.getClass().getDeclaredField(name);
            Desensitized desensitization;
            if (String.class != field.getType() || (desensitization = field.getAnnotation(Desensitized.class)) == null) {
                return value;
            }
            String valueStr = (String) value;
            SensitiveTypeEnum type = desensitization.type();
            switch (type) {
                case CHINESE_NAME:
                    return DesensitizedUtils.chineseName(valueStr);
                case ID_CARD:
                    return DesensitizedUtils.idCardNum(valueStr);
                case FIXED_PHONE:
                    return DesensitizedUtils.fixedPhone(valueStr);
                case bankPhone:
                    return DesensitizedUtils.mobilePhone(valueStr);
                case ADDRESS:
                    return DesensitizedUtils.address(valueStr, 8);
                case EMAIL:
                    return DesensitizedUtils.email(valueStr);
                case BANK_CARD:
                    return DesensitizedUtils.bankCard(valueStr);
                case PASSWORD:
                    return DesensitizedUtils.password(valueStr);
                case CARNUMBER:
                    return DesensitizedUtils.carNumber(valueStr);
                default:
                    return valueStr;
            }
        } catch (NoSuchFieldException e) {
//            log.error("当前数据类型为{},值为{}", object.getClass(), value);
            return value;
        }
//        return value;
    }
}