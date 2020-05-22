package com.instead.pay.menu.mapper;

import com.instead.pay.menu.model.RoleMenu;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleMenuMapper {

    void insert(RoleMenu roleMenu);



    void addRoleMenus(List<RoleMenu> list);


    void delRoleMenus(String roleId);
}
