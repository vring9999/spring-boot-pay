package com.instead.pay.menu.service;

import com.instead.pay.commercial.mapper.CommercialMapper;
import com.instead.pay.commercial.model.Commercial;
import com.instead.pay.menu.mapper.MenuMapper;
import com.instead.pay.menu.mapper.RoleMenuMapper;
import com.instead.pay.menu.model.Menu;
import com.instead.pay.menu.model.RoleMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author vring
 * @ClassName MenuServiceImpl.java
 * @Description TODO
 * @createTime 2019/12/11 14:32
 */
@Service
public class MenuServiceImpl implements MenuService{

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private CommercialMapper commercialMapper;

    @Autowired
    private RoleMenuMapper roleMenuMapper;
    @Override
    public void insert(Menu menu) {
        menuMapper.insert(menu);
    }

    @Override
    public void insert(RoleMenu roleMenu) {
        roleMenuMapper.insert(roleMenu);
    }

    @Override
    public List<Map<String,Object>> findMenuByRole(Map<String,Object> param) {
        return menuMapper.findMenuByRole(param);
    }

    /**
     * 根据用户名查询对应权限菜单
     * @param name
     * @return
     */
    public Map<String, Object> findMenuInfoByUsername(String name){
        Map<String,Object> resultMap = new HashMap<>();
        Map<String,Object> param =  new HashMap<String,Object>();
        Commercial commercial = commercialMapper.login(name);
        List<Map<String, Object>> menuInfoList = new ArrayList<>();
        param.clear();
        param.put("roleId",commercial.getRoleId());
        List<Map<String, Object>> rootList = this.findMenuByRole(param);
        if (!ObjectUtils.isEmpty(rootList)) {
            rootList.forEach(menu -> {
                String menuId = ""+menu.get("menuId");
                param.clear();
                param.put("parentId",menuId);
                List<Map<String, Object>> children = this.findMenuByRole(param);
                Map<String, Object> map = new HashMap<>();
                map.put("menuId", menuId);
                map.put("menuUrl", menu.get("menuUrl"));
                map.put("menuName", menu.get("menuName"));
                map.put("parentId", menu.get("parentId"));
                map.put("urlPre", menu.get("urlPre"));
                map.put("children", children);
                menuInfoList.add(map);
            });
        }
        resultMap.put("menuList",menuInfoList);
        return resultMap;
    }

    @Override
    public List<String> findAllUrl(Map<String, Object> param) {
        return menuMapper.findAllUrl(param);
    }

    @Override
    public void addRoleMenus(List<RoleMenu> list) {
        roleMenuMapper.addRoleMenus(list);
    }

    @Override
    public Menu getMenu(Map<String, Object> param) {
        return menuMapper.getMenu(param);
    }

    @Override
    public void updMenu(Menu menu) {
        menuMapper.updMenu(menu);
    }

    @Override
    public void delMenu(String menuId) {
        menuMapper.delMenu(menuId);
    }

    @Override
    public void delRoleMenus(String roleId) { roleMenuMapper.delRoleMenus(roleId); }


}
