package com.xjh.library.module.admin.entity;

import com.xjh.library.module.admin.validator.AddGroup;
import com.xjh.library.module.admin.validator.UpdateGroup;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDate;

// 管理员用编辑图书信息的对象(在编辑信息和添加图书的时候用到)
@Data
public class EditBookFormVo {
    // 图书的唯一id
    @NotNull(message = "图书id不能为空",groups = UpdateGroup.class)
    @Null(message = "请不要传入图书id字段",groups = AddGroup.class)
    private Long id;

    // 图书的分类ID
    @NotNull(message = "分类id不能为空",groups = AddGroup.class)
    private Long typeId;

    // 图书ISBN号
    @NotBlank(message = "isbn不能为空",groups = AddGroup.class)
    private String isbn;

    // 图书名称
    @NotBlank(message = "图书名称不能为空",groups = AddGroup.class)
    private String name;

    //  图书作者
    @NotBlank(message = "图书作者不能为空",groups = AddGroup.class)
    private String author;

    // 出版社
    @NotBlank(message = "出版社不能为空",groups = AddGroup.class)
    private String press;

    // 图书定价
    private String price;

    // 图书出版日期
    @NotNull(message = "出版日期不能为空",groups = AddGroup.class)
    private LocalDate publishTime;

    // 图书简介
    private String description;

    // 图书封面图片的链接地址
    private String coverImg;

    // 图书的库存数量
    @Min(value = 1,message = "库存最小为1",groups = AddGroup.class)
    @Null(message = "请不要传入图书数量字段stockNum",groups = UpdateGroup.class)
    private Integer stockNum;
}
