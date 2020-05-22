package com.instead.pay;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

@SpringBootTest
class PayApplicationTests {

    @Test
    void contextLoads() {

        int operatorMoney  = 121909023;
        int floatMoney  = 121909023;
        double radio = 0.2;
        System.out.println(radio);
        int handMoney = handMoney(floatMoney,radio);
        System.out.println(handMoney);
        int yMoney = yMoney = operatorMoney - handMoney;
        System.out.println(yMoney);
    }

    private int handMoney(int orderMoney,double radio){
        System.out.println("*************************");
        int handMoney;
        double moneyY=(double) orderMoney/100;  //转换成元为单位
        System.out.println(moneyY);
        double moneyH=moneyY * radio;   //计算提成
        System.out.println(moneyH);
        BigDecimal bd = new BigDecimal(moneyH);
        System.out.println(bd);
        moneyH=bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();  //保留两位小数
        System.out.println(moneyH);
        double moneyA=moneyH*100;   //转换成分为单位
        System.out.println(moneyA);
        handMoney=(int)moneyA;
        System.out.println("*************************");
        return handMoney;
    }

}
