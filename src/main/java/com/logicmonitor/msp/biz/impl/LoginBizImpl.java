package com.logicmonitor.msp.biz.impl;


import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.logicmonitor.msp.biz.LoginBiz;
import com.logicmonitor.msp.dao.UserDao;
import com.logicmonitor.msp.dao.impl.UserDaoImpl;
import com.logicmonitor.msp.domain.User;



public class LoginBizImpl implements LoginBiz {
    private UserDao userDao;
    
    public LoginBizImpl() {
        super();
        userDao = new UserDaoImpl();
    }

    public User login(String username, String password) {
        User user = userDao.findUserByAccountAndPsw(username, password);
        if (user != null) {
            return user;
        }
        return null;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        clearCookie(request, response);
        clearSession(request);
    }

    @Override
    public User loginWithCookie(ServletRequest req) {
        HttpServletRequest request = (HttpServletRequest)req;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            String username = "";
            String password = "";
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("username")) {
                    username = cookie.getValue();
                }
                if (cookie.getName().equals("password")) {
                    password = cookie.getValue();
                }
            }
            User user = login(username, password);
            if (user != null) {
                return user;
            }
        }
        return null;
    }

    @Override
    public void addCookie(String name, String value, int age,
            HttpServletResponse response) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(age);
        response.addCookie(cookie);
    }

    @Override
    public void clearCookie(ServletRequest req, ServletResponse res) {
        HttpServletRequest request = (HttpServletRequest)req;
        HttpServletResponse response = (HttpServletResponse)res;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("username")) {
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
                if (cookie.getName().equals("password")) {
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
        }
    }

    @Override
    public void clearSession(HttpServletRequest request) {
        request.getSession().removeAttribute("user");
    }
    
}
