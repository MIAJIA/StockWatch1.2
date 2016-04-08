package com.logicmonitor.msp.servlet;

import java.io.IOException;
import java.lang.reflect.Type;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.logicmonitor.msp.YahooFetcher.YahooFetchException;
import com.logicmonitor.msp.domain.StockInfo;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

/**
 * Servlet implementation class getOneInfo, get one company's static info from YahooAPI
 */
public class getOneInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int queryIdx = request.getQueryString().indexOf("=");
		String queryParameter = request.getQueryString().substring(queryIdx+1, request.getQueryString().length());
		StockInfo si = new StockInfo();
		Stock s = null;
		
		try {
			s = YahooFinance.get(queryParameter);
		} catch (YahooFetchException e) {
			e.printStackTrace();
		}
		si.setSymbol(s.getSymbol());
		si.setCurrency(s.getCurrency());
		si.setName(s.getName());
		si.setStockExchange(s.getStockExchange());
		Type Type = new TypeToken<StockInfo>(){}.getType();
		response.getWriter().write(new Gson().toJson(si, Type));
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
