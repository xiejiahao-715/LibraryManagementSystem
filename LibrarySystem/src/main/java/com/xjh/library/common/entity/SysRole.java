package com.xjh.library.common.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 系统的角色表
 */
@Data
@TableName(value ="sys_role")
public class SysRole implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 角色id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 角色的名称
     */
    private String name;

    /**
     * 对角色的描述
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