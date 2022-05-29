package com.xjh.library.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xjh.library.common.entity.UserMsg;
import com.xjh.library.common.service.UserMsgService;
import com.xjh.library.common.mapper.UserMsgMapper;
import org.springframework.stereotype.Service;

@Service
public class UserMsgServiceImpl extends ServiceImpl<UserMsgMapper, UserMsg>
    implements UserMsgService{

}




