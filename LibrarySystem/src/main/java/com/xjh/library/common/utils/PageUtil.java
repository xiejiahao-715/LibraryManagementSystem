package com.xjh.library.common.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 封装分页查询的结构
public class PageUtil {
    // 将mybatis-plus的自定义分页查询结构封装为Map集合
    public static <T> Map<String, Object> toMap(IPage<T> page){
        Map<String,Object> data = new HashMap<>();
        // 查询的结果的数量
        List<T> records = page.getRecords();
        // 当前的页号
        long current = page.getCurrent();
        // 总共的页数
        long pages = page.getPages();
        // 每页的数据数量
        long size = page.getSize();
        // 总共的结果数
        long total = page.getTotal();
        // 是否有下一页
        boolean hasMore = page.getCurrent() < pages;
        // 是否有上一页
        boolean hasPrevious = page.getCurrent() > 1L;
        data.put("records",records);
        data.put("total",total);
        data.put("hasMore",hasMore);
        data.put("hasPrevious",hasPrevious);
        data.put("current",current);
        data.put("size",size);
        data.put("pages",pages);
        return data;
    }
}
