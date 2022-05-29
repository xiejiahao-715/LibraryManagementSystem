package com.xjh.library.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xjh.library.common.entity.BookOutStock;
import com.xjh.library.common.service.BookOutStockService;
import com.xjh.library.common.mapper.BookOutStockMapper;
import org.springframework.stereotype.Service;

@Service
public class BookOutStockServiceImpl extends ServiceImpl<BookOutStockMapper, BookOutStock>
    implements BookOutStockService{

}




