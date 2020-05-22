package com.instead.pay.commercial.service;

import com.instead.pay.commercial.mapper.CommercialMapper;
import com.instead.pay.commercial.model.Commercial;
import com.instead.pay.util.Security.SelfUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * @author vring
 * @ClassName UserDetailsServiceImpl.java
 * @createTime 2019/12/9 17:30
 */
@Service("UserDetailsServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private CommercialMapper commercialMapper;

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        Map<String,Object> param = new HashMap<String, Object>() ;
//        param.put("systemUserName",name);
        Commercial commercial = commercialMapper.login(name);
        String roleName = commercialMapper.findRoleNameByUser(name);
        if(null == commercial){
            throw new UsernameNotFoundException(name);
        }
        SelfUserDetails userInfo = new SelfUserDetails();
        userInfo.setUsername(name); //任意登录用户名
        userInfo.setPassword(commercial.getCommercialPassword()); //从数据库获取密码
        Set<SimpleGrantedAuthority> authoritiesSet = new HashSet<>();
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(roleName);
        SimpleGrantedAuthority simpleGrantedAuthority1 = new SimpleGrantedAuthority(commercial.getCommercialNumber());
//        authoritiesSet.add(param);
        authoritiesSet.add(simpleGrantedAuthority);
        authoritiesSet.add(simpleGrantedAuthority1);
        userInfo.setAuthorities(authoritiesSet);
        return userInfo;
//        return new org.springframework.security.core.userdetails.User(systemUser.getSystemUserName(), systemUser.getPassword(), emptyList());
    }
}
