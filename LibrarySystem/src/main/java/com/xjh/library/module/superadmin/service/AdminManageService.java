package com.xjh.library.module.superadmin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xjh.library.module.superadmin.entity.AddAdminFormVo;
import com.xjh.library.module.superadmin.entity.AdminInfoVo;

// 超级管理员对普通管理的管理
public interface AdminManageService {

    /**
     * 分页查询所有的管理员
     * @param current 当前页
     * @param limit 每页的数量
     * @return 返回分页信息
     */
    IPage<AdminInfoVo> getPageAdminInfo(Long current,Long limit);

    /**
     * 添加管理员
     * @param addAdminFormVo 添加管理员的表单对象
     */
    void addAdmin(AddAdminFormVo addAdminFormVo);

}
