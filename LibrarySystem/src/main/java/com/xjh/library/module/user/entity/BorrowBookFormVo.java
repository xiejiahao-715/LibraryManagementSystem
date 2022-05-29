package com.xjh.library.module.user.entity;

import lombok.Data;

import javax.validation.constraints.NotNull;

// 借阅图书时前端传过来的表单对象
@Data
public class BorrowBookFormVo {
    @NotNull(message = "图书ID不能为空")
    private Long bookId;
    // 借阅的时间
    private Integer borrowTime;
}
