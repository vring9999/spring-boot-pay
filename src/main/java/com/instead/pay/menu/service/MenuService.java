package com.instead.pay.menu.service;

import com.instead.pay.menu.model.Menu;
import com.instead.pay.menu.model.RoleMenu;

import java.util.List;
import java.util.Map;

/**
 * @author vring
 * @ClassName MenuService.java
 * @Description TODO
 * @createTime 2019/12/11 14:30
 */
public interface MenuService {
    void insert(Menu menu);

    void insert(RoleMenu roleMenu);

    /*
     * @title
     * @description 根据roleId   menuId  parentId查询菜单
     * @author vring
     * @param: roleId   menuId  parentId
     * @throws
     */
    List<Map<String,Object>> findMenuByRole(Map<String,Object> param);

    Map<String, Object> findMenuInfoByUsername(String name);

    List<String> findAllUrl(Map<String,Object> param);

    void addRoleMenus(List<RoleMenu> list);

    Menu getMenu(Map<String,Object> param);

    void updMenu(Menu menu);

    void delMenu(String menuId);

    void delRoleMenus(String roleId);
}
