package com.xjh.library.common.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xjh.library.common.entity.BookReserve;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xjh.library.module.admin.entity.notify.ReserveNotifyInfo;
import com.xjh.library.module.user.entity.BookInfoVo;
import com.xjh.library.module.user.entity.ReserveBookInfoVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BookReserveMapper extends BaseMapper<BookReserve> {

    /**
     * 分页查询用户的预约信息
     * @param page 分页对象
     * @param wrapper 查询条件 对象的名称需要以 reserve. 开头，例如 wrapper.eq("reserve.id",2222)
     * @return 返回一页预约信息
     */
    IPage<ReserveBookInfoVo> getPageReserveBookInfo(Page<ReserveBookInfoVo> page,@Param(Constants.WRAPPER) QueryWrapper<ReserveBookInfoVo> wrapper);


    /**
     * 获取需要通知的预约信息列表，代表书籍已经有资源了
     * @return 待通知的预约信息列表
     */
    List<ReserveNotifyInfo> getReserveNotifyInfo();
}




