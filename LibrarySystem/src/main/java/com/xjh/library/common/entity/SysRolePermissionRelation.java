package com.xjh.library.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 系统的角色-权限关联表
 */
@Data
@TableName(value ="sys_role_permission_relation")
public class SysRolePermissionRelation implements Serializable {


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 角色-权限关联的唯一id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 角色id
     */
    private Long roleId;

    /**
     * 权限id
     */
    private Long permissionId;
}