package com.xjh.library.module.superadmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xjh.library.common.constants.AccountStatus;
import com.xjh.library.common.constants.SysRoleConstants;
import com.xjh.library.common.entity.SysRole;
import com.xjh.library.common.entity.SysUser;
import com.xjh.library.common.entity.SysUserRoleRelation;
import com.xjh.library.common.exception.MyException;
import com.xjh.library.common.mapper.SysUserMapper;
import com.xjh.library.common.service.SysRoleService;
import com.xjh.library.common.service.SysUserRoleRelationService;
import com.xjh.library.common.service.SysUserService;
import com.xjh.library.module.superadmin.entity.AddAdminFormVo;
import com.xjh.library.module.superadmin.entity.AdminInfoVo;
import com.xjh.library.module.superadmin.service.AdminManageService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminManageServiceImpl implements AdminManageService {

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysUserRoleRelationService sysUserRoleRelationService;

    @Override
    @Transactional(readOnly = true)
    public IPage<AdminInfoVo> getPageAdminInfo(Long current, Long limit) {
        IPage<AdminInfoVo> page = userMapper.getPageAdminInfo(new Page<>(current,limit));
        page.getRecords().forEach(adminInfoVo -> {
            for(AccountStatus status : AccountStatus.values()){
                if(status.getStatusCode().equals(adminInfoVo.getStatus())){
                    adminInfoVo.setDescription(status.getMessage());
                }
            }
        });
        return page;
    }

    @Override
    @Transactional
    public void addAdmin(AddAdminFormVo addAdminFormVo) {
        // 首先判断用户名是否被注册
        LambdaQueryWrapper<SysUser> sysUserWrapper = new LambdaQueryWrapper<>();
        sysUserWrapper.eq(SysUser::getUsername,addAdminFormVo.getUsername());
        if(sysUserService.count(sysUserWrapper) != 0){
            throw new MyException("用户名已经被注册");
        }

        // 保存用户信息
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(addAdminFormVo,sysUser);
        sysUser.setStatus(AccountStatus.NORMAL.getStatusCode());
        if(!sysUserService.save(sysUser)){
            throw new MyException("系统错误，添加管理员失败");
        }

        // 查找管理员的用户id
        LambdaQueryWrapper<SysRole> roleWrapper = new LambdaQueryWrapper<>();
        // 注册为普通用户(借阅者)
        roleWrapper.eq(SysRole::getName, SysRoleConstants.ADMIN);
        roleWrapper.select(SysRole::getId);
        SysRole role = sysRoleService.getOne(roleWrapper);
        if(role == null){
            throw new MyException("系统错误,获取用户角色失败");
        }

        // 添加角色和用户的关联
        SysUserRoleRelation sysUserRoleRelation = new SysUserRoleRelation();
        sysUserRoleRelation.setUserId(sysUser.getId());
        sysUserRoleRelation.setRoleId(role.getId());
        if(!sysUserRoleRelationService.save(sysUserRoleRelation)){
            throw new MyException("系统错误，添加管理员失败");
        }
    }
}
