package com.xjh.library.module.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xjh.library.common.api.CommonResult;
import com.xjh.library.common.constants.SysRoleConstants;
import com.xjh.library.common.utils.PageUtil;
import com.xjh.library.module.admin.entity.BookOutStockInfoVo;
import com.xjh.library.module.admin.entity.BorrowBookDetailVo;
import com.xjh.library.module.admin.entity.EditBookFormVo;
import com.xjh.library.module.admin.service.BookManageService;
import com.xjh.library.module.admin.service.MessageService;
import com.xjh.library.module.admin.validator.AddGroup;
import com.xjh.library.module.admin.validator.UpdateGroup;
import com.xjh.library.module.security.annotation.Authorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Api(tags = "图书管理员：图书管理相关操作")
@ResponseBody
@RestController
@RequestMapping("/api/admin/bookManage")
public class BookManageController {

    @Autowired
    private BookManageService bookManageService;

    @Autowired
    private MessageService messageService;

    @ApiOperation("根据Excel文件批量导入图书")
    @PostMapping("/batchAddBook")
    @Authorize(SysRoleConstants.ADMIN)
    public CommonResult batchAddBook(
            @RequestPart("file")  MultipartFile file){
        bookManageService.batchAddBook(file);
        return CommonResult.success().message("批量导入图书成功");
    }


    @ApiOperation("添加图书")
    @PostMapping("/addBook")
    @Authorize(SysRoleConstants.ADMIN)
    public CommonResult addBook(@Validated(AddGroup.class) @RequestBody EditBookFormVo bookFormVo){
        bookManageService.addBook(bookFormVo);
        return CommonResult.success().message("添加图书成功");
    }


    @ApiOperation("编辑图书")
    @PutMapping("/updateBook")
    @Authorize(SysRoleConstants.ADMIN)
    public CommonResult updateBook(@Validated(UpdateGroup.class)@RequestBody EditBookFormVo bookFormVo){
        bookManageService.updateBook(bookFormVo);
        return CommonResult.success().message("更新图书信息成功");
    }

    @ApiOperation("添加库存")
    @PutMapping("/addBookStock")
    @Authorize(SysRoleConstants.ADMIN)
    public CommonResult addBookStock(
            @RequestParam("bookId")Long bookId,
            @RequestParam("addNum")Integer addNum){
        bookManageService.addBookStock(bookId,addNum);
        return CommonResult.success().message("添加库存成功");
    }

    @ApiOperation("图书出库")
    @PostMapping("/bookOutStock")
    @Authorize(SysRoleConstants.ADMIN)
    public CommonResult bookOutStock(
            @RequestParam("bookId")Long bookId,
            @RequestParam("num")Integer num){
        bookManageService.bookOutStock(bookId,num);
        return CommonResult.success().message("图书出库成功");
    }

    @ApiOperation("获取图书出库记录")
    @GetMapping("/bookOutStock/{current}/{limit}")
    @Authorize(SysRoleConstants.ADMIN)
    public CommonResult getPageBookOutStockInfo(
            @PathVariable("current") Long current,
            @PathVariable("limit") Long limit){
        IPage<BookOutStockInfoVo> page = bookManageService.getPageBookOutStockInfo(current,limit);
        return CommonResult.success().message("获取图书出库记录成功").data(PageUtil.toMap(page));
    }

    @ApiOperation("分页查询获取所有的借阅信息")
    @GetMapping("/bookBorrowDetail/{current}/{limit}")
    @Authorize(SysRoleConstants.ADMIN)
    public CommonResult getPageBorrowBookDetail(
            @PathVariable("current") Long current,
            @PathVariable("limit") Long limit){
        IPage<BorrowBookDetailVo> page = bookManageService.getPageBorrowBookDetail(current,limit);
        return CommonResult.success().message("获取所有的借阅信息").data(PageUtil.toMap(page));
    }

    @ApiOperation("图书催还")
    @PutMapping("/callReturnBook/{borrowId}")
    @Authorize(SysRoleConstants.ADMIN)
    public CommonResult recallBookReturnNotify(
            @PathVariable("borrowId") Long borrowId){
        messageService.recallBookReturnNotify(borrowId);
        return CommonResult.success().message("催还通知已发出");
    }
}
