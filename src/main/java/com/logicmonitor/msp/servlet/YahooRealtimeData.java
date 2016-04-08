package com.logicmonitor.msp.servlet;

import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.logicmonitor.msp.YahooFetcher.YahooFetchException;
import com.logicmonitor.msp.domain.StockPrice;
import com.logicmonitor.msp.service.TimeValidation;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.quotes.stock.StockQuote;

/**
 * Servlet implementation class YahooRealtimeData, get real time data directly from YahooAPI
 */
public class YahooRealtimeData extends HttpServlet {
	private static final long serialVersionUID = 1L;
	boolean marketClosed = false;
//	TimeValidation timeValid = new TimeValidation();
//	StockPrice mscache;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int queryIdx = request.getQueryString().indexOf("=");
		String queryParameter = request.getQueryString().substring(queryIdx+1);
		Stock s = null;

		try {
			s = YahooFinance.get(queryParameter);
		} catch (YahooFetchException e) {
			e.getMessage();
			e.printStackTrace();
		}
		StockPrice ms = new StockPrice();
		ms.setSymbol(s.getSymbol());
		StockQuote sq = s.getQuote();
		ms.setPrice(sq.getPrice());
		ms.setHigh(sq.getDayHigh());
		ms.setLow(sq.getDayLow());
		ms.setOpen(sq.getOpen());
		ms.setClose(sq.getPreviousClose());
		ms.setVolume(sq.getVolume());

		Type Type = new TypeToken<StockPrice>(){}.getType();
		response.getWriter().write(new Gson().toJson(ms, Type));
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
