package com.instead.pay.role.service;

import com.instead.pay.role.mapper.RoleMapper;
import com.instead.pay.role.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public List<Role> queryRole(Map<String, Object> param) {
        return roleMapper.queryRole(param);
    }

    @Override
    public List<String> findRoleNameByUrl(String menuUrl) {
        return roleMapper.findRoleNameByUrl(menuUrl);
    }

    @Override
    public void insertRole(Role role) {
        roleMapper.insertRole(role);
    }

    @Override
    public void updateRole(Role role) {
        roleMapper.updateRole(role);
    }

    @Override
    public void deleteRole(String roleId) {
        roleMapper.deleteRole(roleId);
    }
}
