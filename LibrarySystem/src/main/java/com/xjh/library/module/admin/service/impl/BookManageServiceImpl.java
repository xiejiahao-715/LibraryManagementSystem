package com.xjh.library.module.admin.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xjh.library.common.constants.BookBorrowStatus;
import com.xjh.library.common.constants.BookReserveStatus;
import com.xjh.library.common.entity.BookInfo;
import com.xjh.library.common.entity.BookOutStock;
import com.xjh.library.common.entity.BookStock;
import com.xjh.library.common.exception.MyException;
import com.xjh.library.common.mapper.BookBorrowMapper;
import com.xjh.library.common.mapper.BookOutStockMapper;
import com.xjh.library.common.mapper.BookReserveMapper;
import com.xjh.library.common.service.BookInfoService;
import com.xjh.library.common.service.BookOutStockService;
import com.xjh.library.common.service.BookStockService;
import com.xjh.library.common.service.BookTypeService;
import com.xjh.library.common.utils.FileUtil;
import com.xjh.library.module.admin.entity.BookOutStockInfoVo;
import com.xjh.library.module.admin.entity.BorrowBookDetailVo;
import com.xjh.library.module.admin.entity.EditBookFormVo;
import com.xjh.library.module.admin.entity.ReserveBookDetailVo;
import com.xjh.library.module.admin.entity.excel.BookExcelData;
import com.xjh.library.module.admin.listener.BookExcelDataListener;
import com.xjh.library.module.admin.service.BookManageService;
import com.xjh.library.module.security.UserDetailContextHolder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class BookManageServiceImpl implements BookManageService {

    private final static Lock lock = new ReentrantLock();

    // 操作图书分类
    @Autowired
    private BookTypeService bookTypeService;

    // 操作图书的基本信息
    @Autowired
    private BookInfoService bookInfoService;

    // 操作图书的库存
    @Autowired
    private BookStockService bookStockService;

    @Autowired
    private BookOutStockService bookOutStockService;

    @Autowired
    private BookOutStockMapper bookOutStockMapper;

    @Autowired
    private BookBorrowMapper bookBorrowMapper;

    @Autowired
    private BookReserveMapper bookReserveMapper;


    @Override
    @Transactional
    public void batchAddBook(MultipartFile excelFile) {
        // 检查文件的后缀名
        String fileSuffix = FileUtil.getFileSuffix(excelFile).toLowerCase();
        if(!(fileSuffix.equals(".xls") || fileSuffix.equals(".xlsx"))){
            throw new MyException("只支持Excel格式的文件(.xls或.xlsx)");
        }
        // 上锁
        if(lock.tryLock()){
            try (InputStream inputStream = excelFile.getInputStream()){
                EasyExcel.read(
                        inputStream,
                        BookExcelData.class,
                        new BookExcelDataListener(bookTypeService,bookInfoService,bookStockService))
                        .doReadAll();
            }catch (Exception e){
                throw new MyException("Excel文件异常或不符合规范");
            } finally {
                lock.unlock();
            }
        }else {
            throw new MyException("当前系统正在导入图书中，请稍后再尝试");
        }
    }

    @Override
    @Transactional
    public void addBook(EditBookFormVo bookFormVo) {
        // 判断添加图书的isbn号是否重复
        LambdaQueryWrapper<BookInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BookInfo::getIsbn,bookFormVo.getIsbn());
        if(bookInfoService.count(wrapper)!=0){
            throw new MyException("ISBN号重复，添加失败");
        }
        // 判断图书的分类是否存在
        if(!bookTypeService.existType(bookFormVo.getTypeId())){
            throw new MyException("分类不存在");
        }

        // 添加图书
        BookInfo bookInfo = new BookInfo();
        // 保证id为null
        bookInfo.setId(null);
        BeanUtils.copyProperties(bookFormVo,bookInfo);
        if(bookInfoService.save(bookInfo)){
            // 添加库存
            BookStock bookStock = new BookStock();
            bookStock.setBookId(bookInfo.getId());
            bookStock.setNum(bookFormVo.getStockNum());
            if(bookStockService.save(bookStock)){
                return;
            }
        }
        throw new MyException("添加图书失败");
    }

    @Override
    @Transactional
    public void updateBook(EditBookFormVo bookFormVo) {
        // 判断图书id是否存在
        LambdaQueryWrapper<BookInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BookInfo::getId,bookFormVo.getId());
        if(bookInfoService.count(wrapper) != 1){
            throw new MyException("图书id不存在");
        }

        if(bookFormVo.getTypeId() != null){
            // 判断图书的分类是否存在
            if(!bookTypeService.existType(bookFormVo.getTypeId())){
                throw new MyException("分类不存在");
            }
        }
        // 更新图书基本信息
        BookInfo bookInfo = new BookInfo();
        BeanUtils.copyProperties(bookFormVo,bookInfo);
        if(!bookInfoService.updateById(bookInfo)){
            throw new MyException("更新图书信息失败");
        }
    }

    @Override
    @Transactional
    public void addBookStock(Long bookId, Integer addNum) {
        if (addNum < 1){
            throw new MyException("添加的库存数量无效");
        }
        // 判断图书id是否存在
        LambdaQueryWrapper<BookStock> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BookStock::getBookId,bookId);

        BookStock bookStock = bookStockService.getOne(wrapper);
        if(bookStock == null){
            throw new MyException("图书id不存在");
        }
        bookStock.setNum(bookStock.getNum() + addNum);
        if(!bookStockService.updateById(bookStock)){
            throw new MyException("添加库存失败");
        }
    }

    @Override
    @Transactional
    public void bookOutStock(Long bookId, Integer num) {
        if(!(num != null && num >= 1)){
            throw new MyException("出库数量无效");
        }
        // 判断图书id是否存在
        LambdaQueryWrapper<BookStock> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BookStock::getBookId,bookId);

        BookStock bookStock = bookStockService.getOne(wrapper);
        if(bookStock == null){
            throw new MyException("出库图书id不存在");
        }
        if(bookStock.getNum() < num){
            throw new MyException("出库数量超过图书库存");
        }
        bookStock.setNum(bookStock.getNum() - num);
        if(!bookStockService.updateById(bookStock)){
            throw new MyException("图书出库失败");
        }

        Long uid = UserDetailContextHolder.getUserId();

        // 记录出库记录
        BookOutStock bookOutStock = new BookOutStock();
        bookOutStock.setUserId(uid);
        bookOutStock.setBookId(bookId);
        bookOutStock.setNum(num);
        bookOutStock.setOutStockTime(LocalDateTime.now());

        if(!bookOutStockService.save(bookOutStock)){
            throw new MyException("图书出库失败");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public IPage<BookOutStockInfoVo> getPageBookOutStockInfo(Long current, Long limit) {
        QueryWrapper<BookOutStockInfoVo> wrapper = new QueryWrapper<>();
        // 选择逻辑未删除的数据
        wrapper.eq("out_stock.deleted",0);
        wrapper.orderByDesc("out_stock.out_stock_time");
        return bookOutStockMapper.getPageBookOutStockInfo(new Page<>(current,limit),wrapper);
    }

    @Override
    @Transactional(readOnly = true)
    public IPage<BorrowBookDetailVo> getPageBorrowBookDetail(long current, long limit) {
        QueryWrapper<BorrowBookDetailVo> wrapper = new QueryWrapper<>();
        // 选择逻辑未删除的借阅信息
        wrapper.eq("borrow.deleted",0);
        // 按照结束的日期倒序
        wrapper.orderByDesc("borrow.borrow_book_date");
        IPage<BorrowBookDetailVo> page = bookBorrowMapper.getPageBorrowBookDetail(new Page<>(current,limit),wrapper);
        // 添加状态描述信息
        page.getRecords().forEach(borrowBookDetailVo -> {
            for(BookBorrowStatus status : BookBorrowStatus.values()){
                if(status.getStatusCode().equals(borrowBookDetailVo.getStatus())){
                    borrowBookDetailVo.setDescription(status.getMessage());
                    break;
                }
            }
        });
        return page;
    }

    @Override
    @Transactional(readOnly = true)
    public IPage<ReserveBookDetailVo> getPageReserveBookDetail(long current, long limit) {
        QueryWrapper<ReserveBookDetailVo> wrapper = new QueryWrapper<>();
        // 按预约的时间倒序
        wrapper.orderByDesc("reserve.reserve_time");
        wrapper.eq("reserve.deleted",0);
        IPage<ReserveBookDetailVo> page = bookReserveMapper.getPageReserveBookDetail(new Page<>(current,limit),wrapper);
        // 添加状态描述信息
        page.getRecords().forEach(reserveBookDetailVo -> {
            for(BookReserveStatus status : BookReserveStatus.values()){
                if(status.getStatusCode().equals(reserveBookDetailVo.getStatus())){
                    reserveBookDetailVo.setDescription(status.getMessage());
                    break;
                }
            }
        });
        return page;
    }
}
