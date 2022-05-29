package com.xjh.library.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xjh.library.common.entity.BookInfo;
import com.xjh.library.common.service.BookInfoService;
import com.xjh.library.common.mapper.BookInfoMapper;
import org.springframework.stereotype.Service;

@Service
public class BookInfoServiceImpl extends ServiceImpl<BookInfoMapper, BookInfo>
    implements BookInfoService{

}




