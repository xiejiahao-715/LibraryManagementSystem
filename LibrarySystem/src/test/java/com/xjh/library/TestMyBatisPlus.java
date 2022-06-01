package com.xjh.library;

import com.xjh.library.module.admin.task.NotifyTask;
import com.xjh.library.module.superadmin.entity.statistics.DailyBookBorrowNum;
import com.xjh.library.module.superadmin.mapper.StatisticsMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = LibraryApplication.class)
public class TestMyBatisPlus {

    // 排除定时任务这个bean
    @MockBean
    private NotifyTask notifyTask;

    @Autowired
    private StatisticsMapper statisticsMapper;

    // 测试超级管理员的数据统计接口层
    @Test
    public void testStatisticsMapper(){
        LocalDate date = LocalDate.of(2022,5,1);
        List<DailyBookBorrowNum> list = statisticsMapper.getDailyBookBorrowNum(date,-31);
        list.forEach(System.out::println);
    }

}
