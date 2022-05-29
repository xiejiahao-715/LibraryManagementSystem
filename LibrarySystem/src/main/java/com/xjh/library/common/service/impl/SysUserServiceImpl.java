package com.xjh.library.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xjh.library.common.entity.SysUser;
import com.xjh.library.common.service.SysUserService;
import com.xjh.library.common.mapper.SysUserMapper;
import org.springframework.stereotype.Service;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser>
    implements SysUserService{

}




