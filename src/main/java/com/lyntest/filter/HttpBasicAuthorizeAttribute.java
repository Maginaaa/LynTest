/**
 * @author 简单随风
 * @date
 */

package com.lyntest.filter;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.lyntest.bean.User;
import com.lyntest.config.LoginConfig;
import com.lyntest.utils.JwtHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Configuration
@Order(value = 1)
@WebFilter(filterName = "colationFilter", urlPatterns = "/*")
public class HttpBasicAuthorizeAttribute implements Filter {

    private static ThreadLocal<User> userThreadLocal = new ThreadLocal();

    private static final Set<String> ALLOWED_PATHS = Collections.unmodifiableSet(
            new HashSet<>(Arrays.asList("/login", "/registered","/dist")));

    @Override
    public void destroy() {
        log.info("后台token过滤器,溜了溜了溜了溜了");
        // 可以日志管理添加
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String path = request.getRequestURI().substring(request.getContextPath().length()).replaceAll("[/]+$", "");
        log.info("当前请求url为: " + path);
        //  判断请求url是否为不需要经过过滤器的url，例如登录登出、数据平台api
        boolean boolAllowedPath = false;
        for (String allowedPath:ALLOWED_PATHS){
            if (path.startsWith(allowedPath)){
                boolAllowedPath = true;
                break;
            }else {
                boolAllowedPath = false;
            }
        }

        if (boolAllowedPath) {
            log.info("无须校验token");
            chain.doFilter(request, response);
        } else {
            log.info("过滤器检测token");
            // 校验
            String resultStatusCode = checkHTTPBasicAuthorize(request);
            if (resultStatusCode.equals(LoginConfig.HTTP_STATUS_401)) {
                toResponse((HttpServletResponse) res, 2, (HttpServletRequest) req);
                return;
            } else if (resultStatusCode.equals(LoginConfig.HTTP_STATUS_403)) {
                // 权限不够或超时
                toResponse((HttpServletResponse) res, 0, (HttpServletRequest) req);
                return;
            }
            log.info("后台token过滤器检测通过");
            chain.doFilter(request, response);
        }
    }

    /**
     * 响应
     *
     * @param response
     * @param i
     *            类型
     * @throws IOException
     */
    private void toResponse(HttpServletResponse response, int i, HttpServletRequest request) throws IOException {
        HttpServletResponse httpResponse = response;
        httpResponse.setCharacterEncoding("UTF-8");
        httpResponse.setContentType("application/json; charset=utf-8");
        httpResponse.setHeader("Access-Control-Allow-Origin", "*");
        httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
        httpResponse.setHeader("Access-Control-Allow-Methods", "POST,GET,OPTIONS,DELETE,PATCH,PUT");
        httpResponse.setHeader("Access-Control-Max-Age", "3600");
        httpResponse.setHeader("Access-Control-Allow-Headers",
                "Origin,X-Requested-With,x-requested-with,X-Custom-Header," + "Content-Type,Accept,Authorization");
        String method = request.getMethod();
        if ("OPTIONS".equalsIgnoreCase(method)) {
            log.info("OPTIONS请求");
            httpResponse.setStatus(HttpServletResponse.SC_ACCEPTED);
        }

        ObjectMapper mapper = new ObjectMapper();
        PrintWriter writer = httpResponse.getWriter();

        if (i == 2) {
            //Token 过期
            writer.write(mapper.writeValueAsString("Token过期"));
        }else if (i == 0) {
            // 无效 Token或异常
            writer.write(mapper.writeValueAsString("Token无效或异常"));
        }
        writer.close();

    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
        log.info("后台token过滤器启动");
    }

    /**
     * 检测请求同token信息
     *
     * @param request
     * @return
     */
    private String checkHTTPBasicAuthorize(ServletRequest request) {
        try {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            String auth = httpRequest.getHeader(LoginConfig.TOKEN_HEADER);

            // 如果请求头中没有Authorization信息则直接打回
            if (auth == null || "".equals(auth) || !auth.startsWith(LoginConfig.TOKEN_PREFIX)) {
                // 返回拦截信息
                return LoginConfig.HTTP_STATUS_401;
            }
            auth = auth.replace(LoginConfig.TOKEN_PREFIX, "");
            // 验证token是否过期,并同时可以检查token是否被篡改
            if (JwtHelper.isExpiration(auth)) {
                return LoginConfig.HTTP_STATUS_403;
            }

            // 获取加密后用户信息保存到当前请求线程中
            User u = JwtHelper.getUsername(auth);
            userThreadLocal.set(u);

            return LoginConfig.RES_SUCCESS;
        } catch (Exception ex) {
            return LoginConfig.HTTP_STATUS_403;
        }

    }

    /**
     * 用户信息
     * @return
     */
    public static ThreadLocal<User> getTokenUserInfo() {
        log.info("当前线程id为：" + Thread.currentThread().getId());
        return userThreadLocal;
    }

}
