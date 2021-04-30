package com.example.nextzhkuserver.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.nextzhkuserver.utils.AjaxResult;
import com.example.nextzhkuserver.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;

@Slf4j
public class CommonInterceptor implements AsyncHandlerInterceptor, InitializingBean {

    private final ThreadLocal<Long> time = new ThreadLocal<>();

    @Autowired
    private GlobalConfiguration configuration;

    private JWTVerifier verifier;

    @Override
    public void afterPropertiesSet() {
        try {
            Algorithm algorithm = Algorithm.HMAC256(configuration.getJwtKey());
            verifier = JWT.require(algorithm)
                    .withIssuer("auth0")
                    .build();
        } catch (UnsupportedEncodingException e) {
            log.error("生成验证器失败", e);
        }
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        //让浏览器用utf8来解析返回的数据
        response.setHeader("Content-Type", "text/html;charset=UTF-8");
        //告诉servlet用UTF-8转码，而不是用默认的ISO8859
        response.setCharacterEncoding("UTF-8");
        time.set(Calendar.getInstance().getTimeInMillis());
        if (RequestMethod.OPTIONS.name().equals(request.getMethod())) {
            return true;
        }
        String token = request.getHeader("X-Token");
        if (StringUtils.isNotEmpty(token)) {
            try {
                verifier.verify(token);
            } catch (JWTVerificationException ex) {
                // log.warn("验证失败", ex);
                response.getWriter().write(AjaxResult.error("用户验证失败，请重新登录！").toString());
                return false;
            }
            return true;
        }
        response.getWriter().write(AjaxResult.error("用户验证失败，请重新登录！").toString());
        return false;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        if (ex != null) {
            log.error("MVC Exception", ex);
        }
        if (time.get() != null && !RequestMethod.OPTIONS.name().equals(request.getMethod())) {
            // 统计执行时间，忽略OPTIONS方法
            log.debug("{} -> {} ms -> {}:{}", Thread.currentThread().getId(),
                    Calendar.getInstance().getTimeInMillis() - time.get(),
                    request.getMethod(),
                    request.getRequestURI());
            time.remove();
        }
        AsyncHandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
