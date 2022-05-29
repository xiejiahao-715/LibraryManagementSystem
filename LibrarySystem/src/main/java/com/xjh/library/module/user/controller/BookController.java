package com.xjh.library.module.user.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xjh.library.common.api.CommonResult;
import com.xjh.library.common.constants.SysRoleConstants;
import com.xjh.library.common.entity.BookType;
import com.xjh.library.common.utils.PageUtil;
import com.xjh.library.module.security.annotation.Authorize;
import com.xjh.library.module.user.entity.*;
import com.xjh.library.module.user.service.BookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "借阅者：有关图书的操作")
@ResponseBody
@RestController
@RequestMapping("/api/user/book")
public class BookController {

    @Autowired
    private BookService bookService;

    @ApiOperation("分页查询图书信息")
    @PostMapping("/page/{current}/{limit}")
    @Authorize({SysRoleConstants.BORROWER,SysRoleConstants.ADMIN})
    public CommonResult getPageBookInfo(
            @PathVariable("current") Long current,
            @PathVariable("limit") Long limit,
            @RequestBody(required = false) QueryBookFormVo queryBookFormVo){
        IPage<BookInfoVo> pageInfo = bookService.getPageBookInfo(current,limit,queryBookFormVo);
        return CommonResult.success().message("分页查询图书信息成功").data(PageUtil.toMap(pageInfo));
    }

    @ApiOperation("获取所有的图书分类")
    @GetMapping("/typeList")
    public CommonResult getBookTypeList(){
        List<BookType> typeList = bookService.getBookTypeList();
        return CommonResult.success().message("获取所有图书分类成功").data("types",typeList);
    }

    @ApiOperation("根据图书id获取图书信息")
    @GetMapping("/info")
    public CommonResult getBookInfo(@RequestParam("bookId")Long bookId){
        BookInfoVo bookInfo = bookService.getBookInfoById(bookId);
        return CommonResult.success().message("获取图书信息成功").data("book",bookInfo);
    }

    @ApiOperation("借阅图书")
    @PostMapping("/borrow")
    @Authorize(SysRoleConstants.BORROWER)
    public CommonResult borrowBook(@Valid @RequestBody BorrowBookFormVo borrowBookFormVo){
        bookService.borrowBook(borrowBookFormVo);
        return CommonResult.success().message("借阅图书成功");
    }

    @ApiOperation("预约图书")
    @PostMapping("/reserve/{bookId}")
    @Authorize(SysRoleConstants.BORROWER)
    public CommonResult reserveBook(@PathVariable("bookId") Long bookId){
        bookService.reserveBook(bookId);
        return CommonResult.success().message("预定图书成功");
    }

    @ApiOperation("分页查询获取图书借阅记录")
    @GetMapping("/borrow/page/{current}/{limit}")
    @Authorize(SysRoleConstants.BORROWER)
    public CommonResult getPageBorrowBookInfo(
            @PathVariable("current") Long current,
            @PathVariable("limit") Long limit){
        IPage<BorrowBookInfoVo> pageInfo = bookService.getPageBorrowBookInfo(current,limit);
        return CommonResult.success().message("分页查询获取图书借阅记录成功").data(PageUtil.toMap(pageInfo));
    }

    @ApiOperation("分页查询获取图书的预约记录")
    @GetMapping("/reserve/page/{current}/{limit}")
    @Authorize(SysRoleConstants.BORROWER)
    public CommonResult getPageReserveBookInfo(
            @PathVariable("current") Long current,
            @PathVariable("limit") Long limit){
        IPage<ReserveBookInfoVo> pageInfo = bookService.getPageReserveBookInfo(current,limit);
        return CommonResult.success().message("分页查询获取图书预约记录成功").data(PageUtil.toMap(pageInfo));
    }

    @ApiOperation("还书")
    @PostMapping("/return/{borrowId}")
    @Authorize(SysRoleConstants.BORROWER)
    public CommonResult returnBook(
            @PathVariable("borrowId") Long borrowId){
        bookService.returnBook(borrowId);
        return CommonResult.success().message("归还图书成功");
    }

    @ApiOperation("续借")
    @PostMapping("/renew")
    @Authorize(SysRoleConstants.BORROWER)
    public CommonResult renewBook(@Valid @RequestBody RenewBookFormVo renewBookFormVo){
        bookService.renewBook(renewBookFormVo);
        return CommonResult.success().message("借阅图书成功");
    }
}
