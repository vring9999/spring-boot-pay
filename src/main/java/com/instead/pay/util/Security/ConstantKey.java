package com.instead.pay.util.Security;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class ConstantKey {
    /**
     * 签名key
     */
//    @Value("${SIGNING_KEY}")
    public static final String SIGNING_KEY = "spring-security-@Jwt!&Secret^#";

}
