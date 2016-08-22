package com.logicmonitor.msp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.logicmonitor.msp.YahooFetcher.DataFetcher;
import com.logicmonitor.msp.dao.StockDao;
import com.logicmonitor.msp.dao.impl.StockDaoJdbcImpl;
import com.logicmonitor.msp.domain.StockInfo;
import com.logicmonitor.msp.domain.StockList;
import com.logicmonitor.msp.domain.StockPrice;

/**
 * A StockService to use Data fetcher to get all stocks' quotes and info
 * periodically in stockList and store them into data base use dao.
 *
 * @author Will Zhang
 */
public class StockService {
  private static StockService instance = null;
  private  ScheduledExecutorService scheduExec;
  DataFetcher fetchData;
  StockDao dao;
  StockList stockList;
  boolean initRun;

  /**
   * Constructor--creates StockService to use Data fetcher to get all stocks' prices 
   * in stockList and store them into data base use dao.
   * @param initRun
   *            - the indicator of first run of the program
   * @param scheduExec
   *            - ScheduledExecutorService to schedule commands to execute periodically
   *            or run directly
   * @param fetchData
   *            - fetcher to get data from Yahoo finance API 
   * @param dao
   *            - the data access object/interface with data base
   * @param stockList
   *            - a list of total stock symbols that users subscribed and number of 
   *            subscriber of each stock.                   
   */
  private StockService(){
    initRun = true;
    this.scheduExec =  Executors.newScheduledThreadPool(5);  
    fetchData = new DataFetcher();
    dao = new StockDaoJdbcImpl();
    stockList = new StockList();
  }

  //Lazy instantiation StockService using double locking mechanism.
  public static StockService getInstance() {
    if (instance == null) {
      synchronized (StockService.class) {
        if (instance == null) {
          instance = new StockService();
        }
      }
    }
    return instance;
  }

  /**
   * Use for fetch add new a stock to stockList and data base.
   * If it's the first run time, call fetchAddtoDB automatically.
   */
  public void fetchAddNewtoDB(String symbol){
    stockList.put(symbol, stockList.getOrDefault(symbol, 0) + 1);
    this.fetchAddInfo(symbol);
    this.fetchAddOneStockRealTime(symbol);
    this.fetchAddOneStockMonthLongDaily(symbol);
    this.fetchAddOneStockYearLongWeekly(symbol);
    if (initRun == true) {
      initRun = false;
      this.fetchAddtoDB(stockList);
    }
  }

  /**
   * Use for fetch all stock in stockList and data base periodically 
   * (every 2 seconds for realtime chart, 24 hours for monthly chart 
   * and 1 week for year chart).
   * 
   */
  private void fetchAddtoDB(StockList symbolList) {
    this.fetchAddRealTime();
    this.fetchAddDaily();
    this.fetchAddWeekly();
  }

  /**
   * Use for a user to un-subscribe a stock that he/she watched before
   */
  public void unsubscribeStock(String symbol){
    scheduExec.execute(new Runnable() {
      public void run() {
        if (stockList.get(symbol) == 1) {
          stockList.remove(symbol);
          dao.delAllFromDBDAO(symbol);
        } else {
          stockList.put(symbol, stockList.get(symbol) - 1);
        }
      }
    });
  }

  /**
   * Use to fetch add a new stock's info
   */
  private void fetchAddInfo (String symbol) {
    if (!stockList.contains(symbol) ) {
      StockInfo stkInfo = new StockInfo ();
      scheduExec.execute(new Runnable() {
        public void run() {
          fetchData.getStockInfoFromYahoo(symbol , stkInfo);
          dao.addStockInfo(stkInfo);
        }
      });
    }
  }

  /**
   * Use to fetch add a new stock's realtime quote
   */
  private void fetchAddOneStockRealTime (String symbol) {
    if (!stockList.contains(symbol) ) {
      StockPrice stockPrice = new StockPrice();
      scheduExec.execute(new Runnable() {
        public void run() {
          fetchData.getOneRealTimeDataFromYahoo(symbol , stockPrice);
          dao.addRealTimeData(stockPrice);     
        }
      });
    }
  }

  /**
   * Use to fetch add a new stock's latest daily quote for a month long
   */
  private void fetchAddOneStockMonthLongDaily (String symbol) {
    if (!stockList.contains(symbol) ) {
      List<StockPrice> stockPriceList = new ArrayList<StockPrice> ();
      scheduExec.execute(new Runnable() {
        public void run() {
          fetchData.getMonthLongDailyDataFromYahoo(symbol, stockPriceList);
          for(StockPrice stockPrice : stockPriceList) {       
            dao.addDailyData(stockPrice);
          }
        }
      });
    }
  }

  /**
   * Use to fetch add a new stock's latest weekly quote for a year long
   */
  private void fetchAddOneStockYearLongWeekly(String symbol) {
    List<StockPrice> stockPriceList = new ArrayList<StockPrice> ();
    scheduExec.execute(new Runnable() {
      public void run() {
        fetchData.getYearLongWeeklyDataFromYahoo(symbol, stockPriceList);
        for (StockPrice stockPrice : stockPriceList) {
          dao.addWeeklyData(stockPrice);
        }
      }
    });
  }

  /**
   * Use to fetch add all stock's realtime quote include in stockList 
   * for every 2 seconds.
   */
  private void fetchAddRealTime() {
    List<StockPrice> stockPriceList = new ArrayList<StockPrice> ();
    scheduExec.scheduleAtFixedRate(new Runnable() {
      public void run() {
        fetchData.getRealTimeDataFromYahoo(stockList , stockPriceList);
        for (StockPrice stockPrice : stockPriceList ) {
          dao.addRealTimeData(stockPrice);
        }
      }
    },0, 2000, TimeUnit.MILLISECONDS);
  }

  /**
   * Use to fetch add all stock's daily quote include in stockList 
   * for every 24 hours.
   */
  private void fetchAddDaily (){
    List<StockPrice> stockPriceList;
    stockPriceList = new ArrayList<StockPrice> ();
    scheduExec.scheduleAtFixedRate(new Runnable() {
      public void run() {
        fetchData.updateDailyDataFromYahoo(stockList , stockPriceList);
        for (StockPrice stockPrice : stockPriceList ) {
          dao.addDailyData(stockPrice);
        }
      }
    },0, 1000 * 60 * 60 * 24, TimeUnit.MILLISECONDS);
  }

  /**
   * Use to fetch add all stock's weekly quote include in stockList and
   * for every week.
   */
  private void fetchAddWeekly (){
    List<StockPrice> stockPriceList;
    stockPriceList = new ArrayList<StockPrice> ();
    scheduExec.scheduleAtFixedRate(new Runnable() {
      public void run() {
        fetchData.updateWeeklyDataFromYahoo(stockList , stockPriceList);
        for (StockPrice stockPrice : stockPriceList ) {
          dao.addWeeklyData(stockPrice);
        }
      }
    },0,1000 * 60 * 60 * 24 * 7,TimeUnit.MILLISECONDS);
  }
}
