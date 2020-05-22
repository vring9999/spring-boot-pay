package com.instead.pay.role.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 权限角色
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class Role {
    private String roleId;

    private String roleName;

    private int roleLevel;
}
