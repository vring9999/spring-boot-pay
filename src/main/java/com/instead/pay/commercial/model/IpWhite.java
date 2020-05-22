package com.instead.pay.commercial.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author vring
 * @ClassName IpWhite.java
 * @Description ip白名單
 * @createTime 2019/12/18 15:20
 */
@Data
@AllArgsConstructor
@Component
@NoArgsConstructor
public class IpWhite {

    private String commercialNumber;
    private String tokenUrl;
    private String remark;

    public IpWhite(String commercialNumber, String tokenUrl) {
        this.commercialNumber = commercialNumber;
        this.tokenUrl = tokenUrl;
    }
}
