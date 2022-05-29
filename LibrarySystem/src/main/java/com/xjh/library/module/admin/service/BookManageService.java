package com.xjh.library.module.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xjh.library.module.admin.entity.BookOutStockInfoVo;
import com.xjh.library.module.admin.entity.BorrowBookDetailVo;
import com.xjh.library.module.admin.entity.EditBookFormVo;
import org.springframework.web.multipart.MultipartFile;

// 图书管理员：管理图书信息的操作
public interface  BookManageService {

    /**
     * 通过Excel文件实现批量的图书入库操作
     * @param excelFile excel格式的文件
     */
    void batchAddBook(MultipartFile excelFile);

    /**
     * 添加一本图书
     * @param bookFormVo 图书的信息
     */
    void addBook(EditBookFormVo bookFormVo);

    /**
     * 修改图书基本信息
     * @param bookFormVo 图书信息表单
     */
    void updateBook(EditBookFormVo bookFormVo);

    /**
     * 添加图书库存
     * @param bookId 要添加库存的图书id
     * @param addNum 要添加库存的数量
     */
    void addBookStock(Long bookId,Integer addNum);

    /**
     * 图书出库
     * @param bookId 需要出库的图书id
     * @param num 需要出库的图书数量
     */
    void bookOutStock(Long bookId, Integer num);

    /**
     * 分页查询获取图书的出库记录
     * @param current 当前页
     * @param limit 每页数量
     * @return 返回分页信息
     */
    IPage<BookOutStockInfoVo> getPageBookOutStockInfo(Long current, Long limit);

    /**
     * 分页查询获取图书的详细借阅记录
     * @param current 当前页
     * @param limit 每页数量
     * @return 返回分页信息
     */
    IPage<BorrowBookDetailVo> getPageBorrowBookDetail(long current,long limit);
}
