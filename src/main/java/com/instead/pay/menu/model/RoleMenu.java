package com.instead.pay.menu.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author vring
 * @ClassName RoleMenu.java
 * @Description TODO
 * @createTime 2019/12/11 14:27
 */
@Data
@Component
@AllArgsConstructor
@NoArgsConstructor
public class RoleMenu {
    private String id;
    private String roleId;
    private String menuId;
    private Date updateTime;
}
