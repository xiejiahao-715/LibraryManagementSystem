package com.xjh.library.module.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xjh.library.module.admin.entity.BorrowerInfoVo;

// 借阅者管理
public interface BorrowerManageService {

    /**
     * 获取借阅者的分页信息 只要普通借阅者没有被删除，这可以查询出来，不看用户状态
     * @param current 当前页
     * @param limit 没页的数量
     * @return 返回分页对象
     */
    IPage<BorrowerInfoVo> getPageBorrowerInfo(Long current,Long limit);

    /**
     * 修改借阅者的最大借阅图书的数量
     * @param borrowerId 借阅者的id
     * @param newValue 新的最大借阅数量的值
     */
    void updateBorrowMaxNum(Long borrowerId,Integer newValue);

}
