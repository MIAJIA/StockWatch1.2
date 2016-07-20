package com.logicmonitor.msp.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.logicmonitor.msp.YahooFetcher.DataFetcher;
import com.logicmonitor.msp.dao.StockDao;
import com.logicmonitor.msp.dao.impl.StockDaoJdbcImpl;
import com.logicmonitor.msp.domain.StockInfo;
import com.logicmonitor.msp.domain.StockPrice;

/**
 * StockService to fetch data from YahooAPI to Database
 *
 */
public class StockService {
	DataFetcher fetchData;
	StockDao dao;
	List<String> strList;
	boolean FirstRun;
	TimeValidation timeValid = new TimeValidation();
		
	
	public StockService() {
		fetchData = new DataFetcher();
		dao = new StockDaoJdbcImpl();
//		strList = new ArrayList<String>();
		strList = Collections.synchronizedList(new ArrayList<String>());
		FirstRun = true;
	}
	
	//add new stock to monitor
	public void fetchAddNewtoDB(String symbol){
		strList.add(symbol);
		RunnableFetchAddOneStockMonthLongDaily T1 = new RunnableFetchAddOneStockMonthLongDaily(symbol,fetchData, dao);
		T1.start();
		RunnableFetchAddOneStockYearLongWeekly T2 = new RunnableFetchAddOneStockYearLongWeekly(symbol,fetchData, dao);
		T2.start();
		RunnableFetchAddInfo T3 = new RunnableFetchAddInfo(symbol,fetchData, dao);
		T3.start();
		if (FirstRun == true) {
			FirstRun = false;
			this.fetchAddtoDB(strList);
		}
	}
	
	
	//update stock price regularly 
	private void fetchAddtoDB(List<String> symbolList){
		RunnableFetchAddRealTime T1 = new RunnableFetchAddRealTime(symbolList,fetchData, dao);
		T1.start();
		RunnableFetchAddDaily T2 = new RunnableFetchAddDaily(symbolList,fetchData, dao);
		T2.start();
		RunnableFetchAddWeekly T3 = new RunnableFetchAddWeekly(symbolList,fetchData, dao);
		T3.start();
	}
	
	
	class RunnableFetchAddOneStockYearLongWeekly implements Runnable {
		private Thread t;
		private String threadName;
		DataFetcher fetchData;
		String symbol;
		StockDao dao;
		StockPrice stockPrice;
		List<StockPrice> stockPriceList;
		
		public RunnableFetchAddOneStockYearLongWeekly (String symbol, DataFetcher fetchData, StockDao dao) {
			this.symbol = symbol;
			this.fetchData = fetchData;
			this.dao = dao;
			stockPriceList = new ArrayList<StockPrice>();
			threadName = "FetchAddOneStockYearLongWeekly";
		}
		
		public void run() {
			fetchData.getYearLongWeeklyDataFromYahoo(symbol, stockPriceList);
			for (StockPrice stockPrice : stockPriceList) {
				dao.addWeeklyData(stockPrice);
			}
		}

		public void start ()
		{
			if (t == null)
			{
				t = new Thread (this, threadName);
				t.start ();
			}
		}
	}


	class RunnableFetchAddOneStockMonthLongDaily implements Runnable {
		private Thread t;
		private String threadName;
		DataFetcher fetchData;
		String symbol;
		StockDao dao;
		StockPrice stockPrice;
		List<StockPrice> stockPriceList;
		
		public RunnableFetchAddOneStockMonthLongDaily (String symbol, DataFetcher fetchData, StockDao dao) {
			this.symbol = symbol;
			this.fetchData = fetchData;
			this.dao = dao;
			stockPriceList = new ArrayList<StockPrice>();

			threadName = "FetchAddOneStockYearLongWeekly";
		}
		
		public void run() {
			fetchData.getMonthLongDailyDataFromYahoo(symbol, stockPriceList);
			for(StockPrice stockPrice : stockPriceList) {		
				dao.addDailyData(stockPrice);
			}
		}
		
		public void start ()
		{
			if (t == null)
			{
				t = new Thread (this, threadName);
				t.start ();
			}
		}
	}	


	class RunnableFetchAddOneStockRealTime implements Runnable {
		private Thread t;
		private String threadName;
		DataFetcher fetchData;
		String symbol;
		StockDao dao;
		StockPrice stockPrice;
		
		public RunnableFetchAddOneStockRealTime (String symbol, DataFetcher fetchData, StockDao dao) {
			this.symbol = symbol;
			this.fetchData = fetchData;
			this.dao = dao;
			stockPrice = new StockPrice();
			threadName = "FetchAddOneStockRealTime";
		}
		
		public void run() {
			int i = 0;
			while(true) {
				fetchData.getOneRealTimeDataFromYahoo(symbol , stockPrice);
				dao.addRealTimeData(stockPrice);     
				i++;
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		public void start ()
		{
			if (t == null)
			{
				t = new Thread (this, threadName);
				t.start ();
			}
		}
	}


	class RunnableFetchAddInfo implements Runnable {
		private Thread t;
		private String threadName;
		DataFetcher fetchData;
		String symbol;
		StockDao dao;
		StockInfo stkInfo;
		
		public RunnableFetchAddInfo (String symbol, DataFetcher fetchData, StockDao dao) {
			this.symbol = symbol;
			this.fetchData = fetchData;
			this.dao = dao;
			stkInfo = new StockInfo ();
			threadName = "FetchAddInfo";
		}
		
		public void run() {
			fetchData.getStockInfoFromYahoo(symbol , stkInfo);
			dao.addStockInfo(stkInfo);
		}

		public void start ()
		{
			if (t == null)
			{
				t = new Thread (this, threadName);
				t.start ();
			}
		}
	}

	
	class RunnableFetchAddRealTime implements Runnable {
		private Thread t;
		private String threadName;
		DataFetcher fetchData;
		List<String> symbolList;
		StockDao dao;
		List<StockPrice> stockPriceList;
		
		public RunnableFetchAddRealTime (List<String> symbolList, DataFetcher fetchData, StockDao dao) {
			this.symbolList = symbolList;
			this.fetchData = fetchData;
			this.dao = dao;

			stockPriceList = new ArrayList<StockPrice> ();
			threadName = "FetchAddRealTime";
		}
		
		public void run() {
			int i = 0;
			while(timeValid.isValidate()) {
				fetchData.getRealTimeDataFromYahoo(symbolList , stockPriceList);
				for (StockPrice stockPrice : stockPriceList ) {
					dao.addRealTimeData(stockPrice);
				}
				i++;
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		public void start ()
		{
			if (t == null)
			{
				t = new Thread (this, threadName);
				t.start ();
			}
		}
	}


	class RunnableFetchAddWeekly implements Runnable {
		private Thread t;
		private String threadName;
		DataFetcher fetchData;
		List<String> symbolList;
		StockDao dao;
		List<StockPrice> stockPriceList;
		
		public RunnableFetchAddWeekly (List<String> symbolList, DataFetcher fetchData, StockDao dao) {
			this.symbolList = symbolList;
			this.fetchData = fetchData;
			this.dao = dao;
			stockPriceList = new ArrayList<StockPrice> ();
			threadName = "FetchAddWeekly";
		}
		
		public void run() {
			int i = 0;
			while(true) {
				fetchData.updateWeeklyDataFromYahoo(symbolList , stockPriceList);
				for (StockPrice stockPrice : stockPriceList ) {
					dao.addWeeklyData(stockPrice);
				}
				
				i++;
				try {
					Thread.sleep(604800000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}

		public void start ()
		{
			if (t == null)
			{
				t = new Thread (this, threadName);
				t.start ();
			}
		}
	}


	class RunnableFetchAddDaily implements Runnable {
		private Thread t;
		private String threadName;
		DataFetcher fetchData;
		List<String> symbolList;
		StockDao dao;
		List<StockPrice> stockPriceList;
		
		public RunnableFetchAddDaily (List<String> symbolList, DataFetcher fetchData, StockDao dao) {
			this.symbolList = symbolList;
			this.fetchData = fetchData;
			this.dao = dao;

			stockPriceList = new ArrayList<StockPrice> ();
			threadName = "FetchAddDaily";
		}
		
		public void run() {
			int i = 0;
			while(true) {
				fetchData.updateDailyDataFromYahoo(symbolList , stockPriceList);
				for (StockPrice stockPrice : stockPriceList ) {
					dao.addDailyData(stockPrice);
				}
				i++;
				try {
					Thread.sleep(86400000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		public void start ()
		{
			if (t == null)
			{
				t = new Thread (this, threadName);
				t.start ();
			}
		}

	}

}