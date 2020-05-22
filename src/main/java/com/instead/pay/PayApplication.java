package com.instead.pay;

import com.instead.pay.util.ApplicationUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;



@SpringBootApplication
@EnableTransactionManagement
@MapperScan("com.instead.pay.*.mapper")
@EnableScheduling
@ServletComponentScan
public class PayApplication {

//    @Autowired
//    private RestTemplateBuilder builder;
    // 使用RestTemplateBuilder来实例化RestTemplate对象，spring默认已经注入了RestTemplateBuilder实例
    @Bean
    public RestTemplate restTemplate() {
//        return builder.build();
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
//        HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
//        requestFactory.setConnectionRequestTimeout(10000);
        requestFactory.setConnectTimeout(100000);
        requestFactory.setReadTimeout(100000);
        return new RestTemplate(requestFactory);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public ApplicationUtil applicationUtil() {
        return new ApplicationUtil();
    }

    public static void main(String[] args) {
        SpringApplication.run(PayApplication.class, args);
    }

}
