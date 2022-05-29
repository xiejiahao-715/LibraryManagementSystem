package com.xjh.library.module.admin.task;

import com.xjh.library.module.admin.config.ScheduleTaskPoolConfig;
import com.xjh.library.module.admin.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NotifyTask {

    @Autowired
    private MessageService messageService;

    @Async(ScheduleTaskPoolConfig.MY_TASK_POOL)
    @Scheduled(fixedRate = 60000,initialDelay = 30000)
    public void borrowTimeOutNotifyTask(){
        messageService.borrowTimeOutNotify();
        log.info("任务：扫描超时未还订单成功");
    }

    @Async(ScheduleTaskPoolConfig.MY_TASK_POOL)
    @Scheduled(fixedRate = 60000)
    public void reserveNotifyTask(){
        messageService.reserveNotify();
        log.info("任务：扫描预约订单成功");
    }

}
