package com.xjh.library.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xjh.library.common.entity.BookBorrow;
import com.xjh.library.common.service.BookBorrowService;
import com.xjh.library.common.mapper.BookBorrowMapper;
import org.springframework.stereotype.Service;

@Service
public class BookBorrowServiceImpl extends ServiceImpl<BookBorrowMapper, BookBorrow>
    implements BookBorrowService{

}




