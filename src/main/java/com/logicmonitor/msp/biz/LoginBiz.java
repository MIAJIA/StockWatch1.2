package com.logicmonitor.msp.biz;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.logicmonitor.msp.domain.User;


public interface LoginBiz {
	 public User login(String account, String password);
	    public void logout(HttpServletRequest request, HttpServletResponse response);
	    public void addCookie(String name, String value, int age, HttpServletResponse response);
	    public User loginWithCookie(ServletRequest req);
	    public void clearCookie(ServletRequest req, ServletResponse res);
	    public void clearSession(HttpServletRequest request);
}
