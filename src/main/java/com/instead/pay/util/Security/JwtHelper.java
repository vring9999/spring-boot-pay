package com.instead.pay.util.Security;

import com.alibaba.fastjson.JSONObject;
import com.instead.pay.commercial.model.Commercial;
import com.instead.pay.util.*;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.security.Key;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @FileName: JwtHelper
 * @Description: 实现Jwt
 */

@Slf4j
@Configuration
public class JwtHelper {


    public static long expiration;//token超时时间


    public static String base64Security;
    private static final String ISS = "payWeb";

    @Value("${jwt.expiration}")
    public void setExpiration(long expiration) {
        this.expiration = expiration;
    }

    @Value("${jwt.secret}")
    public void setBase64Security(String base64Security) {
        this.base64Security = base64Security;
    }

    /**
     * 解析token
     *
     * @param jsonWebToken
     * @return claims
     */
    public static Claims parseToken(String jsonWebToken) {
        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(base64Security))
                .parseClaimsJws(jsonWebToken.replace("Bearer ", "")).getBody();
        return claims;
    }

    // 是否已过期
    public static boolean isExpiration(String token) {
        try {
            return parseToken(token).getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        }
    }

    /**
     * 新建token
     *
     * @param name
     * @return
     */
    public static String createToken(String name, String roleName) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        // 生成签名密钥
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(base64Security);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("roleName", roleName);
        // 添加构成JWT的参数
        JwtBuilder builder = Jwts.builder().
                signWith(signatureAlgorithm, signingKey).
                setHeaderParam("typ", "JWT").
                setClaims(map).
                setIssuer(ISS).
                setSubject(name).
                setIssuedAt(now);// 添加Token签发时间
        // 添加Token过期时间
        if (expiration >= 0) {
            long expMillis = nowMillis + expiration;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp).setNotBefore(now);
        }
        // 生成JWT
        return builder.compact();
    }

    /**

     * 刷新令牌
     * 如果token沒有過期 註解往下執行 不會進入
     * token过期的两种情况：
     * 1 token过期  而redis中存储的商户信息还没有过期   则生成一个新的token覆盖原来的value  并删除原来的旧token包含的key-value
     * @param
     * @return
     */
    public static UsernamePasswordAuthenticationToken refreshToken(HttpServletResponse response,String token, RedisUtil redisUtil, ThreadUtil threadUtil) throws IOException {
            //因为更新token后 原来的token会从redis缓存中删除掉  如果更新token后没有使用新的token访问资源  则会出现jsonStr为空的情况
            String jsonStr = redisUtil.get(token);//没有缓存时间
            if(StringUtil.isEmpty(jsonStr)){
                response.setStatus(500);
                UrlResponse response1 = new UrlResponse(false, ErrorCodeContents.LOGIN_TOKEN_TIMEOUT, "令牌已更新,请使用新的令牌！", "");
                response.getWriter().write(GsonUtil.GSON.toJson(response1));
                return null;
            }
            JSONObject jsonObject = JSONObject.parseObject(jsonStr);
            Commercial commercial = (Commercial) JSONObject.parseObject(jsonObject.getString("obj"), Commercial.class);
            log.info("根据token获取的商户信息:{}",commercial.toString());
            String roleName = jsonObject.getString("roleName");
            String info = redisUtil.get(commercial.getCommercialName());//有缓存时间的
            log.info("redis中的商户信息:{}",info);
            //redis中的商户信息已过期   提示重新登录
            if (StringUtil.isEmpty(info)) {
                UrlResponse response1 = new UrlResponse(false, ErrorCodeContents.LOGIN_TOKEN_TIMEOUT, "令牌已过期,请重新登录！", "");
                response.getWriter().write(GsonUtil.GSON.toJson(response1));
                return null;
            } else { //如果redis中的商户信息没有过期   则是生成新的token替换原来的token值
                String newToken = "Bearer " + JwtHelper.createToken(commercial.getCommercialName(), roleName);
                response.addHeader("Authorization", newToken);
                redisUtil.not_Refresh_time(commercial.getCommercialName(), jsonObject.getString("obj"));
                redisUtil.ins(commercial.getCommercialId(), newToken);
                redisUtil.ins(newToken, jsonObject.toString());
                redisUtil.delete(token);
                threadUtil.bind(commercial);
                return new UsernamePasswordAuthenticationToken(commercial.getCommercialName(), null,
                        Collections.singleton(new SimpleGrantedAuthority(roleName)));
            }
    }

    // 从token中获取用户名
    public static String getUsername(String token) {
        return parseToken(token).getSubject();
    }

    // 获取用户角色
    public static String getUserRole(String token) {
        return (String) parseToken(token).get("roleName");
    }


}
