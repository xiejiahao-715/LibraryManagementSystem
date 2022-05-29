package com.xjh.library.common.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

// MyBatisPlus的自动填充功能
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject,"gmtCreate", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject,"gmtUpdate", LocalDateTime.class, LocalDateTime.now());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject,"gmtUpdate", LocalDateTime.class, LocalDateTime.now());
    }
}