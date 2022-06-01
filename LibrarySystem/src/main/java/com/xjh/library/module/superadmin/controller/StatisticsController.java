package com.xjh.library.module.superadmin.controller;

import com.xjh.library.common.api.CommonResult;
import com.xjh.library.common.config.JacksonSerializerConfig;
import com.xjh.library.common.constants.SysRoleConstants;
import com.xjh.library.module.security.annotation.Authorize;
import com.xjh.library.module.superadmin.entity.statistics.BookNumOfType;
import com.xjh.library.module.superadmin.entity.statistics.DailyBookBorrowNum;
import com.xjh.library.module.superadmin.service.StatisticsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Api(tags = "超级管理员：数据统计接口")
@ResponseBody
@RestController
@RequestMapping("/api/superadmin/statistics")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @ApiOperation("统计图书的日借阅数量")
    @GetMapping("/dailyBookBorrowNum")
    @Authorize(SysRoleConstants.SUPER_ADMIN)
    public CommonResult getDailyBookBorrowNum(
            @DateTimeFormat(pattern = JacksonSerializerConfig.DEFAULT_DATE_FORMAT)
            @RequestParam("date") LocalDate date,
            @RequestParam("offsetDay") Integer offsetDay){
        List<DailyBookBorrowNum> data = statisticsService.getDailyBookBorrowNum(date,offsetDay);
        return CommonResult.success().message("获取图书日借阅量成功").data("list",data);
    }

    @ApiOperation("统计每一种分类对应的图书数量")
    @GetMapping("/bookNumOfType")
    @Authorize(SysRoleConstants.SUPER_ADMIN)
    public CommonResult getBookNumOfType(){
        List<BookNumOfType> data = statisticsService.getBookNumOfType();
        return CommonResult.success().message("获取每一种分类对应的图书数量成功").data("list",data);
    }
}
