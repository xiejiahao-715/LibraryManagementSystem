package com.xjh.library.module.admin.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xjh.library.common.constants.AccountStatus;
import com.xjh.library.common.entity.UserResource;
import com.xjh.library.common.exception.MyException;
import com.xjh.library.common.mapper.SysUserMapper;
import com.xjh.library.common.service.UserResourceService;
import com.xjh.library.module.admin.entity.BorrowerInfoVo;
import com.xjh.library.module.admin.service.BorrowerManageService;
import com.xjh.library.module.security.UserDetailContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BorrowerManageServiceImpl implements BorrowerManageService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private UserResourceService userResourceService;

    @Override
    @Transactional(readOnly = true)
    public IPage<BorrowerInfoVo> getPageBorrowerInfo(Long current, Long limit) {
        IPage<BorrowerInfoVo> page = sysUserMapper.getPageBorrowerInfo(new Page<>(current,limit));
        // 添加状态的描述信息
        page.getRecords().forEach(borrowerInfoVo -> {
            for(AccountStatus status : AccountStatus.values()){
                if(status.getStatusCode().equals(borrowerInfoVo.getStatus())){
                    borrowerInfoVo.setDescription(status.getMessage());
                }
            }
        });
        return page;
    }

    @Override
    @Transactional
    public void updateBorrowMaxNum(Long borrowerId, Integer newValue) {
        // 获取用户的资源信息
        UserResource userResource = userResourceService.getUserResourceById(borrowerId);
        if(userResource == null){
            throw new MyException("系统出错，无法找到用户资源");
        }
        // 如果设置的用户最大资源小于目前已经借阅的图书数量，则出错
        if(newValue < userResource.getBorrowedNum()){
            throw new MyException("最大借阅数必须大于或等于已借阅的图书数量");
        }

        // 不需要修改已借阅的数量
        userResource.setBorrowedNum(null);
        // 设置借阅的最大数量
        userResource.setBorrowMaxNum(newValue);
        if(!userResourceService.updateById(userResource)){
            throw new MyException("系统出错,修改最大借阅数量失败");
        }
    }
}
