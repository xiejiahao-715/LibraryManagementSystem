package com.xjh.library.common.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xjh.library.common.entity.BookInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xjh.library.module.user.entity.BookInfoVo;
import com.xjh.library.module.user.entity.BorrowBookInfoVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface BookInfoMapper extends BaseMapper<BookInfo> {

    /**
     * 自定义分页查询 得到图书信息列表
     * @param page 分页对象
     * @param wrapper 查询条件 对象的名称需要以 info. 开头，例如 wrapper.eq("info.id",2222)
     * @return 返回自定义分页对象
     */
    IPage<BookInfoVo> getPageBookInfo(Page<BookInfoVo> page,@Param(Constants.WRAPPER) QueryWrapper<BookInfoVo> wrapper);

    /**
     * 根据图书id获取图书信息
     * @param bookId 图书id
     * @return 图书信息
     */
    BookInfoVo getBookInfoById(@Param("bookId") Long bookId);

}




