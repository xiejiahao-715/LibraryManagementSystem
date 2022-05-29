package com.xjh.library.common.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xjh.library.common.entity.BookBorrow;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xjh.library.module.admin.entity.BorrowBookDetailVo;
import com.xjh.library.module.admin.entity.notify.BorrowTimeoutNotifyInfo;
import com.xjh.library.module.user.entity.BorrowBookInfoVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BookBorrowMapper extends BaseMapper<BookBorrow> {
    /**
     * 借阅者 自定义分页查询 得到图书借阅信息列表
     * @param wrapper 查询条件 对象的名称需要以 borrow. 开头，例如 wrapper.eq("borrow.id",2222)
     * @return 返回自定义分页对象
     */
    IPage<BorrowBookInfoVo> getPageBorrowBookInfo(Page<BorrowBookInfoVo> page, @Param(Constants.WRAPPER) QueryWrapper<BorrowBookInfoVo> wrapper);

    /**
     * 获取借阅订单中超时未还的订单信息
     * @return 返回需要被通知的借阅订单列表信息
     */
    List<BorrowTimeoutNotifyInfo> getBorrowTimeoutNotifyInfo();

    /**
     * 管理 自定义分页查询 得到图书借阅的详细信息
     * @param wrapper 查询条件 对象的名称需要以 borrow. 开头，例如 wrapper.eq("borrow.id",2222)
     * @return 返回自定义分页对象
     */
    IPage<BorrowBookDetailVo> getPageBorrowBookDetail(Page<BorrowBookDetailVo> page,@Param(Constants.WRAPPER) QueryWrapper<BorrowBookDetailVo> wrapper);
}




