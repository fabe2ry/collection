package com.fabe2ry.aop.aspect;

import com.fabe2ry.aop.annotation.WebLogAnnotation;
import com.fabe2ry.controller.util.LoginHelper;
import com.fabe2ry.model.LogModel;
import com.fabe2ry.service.LogService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.logging.Logger;

/**
 * Created by xiaoxq on 2018/9/19.
 */
@Aspect
@Component
public class WebLogAspect {

    Logger logger = Logger.getLogger(getClass().getName());
    @Autowired
    LogService logService;

//    @Pointcut("execution(public * com.fabe2ry.controller.UserController.*(..))")
    @Pointcut("@annotation(com.fabe2ry.aop.annotation.WebLogAnnotation)")
    public void webLogPointCut(){};


    @Before("webLogPointCut()")
    public void doBefore(JoinPoint joinPoint){
//        logger.info("doBefore");
    }


    @After("webLogPointCut()")
    public void doAfter(JoinPoint joinPoint){
//        logger.info("doAfter");
    }

    @AfterReturning(value = "webLogPointCut()", returning = "ret")
    public void doAfterReturn(JoinPoint joinPoint, Object ret){
//        logger.info("doAfterReturn");
        if(ret != null){
//            logger.info(ret.toString());
        }
    }

//    @Around("webLogPointCut()")
    @Around(value = "webLogPointCut() && @annotation(webLogAnnotation)")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint, WebLogAnnotation webLogAnnotation) throws Throwable {
//        logger.info("doAround");
        Object object = null;
//        TODO:操作的位置
//        TODO:同步问题
//        TODO:参数哪里来
//        TODO:proceedingJoinPoint使用
//        TODO:看demo,学习其他方式,学习要注意的技巧
//        TODO:写博客,记录,整理思路
//        TODO:date装潢
//        TODO:mybatis操作细节
//        TODO:aop原理
//        start time
        Calendar calendar = Calendar.getInstance();
        Date beforeDate = calendar.getTime();
        long beforeTime = calendar.getTimeInMillis();

//        init log model
        LogModel logModel = new LogModel();
        logModel.setOperate_date(beforeDate);
        logModel.setType("INFO");

//        request parse
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        parseHttpForLogModel(logModel, request);

//        annotation parse
        logModel.setTitle(webLogAnnotation.annotationName());

        try {
//            logger.info("before logging");
            object = proceedingJoinPoint.proceed();
//            logger.info("returning logging");
        } catch (Throwable e) {
//            logger.info("throwing logging");
//            TODO:记录什么异常信息
            logModel.setType("ERROR");
            logModel.setException(e.getMessage());
//            e.printStackTrace();
            throw e;
        }

//        end time
        calendar = Calendar.getInstance();
        long afterTime = calendar.getTimeInMillis();
        long lastTime = afterTime - beforeTime;
        logModel.setTimeout(String.valueOf(lastTime));

//        load model
        loadLogModel(logModel);
//        logger.info("after logging");
        return object;
    }

    @AfterThrowing( value = "webLogPointCut()")
    public void c(JoinPoint joinPoint){
//        logger.info("doAfterThrowing");
    }

//    为logmedel记录http的信息
    public void parseHttpForLogModel(LogModel logModel, HttpServletRequest request){
//        request
        logModel.setRequest_uri(request.getRequestURI());
        logModel.setRemote_addr(request.getRemoteAddr());
        logModel.setMethod(request.getMethod());
////        TODO:这是什么
//        logger.info("user:" + request.getRemoteUser());

//        cookie
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for(Cookie cookie : cookies){
                if(cookie.getName().equals(LoginHelper.COOKIE_USER_NAME)){
                    logModel.setUser_id(cookie.getValue());
                }
            }
        }

//        params
        StringBuffer stringBuffer = new StringBuffer();
        Enumeration enumeration = request.getParameterNames();
        while(enumeration.hasMoreElements()){
            String key = (String) enumeration.nextElement();
            String value = request.getParameter(key);
            stringBuffer.append(key + "=" + value);
            if(enumeration.hasMoreElements()){
                stringBuffer.append("&");
            }
        }
        String params = stringBuffer.toString();
        if(params.length() > 0){
            logModel.setParams(stringBuffer.toString());
        }
    }

//    装载logmodel
    public void loadLogModel(LogModel logModel){
        logService.insertLog(logModel);
    }


}
