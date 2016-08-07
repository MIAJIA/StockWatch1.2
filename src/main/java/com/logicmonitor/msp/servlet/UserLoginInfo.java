package com.logicmonitor.msp.servlet;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.logicmonitor.msp.biz.LoginBiz;
import com.logicmonitor.msp.biz.impl.LoginBizImpl;
import com.logicmonitor.msp.domain.User;
import com.logicmonitor.msp.service.UserService;



/**
 * Servlet implementation class UserLoginInfo
 */
public class UserLoginInfo extends HttpServlet {
  private static final long serialVersionUID = 1L;
  public UserLoginInfo() {}

  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    //		response.getWriter().append("Served at: ").append(request.getContextPath());
    //		int queryIdx = request.getQueryString().indexOf("=");
    //		String queryParameter = request.getQueryString().substring(queryIdx+1, request.getQueryString().length());
    //		String[] querys = queryParameter.split("_");
    //		UserService us = UserService.getInstance();
    //		if((querys.length > 0) && us.checkLogin(querys[0], querys[1])) {
    //			response.setStatus(response.SC_OK);
    //		} else {
    //			response.sendError(HttpServletResponse.SC_NOT_FOUND);	
    //		}
    String username = request.getParameter("username");
    String password = request.getParameter("password"); 
    UserService us = UserService.getInstance();
    //		System.out.println("UserService inc" + us);
    LoginBiz loginBiz = new LoginBizImpl();
    //		System.out.println("loginBiz inc" + loginBiz);
    User user = us.checkLogin(username, password);
    if (user != null) {
      //		   System.out.println("user != null");
      loginBiz.addCookie("username", username, 60*60*24*30, response);
      loginBiz.addCookie("password", password, 60*60*24*30, response);
      request.getSession().setAttribute("user", user);
      System.out.println("request.getSession" + request.getSession());
      response.setStatus(response.SC_OK);
    } else {
      response.sendError(HttpServletResponse.SC_NOT_FOUND); 
    }
  }

  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    doGet(request, response);
  }

}
