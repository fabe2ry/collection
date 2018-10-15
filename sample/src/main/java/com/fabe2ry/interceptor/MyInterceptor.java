package com.fabe2ry.interceptor;

import com.fabe2ry.controller.util.LoginHelper;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.logging.Logger;

/**
 * Created by xiaoxq on 2018/9/19.
 */
public class MyInterceptor implements HandlerInterceptor {
    Logger logger = Logger.getLogger(getClass().getName());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(LoginHelper.checkHasLogined(request, response)){
            return true;
        }

        if(request.getRequestURI().equals("/api/user/login") || request.getRequestURI().equals("/api/user/logout")
                || request.getRequestURI().equals("/api/user/register")){
            return true;
        }
//        logger.info(request.getRequestURL().toString());
//        logger.info(request.getRequestURI());
//        logger.info(request.getContextPath());
//        logger.info(request.getServletPath());
//        logger.info(request.getQueryString());
//        logger.info(request.getLocalPort() + "");
        response.setStatus(401);
//        设置跨域url
        int port = 8888;
        String corxUrl = "http://localhost:" + port;

        response.setHeader("Access-Control-Allow-Origin", corxUrl);
        response.addHeader("Access-Control-Allow-Credentials", "true");
        response.getWriter().append("{");
        response.getWriter().append("message : Unauthorized");
        response.getWriter().append("}");
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//        logger.info("postHandle");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//        logger.info("afterCompletion");
    }

}
