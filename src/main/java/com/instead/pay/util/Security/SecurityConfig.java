package com.instead.pay.util.Security;

import com.instead.pay.commercial.service.CommercialService;
import com.instead.pay.handler.Http401AuthenticationEntryPoint;
import com.instead.pay.filter.JWTAuthenticationFilter;
import com.instead.pay.handler.UrlLogoutSuccessHandler;
import com.instead.pay.util.RedisUtil;
import com.instead.pay.util.ThreadUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * SpringSecurity的配置
 * @EnableWebSecurity   加载了WebSecurityConfiguration配置类, 配置安全认证策略。2: 加载了AuthenticationConfiguration, 配置了认证信息。
 * 通过SpringSecurity的配置，将JWTLoginFilter，JWTAuthenticationFilter组合在一起
 * @EnableGlobalMethodSecurity 表示开启注解  并判断用户对某个控制层的方法是否具有访问权限
 * securedEnabled = true  表示 开启@Secured 注解过滤权限
 */
@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableGlobalMethodSecurity(securedEnabled = true)
@Slf4j
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    /**
     * 读取请求白名单
     */
    @Value("#{'${AUTH_WHITELIST}'.split(',')}")
    private String[] AUTH_WHITELIST;

    @Value("#{'${IP_WHITELIST}'.split(',')}")
    private String[] IP_WHITELIST;

    @Value("#{'${PASS_IP}'.split(',')}")
    private String[] PASS_IP;

    @Qualifier("UserDetailsServiceImpl")

    @Autowired
    private UserDetailsService userDetailsService;

    @Resource
    private UrlLogoutSuccessHandler logoutSuccessHandler; //自定义注销成功处理器

//    @Resource
//    private UrlAuthenticationSuccessHandler authenticationSuccessHandler; //自定义登录成功处理器

//    @Resource
//    private UrlAuthenticationFailureHandler authenticationFailureHandler; //自定义登录失败处理器

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private ThreadUtil threadUtil;

    @Autowired
    private CommercialService commercialService;

    @Resource
    private SelfFilterInvocationSecurityMetadataSource filterInvocationSecurityMetadataSource; //动态获取url权限配置

    @Resource
    private SelfAccessDecisionManager accessDecisionManager; //权限判断

    // 该方法是登录的时候会进入
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 使用自定义身份验证组件
        auth.authenticationProvider(new CustomAuthenticationProvider(userDetailsService, bCryptPasswordEncoder, redisUtil));
    }

    // 设置 HTTP 验证规则
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // csrf() 跨域
        http.cors()
                .and()
                .csrf().disable()
                // 去掉session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()

                // AUTH_WHITELIST 这个里面的所有都放行   AUTH_WHITELIST
                .antMatchers(AUTH_WHITELIST).permitAll()
                .antMatchers().permitAll()
                // 所有请求需要身份认证
                .anyRequest().authenticated()

//                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
//                    @Override
//                    public <O extends FilterSecurityInterceptor> O postProcess(O o) {
//                        o.setSecurityMetadataSource(filterInvocationSecurityMetadataSource); //动态获取url权限配置
//                        o.setAccessDecisionManager(accessDecisionManager); //权限判断
//                        return o;
//                    }
//                })
                .and()
////                //添加自定义异常入口，处理accessdeine异常
                .exceptionHandling().authenticationEntryPoint(
                new Http401AuthenticationEntryPoint("Basic realm=\"MyApp\""))
                .and()
////              .addFilter(new JWTLoginFilter(authenticationManager(),redisUtil))
//                .formLogin() // 用httpBasic的时候这个不要加
//                .loginProcessingUrl("/Commercial/login")
//                .and()
                .addFilter(new JWTAuthenticationFilter(authenticationManager(), commercialService, redisUtil, threadUtil))
                .logout() // 默认注销行为为logout，可以通过下面的方式来修改
//                .logoutUrl("/logout")
//                .logoutSuccessUrl("/login")// 设置注销成功后跳转页面，默认是跳转到登录页面;
                .logoutSuccessHandler(logoutSuccessHandler)
//                .successHandler(authenticationSuccessHandler) //验证成功处理器(前后端分离)
//                .failureHandler(authenticationFailureHandler) //验证失败处理器(前后端分离)
                .permitAll();
//                .and()
//                .httpBasic(); // 开启basic认证
    }


    //配置拦截器
//    public void addInterceptors(InterceptorRegistry registry){
//        //registry.addInterceptor此方法添加拦截器
//        registry.addInterceptor(new SessionInterceptor()).addPathPatterns("/**")
//                .excludePathPatterns("/static/**","/user/login/**","/login/**","/welcome/**","/error/**","/lib/layui/**");//需要配置2：----------- 告知拦截器：/static/admin/** 与 /static/user/** 不需要拦截 （配置的是 路径）
//    }


//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        //解决静态资源被拦截的问题
//        web.ignoring().antMatchers("/static/**");
//    }

 /*   *//**
     * 添加静态资源文件，外部可以直接访问地址
     * @param registry
     *//*
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //需要配置1：----------- 需要告知系统，这是要被当成静态文件的！
        //第一个方法设置访问路径前缀，第二个方法设置资源路径
        log.info("come    lanjie ");
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }*/
}
