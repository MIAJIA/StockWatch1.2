package com.logicmonitor.msp.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.logicmonitor.msp.dao.StockDao;
import com.logicmonitor.msp.dao.impl.StockDaoJdbcImpl;
import com.logicmonitor.msp.domain.User;
import com.logicmonitor.msp.service.StockService;
import com.logicmonitor.msp.service.UserService;

/**
 * Servlet implementation class addOneStockToService, stockService start fetch the stock from YahooAPI
 */
public class addOneStockToService extends HttpServlet {
	private static final long serialVersionUID = 1L;


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int queryIdx = request.getQueryString().indexOf("=");
		String queryAdd = request.getQueryString().substring(queryIdx+1, request.getQueryString().length());
		String[] querys = queryAdd.split("_");
//	   String username = request.getParameter("username");
//	    String symbol = request.getParameter("symbol"); 
		if(querys != null) {	
		    StockService ss = StockService.getInstance();
		    UserService us = UserService.getInstance();
		    if (querys.length == 1) {
	            ss.fetchAddNewtoDB(queryAdd);
		    } else {
		      User curUser = us.userList.get(querys[0]);
		      String symbol = querys[1];
		      if (!ss.stockList.contains(symbol)) {
                ss.fetchAddNewtoDB(symbol);
                ss.stockList.add(symbol);
              }
              us.addOneToWatchList(querys[0], symbol);
//		      if (!ss.stockList.contains(symbol)) {
//		        ss.fetchAddNewtoDB(symbol);
//		        ss.stockList.add(symbol);
//		      }
//		      us.addOneToWatchList(username, symbol);
		    }
		    response.setStatus(response.SC_OK);
		} else {
		    response.sendError(HttpServletResponse.SC_NOT_FOUND);	
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
