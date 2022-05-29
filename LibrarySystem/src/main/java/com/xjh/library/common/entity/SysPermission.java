package com.xjh.library.common.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 系统的权限表
 */
@Data
@TableName(value ="sys_permission")
public class SysPermission implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 权限的唯一id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 权限的名称
     */
    private String name;

    /**
     * 对该权限的描述
     */
    private String description;

    /**
     * 逻辑删除字段
     */
    @TableLogic
    @TableField(select = false)
    private Boolean deleted;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT,select = false)
    private LocalDateTime gmtCreate;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE,select = false)
    private LocalDateTime gmtUpdate;
}