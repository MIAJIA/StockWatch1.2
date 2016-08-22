package com.logicmonitor.msp.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.logicmonitor.msp.YahooFetcher.DataFetcher;
import com.logicmonitor.msp.dao.StockDao;
import com.logicmonitor.msp.dao.impl.StockDaoJdbcImpl;
import com.logicmonitor.msp.domain.StockInfo;
import com.logicmonitor.msp.domain.StockList;
import com.logicmonitor.msp.domain.StockPrice;

/**
 * StockService to fetch data from YahooAPI to Database
 *
 */
public class StockService_ {
  private static StockService_ instance = null;
  DataFetcher fetchData;
  StockDao dao;
  public StockList stockList;
  boolean FirstRun;
  //add new stock to monitor
  RunnableFetchAddOneStockMonthLongDaily T1;
  RunnableFetchAddOneStockYearLongWeekly T2;
  RunnableFetchAddInfo T3 ;
  //update stock price regularly 
  RunnableFetchAddRealTime T4;
  RunnableFetchAddDaily T5;
  RunnableFetchAddWeekly T6;
  TimeValidation timeValid = new TimeValidation();
//  Timer uploadCheckerTimer = new Timer(true);
//  uploadCheckerTimer.scheduleAtFixedRate(
//      new TimerTask() {
//        public void run() { NewUploadServer.getInstance().checkAndUploadFiles(); }
//      }, 0, 60 * 1000);


  private StockService_() {
    fetchData = new DataFetcher();
    dao = new StockDaoJdbcImpl();
    stockList = new StockList();
    FirstRun = true;
  }
  public static StockService_ getInstance() {
    if (instance == null) {
      synchronized (StockService.class) {
        if (instance == null) {
          instance = new StockService_();
        }
      }
    }
    return instance;
  }
  public void delFromDB(String symbol){
    stockList.remove(symbol);
    dao.delAllFromDBDAO(symbol);
  }
  //add new stock to monitor
  public void fetchAddNewtoDB(String symbol){
    stockList.put(symbol, stockList.getOrDefault(symbol, 0) + 1);
    T1 = new RunnableFetchAddOneStockMonthLongDaily(symbol,fetchData, dao);
    T1.start();
    T2 = new RunnableFetchAddOneStockYearLongWeekly(symbol,fetchData, dao);
    T2.start();
    T3 = new RunnableFetchAddInfo(symbol,fetchData, dao);
    T3.start();
    if (FirstRun == true) {
      FirstRun = false;
      this.fetchAddtoDB(stockList);
    }
  }


  //update stock price regularly 
  private void fetchAddtoDB(StockList symbolList){
    T4 = new RunnableFetchAddRealTime(symbolList,fetchData, dao);
    T4.start();
    T5 = new RunnableFetchAddDaily(symbolList,fetchData, dao);
    T5.start();
    T6 = new RunnableFetchAddWeekly(symbolList,fetchData, dao);
    T6.start();
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


  class RunnableFetchAddOneStockMonthLongDaily extends FetchAddThread {   
    String symbol;
    List<StockPrice> stockPriceList;
    public RunnableFetchAddOneStockMonthLongDaily (String symbol, DataFetcher fetchData, StockDao dao) {
      super(fetchData, dao);
      this.setThreadName("FetchAddOneStockYearLongWeekly");
      this.symbol = symbol;
      stockPriceList = new ArrayList<StockPrice>();
    }


    @Override
    public void run() {
      fetchData.getMonthLongDailyDataFromYahoo(symbol, stockPriceList);
      for(StockPrice stockPrice : stockPriceList) {       
        dao.addDailyData(stockPrice);
      }
    }
  }   


  class RunnableFetchAddOneStockRealTime extends FetchAddThread {
    String symbol;
    StockPrice stockPrice;
    public RunnableFetchAddOneStockRealTime (String symbol, DataFetcher fetchData, StockDao dao) {
      super(fetchData, dao);
      this.symbol = symbol;
      stockPrice = new StockPrice();
      this.setThreadName("FetchAddOneStockRealTime");
    }

    @Override
    public void run() {
      while(true) {
        fetchData.getOneRealTimeDataFromYahoo(symbol , stockPrice);
        dao.addRealTimeData(stockPrice);     
        try {
          Thread.sleep(10000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }
  }


  class RunnableFetchAddInfo extends FetchAddThread {
    StockInfo stkInfo;
    String symbol;
    public RunnableFetchAddInfo (String symbol, DataFetcher fetchData, StockDao dao) {
      super(fetchData, dao);
      this.symbol = symbol;
      stkInfo = new StockInfo ();
      this.setThreadName("FetchAddInfo");
    }

    @Override
    public void run() {
      fetchData.getStockInfoFromYahoo(symbol , stkInfo);
      dao.addStockInfo(stkInfo);
    }
  }


  class RunnableFetchAddRealTime extends FetchAddThread  {
    List<StockPrice> stockPriceList;
    StockList symbolList;
    
    public RunnableFetchAddRealTime (StockList symbolList, DataFetcher fetchData, StockDao dao) {
      super(fetchData, dao);
      this.symbolList = symbolList;
      stockPriceList = new ArrayList<StockPrice> ();
      this.setThreadName("FetchAddRealTime");
    }

    @Override
    public void run() {
      while(TimeValidation.isValidate()) {
        fetchData.getRealTimeDataFromYahoo(symbolList , stockPriceList);
        for (StockPrice stockPrice : stockPriceList ) {
          dao.addRealTimeData(stockPrice);
        }
        try {
          Thread.sleep(1000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }
  }


  class RunnableFetchAddWeekly extends FetchAddThread  {
    StockList symbolList;
    List<StockPrice> stockPriceList;

    public RunnableFetchAddWeekly (StockList symbolList, DataFetcher fetchData, StockDao dao) {
      super(fetchData, dao);
      this.setThreadName("FetchAddWeekly");
      this.symbolList = symbolList;
      stockPriceList = new ArrayList<StockPrice> ();
    }
    @Override
    public void run() {
      while(true) {
        fetchData.updateWeeklyDataFromYahoo(symbolList , stockPriceList);
        for (StockPrice stockPrice : stockPriceList ) {
          dao.addWeeklyData(stockPrice);
        }
        try {
          Thread.sleep(604800000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }

    }
  }


  class RunnableFetchAddDaily extends FetchAddThread  {
    StockList symbolList;
    List<StockPrice> stockPriceList;

    public RunnableFetchAddDaily (StockList symbolList, DataFetcher fetchData, StockDao dao) {
      super(fetchData, dao);
      this.symbolList = symbolList;
      stockPriceList = new ArrayList<StockPrice> ();
      this.setThreadName("FetchAddDaily");
    }
    @Override
    public void run() {
      while(true) {
        fetchData.updateDailyDataFromYahoo(symbolList , stockPriceList);
        for (StockPrice stockPrice : stockPriceList ) {
          dao.addDailyData(stockPrice);
        }
        try {
          Thread.sleep(86400000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }

  }

}