package com.xjh.library.common.service;

import com.xjh.library.common.entity.BookStock;
import com.baomidou.mybatisplus.extension.service.IService;

public interface BookStockService extends IService<BookStock> {
    /**
     * 根据图书的获取库存信息，只查询图书id和图书库存两列
     * @param bookId 图书id
     * @return 如果不存在则会返回null，存在返回图书id和图书库存
     */
    BookStock getBookStockById(Long bookId);
}
