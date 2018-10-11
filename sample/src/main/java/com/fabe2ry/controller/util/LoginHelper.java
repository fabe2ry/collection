package com.fabe2ry.controller.util;

import com.fabe2ry.model.util.ListVo;
import com.fabe2ry.model.util.ResultVo;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;

/**
 * Created by xiaoxq on 2018/9/17.
 */
public class LoginHelper {

    public static String COOKIE_USER_NAME = "userName";
    public static String COOKIE_USER_PASSWORD = "password";
    public static String SESSION_USER_NAME = "userName";
    public static String SESSION_USER_PASSWORD = "password";

    //    添加cookie和session
    public static void addCookieAndSeesion(HttpServletRequest request, HttpServletResponse response, String userName, String userPassword){
        Cookie userNameCookie = new Cookie(COOKIE_USER_NAME, userName);
        userNameCookie.setMaxAge(30*60);
        userNameCookie.setPath("/");
        response.addCookie(userNameCookie);

//        Cookie userPasswordCookie = new Cookie(COOKIE_USER_PASSWORD, userPassword);
//        userPasswordCookie.setMaxAge(5*60);
//        userPasswordCookie.setPath("/");
//        response.addCookie(userPasswordCookie);

        HttpSession session = request.getSession();
        session.setAttribute(SESSION_USER_NAME, userName);
        session.setAttribute(SESSION_USER_PASSWORD, userPassword);
    }

//    根据键值获取value
    public static String getCookie(HttpServletRequest request, HttpServletResponse response){
        Cookie[] cookies = request.getCookies();
        if(cookies == null){
            return null;
        }

        for(Cookie cookie : cookies){
            if(cookie.getName().equals(COOKIE_USER_NAME)){
                String returnName = cookie.getValue();
                return returnName;
            }
        }

        return null;
    }

    //    验证是否已经登陆
    public static boolean checkHasLogined(HttpServletRequest request, HttpServletResponse response){
        Cookie[] cookies = request.getCookies();
        if(cookies == null){
            System.out.println("cookie失效");
            return false;
        }
        String cookieUserName = null;
        for(Cookie cookie : cookies){
            if(cookie.getName().equals(COOKIE_USER_NAME)){
                cookieUserName = cookie.getValue();
            }
        }
        if(cookieUserName != null){
            HttpSession session = request.getSession();
            if(session != null && session.getAttribute(SESSION_USER_NAME) != null && session.getAttribute(SESSION_USER_NAME).equals(cookieUserName)){
                return true;
            }
            System.out.println("session失效");
        }
        System.out.println("user name失效");
        return false;
    }

    //    关闭sesion和cookie信息，实现下线功能
    public static void removeCookieAndSession(HttpServletRequest request, HttpServletResponse response){
        Cookie userNameCookie = new Cookie(COOKIE_USER_NAME, null);
        userNameCookie.setMaxAge(0);
        userNameCookie.setPath("/");
        response.addCookie(userNameCookie);

        HttpSession session = request.getSession(false);
        Enumeration<String> enumeration = session.getAttributeNames();
        while(enumeration.hasMoreElements()){
            session.removeAttribute(enumeration.nextElement());
        }
    }

    //    返回一个错误的ResultVo
    public static ResultVo getFailResultVo(String message){
        ResultVo resultVo = new ResultVo();
        resultVo.setSuccess(false);
        resultVo.setMessage(message);
        resultVo.setResult(null);
        return resultVo;
    }

    public static ListVo getFailListVo(String message){
        ListVo resultVo = new ListVo();
        resultVo.setSuccess(false);
        resultVo.setTotal(0);
        resultVo.setMessage(message);
        resultVo.setResult(null);
        return resultVo;
    }

}
