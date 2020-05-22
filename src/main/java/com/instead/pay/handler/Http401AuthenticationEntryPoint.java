package com.instead.pay.handler;

import com.instead.pay.util.GsonUtil;
import com.instead.pay.util.Security.UrlResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Description: 自定义认证拦截器
 */
public class Http401AuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final String headerValue;

    public Http401AuthenticationEntryPoint(String headerValue) {
        this.headerValue = headerValue;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        UrlResponse urlResponse = new UrlResponse(false,HttpServletResponse.SC_UNAUTHORIZED+"","访问资源未经授权，请认证","");
        // 这里是用 WWW-Authenticate
        response.setHeader("WWW-Authenticate", this.headerValue);
        response.setStatus(401);
//        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
        response.getWriter().write(GsonUtil.GSON.toJson(urlResponse));
    }

}
