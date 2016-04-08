package com.logicmonitor.msp.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.logicmonitor.msp.dao.StockDao;
import com.logicmonitor.msp.dao.impl.StockDaoJdbcImpl;


/**
 * Servlet implementation class DelOneStock, delete all information of a company from database
 */
public class delOneStock extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int queryIdx = request.getQueryString().indexOf("=");
		String queryDel = request.getQueryString().substring(queryIdx+1, request.getQueryString().length());
		StockDao dao = null;
		if(queryDel != null) {
			dao = new StockDaoJdbcImpl();
			dao.delAllFromDBDAO(queryDel);
			response.setStatus(response.SC_OK);
		} else {
		    response.sendError(HttpServletResponse.SC_NOT_FOUND);	
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
