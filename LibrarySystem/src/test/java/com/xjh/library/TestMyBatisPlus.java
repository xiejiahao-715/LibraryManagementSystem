package com.xjh.library;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xjh.library.common.mapper.BookInfoMapper;
import com.xjh.library.module.user.entity.BookInfoVo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LibraryApplication.class)
public class TestMyBatisPlus {

    @Autowired
    private BookInfoMapper bookInfoMapper;

    @Test
    public void testGetPageBookInfo(){
        QueryWrapper<BookInfoVo> wrapper = new QueryWrapper<>();
        wrapper.eq("info.id",1526755258158645250L);
        IPage<BookInfoVo> bookInfoPage = bookInfoMapper.getPageBookInfo(new Page<>(1,10),wrapper);
        System.out.println(bookInfoPage.getRecords());
    }

}
