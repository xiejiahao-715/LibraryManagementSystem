package com.xjh.library.common.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xjh.library.common.entity.BookOutStock;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xjh.library.module.admin.entity.BookOutStockInfoVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface BookOutStockMapper extends BaseMapper<BookOutStock> {

    /**
     * 自定义分页查询 获取突出的出库记录
     * @param wrapper 查询条件 对象的名称需要以 outStock. 开头，例如 wrapper.eq("outStock.id",2222)
     * @return 返回自定义分页对象
     */
    IPage<BookOutStockInfoVo> getPageBookOutStockInfo(Page<BookOutStockInfoVo> page,@Param(Constants.WRAPPER) QueryWrapper<BookOutStockInfoVo> wrapper);
}




