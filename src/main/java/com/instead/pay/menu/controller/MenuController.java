package com.instead.pay.menu.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.instead.pay.menu.model.Menu;
import com.instead.pay.menu.model.RoleMenu;
import com.instead.pay.menu.service.MenuService;
import com.instead.pay.util.ErrorCodeContents;
import com.instead.pay.util.ResultKey;
import com.instead.pay.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.function.Function;

/**
 * @author vring
 * @ClassName MenuController.java
 * @Description TODO
 * @createTime 2019/12/11 16:04
 */
@Controller
@Slf4j
@RequestMapping("/menu")
public class MenuController {
    @Autowired
    private MenuService menuService;

    /**
     * @title findMenuByUser
     * @description   根据登录用户名查询对应的权限菜单
     * @author vring
     * @param: 用户名
     * @throws
     */
    @ResponseBody
    @PostMapping(value = "/getMenuByUser")
    public JSONObject findMenuByUser(String userName,String commercialNumber) {
        JSONObject jsonObject = new JSONObject();
        //查询当前登录的用户对应的权限菜单
        Map<String, Object> userInfo = menuService.findMenuInfoByUsername(userName);
        jsonObject.put(ResultKey.KEY_CODE,ErrorCodeContents.SUCCESS_CODE);
        jsonObject.put(ResultKey.KEY_SUCC, true);
        jsonObject.put(ResultKey.KEY_DATA,userInfo);
        return jsonObject;
    }

    /**
     * 查询目录信息
     * @return
     */
    @RequestMapping(value = "/queryMenu", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject queryMenu(){
        JSONObject jsonObject = new JSONObject();
        try {
            List<Map<String,Object>> findMenuByRole=menuService.findMenuByRole(null);
            jsonObject.put(ResultKey.KEY_LIST_DATA, findMenuByRole);
            jsonObject.put(ResultKey.KEY_SUCC, true);
            jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.SUCCESS_CODE);
            jsonObject.put(ResultKey.KEY_MSG, "查询成功！");
        } catch (Exception e) {
            log.error("{}", e);
            jsonObject.put(ResultKey.KEY_SUCC, false);
            jsonObject.put(ResultKey.KEY_CODE, ErrorCodeContents.USER_IS_NOT_FOUND);
            jsonObject.put(ResultKey.KEY_MSG, "查询失败，请稍后再试！");
        }
        return jsonObject;
    }


    /**
     * 角色添加菜单权限
     *
     * @param json
     * @return
     */
    @RequestMapping(value = "/addRoleMenu", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject addRoleMenu(@RequestBody JSONObject json) {
        JSONObject resultJson = new JSONObject();
        try {
            JSONArray arr = json.getJSONArray("menus");//分配的menuId集合
            String roleId = json.getString("roleId");
            //需要新增的permission
            List<RoleMenu> addList = new ArrayList<>();
            if(arr.size()!=0) {
                for (int i = 0; i < arr.size(); i++) {
                    String menuId = arr.getJSONObject(i).getString("menuId");
                    RoleMenu R = new RoleMenu();
                    //不重复插入root根目录
                    if (menuId.equals("root")) {
                        continue;
                    }
                    R.setMenuId(menuId);
                    R.setRoleId(roleId);
                    R.setUpdateTime(new Date());
                    R.setId(StringUtil.getUuid());
                    addList.add(R);//新增 二级
                /*List<String> newMenuIds = menuPermissionService.selectMenuIdForThree(menuId);
                if(newMenuIds.size() != 0) {
                    for(int j = 0 ; j < newMenuIds.size() ; j++) {
                        MenuPermission pp = new MenuPermission();
                        pp.setMenuId(newMenuIds.get(j));
                        pp.setUserId(userId);
                        pp.setCreateTime(new Date());
                        pp.setPermissionId(StringUtil.getUuid());
                        addlist.add(pp);//新增三级
                    }
                }*/
                }
                menuService.delRoleMenus(roleId);
                menuService.addRoleMenus(addList);
                resultJson.put(ResultKey.KEY_SUCC, true);
                resultJson.put(ResultKey.KEY_CODE, ErrorCodeContents.SUCCESS_CODE);
                resultJson.put(ResultKey.KEY_MSG, "操作成功！");
            }else{  //没有菜单
                menuService.delRoleMenus(roleId);
            }
        } catch (Exception e) {
            log.error("{}", e);
            resultJson.put(ResultKey.KEY_SUCC, false);
            resultJson.put(ResultKey.KEY_CODE, ErrorCodeContents.USER_IS_NOT_FOUND);
            resultJson.put(ResultKey.KEY_MSG, "添加失败！");
        }
        return resultJson;
    }


    /**
     * 添加目录
     *
     * @param menu
     * @return
     */
    @RequestMapping(value = "/addMenu", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject addMenu(Menu menu) {
        JSONObject resultJson = new JSONObject();
        try {
            return isMenuName(menu, Object -> {
                menu.setMenuId(StringUtil.getUuid());
                menu.setUpdateTime(new Date());
                menuService.insert(menu);
                resultJson.put(ResultKey.KEY_SUCC, true);
                resultJson.put(ResultKey.KEY_CODE, ErrorCodeContents.SUCCESS_CODE);
                resultJson.put(ResultKey.KEY_MSG, "添加成功！");
                return resultJson;
            });
        } catch (Exception e) {
            log.error("{}", e);
            resultJson.put(ResultKey.KEY_SUCC, false);
            resultJson.put(ResultKey.KEY_CODE, ErrorCodeContents.USER_IS_NOT_FOUND);
            resultJson.put(ResultKey.KEY_MSG, "添加失败！");
        }
        return resultJson;
    }

    /**
     * 修改目录
     *
     * @param menu
     * @return
     */
    @RequestMapping(value = "/updMenu", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject updMenu(Menu menu) {
        JSONObject resultJson = new JSONObject();
        try {
            return isMenuName(menu, object -> {
                menu.setUpdateTime(new Date());
                menuService.updMenu(menu);
                resultJson.put(ResultKey.KEY_SUCC, true);
                resultJson.put(ResultKey.KEY_CODE, ErrorCodeContents.SUCCESS_CODE);
                resultJson.put(ResultKey.KEY_MSG, "修改成功！");
                return resultJson;
            });
        } catch (Exception e) {
            log.error("{}", e);
            resultJson.put(ResultKey.KEY_SUCC, false);
            resultJson.put(ResultKey.KEY_CODE, ErrorCodeContents.USER_IS_NOT_FOUND);
            resultJson.put(ResultKey.KEY_MSG, "修改失败！");
        }
        return resultJson;
    }

    /**
     * 删除目录
     *
     * @param menuId
     * @return
     */
    @RequestMapping(value = "/delMenu", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject delMenu(String menuId) {
        JSONObject resultJson = new JSONObject();
        try {
            menuService.delMenu(menuId);
            resultJson.put(ResultKey.KEY_SUCC, true);
            resultJson.put(ResultKey.KEY_CODE, ErrorCodeContents.SUCCESS_CODE);
            resultJson.put(ResultKey.KEY_MSG, "删除成功！");
        } catch (Exception e) {
            log.error("{}", e);
            resultJson.put(ResultKey.KEY_SUCC, false);
            resultJson.put(ResultKey.KEY_CODE, ErrorCodeContents.USER_IS_NOT_FOUND);
            resultJson.put(ResultKey.KEY_MSG, "删除失败！");
        }
        return resultJson;
    }


    /**
     * 判断目录是否重复
     *
     * @param menu
     * @param function
     * @return
     */
    private JSONObject isMenuName(Menu menu, Function<JSONObject, JSONObject> function) {
        JSONObject resultJson = new JSONObject();
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("menuName", menu.getMenuName());
            Menu m = menuService.getMenu(param);
            if (null == m) {    //通过
                return function.apply(resultJson);
            } else {
                resultJson.put(ResultKey.KEY_SUCC, false);
                resultJson.put(ResultKey.KEY_CODE, ErrorCodeContents.USER_IS_NOT_FOUND);
                resultJson.put(ResultKey.KEY_MSG, "已存在该目录，添加失败！");
                return resultJson;
            }
        } catch (Exception e) {
            log.error("{}", e);
            resultJson.put(ResultKey.KEY_SUCC, false);
            resultJson.put(ResultKey.KEY_CODE, ErrorCodeContents.USER_IS_NOT_FOUND);
            resultJson.put(ResultKey.KEY_MSG, "添加失败,请检查数据库！");
        }
        return resultJson;
    }
}
