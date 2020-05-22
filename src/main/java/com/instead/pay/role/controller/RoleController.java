package com.instead.pay.role.controller;

import com.alibaba.fastjson.JSONObject;
import com.instead.pay.commercial.service.CommercialService;
import com.instead.pay.role.model.Role;
import com.instead.pay.role.service.RoleService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * @author vring
 * @ClassName RoleController.java
 * @Description
 * @createTime 2019/12/13 10:06
 */
@Component
@Controller
@RequestMapping("/role")
@Slf4j
public class RoleController {
    @Autowired
    private RoleService roleService;

    @Autowired
    private CommercialService commercialService;
//
//    @ApiOperation(value = "查询用户权限")
//    @GetMapping("/authorityList")
//    public List<String> authorityList(){
//        List<String> authentication = getAuthentication();
//        return authentication;
//    }


//    /**
//     * 获取用户所拥有的权限列表
//     * @return
//     */
//    public List<String> getAuthentication() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
//        List<String> list = new ArrayList<>();
//        for (GrantedAuthority grantedAuthority : authorities) {
//            log.info("权限列表：{}", grantedAuthority.getAuthority());
//            list.add(grantedAuthority.getAuthority());
//        }
//
//        return list;
//    }

    /**
     * 添加角色
     * @param role
     * @return
     */
    @RequestMapping(value = "/addRole", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject addRole(Role role){
        JSONObject jsonObject=new JSONObject();
        try {
            //创建roleId
            String roleId= UUID.randomUUID().toString();
            role.setRoleId(roleId);
            roleService.insertRole(role);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    /**
     * 删除角色
     * @param roleId
     * @return
     */
    @RequestMapping(value = "/delRole", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject delRole(String roleId){
        JSONObject jsonObject=new JSONObject();
        try {
            roleService.deleteRole(roleId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


}
