package com.xjh.library.module.security.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xjh.library.common.constants.AccountStatus;
import com.xjh.library.common.constants.SysRoleConstants;
import com.xjh.library.common.constants.UserResourceConstants;
import com.xjh.library.common.entity.SysRole;
import com.xjh.library.common.entity.SysUser;
import com.xjh.library.common.entity.SysUserRoleRelation;
import com.xjh.library.common.entity.UserResource;
import com.xjh.library.common.exception.MyException;
import com.xjh.library.common.mapper.SysUserMapper;
import com.xjh.library.common.service.SysRoleService;
import com.xjh.library.common.service.SysUserRoleRelationService;
import com.xjh.library.common.service.SysUserService;
import com.xjh.library.common.service.UserResourceService;
import com.xjh.library.module.security.UserDetailContextHolder;
import com.xjh.library.module.security.cache.UserDetailCacheService;
import com.xjh.library.module.security.entity.LoginFormVo;
import com.xjh.library.module.security.entity.RegisterFormVo;
import com.xjh.library.module.security.entity.UserDetail;
import com.xjh.library.module.security.service.LoginService;
import com.xjh.library.module.security.utils.JwtTokenUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private UserDetailCacheService userDetailCacheService;

    // 用户 表
    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysUserRoleRelationService sysUserRoleRelationService;

    @Autowired
    private UserResourceService userResourceService;


    @Override
    @Transactional(readOnly = true)
    public String login(LoginFormVo loginFormVo) {
        // 判断用户名是否存在，校验密码
        String username = loginFormVo.getUsername();
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername,username);
        // 查询指定的列
        wrapper.select(SysUser::getId,SysUser::getPassword,SysUser::getStatus);
        SysUser sysUser = sysUserService.getOne(wrapper);
        if(sysUser == null){
            throw new MyException("用户名不存在");
        }
        if(!Objects.equals(sysUser.getPassword(),loginFormVo.getPassword())){
            throw new MyException("密码错误");
        }
        // 查看用户的状态是否正常
        if (!Objects.equals(sysUser.getStatus(), AccountStatus.NORMAL.getStatusCode())) {
            // 用户状态不正常抛出异常
            for(AccountStatus status : AccountStatus.values()){
                if(status.getStatusCode().equals(sysUser.getStatus())){
                    throw new MyException(status.getMessage());
                }
            }
            throw new MyException("用户状态异常，请联系管理员查看");
        }
        // 登录成功返回token
        String token = JwtTokenUtil.buildToken(sysUser.getId());
        // 清空缓存中存在的用户信息，便于缓存更新
        userDetailCacheService.clearUserDetail(sysUser.getId());
        return token;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetail getUserDetail() {
        // 首先获取用户id
        Long id = UserDetailContextHolder.getUserId();
        // 先查看缓存中是否存在
        UserDetail userDetail = userDetailCacheService.getUserDetail(id);
        if(userDetail != null){
            // 缓存中存在则取缓存
            return userDetail;
        }
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getId,id);
        wrapper.eq(SysUser::getStatus,AccountStatus.NORMAL.getStatusCode());
        wrapper.select(SysUser::getId,SysUser::getUsername,SysUser::getEmail);
        SysUser user = sysUserService.getOne(wrapper);
        if(user == null){
            throw new MyException("无法拉取该用户信息，用户不存在或状态异常");
        }
        // 获取用户的角色名称
        List<String> roles = sysUserMapper.getUserRoles(id);
        userDetail = new UserDetail();
        //TODO 这里的权限管理暂且使用用户角色来代替，权限表的设置还没有完成
        userDetail.setPermissions(roles);
        BeanUtils.copyProperties(user,userDetail);
        // 将用户详细信息存入redis
        userDetailCacheService.setUserDetail(userDetail);
        return userDetail;
    }

    @Override
    @Transactional
    public void register(RegisterFormVo registerFormVo) {
        // 首先验证密码和确认密码已否一直
        if(!Objects.equals(registerFormVo.getPassword(),registerFormVo.getConfirmPassword())){
            throw new MyException("密码和确认密码不同");
        }
        // 判断用户名是否已经被注册
        LambdaQueryWrapper<SysUser> userWrapper = new LambdaQueryWrapper<>();
        userWrapper.eq(SysUser::getUsername,registerFormVo.getUsername());
        userWrapper.ne(SysUser::getStatus,AccountStatus.LOG_OFF.getStatusCode());
        if(sysUserService.count(userWrapper) != 0){
            throw new MyException("用户名已经被注册");
        }

        // 添加用户信息
        SysUser user = new SysUser();
        BeanUtils.copyProperties(registerFormVo,user);
        user.setStatus(AccountStatus.NORMAL.getStatusCode());
        if(!sysUserService.save(user)){
            throw new MyException("系统出错,注册失败");
        }

        // 初始化用户的角色
        // 查询用户角色的id
        LambdaQueryWrapper<SysRole> roleWrapper = new LambdaQueryWrapper<>();
        // 注册为普通用户(借阅者)
        roleWrapper.eq(SysRole::getName, SysRoleConstants.BORROWER);
        roleWrapper.select(SysRole::getId);
        SysRole role = sysRoleService.getOne(roleWrapper);
        if(role == null){
            throw new MyException("系统错误,获取用户角色失败");
        }

        // 添加 用户-角色 信息
        SysUserRoleRelation sysUserRoleRelation = new SysUserRoleRelation();
        sysUserRoleRelation.setRoleId(role.getId());
        sysUserRoleRelation.setUserId(user.getId());
        if(!sysUserRoleRelationService.save(sysUserRoleRelation)){
            throw new MyException("系统错误,保存用户角色失败");
        }

        // 初始化用户的资源信息
        UserResource userResource = new UserResource();
        userResource.setUserId(user.getId());
        // 初始化借阅数量为0
        userResource.setBorrowedNum(0);
        // 设置默认的最大借阅数量
        userResource.setBorrowMaxNum(UserResourceConstants.DEFAULT_MAX_BORROW_NUM);
        if(!userResourceService.save(userResource)){
            throw new MyException("系统错误,初始化用户资源失败");
        }
    }
}
