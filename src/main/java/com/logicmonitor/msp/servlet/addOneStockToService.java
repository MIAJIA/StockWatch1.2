package com.logicmonitor.msp.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.logicmonitor.msp.service.StockService;

/**
 * Servlet implementation class addOneStockToService, stockService start fetch the stock from YahooAPI
 */
public class addOneStockToService extends HttpServlet {
	private static final long serialVersionUID = 1L;


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int queryIdx = request.getQueryString().indexOf("=");
		String queryAdd = request.getQueryString().substring(queryIdx+1, request.getQueryString().length());
		if(queryAdd != null) {				
			StockService stockService = new StockService();
			stockService.fetchAddNewtoDB(queryAdd);
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
