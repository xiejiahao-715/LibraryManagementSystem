package com.xjh.library.module.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xjh.library.common.entity.BookType;
import com.xjh.library.module.user.entity.*;

import java.util.List;

// 图书借阅者(普通用户) 有关图书的一些服务
public interface BookService {

    /**
     * 分页查询图书信息
     * @param current 当前页号
     * @param limit 每一页的数量
     * @return 返回分页对象
     */
    IPage<BookInfoVo> getPageBookInfo(Long current, Long limit, QueryBookFormVo queryBookFormVo);

    /**
     * 获取所有图书分类的列表
     * @return 返回分类列表
     */
    List<BookType> getBookTypeList();

    /**
     * 根据图书id获取图书的信息
     * @param bookId 图书信息
     * @return 返回图书信息
     */
    BookInfoVo getBookInfoById(Long bookId);

    /**
     * 借阅图书
     * @param borrowBookFormVo 借阅图书的表单
     */
    void borrowBook(BorrowBookFormVo borrowBookFormVo);

    /**
     * 预约图书
     * @param bookId 图书id
     */
    void reserveBook(Long bookId);

    /**
     * 分页查询用户的借阅信息
     * @param current 当前页
     * @param limit 每页的数量
     * @return 返回一页借阅信息
     */
    IPage<BorrowBookInfoVo> getPageBorrowBookInfo(Long current, Long limit);

    /**
     * 分页查询用户的预约信息
     * @param current 当前页
     * @param limit 每页的数量
     *  @return 返回一页预约信息
     */
    IPage<ReserveBookInfoVo> getPageReserveBookInfo(Long current,Long limit);

    /**
     * 用户归还图书
     * @param borrowId 借阅图书的订单id
     */
    void returnBook(Long borrowId);

    /**
     * 续借图书
     * @param renewBookFormVo 续借的表单对象
     */
    void renewBook(RenewBookFormVo renewBookFormVo);

}
