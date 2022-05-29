package com.xjh.library.common.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 图书出库记录表
 */
@TableName(value ="book_out_stock")
@Data
public class BookOutStock implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 出库记录id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 图书id
     */
    private Long bookId;

    /**
     * 负责出库管理员的id
     */
    private Long userId;

    /**
     * 出库的时间
     */
    private LocalDateTime outStockTime;

    /**
     * 出库图书的数量
     */
    private Integer num;

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