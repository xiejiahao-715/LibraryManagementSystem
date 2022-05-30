package com.xjh.library.module.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xjh.library.common.constants.BookBorrowStatus;
import com.xjh.library.common.constants.BookReserveStatus;
import com.xjh.library.common.entity.*;
import com.xjh.library.common.exception.MyException;
import com.xjh.library.common.mapper.BookBorrowMapper;
import com.xjh.library.common.mapper.BookInfoMapper;
import com.xjh.library.common.mapper.BookReserveMapper;
import com.xjh.library.common.service.*;
import com.xjh.library.module.security.UserDetailContextHolder;
import com.xjh.library.module.user.entity.*;
import com.xjh.library.module.user.service.BookService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookReserveMapper bookReserveMapper;

    @Autowired
    private BookReserveService bookReserveService;

    @Autowired
    private UserResourceService userResourceService;

    @Autowired
    private BookStockService bookStockService;

    @Autowired
    private BookInfoMapper bookInfoMapper;

    @Autowired
    private BookTypeService bookTypeService;

    @Autowired
    private BookBorrowService bookBorrowService;

    @Autowired
    private BookBorrowMapper bookBorrowMapper;

    @Override
    @Transactional(readOnly = true)
    public IPage<BookInfoVo> getPageBookInfo(Long current, Long limit, QueryBookFormVo queryBookFormVo) {
        QueryWrapper<BookInfoVo> wrapper = new QueryWrapper<>();

        if(queryBookFormVo.getTypeId() != null){
            wrapper.eq("info.type_id",queryBookFormVo.getTypeId());
        }
        if(queryBookFormVo.getAuthor() != null){
            wrapper.like("info.author",queryBookFormVo.getAuthor());
        }
        if(queryBookFormVo.getIsbn() != null){
            wrapper.eq("info.ISBN",queryBookFormVo.getIsbn());
        }
        if (queryBookFormVo.getName() != null) {
            wrapper.like("info.name", queryBookFormVo.getName());
        }
        // 只选择逻辑存在的
        wrapper.eq("info.deleted",0);
        // 排序条件
        if(queryBookFormVo.getSort() != null){
            if(queryBookFormVo.getSort() == 1){
                // 按照 发布时间倒序
                wrapper.orderByDesc("info.publish_time");
            }else if(queryBookFormVo.getSort() == 2){
                // 按照更新日期排序
                wrapper.orderByDesc("info.gmt_update");
            }
        }
        return bookInfoMapper.getPageBookInfo(new Page<>(current,limit),wrapper);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookType> getBookTypeList() {
        return bookTypeService.list();
    }

    @Override
    @Transactional(readOnly = true)
    public BookInfoVo getBookInfoById(Long bookId) {
        BookInfoVo bookInfoVo = bookInfoMapper.getBookInfoById(bookId);
        if(bookInfoVo == null){
            throw new MyException("图书不存在");
        }
        return bookInfoVo;
    }

    @Override
    @Transactional
    public void borrowBook(BorrowBookFormVo borrowBookFormVo) {
        // 首先判断图书是否存在,以及库存是否还有
        Long bookId = borrowBookFormVo.getBookId();
        BookStock bookStock = bookStockService.getBookStockById(bookId);
        if(bookStock == null){
            throw new MyException("图书不存在");
        }
        if(bookStock.getNum() <= 0){
            throw new MyException("图书已无库存");
        }
        // 图书存在且有库存则修改库存表
        bookStock.setNum(bookStock.getNum() - 1);
        if(!bookStockService.updateById(bookStock)){
            throw new MyException("修改图书库存失败");
        }

        // 获取用户id
        Long uid = UserDetailContextHolder.getUserId();

        // 判断用户是否已经借阅了同一本书
        LambdaQueryWrapper<BookBorrow> borrowWrapper = new LambdaQueryWrapper<>();
        borrowWrapper.eq(BookBorrow::getUserId,uid);
        borrowWrapper.eq(BookBorrow::getBookId,bookId);
        borrowWrapper.ne(BookBorrow::getStatus,BookBorrowStatus.FINISH.getStatusCode());
        if(bookBorrowService.count(borrowWrapper) != 0){
            throw new MyException("不可借阅同一本书");
        }

        // 判断用户是否还可以借书
        UserResource userResource = userResourceService.getUserResourceById(uid);
        if(userResource.getBorrowedNum() >= userResource.getBorrowMaxNum()){
            throw new MyException("已达到最大借阅数量，不可再借阅");
        }
        // 用户借阅数量增加1
        userResource.setBorrowedNum(userResource.getBorrowedNum() + 1);
        // 修改到数据库
        if(!userResourceService.updateById(userResource)){
            throw new MyException("修改用户资源失败");
        }

        // 记录借阅的记录到借阅表中
        BookBorrow bookBorrow = new BookBorrow();
        bookBorrow.setBookId(bookId);
        // 获取用户id
        bookBorrow.setUserId(uid);
        // 获取借阅时长
        Integer borrowTime = borrowBookFormVo.getBorrowTime();
        if(borrowTime == null){ // 默认借阅30天（最长借阅时间）
            borrowTime = 30;
        }
        if(borrowTime > 30){
            throw new MyException("借阅时间不能长于30天");
        }
        bookBorrow.setBorrowTime(borrowTime);
        // 生成借阅的起始时间
        LocalDateTime borrowBookDate = LocalDateTime.now();
        // 生成还书的时间
        LocalDateTime returnBookDate = borrowBookDate.plusDays(borrowTime);
        bookBorrow.setBorrowBookDate(borrowBookDate);
        bookBorrow.setReturnBookDate(returnBookDate);
        // 设置订单的状态
        bookBorrow.setStatus(BookBorrowStatus.BORROWING.getStatusCode());
        // 更新到数据库中
        if(!bookBorrowService.save(bookBorrow)){
            throw new MyException("系统出错，借阅图书失败");
        }
    }

    @Override
    @Transactional
    public void reserveBook(Long bookId) {
        Long uid = UserDetailContextHolder.getUserId();

        // 判断用户是否已经借阅了同一本书还没有归还
        LambdaQueryWrapper<BookBorrow> borrowWrapper = new LambdaQueryWrapper<>();
        borrowWrapper.eq(BookBorrow::getBookId,bookId);
        borrowWrapper.eq(BookBorrow::getUserId,uid);
        borrowWrapper.ne(BookBorrow::getStatus,BookBorrowStatus.FINISH.getStatusCode());
        if(bookBorrowService.count(borrowWrapper) != 0){
            throw new MyException("已经借阅了该图书无法预约");
        }

        // 判断图书是否已经没有了库存
        BookStock bookStock = bookStockService.getBookStockById(bookId);
        if(bookStock == null){
            throw new MyException("图书不存在");
        }
        if(bookStock.getNum() > 0){
            throw new MyException("图书还有库存，请去借阅");
        }


        // 判断用户是否已经预约了同一本书
        LambdaQueryWrapper<BookReserve> reserveWrapper = new LambdaQueryWrapper<>();
        reserveWrapper.eq(BookReserve::getUserId,uid);
        reserveWrapper.eq(BookReserve::getBookId,bookId);
        reserveWrapper.eq(BookReserve::getStatus,BookReserveStatus.RESERVING.getStatusCode());
        if(bookReserveService.count(reserveWrapper) != 0){
            throw new MyException("不可同时预约同一本书");
        }

        // 添加预约信息到表中
        BookReserve bookReserve = new BookReserve();
        bookReserve.setUserId(uid);
        bookReserve.setBookId(bookId);
        bookReserve.setStatus(BookReserveStatus.RESERVING.getStatusCode());
        bookReserve.setReserveTime(LocalDateTime.now());
        if(!bookReserveService.save(bookReserve)){
            throw new MyException("系统出错，预定图书失败");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public IPage<BorrowBookInfoVo> getPageBorrowBookInfo(Long current, Long limit) {
        Long uid = UserDetailContextHolder.getUserId();

        QueryWrapper<BorrowBookInfoVo> wrapper = new QueryWrapper<>();
        wrapper.eq("borrow.user_id",uid);
        // 按照借书的日期倒序
        wrapper.orderByDesc("borrow.borrow_book_date");

        IPage<BorrowBookInfoVo> page = bookBorrowMapper.getPageBorrowBookInfo(new Page<>(current,limit),wrapper);
        // 为借阅信息增加描述
        page.getRecords().forEach(borrowBookInfoVo -> {
            for(BookBorrowStatus status : BookBorrowStatus.values()){
                if(status.getStatusCode().equals(borrowBookInfoVo.getStatus())){
                    borrowBookInfoVo.setDescription(status.getMessage());
                    break;
                }
            }
        });
        return page;
    }

    @Override
    @Transactional(readOnly = true)
    public IPage<ReserveBookInfoVo> getPageReserveBookInfo(Long current, Long limit) {
        Long uid = UserDetailContextHolder.getUserId();

        QueryWrapper<ReserveBookInfoVo> wrapper = new QueryWrapper<>();
        wrapper.eq("reserve.user_id",uid);
        // 按照预约时间倒序
        wrapper.orderByDesc("reserve.reserve_time");

        IPage<ReserveBookInfoVo> page = bookReserveMapper.getPageReserveBookInfo(new Page<>(current,limit),wrapper);

        page.getRecords().forEach(reserveBookInfoVo -> {
            for(BookReserveStatus status : BookReserveStatus.values()){
                if(status.getStatusCode().equals(reserveBookInfoVo.getStatus())){
                    reserveBookInfoVo.setDescription(status.getMessage());
                    break;
                }
            }
        });
        return page;
    }

    @Override
    @Transactional
    public void returnBook(Long borrowId) {
        Long uid = UserDetailContextHolder.getUserId();
        // 首先获取图书的订单信息
        LambdaQueryWrapper<BookBorrow> borrowWrapper = new LambdaQueryWrapper<>();
        borrowWrapper.ne(BookBorrow::getStatus,BookBorrowStatus.FINISH.getStatusCode());
        borrowWrapper.eq(BookBorrow::getId,borrowId);
        borrowWrapper.eq(BookBorrow::getUserId,uid);
        BookBorrow bookBorrow = bookBorrowService.getOne(borrowWrapper);
        if(bookBorrow == null){
            throw new MyException("借阅记录不存在");
        }

        // 获取图书的库存信息
        BookStock bookStock = bookStockService.getBookStockById(bookBorrow.getBookId());
        // 使库存 +1
        bookStock.setNum(bookStock.getNum() + 1);
        if(!bookStockService.updateById(bookStock)){
            throw new MyException("系统异常，还书失败(修改图书库存失败)");
        }

        // 修改用户的图书资源
        UserResource userResource = userResourceService.getUserResourceById(uid);
        // 将用户已借阅图书的数量 -1
        if(userResource.getBorrowedNum() > 0){
            userResource.setBorrowedNum(userResource.getBorrowedNum()-1);
            if(!userResourceService.updateById(userResource)){
                throw new MyException("系统异常，还书失败(修改用户资源失败)");
            }
        }else{
            throw new MyException("用户资源数异常");
        }

        // 完成订单
        BookBorrow finishBookBorrow = new BookBorrow();
        finishBookBorrow.setId(borrowId);
        finishBookBorrow.setEndDate(LocalDateTime.now());
        finishBookBorrow.setStatus(BookBorrowStatus.FINISH.getStatusCode());

        if(!bookBorrowService.updateById(finishBookBorrow)){
            throw new MyException("系统异常，还书失败(修改订单信息失败)");
        }
    }

    @Override
    @Transactional
    public void renewBook(RenewBookFormVo renewBookFormVo) {
        Long uid = UserDetailContextHolder.getUserId();
        Long borrowId = renewBookFormVo.getBorrowId();
        // 首先获取图书的订单信息  只获取在借阅中的图书
        LambdaQueryWrapper<BookBorrow> borrowWrapper = new LambdaQueryWrapper<>();
        borrowWrapper.eq(BookBorrow::getStatus,BookBorrowStatus.BORROWING.getStatusCode());
        borrowWrapper.eq(BookBorrow::getId,borrowId);
        borrowWrapper.eq(BookBorrow::getUserId,uid);
        BookBorrow bookBorrow = bookBorrowService.getOne(borrowWrapper);
        if(bookBorrow == null){
            throw new MyException("该借阅订单不存在或无法续借(只能续借1次)");
        }

        // 续借的对象
        BookBorrow renewBook = new BookBorrow();
        // 增加借阅的时间
        int borrowTime = bookBorrow.getBorrowTime() + renewBookFormVo.getRenewTime();
        // 修改还书的时间
        LocalDateTime returnBookDate = bookBorrow.getBorrowBookDate().plusDays(borrowTime);
        renewBook.setId(renewBookFormVo.getBorrowId());
        renewBook.setReturnBookDate(returnBookDate);
        renewBook.setBorrowTime(borrowTime);
        renewBook.setStatus(BookBorrowStatus.RE_BORROW.getStatusCode());
        if(!bookBorrowService.updateById(renewBook)){
            throw new MyException("借阅图书失败");
        }
    }
}
