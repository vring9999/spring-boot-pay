package com.instead.pay.filter;

import com.instead.pay.commercial.model.Commercial;
import com.instead.pay.commercial.service.CommercialService;
import com.instead.pay.exception.TokenException;
import com.instead.pay.util.*;
import com.instead.pay.util.GsonUtil;
import com.instead.pay.util.Security.*;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * 自定义JWT认证过滤器
 * 该类继承自BasicAuthenticationFilter，在doFilterInternal方法中，
 * 从http头的Authorization 项读取token数据，然后用Jwts包提供的方法校验token的合法性。
 * 如果校验通过，就认为这是一个取得授权的合法请求
 */
@Slf4j
public class JWTAuthenticationFilter extends BasicAuthenticationFilter {
    // 定义 service
    private CommercialService commercialService;
    private RedisUtil redisUtil;
    private ThreadUtil threadUtil;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, CommercialService commercialService, RedisUtil redisUtil, ThreadUtil threadUtil) {
        super(authenticationManager);
        // 初始化
        this.commercialService = commercialService;
        this.redisUtil = redisUtil;
        this.threadUtil = threadUtil;
    }

    /**
     * 检测ip 是否合法
     *
     * @param request
     * @param response
     * @return
     */
    private boolean checkIp(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //获取请求ip
        String ip = StringUtil.getRealIP(request);
        /*if (ArrayUtils.contains(PASS_IP,ip)) {
            return true;
        }*/
        //获得所有请求参数名
        Enumeration<?> params = request.getParameterNames();
        Map<String, Object> reqParams = new HashMap<String, Object>();
        while (params.hasMoreElements()) {
            //得到参数名
            String name = params.nextElement().toString();
            //得到参数对应值
            String value = request.getParameter(name);
            reqParams.put(name, value);
            if (name.equals("commercialNumber")) break;
        }
        log.info("reqParams : {}", reqParams);
        boolean flag = reqParams.containsKey("commercialNumber");//判断请求参数中是否包含商户号
        if (flag) {
            String commercialNumber = (String) reqParams.get("commercialNumber");
            if (!StringUtil.isEmpty(commercialNumber) && !UsedCode.SYSTEM_NUMBER.equals(commercialNumber)) {
                List<String> list = commercialService.queryTokenUrl(commercialNumber);
                if (!list.contains(ip)) {
                    log.error("非法ip访问:{}", ip);
                    UrlResponse urlResponse = new UrlResponse(false,ErrorCodeContents.NOT_IN_WHITELIST,"当前请求IP不在白名单",ip);
                    response.getWriter().write(GsonUtil.GSON.toJson(urlResponse));
                    return false;
                }
            }
        } else {
            log.error("未携带商户号，非法请求访问:{}", ip);
            UrlResponse urlResponse = new UrlResponse(false,ErrorCodeContents.NO_FOUND_COMMERCIALNUMBER,"未携带商户号",ip);
            response.getWriter().write(GsonUtil.GSON.toJson(urlResponse));
            return false;
        }
        return true;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        response.setHeader("Access-Control-Allow-Origin", "*");     //允许所有来源访问
        response.setHeader("Access-Control-Allow-Methods", "PUT,POST,GET,DELETE,OPTIONS");    //允许访问的方式
        response.setHeader("Access-Control-Max-Age", "*");          //来指定本次预检请求的有效期，单位为秒
//        response.setHeader("Access-Control-Allow-Headers", "*");    //跨域 Header
        response.setHeader("Access-Control-Allow-Headers",
                "Origin,X-Requested-With,Content-Type,Accept,Content-Length,Authorization");
        response.setCharacterEncoding("UTF-8");
        String path = getPath(request);
        /* log.info("请求：{}",path);*/
//        boolean flag = ArrayUtils.contains(IP_WHITELIST,path);
//        //ip校验的访问接口
//        if(flag){
//            log.info("path---->>"+path);
//            log.info("come in---->>"+flag);
//            if (!checkIp(request, response)) {
//                return;
//            }
//        }

        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }
        //token校验
        UsernamePasswordAuthenticationToken authentication = getAuthentication(request, response);
        if (null == authentication) {
            return;
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }

    /*验证不在白名单中的请求的token
     * @title getAuthentication
     * @description  用户初次登陆的时候    redis中存储了：
     * 1 商户名，商户信息，过期时间为两天（token的过期时间为2h）
     * 2 商户id:token
     * 3 token:jsonObj(商户信息，roleName)
     * 当用户发送需要经过token认证的请求的时候  就会把商户信息绑定到线程池中 此时就可从线程池中根据token取出绑定的信息
     * @author vring
     * @param: request response
     * @throws  IOException
     */
    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException {
        long start = System.currentTimeMillis();
        response.setCharacterEncoding("utf-8");
        String token = request.getHeader("Authorization");
        if (token == null || token.isEmpty()) {
            UrlResponse response1 = new UrlResponse(false, ErrorCodeContents.NO_FOUND_LOGINTOKEN, "请携带Token令牌！", "");
            response.getWriter().write(GsonUtil.GSON.toJson(response1));
            return null;
        }
        try {
            boolean flag = JwtHelper.isExpiration(token);

             if (flag) {//已超时   验证是否刷新token
                return JwtHelper.refreshToken(response,token,redisUtil,threadUtil);
            }

            String username = JwtHelper.getUsername(token);
            long end = System.currentTimeMillis();
            log.info("login user : {},execution time: {}",username, (end - start) + " ms");
            if (username != null) {
                String jsonStr = redisUtil.get(username);
                log.info("jsonStr:{}",jsonStr);
                Commercial commercial = GsonUtil.stringToBo(jsonStr, Commercial.class);
                String path = getPath(request);
                log.info("请求：{}",path);
                threadUtil.bind(commercial);
                String roleName = JwtHelper.getUserRole(token);
                String old_token = redisUtil.get(commercial.getCommercialId());
                if(!old_token.equals(token)){
                    UrlResponse response1 = new UrlResponse(false, ErrorCodeContents.USER_IS_LOGGED_IN_ELSEWHERE, "用户已在其他地方登录！", "");
                    response.getWriter().write(GsonUtil.GSON.toJson(response1));
                    return null;
                }
//                ArrayList<GrantedAuthority> authorities = new ArrayList<>();
//                authorities.add(new GrantedAuthorityImpl(roleName));
//                return new UsernamePasswordAuthenticationToken(username, null, authorities);
                return new UsernamePasswordAuthenticationToken(username, null,
                        Collections.singleton(new SimpleGrantedAuthority(roleName)));
            }
        } catch (ExpiredJwtException e) {
            log.error("Token令牌已过期");
            response.setStatus(500);
            UrlResponse response1 = new UrlResponse(false, ErrorCodeContents.LOGIN_TOKEN_TIMEOUT, "令牌已过期！", "");
            response.getWriter().write(GsonUtil.GSON.toJson(response1));
//            throw new TokenException("Token已过期");
        } catch (UnsupportedJwtException e) {
            log.error("Token令牌格式错误");
            throw new TokenException("令牌格式错误");
        } catch (MalformedJwtException e) {
            log.error("Token令牌没有被正确构造");
            response.setStatus(500);
            UrlResponse response1 = new UrlResponse(false, ErrorCodeContents.CHECK_LOGINTOKEN_FAILUE, "令牌格式错误！", "");
            response.getWriter().write(GsonUtil.GSON.toJson(response1));
//            throw new TokenException("Token没有被正确构造");
        } catch (SignatureException e) {
            log.error("签名失败 ");
            response.setStatus(500);
            UrlResponse response1 = new UrlResponse(false, ErrorCodeContents.CHECK_LOGINSIGN_FAILUE, "签名失败！", "");
            response.getWriter().write(GsonUtil.GSON.toJson(response1));
//            throw new TokenException("签名失败");
        } catch (IllegalArgumentException e) {
            log.error("非法参数异常");
            response.setStatus(500);
            UrlResponse response1 = new UrlResponse(false, ErrorCodeContents.PARAMS_INCOMPLETE, "非法参数异常！", "");
            response.getWriter().write(GsonUtil.GSON.toJson(response1));
//            throw new TokenException("非法参数异常");
        }catch (Exception e){
            log.error("{}",e);
        }
        return null;
    }


    public String getPath(HttpServletRequest request){
        String path = request.getContextPath();
        String requestUrl = request.getRequestURL().toString();
        int num = requestUrl.indexOf(path) + path.length();
        String realPath = requestUrl.substring(num);
        int index1 = realPath.indexOf("/");
        int index2 = realPath.indexOf("/", index1+1);
        if(index2 != -1){
            String result = realPath.substring(index1+1,index2);
            if(result.equals("css") || result.equals("js") || result.equals("fonts") || result.equals("img")){
                return "";
            }
        }

        return realPath;
    }
}
