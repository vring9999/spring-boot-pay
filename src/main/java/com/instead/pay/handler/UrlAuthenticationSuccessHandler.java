package com.instead.pay.handler;

import com.instead.pay.menu.service.MenuService;
import com.instead.pay.util.GsonUtil;
import com.instead.pay.util.Security.UrlResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @author Andon
 * @date 2019/3/20
 *
 * 登录成功
 */
@Component
public class UrlAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Resource
    private MenuService menuService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {

        httpServletResponse.setCharacterEncoding("UTF-8");
        UrlResponse response = new UrlResponse();
        response.setSuccess(true);
        response.setErrorCode("10000");
        response.setMes("Login Success!");
        String username = (String) authentication.getPrincipal(); //表单输入的用户名
        Map<String, Object> userInfo = (Map<String, Object>) menuService.findMenuInfoByUsername(username);
        response.setData(userInfo);
        httpServletResponse.getWriter().write(GsonUtil.GSON.toJson(response));
    }
}
