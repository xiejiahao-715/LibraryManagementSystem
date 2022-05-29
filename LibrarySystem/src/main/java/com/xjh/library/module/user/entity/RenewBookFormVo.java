package com.xjh.library.module.user.entity;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

// 续借图书的表单对象
@Data
public class RenewBookFormVo {

    // 续借订单的id
    @NotNull(message = "续借订单id不能为空")
    private Long borrowId;

    // 续借的时间
    @Range(min=1, max=30,message = "续借时间为1-30天内")
    private Integer renewTime;

}
