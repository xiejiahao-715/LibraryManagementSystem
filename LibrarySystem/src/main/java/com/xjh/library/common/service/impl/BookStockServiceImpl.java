package com.xjh.library.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xjh.library.common.entity.BookStock;
import com.xjh.library.common.service.BookStockService;
import com.xjh.library.common.mapper.BookStockMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookStockServiceImpl extends ServiceImpl<BookStockMapper, BookStock>
    implements BookStockService{

    @Override
    @Transactional(readOnly = true)
    public BookStock getBookStockById(Long bookId) {
        LambdaQueryWrapper<BookStock> stockWrapper = new LambdaQueryWrapper<>();
        stockWrapper.eq(BookStock::getBookId,bookId);
        // 只需要图书id和图书库存两列
        stockWrapper.select(BookStock::getBookId,BookStock::getNum);
        return this.getOne(stockWrapper);
    }
}




