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

import com.logicmonitor.msp.dao.StockDao;
import com.logicmonitor.msp.dao.impl.StockDaoJdbcImpl;

import com.logicmonitor.msp.domain.StockPrice;


/**
 * Servlet implementation class OneCompanyStockInfo, return select history price list of a year/ a month
 */
public class GetOneHistoryPrice extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<StockPrice> stockPriceList = new ArrayList<StockPrice>();
		int queryIdx = request.getQueryString().indexOf("=");
		String queryParameter = request.getQueryString().substring(queryIdx+1, request.getQueryString().length());
		String[] querys = queryParameter.split("_");
		StockDao dao = new StockDaoJdbcImpl();
		if(querys.length > 0) {
			if(querys[1].equals("Year")) {
//				System.out.println("ASK FOR YEARLY DATA");
				stockPriceList = dao.getWeeklyData(querys[2]);
//				System.out.println(stockPriceList);
			} else if(querys[1].equals("Month")) {
				stockPriceList = dao.getDailyData(querys[2]);
			} else if(querys[1].equals("Week")) {
//				System.out.println("ASK FOR realtime DATA");
				stockPriceList = dao.getRealTimeData(querys[2]);
//				System.out.println(stockPriceList);
			}
		}
		Type listType = new TypeToken<List<StockPrice>>(){}.getType();
		response.getWriter().write(new Gson().toJson(stockPriceList, listType));
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
