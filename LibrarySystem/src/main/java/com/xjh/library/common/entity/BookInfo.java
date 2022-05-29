package com.xjh.library.common.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 图书的基本信息表
 */
@Data
@TableName(value ="book_info")
public class BookInfo implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 图书唯一id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 图书的分类ID
     */
    private Long typeId;

    /**
     * 图书ISBN号
     */
    private String isbn;

    /**
     * 图书名称
     */
    private String name;

    /**
     * 图书作者
     */
    private String author;

    /**
     * 出版社
     */
    private String press;

    /**
     * 图书定价
     */
    private String price;

    /**
     * 图书出版日期
     */
    private LocalDate publishTime;

    /**
     * 图书简介
     */
    private String description;

    /**
     * 图书封面图片的链接地址
     */
    private String coverImg;

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