package com.xjh.library.module.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xjh.library.common.constants.BookBorrowStatus;
import com.xjh.library.common.constants.BookReserveStatus;
import com.xjh.library.common.entity.BookBorrow;
import com.xjh.library.common.entity.BookInfo;
import com.xjh.library.common.entity.BookReserve;
import com.xjh.library.common.entity.UserMsg;
import com.xjh.library.common.exception.MyException;
import com.xjh.library.common.mapper.BookBorrowMapper;
import com.xjh.library.common.mapper.BookReserveMapper;
import com.xjh.library.common.service.BookBorrowService;
import com.xjh.library.common.service.BookInfoService;
import com.xjh.library.common.service.BookReserveService;
import com.xjh.library.common.service.UserMsgService;
import com.xjh.library.module.admin.entity.notify.BorrowTimeoutNotifyInfo;
import com.xjh.library.module.admin.entity.notify.ReserveNotifyInfo;
import com.xjh.library.module.admin.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private BookBorrowService bookBorrowService;

    @Autowired
    private BookBorrowMapper bookBorrowMapper;

    @Autowired
    private UserMsgService userMsgService;

    @Autowired
    private BookReserveMapper bookReserveMapper;

    @Autowired
    private BookReserveService bookReserveService;

    @Autowired
    private BookInfoService bookInfoService;

    @Override
    @Transactional
    public void borrowTimeOutNotify() {

        // 找出所有再借阅中或者续借中的订单，并且还书时间小于当前时间
        List<BorrowTimeoutNotifyInfo> notifyInfos = bookBorrowMapper.getBorrowTimeoutNotifyInfo();
        if(notifyInfos.size() > 0){
            String msgTemplate = "您于%s借阅的图书：%s（ISBN：%s）超时未还,请及时还书";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            // TODO 这里可以使用批量更新的方法，赶时间就没用了
            for(BorrowTimeoutNotifyInfo info : notifyInfos){
                String msg = String.format(msgTemplate,
                        formatter.format(info.getBorrowBookDate()),
                        info.getBookName(),
                        info.getIsbn());

                UserMsg userMsg = new UserMsg();
                userMsg.setUserId(info.getUserId());
                userMsg.setMessage(msg);
                userMsg.setSendDate(LocalDateTime.now());

                // 添加用户消息
                if(userMsgService.save(userMsg)){
                    // 修改图书借阅信息的状态为超时未还
                    BookBorrow timeoutBorrow = new BookBorrow();
                    timeoutBorrow.setId(info.getBorrowId());
                    timeoutBorrow.setStatus(BookBorrowStatus.TIMEOUT.getStatusCode());
                    if(bookBorrowService.updateById(timeoutBorrow)){
                        break;
                    }
                }
                throw new MyException("书籍到期通知导入失败");
            }
        }
    }

    @Override
    @Transactional
    public void reserveNotify(){
        // 找出所有未通知的预约订单同时对应的书籍存在库存
        List<ReserveNotifyInfo> reserveNotifyInfoList = bookReserveMapper.getReserveNotifyInfo();

        // 预约订单中有图书存在库存则通知用户
        if(reserveNotifyInfoList.size() > 0){
            String msgTemplate = "您于%s预定的图书：%s（ISBN：%s）已经存在库存，可以去借阅了";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            // TODO 这里可以使用批量更新的方法，赶时间就没用了
            for(ReserveNotifyInfo reserveNotifyInfo : reserveNotifyInfoList){
                String msg = String.format(msgTemplate,
                        formatter.format(reserveNotifyInfo.getReserveTime()),
                        reserveNotifyInfo.getBookName(),
                        reserveNotifyInfo.getIsbn());
                UserMsg userMsg = new UserMsg();
                userMsg.setUserId(reserveNotifyInfo.getUserId());
                userMsg.setMessage(msg);
                userMsg.setSendDate(LocalDateTime.now());
                if(userMsgService.save(userMsg)){
                    // 修改图书预约信息
                    BookReserve bookReserve = new BookReserve();
                    bookReserve.setId(reserveNotifyInfo.getReserveId());
                    bookReserve.setNotifyTime(LocalDateTime.now());
                    bookReserve.setStatus(BookReserveStatus.NOTIFIED.getStatusCode());
                    if(bookReserveService.updateById(bookReserve)){
                        break;
                    }
                }
                throw new MyException("预约通知导入失败");
            }
        }
    }

    @Override
    @Transactional
    public void recallBookReturnNotify(Long borrowId) {
        // 首先判断借阅订单的id是否存在
        LambdaQueryWrapper<BookBorrow> bookBorrowWrapper = new LambdaQueryWrapper<>();
        bookBorrowWrapper.eq(BookBorrow::getId,borrowId);
        BookBorrow bookBorrow = bookBorrowService.getOne(bookBorrowWrapper);
        if(bookBorrow == null){
            throw new MyException("借阅订单id不存在");
        }
        // 查看图书订单是否完成，订单已完成则不需要通知
        if(BookBorrowStatus.FINISH.getStatusCode().equals(bookBorrow.getStatus())){
            throw new MyException("借阅的书籍已归还");
        }

        // 查看对应的图书信息
        BookInfo bookInfo = bookInfoService.getById(bookBorrow.getBookId());
        if(bookInfo == null){
            throw new MyException("催还图书失败");
        }
        String msgTemplate = "管理员提示您请尽快归还于%s借阅的图书%s（ISBN：%s）,截止归还时间为：%s";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String msg = String.format(msgTemplate,
                formatter.format(bookBorrow.getBorrowBookDate()),
                bookInfo.getName(),
                bookInfo.getIsbn(),
                formatter.format(bookBorrow.getReturnBookDate()));
        UserMsg userMsg = new UserMsg();
        userMsg.setUserId(bookBorrow.getUserId());
        userMsg.setSendDate(LocalDateTime.now());
        userMsg.setMessage(msg);

        // 存储发送的信息
        if(!userMsgService.save(userMsg)){
            throw new MyException("催还图书失败");
        }
    }
}