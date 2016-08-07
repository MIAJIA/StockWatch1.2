package com.logicmonitor.msp.servlet;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.logicmonitor.msp.domain.StockList;
import com.logicmonitor.msp.service.UserService;
/**
 * Servlet implementation class GetUserWatchList
 */
public class GetUserWatchList extends HttpServlet {
  private static final long serialVersionUID = 1L;

  public GetUserWatchList() {
  }

  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    int queryIdx = request.getQueryString().indexOf("=");
    String queryParameter = request.getQueryString().substring(queryIdx+1, request.getQueryString().length());
    UserService us = UserService.getInstance();
    List<String> watchlist = new ArrayList<String>();
    us.getUserWatchList(queryParameter, watchlist);
    Type Type = new TypeToken<StockList>(){}.getType();
    response.getWriter().write(new Gson().toJson(watchlist, Type));
  }

protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
  doGet(request, response);
}

}
