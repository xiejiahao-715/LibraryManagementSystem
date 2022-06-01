package com.xjh.library.module.superadmin.service.impl;

import com.xjh.library.module.superadmin.entity.statistics.BookNumOfType;
import com.xjh.library.module.superadmin.entity.statistics.DailyBookBorrowNum;
import com.xjh.library.module.superadmin.mapper.StatisticsMapper;
import com.xjh.library.module.superadmin.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    private StatisticsMapper statisticsMapper;

    @Override
    @Transactional
    public List<DailyBookBorrowNum> getDailyBookBorrowNum(LocalDate date, Integer offsetDay) {
        return statisticsMapper.getDailyBookBorrowNum(date,offsetDay);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookNumOfType> getBookNumOfType() {
        return statisticsMapper.getBookNumOfType();
    }
}
