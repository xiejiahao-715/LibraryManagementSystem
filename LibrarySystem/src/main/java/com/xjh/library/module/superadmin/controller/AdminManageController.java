package com.xjh.library.module.superadmin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xjh.library.common.api.CommonResult;
import com.xjh.library.common.constants.SysRoleConstants;
import com.xjh.library.common.utils.PageUtil;
import com.xjh.library.module.security.annotation.Authorize;
import com.xjh.library.module.superadmin.entity.AddAdminFormVo;
import com.xjh.library.module.superadmin.entity.AdminInfoVo;
import com.xjh.library.module.superadmin.service.AdminManageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = "超级管理员：管理普通的管理员")
@ResponseBody
@RestController
@RequestMapping("/api/superadmin/adminManage")
public class AdminManageController {

    @Autowired
    private AdminManageService adminManageService;

    @ApiOperation("分页查询管理员的信息")
    @GetMapping("/page/{current}/{limit}")
    @Authorize(SysRoleConstants.SUPER_ADMIN)
    public CommonResult getPageAdminInfo(
            @PathVariable("current") Long current,
            @PathVariable("limit") Long limit){
        IPage<AdminInfoVo> page = adminManageService.getPageAdminInfo(current,limit);
        return CommonResult.success().message("分页查询管理员信息成功").data(PageUtil.toMap(page));
    }

    @ApiOperation("添加管理员")
    @PostMapping("/add")
    @Authorize(SysRoleConstants.SUPER_ADMIN)
    public CommonResult addAdmin(@Valid @RequestBody AddAdminFormVo addAdminFormVo){
        adminManageService.addAdmin(addAdminFormVo);
        return CommonResult.success().message("添加管理员成功");
    }
}