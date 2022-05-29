package com.xjh.library.module.admin.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xjh.library.common.entity.BookInfo;
import com.xjh.library.common.entity.BookStock;
import com.xjh.library.common.entity.BookType;
import com.xjh.library.common.exception.MyException;
import com.xjh.library.common.service.BookInfoService;
import com.xjh.library.common.service.BookStockService;
import com.xjh.library.common.service.BookTypeService;
import com.xjh.library.module.admin.entity.excel.BookExcelData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.util.Objects;

// 批量导入图书时Excel文件的监听器
@Slf4j
public class BookExcelDataListener implements ReadListener<BookExcelData> {

    // 操作图书分类
    private final BookTypeService bookTypeService;
    // 操作图书的基本信息
    private final BookInfoService bookInfoService;
    // 操作图书的库存
    private final BookStockService bookStockService;

    public BookExcelDataListener(
            BookTypeService bookTypeService,
            BookInfoService bookInfoService,
            BookStockService bookStockService){
        this.bookTypeService = bookTypeService;
        this.bookInfoService = bookInfoService;
        this.bookStockService = bookStockService;
    }

    // 当前分类的名称
    private String currentTypeName = null;

    // 当前分类的ID
    private Long currentTypeId = null;

    // 根据sheetName来设置当前监听器的对应的图书分类id和name
    private void setOrAddBookType(String sheetName){
        if(!Objects.equals(sheetName,currentTypeName)){
            // 当前分类的名称和excel的sheet表的名称不同，则更新
            // 查看读取的sheet表代表的分类是否存在，不存在则添加
            LambdaQueryWrapper<BookType> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(BookType::getName,sheetName);
            wrapper.select(BookType::getId,BookType::getName);
            BookType bookType = bookTypeService.getOne(wrapper);
            if(bookType == null){
                // 代表分类不存着则需要添加分类
                bookType = new BookType();
                bookType.setName(sheetName);
                if(bookTypeService.save(bookType)){
                    // 添加分类成功
                    this.currentTypeName = sheetName;
                    this.currentTypeId = bookType.getId();
                }else{
                    throw new MyException("批量导入数据过程中，填在分类失败异常终止");
                }
            }else{
                // 分类存在，则直接进行更新相关临时变量
                this.currentTypeName = sheetName;
                this.currentTypeId = bookType.getId();
            }
        }
    }

    // 每一行数据解析都会调用
    // TODO 这里使用的是for循环的插入方式，效率比较低，建议优化，例如batch批处理的方式，拼接sql等
    @Override
    public void invoke(BookExcelData bookExcelData, AnalysisContext analysisContext) {
        // 获取当前图书的类型,即sheet的名称
        String typeName =  analysisContext.readSheetHolder().getSheetName();
        setOrAddBookType(typeName);
        // 导入或修改对应的图书信息
        // 查看对应的ISBN号是否存在
        LambdaQueryWrapper<BookInfo> bookInfoWrapper = new LambdaQueryWrapper<>();
        bookInfoWrapper.eq(BookInfo::getIsbn,bookExcelData.getIsbn());
        BookInfo bookInfo = bookInfoService.getOne(bookInfoWrapper);
        if(bookInfo == null){ // 数据库中没有对应的数组
            bookInfo = new BookInfo();
            BeanUtils.copyProperties(bookExcelData,bookInfo);
            bookInfo.setTypeId(currentTypeId);
            bookInfoService.save(bookInfo);
        }else{ // 数据库存在对应的数据
            BeanUtils.copyProperties(bookExcelData,bookInfo);
            bookInfo.setTypeId(currentTypeId);
            bookInfoService.updateById(bookInfo);
        }
        // 导入或修改对应的库存信息
        // 获取图书的库存
        Long bookId = bookInfo.getId();
        BookStock bookStock = bookStockService.getById(bookId);
        if(bookStock == null){
            bookStock = new BookStock();
            bookStock.setBookId(bookId);
            bookStock.setNum(bookExcelData.getNum());
            bookStockService.save(bookStock);
        }else{
            bookStock.setNum(bookStock.getNum() + bookExcelData.getNum());
            bookStockService.updateById(bookStock);
        }
        log.info("导入一条数据成功："+bookExcelData);

    }

    // 在每一个sheet读完后就会调用
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
    }
}
