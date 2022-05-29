package com.xjh.library.common.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 借阅者(普通用户)的资源信息
 */
@Data
@TableName(value ="user_resource")
public class UserResource implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    @TableId(type = IdType.INPUT)
    private Long userId;

    /**
     * 每个用户能借阅的最多图书数量
     */
    private Integer borrowMaxNum;

    /**
     * 用户已经借阅图书的数量
     */
    private Integer borrowedNum;

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