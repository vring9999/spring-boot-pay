package com.instead.pay.menu.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author vring
 * @ClassName Menu.java
 * @Description TODO
 * @createTime 2019/12/11 14:24
 */
@Data
@Component
@AllArgsConstructor
@NoArgsConstructor
public class Menu {
    private String menuId;
    private String parentId;
    private String urlPre;
    private String menuName;
    private String menuUrl;
    private Date updateTime;
    private String remark;
}
