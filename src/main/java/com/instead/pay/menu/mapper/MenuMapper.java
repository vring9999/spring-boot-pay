package com.instead.pay.menu.mapper;

import com.instead.pay.menu.model.Menu;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface MenuMapper {

    void insert(Menu menu);
    /*
     * @title
     * @description 根据roleId   menuId  parentId查询菜单
     * @author vring
     * @param: roleId   menuId  parentId  roleName
     * @throws
     */
    List<Map<String,Object>> findMenuByRole(Map<String,Object> param);

    Menu getMenu(Map<String,Object> param);

    void updMenu(Menu menu);

    void delMenu(String menuId);

    List<String> findAllUrl(Map<String,Object> param);

}
