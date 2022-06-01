package com.xjh.library.module.superadmin.mapper;

import com.xjh.library.module.superadmin.entity.statistics.BookNumOfType;
import com.xjh.library.module.superadmin.entity.statistics.DailyBookBorrowNum;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface StatisticsMapper {

    /**
     * 统计图书的日借阅数量（如果数据库对应该天的数据不存在，将会自动补0）
     * 举一个例子，当 date = '2020-05-01'，offsetDay = 31，
     * 代表统计的是 2020-05-01 00:00:00至 2020-06-01 00:00:00时间段的时间，注意不包括 2020-06-01这一天，总共31条数据
     * 当offsetDay = 0的时候，代表不偏移，则查询出来的是空列表
     *
     * @param date 代表统计时的某一个基准时间
     * @param offsetDay 相对于基准时间便宜的天数，支持正负数，正数代表相对于基准时间向后统计，负数代表相对于基准时间向前统计
     * @return 返回图书的日借阅数量列表，按查询出来的时间升序
     */
    List<DailyBookBorrowNum> getDailyBookBorrowNum(
            @Param("date") LocalDate date,
            @Param("offsetDay") Integer offsetDay);

    /**
     * 获取每一种图书分类对应的图书数量（此数量不是指的图书库存，而是值记载的图书的数量）
     * @return 返回的数据存储在数组中
     */
    List<BookNumOfType> getBookNumOfType();
}
