package com.instead.pay.role.mapper;

import com.instead.pay.role.model.Role;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface RoleMapper {

    List<Role> queryRole(Map<String,Object> param);

    List<String> findRoleNameByUrl(String menuUrl);

    void insertRole(Role role);

    void updateRole(Role role);

    void deleteRole(String roleId);
}
