package com.xjh.library.module.user.controller;

import com.xjh.library.common.api.CommonResult;
import com.xjh.library.common.constants.SysRoleConstants;
import com.xjh.library.common.entity.UserMsg;
import com.xjh.library.common.entity.UserResource;
import com.xjh.library.module.security.annotation.Authorize;
import com.xjh.library.module.user.service.UserManageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "借阅者：账号管理相关操作")
@ResponseBody
@RestController
@RequestMapping("/api/user/userManage")
public class UserManageController {

    @Autowired
    private UserManageService userManageService;

    @ApiOperation("获取用户的资源数")
    @GetMapping("/resource")
    @Authorize(SysRoleConstants.BORROWER)
    public CommonResult getResource(){
        UserResource resource = userManageService.getResource();
        return CommonResult.success().message("获取用户志愿成功").data("resource",resource);
    }

    @ApiOperation("获取用户的消息列表")
    @GetMapping("/msg")
    public CommonResult getMsg(){
        List<UserMsg> userMsgList = userManageService.getUserMsgList();
        return CommonResult.success().message("获取消息列表成功").data("msg",userMsgList);
    }

}
