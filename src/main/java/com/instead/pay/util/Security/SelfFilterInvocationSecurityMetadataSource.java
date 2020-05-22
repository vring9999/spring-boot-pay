package com.instead.pay.util.Security;

import com.instead.pay.menu.service.MenuService;
import com.instead.pay.role.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author vring
 * @date 2019/12/11
 * <p>
 * * 动态获取url权限配置
 */
@Slf4j
@Component
public class SelfFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    @Autowired
    private MenuService menuService;

    @Autowired
    private RoleService roleService;

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {

        Set<ConfigAttribute> set = new HashSet<>();
        // 获取请求地址
        String requestUrl = ((FilterInvocation) o).getRequestUrl();
        log.info("requestUrl >> {}", requestUrl);
        HttpServletRequest req = ((FilterInvocation) o).getHttpRequest();
        List<String> menuUrl = menuService.findAllUrl(null);
        for (String url : menuUrl) {
            //将这个请求url和数据库中查询出来的所有url pattern一一对照，看符合哪一个url pattern，
//             然后就获取到该url pattern所对应的角色
            if (antPathMatcher.match(url, requestUrl)) {
                List<String> roleNames = roleService.findRoleNameByUrl(url);//当前请求需要的权限
                roleNames.forEach(roleName -> {
                    SecurityConfig securityConfig = new SecurityConfig(roleName);
                    set.add(securityConfig);
                });
            }
        }
        //没有匹配上的资源，都是登录访问
//        if (ObjectUtils.isEmpty(set)) {
//            return SecurityConfig.createList("ROLE_LOGIN");
//        }
        return set;
    }


    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return FilterInvocation.class.isAssignableFrom(aClass);
    }
}
