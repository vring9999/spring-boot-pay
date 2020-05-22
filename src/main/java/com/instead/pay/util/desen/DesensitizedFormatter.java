package com.instead.pay.util.desen;

import org.apache.commons.lang3.StringUtils;
import org.springframework.format.Formatter;
 
import java.text.ParseException;
import java.util.Locale;
 
 
public class DesensitizedFormatter implements Formatter<String> {
    private SensitiveTypeEnum typeEnum;
 
    public SensitiveTypeEnum getTypeEnum() {
        return typeEnum;
    }
 
    public void setTypeEnum(SensitiveTypeEnum typeEnum) {
        this.typeEnum = typeEnum;
    }
 
    @Override
    public String parse(String valueStr, Locale locale) throws ParseException {
        if (StringUtils.isNotBlank(valueStr)) {
            switch (typeEnum) {
                case CHINESE_NAME:
                    valueStr = DesensitizedUtils.chineseName(valueStr);
                    break;
                case ID_CARD:
                    valueStr = DesensitizedUtils.idCardNum(valueStr);
                    break;
                case FIXED_PHONE:
                    valueStr = DesensitizedUtils.fixedPhone(valueStr);
                    break;
                case bankPhone:
                    valueStr = DesensitizedUtils.mobilePhone(valueStr);
                    break;
                case ADDRESS:
                    valueStr = DesensitizedUtils.address(valueStr, 8);
                    break;
                case EMAIL:
                    valueStr = DesensitizedUtils.email(valueStr);
                    break;
                case BANK_CARD:
                    valueStr = DesensitizedUtils.bankCard(valueStr);
                    break;
                case PASSWORD:
                    valueStr = DesensitizedUtils.password(valueStr);
                    break;
                case CARNUMBER:
                    valueStr = DesensitizedUtils.carNumber(valueStr);
                    break;
                default:
            }
        }
        return valueStr;
    }
 
    @Override
    public String print(String s, Locale locale) {
        return s;
    }
}