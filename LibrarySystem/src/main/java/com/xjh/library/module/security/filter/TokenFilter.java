package com.xjh.library.module.security.filter;

import com.xjh.library.common.api.ResultCode;
import com.xjh.library.common.exception.MyException;
import com.xjh.library.module.security.config.SecurityFilterConfig;
import com.xjh.library.module.security.entity.UserDetail;
import com.xjh.library.module.security.UserDetailContextHolder;
import com.xjh.library.module.security.utils.JwtTokenUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

// token的拦截器
public class TokenFilter implements Filter {

    // 默认排除的路径，不需要拦截
    private List<String> excludePathList;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.excludePathList = SecurityFilterConfig.getExcludePathList(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        // 强制类型转换
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        // 查看是否为排除的路径
        if(SecurityFilterConfig.isExcludePath(request,excludePathList)){
            chain.doFilter(servletRequest,servletResponse);
            return;
        }
        // 获取请求头中token
        String token = request.getHeader(JwtTokenUtil.TOKEN_HEADER_NAME);
        if(token == null){
            throw new MyException(ResultCode.USER_RE_LOGIN.getCode(), "请先登录");
        }
        Long uid = JwtTokenUtil.verifyToken(token);
        if(uid == null){
            throw new MyException(ResultCode.USER_RE_LOGIN.getCode(), "用户登录已过期");
        }
        try {
            UserDetail userDetail = new UserDetail();
            userDetail.setId(uid);
            // 设置临时的用户信息，只包含用户的id
            UserDetailContextHolder.setContext(userDetail);
            chain.doFilter(servletRequest,servletResponse);
        } finally {
            // 清空上下文，防止内存泄露
            UserDetailContextHolder.clearContext();
        }
    }
}
