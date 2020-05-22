package com.instead.pay.role.service;

import com.instead.pay.role.model.Role;

import java.util.List;
import java.util.Map;

public interface RoleService {
    List<Role> queryRole(Map<String,Object> param);

    List<String> findRoleNameByUrl(String menuUrl);

    void insertRole(Role role);

    void updateRole(Role role);

    void deleteRole(String roleId);
}
