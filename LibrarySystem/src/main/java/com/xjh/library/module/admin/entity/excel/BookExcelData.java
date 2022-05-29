package com.xjh.library.module.admin.entity.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.converters.ReadConverterContext;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.xjh.library.common.exception.MyException;
import lombok.Data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

// 批量插入图书的Excel模板类
@Data
public class BookExcelData {

    @ExcelProperty("ISBN")
    private String isbn;

    @ExcelProperty("标题")
    private String name;

    @ExcelProperty("作者")
    private String author;

    @ExcelProperty("出版社")
    private String press;

    @ExcelProperty("图书价格")
    private String price;

    @ExcelProperty(value = "出版时间",converter = CustomStringLocalDateConverter.class)
    private LocalDate publishTime;

    @ExcelProperty("图书简介")
    private String description;

    @ExcelProperty("图片链接")
    private String coverImg;

    @ExcelProperty("数量")
    private Integer num;


    // 自定义 字符串转日期
    public static class CustomStringLocalDateConverter implements Converter<LocalDate> {

        private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-M-d");

        @Override
        public Class<?> supportJavaTypeKey() {
            return LocalDate.class;
        }

        @Override
        public CellDataTypeEnum supportExcelTypeKey() {
            return CellDataTypeEnum.STRING;
        }

        @Override
        public LocalDate convertToJavaData(ReadConverterContext<?> context) throws Exception {
            // excel文件中的日期字符串
            String time = context.getReadCellData().getStringValue().trim();
            // 标准的日期字符串
            String standardTime = null;
            if(Pattern.matches("^[1-9][0-9]{3}-[1]?[0-9]$",time)){
                // 如果日期格式形如 2019-2 的格式，后面自动改为2019-2-1
                standardTime = time + "-1";
            }else if(Pattern.matches("^[1-9][0-9]{3}-[1]?[0-9]-[1-3]?[0-9]$",time)){
                standardTime = time;
            }else if(Pattern.matches("^[1-9][0-9]{3}$",time)){
                // 如果日期格式形如 2019 的格式，后面自动改为2019-1-1
                standardTime = time + "-1-1";
            }else{
                throw new MyException("excel文件中日期格式不正确");
            }
            return LocalDate.parse(standardTime,dateTimeFormatter);
        }
    }
}
