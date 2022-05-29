package com.xjh.library.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xjh.library.common.entity.BookType;
import com.xjh.library.common.service.BookTypeService;
import com.xjh.library.common.mapper.BookTypeMapper;
import org.springframework.stereotype.Service;

@Service
public class BookTypeServiceImpl extends ServiceImpl<BookTypeMapper, BookType>
    implements BookTypeService{
    @Override
    public boolean existType(Long typeId) {
        LambdaQueryWrapper<BookType> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BookType::getId,typeId);
        return this.count(wrapper) == 1;
    }
}




