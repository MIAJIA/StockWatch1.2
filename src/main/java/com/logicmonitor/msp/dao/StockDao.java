package com.logicmonitor.msp.dao;

import java.util.List;

import com.logicmonitor.msp.domain.StockInfo;
import com.logicmonitor.msp.domain.StockPrice;

/**
 *  provide interface for CRUD database
 */
public interface StockDao {
	//insert stock info/price into relative table
	public void addStockInfo(StockInfo info);
	public void addRealTimeData(StockPrice realTimeStock);
	public void addDailyData(StockPrice dailyStock);
	public void addWeeklyData(StockPrice weeklyStock);
	
	
	//get stock info/price from relative table
	public List<StockPrice> getRealTimeData(String symbol);
	public List<StockPrice> getDailyData(String symbol);
	public List<StockPrice> getWeeklyData(String symbol);
	public StockInfo getOneInfoFromDBDAO(String symbol);
	//list all the symbols that exist database.
	public List<StockInfo> getAllInfoFromDBDAO();  
//	public List<StockInfo> getSelectInfoFromDBDAO(List<String> symbolList);  

	
	//delete an stock info/price existing from relative table
	public void delAllFromDBDAO(String symbol);
	public void delInfolFromDBDAO(String symbol);
	public void delRealTimeData(String symbol);
	public void delDailyData(String symbol);
	public void delWeeklyData(String symbol);

}
