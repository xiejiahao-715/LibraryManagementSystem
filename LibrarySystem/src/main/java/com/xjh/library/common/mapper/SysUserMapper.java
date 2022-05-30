package com.xjh.library.common.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xjh.library.common.entity.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xjh.library.module.admin.entity.BorrowerInfoVo;
import com.xjh.library.module.superadmin.entity.AdminInfoVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {
    // 获取用户的角色
    List<String> getUserRoles(@Param("uid")Long uid);

    /**
     * 获取借阅者的分页信息 只要普通借阅者没有被删除，这可以查询出来，不看用户状态
     * @param page 分页对象
     * @return 返回分页信息
     */
    IPage<BorrowerInfoVo> getPageBorrowerInfo(Page<BorrowerInfoVo> page);

    /**
     * 获取管理员的分页信息
     * @param page 分页对象
     * @return 返回分页信息
     */
    IPage<AdminInfoVo> getPageAdminInfo(Page<BorrowerInfoVo> page);
}




