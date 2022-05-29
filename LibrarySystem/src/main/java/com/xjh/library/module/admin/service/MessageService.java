package com.xjh.library.module.admin.service;

// 消息服务：有关 书籍到期，预约通知，缺书通知的服务
public interface MessageService {

    /**
     * 书籍到期通知
     */
    void borrowTimeOutNotify();

    /**
     * 预约提醒，书籍存在了库存会提醒借阅者
     */
    void reserveNotify();


    /**
     * 管理员主动催还图书的接口
     * @param borrowId 借阅订单的唯一id
     */
    void recallBookReturnNotify(Long borrowId);
}
