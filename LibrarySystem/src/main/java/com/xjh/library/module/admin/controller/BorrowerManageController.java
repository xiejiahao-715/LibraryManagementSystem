package com.xjh.library.module.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xjh.library.common.api.CommonResult;
import com.xjh.library.common.constants.SysRoleConstants;
import com.xjh.library.common.utils.PageUtil;
import com.xjh.library.module.admin.entity.BorrowerInfoVo;
import com.xjh.library.module.admin.service.BorrowerManageService;
import com.xjh.library.module.security.annotation.Authorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "图书管理员：借阅者管理相关操作")
@ResponseBody
@RestController
@RequestMapping("/api/admin/borrowerManage")
public class BorrowerManageController {

    @Autowired
    private BorrowerManageService borrowerManageService;


    @ApiOperation("分页查询获取借阅者信息")
    @GetMapping("/page/{current}/{limit}")
    @Authorize(SysRoleConstants.ADMIN)
    public CommonResult getPageBorrowerInfo(
            @PathVariable("current") Long current,
            @PathVariable("limit") Long limit){
        IPage<BorrowerInfoVo> page = borrowerManageService.getPageBorrowerInfo(current,limit);
        return CommonResult.success().data(PageUtil.toMap(page));
    }

    @ApiOperation("修改用户的最大借阅数量")
    @PutMapping("/maxBorrowNum/{borrowerId}")
    @Authorize(SysRoleConstants.ADMIN)
    public CommonResult updateBorrowMaxNum(
            @PathVariable("borrowerId") Long borrowerId,
            @RequestParam("newValue")Integer newValue){
        borrowerManageService.updateBorrowMaxNum(borrowerId,newValue);
        return CommonResult.success().message("修改用户的最大借阅数量");
    }
}
