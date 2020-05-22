package com.instead.pay.handler;

import com.instead.pay.util.GsonUtil;
import com.instead.pay.util.Security.UrlResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author vring
 * @date 2019/12/12
 *
 * 登录失败
 */
@SuppressWarnings("Duplicates")
@Component
public class UrlAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {

        UrlResponse response = new UrlResponse();
        response.setSuccess(false);
        response.setErrorCode("401");
        response.setMes("Login Failure!");
        response.setData(null);

        httpServletResponse.setStatus(401);
        httpServletResponse.getWriter().write(GsonUtil.GSON.toJson(response));
    }
}
