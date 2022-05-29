package com.xjh.library.module.security.filter;

import com.xjh.library.common.api.ResultCode;
import com.xjh.library.common.exception.MyException;
import com.xjh.library.module.security.UserDetailContextHolder;
import com.xjh.library.module.security.cache.UserDetailCacheService;
import com.xjh.library.module.security.config.SecurityFilterConfig;
import com.xjh.library.module.security.entity.UserDetail;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

// 初始化用户上下文信息的过滤器
public class InitUserDetailFilter implements Filter {

    @Autowired
    private UserDetailCacheService userDetailCacheService;

    // 默认排除的路径，不需要拦截
    private List<String> excludePathList;

    @Override
    public void init(FilterConfig filterConfig) {
        this.excludePathList = SecurityFilterConfig.getExcludePathList(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        // 首先强制转换为HttpServletRequest类型
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        // 判断该请求是否需要被拦截
        if(SecurityFilterConfig.isExcludePath(request,excludePathList)){
            // 该请求包含在不拦截列表中，放行
            chain.doFilter(servletRequest,servletResponse);
            return;
        }
        Long uid = UserDetailContextHolder.getUserId();
        // 从redis中获取用户信息
        UserDetail userDetail = userDetailCacheService.getUserDetail(uid);
        if(userDetail == null){
            // 这里如果报错可能是redis出问题了
            throw new MyException(ResultCode.USER_RE_LOGIN.getCode(),"系统出错,未找到授权信息,请尝试重新登录");
        }
        // 重新设置完整的上下文信息
        UserDetailContextHolder.setContext(userDetail);

        chain.doFilter(servletRequest,servletResponse);
    }
}
