package com.logicmonitor.msp.servlet;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.logicmonitor.msp.dao.impl.StockDaoJdbcImpl;
import com.logicmonitor.msp.domain.StockInfo;

/**
 * Servlet implementation class AllCompaniesInfo, get all static company info in database.
 */
public class AllCompaniesInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public AllCompaniesInfo() {
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		StockDaoJdbcImpl dao = new StockDaoJdbcImpl();
		List<StockInfo> stockInfoList = dao.getAllInfoFromDBDAO();
		Type listType = new TypeToken<List<StockInfo>>(){}.getType();
		response.getWriter().write(new Gson().toJson(stockInfoList, listType));
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
