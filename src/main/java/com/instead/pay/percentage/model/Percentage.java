package com.instead.pay.percentage.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author vring
 * @ClassName Percentage.java
 * @Description 抽成
 * @createTime 2019/12/5 16:11
 */
@Data   //GETTER   SETTER
@Component         //实例化
@NoArgsConstructor      //无参构造
public class Percentage {
    private String percentageId;
    private String orderId;
    private String commercialNumber;
    private Date createTime;
    private int collectionMoney;
    private int remark;
    private int collectionType;

    public Percentage(String percentageId, String orderId, String commercialNumber,int collectionMoney, Date createTime,int collectionType) {
        this.percentageId = percentageId;
        this.orderId = orderId;
        this.commercialNumber = commercialNumber;
        this.createTime = createTime;
        this.collectionMoney = collectionMoney;
        this.collectionType = collectionType;
    }

}
