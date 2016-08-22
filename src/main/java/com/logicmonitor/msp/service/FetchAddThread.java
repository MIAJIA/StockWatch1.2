package com.logicmonitor.msp.service;

import com.logicmonitor.msp.YahooFetcher.DataFetcher;
import com.logicmonitor.msp.dao.StockDao;

public abstract class FetchAddThread implements Runnable{
    private Thread t;
    private String threadName;
    DataFetcher fetchData;
    StockDao dao;
    
    public FetchAddThread (DataFetcher fetchData, StockDao dao) {
        this.fetchData = fetchData;
        this.dao = dao;
//        stockPriceList = new ArrayList<StockPrice>();
    }
    
    public abstract void run();
    
    public void start ()
    {
        if (t == null)
        {
            t = new Thread (this, getThreadName());
            t.start ();
        }
    }

    public String getThreadName() {
      return threadName;
    }

    public void setThreadName(String threadName) {
      this.threadName = threadName;
    }

}
