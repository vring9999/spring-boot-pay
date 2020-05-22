package com.instead.pay.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.instead.pay.commercial.model.Commercial;
import com.instead.pay.util.ErrorCodeContents;
import com.instead.pay.util.GsonUtil;
import com.instead.pay.util.RedisUtil;
import com.instead.pay.util.Security.UrlResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 验证用户名密码正确后，生成一个token，并将token返回给客户端
 * 该类继承自UsernamePasswordAuthenticationFilter，重写了其中的3个方法
 */
@Slf4j
public class JWTLoginFilter extends  UsernamePasswordAuthenticationFilter{

    private RedisUtil redisUtil;

    private AuthenticationManager authenticationManager;

    public JWTLoginFilter(AuthenticationManager authenticationManager,RedisUtil redisUtil) {
        this.authenticationManager = authenticationManager;
        this.redisUtil = redisUtil;

    }

    // 接收并解析用户凭证
    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException {
        try {
            Commercial commercial = new ObjectMapper().readValue(req.getInputStream(), Commercial.class);
            List<String> list = new ArrayList<>();
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            commercial.getCommercialName(),
                            commercial.getCommercialPassword(),
//                            user.getSystemUserName(),
//                            user.getPassword(),
                            new ArrayList<>())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 用户成功登录后，这个方法会被调用，我们在这个方法里生成token
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8");
        request.getHeader("token");
        String token = null;
        try {
            Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
//            // 定义存放权限角色集合的对象
//            List roleList = new ArrayList<>();
//            for (GrantedAuthority grantedAuthority : authorities) {
//                roleList.add(grantedAuthority.getAuthority());
//            }
            //Calendar calendar = Calendar.getInstance();
            //Date now = calendar.getTime();
            // 设置签发时间
            //calendar.setTime(new Date());
            // 设置过期时间
            //calendar.add(Calendar.MINUTE, 2);// 10分钟
           // Date time = calendar.getTime();
            // Jwts.builder()
                   // .setSubject(auth.getName() + "-" + roleList)
                    //.setIssuedAt(now)//签发时间  ;
                    //.setExpiration(time)//过期时间
                    //.signWith(SignatureAlgorithm.HS512, ConstantKey.SIGNING_KEY) //采用什么算法是可以自己选择的，不一定非要采用HS512
                    //.compact();
           // token = JwtHelper.createToken(auth.getName() + "-" + roleList);
            log.info("token : {}",token);
            String username = (String) auth.getPrincipal(); //表单输入的用户名
            boolean res =  redisUtil.ins(username,token,24, TimeUnit.HOURS);
            log.info("redis insert : {}",res);
            // 登录成功后，返回token到header里面
            response.addHeader("Authorization", "Bearer " + token);
            UrlResponse urlResponseresponse = new UrlResponse();
            urlResponseresponse.setSuccess(true);
            urlResponseresponse.setErrorCode(ErrorCodeContents.SUCCESS_CODE);
            urlResponseresponse.setMes("Login Success!");
//
//            //查询当前登录的用户对应的权限菜单
//            Map<String, Object> userInfo = menuService.findMenuInfoByUsername(username);
//            urlResponseresponse.setData(userInfo);
            response.getWriter().write(GsonUtil.GSON.toJson(urlResponseresponse));
        } catch (Exception e) {
            log.error("登录失败！{}",e);
        }
    }
    // 用户登录失败后，这个方法会被调用
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        UrlResponse urlResponseresponse = new UrlResponse();
        urlResponseresponse.setSuccess(false);
        urlResponseresponse.setErrorCode(ErrorCodeContents.FAILUE_CODE);
        urlResponseresponse.setMes("Login Failure!");
        urlResponseresponse.setData(null);
        response.setStatus(401);
        response.getWriter().write(GsonUtil.GSON.toJson(urlResponseresponse));
    }

}