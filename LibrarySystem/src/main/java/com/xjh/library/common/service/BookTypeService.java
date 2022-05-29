package com.xjh.library.common.service;

import com.xjh.library.common.entity.BookType;
import com.baomidou.mybatisplus.extension.service.IService;

public interface BookTypeService extends IService<BookType> {
    /**
     * 判断某一个typeId对象的图书类型是否存在
     * @param typeId 图书是否存在
     * @return 返回是否存在的结果
     */
    boolean existType(Long typeId);
}
