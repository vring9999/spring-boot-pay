package com.instead.pay.handler;

import com.instead.pay.util.GsonUtil;
import com.instead.pay.util.Security.UrlResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author vring
 * @date 2019/12/12
 * <p>
 * 注销成功
 */
@Component
public class UrlLogoutSuccessHandler implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        UrlResponse response = new UrlResponse();
        response.setSuccess(true);
        response.setErrorCode("200");
        response.setMes("Logout Success!!");
        response.setData(null);
        httpServletResponse.getWriter().write(GsonUtil.GSON.toJson(response));
    }
}
