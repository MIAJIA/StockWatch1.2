package com.logicmonitor.msp.service;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.logicmonitor.msp.dao.StockDao;
import com.logicmonitor.msp.dao.impl.StockDaoJdbcImpl;
import com.logicmonitor.msp.domain.StockInfo;
import com.logicmonitor.msp.service.StockService;

public class StockServiceTest {
//	@Test
//	public void testFetchAddtoDB() throws Exception{
//
//		List<String> strList = new ArrayList<String>();
//		strList.add("GOOG");
//		strList.add("AAPL");
//		test.fetchAddtoDB(strList);
//		System.out.println("end function1");
//		test.fetchAddNewtoDB("TSLA");
//		//		strList.add("SPY");
//		//		System.out.println("end function2");
//		// still need add "TSLA" to strList;
//	}
//
//	@Test
//	public void testFetchAddtoDB() throws Exception{
//		List<String> symbolList = new ArrayList<String>();
//		symbolList.add("GOOG");
//		symbolList.add("INSQ");
//		StockService stockService = new StockService();
//		stockService.fetchAddtoDB(symbolList);
//		StockDao stockDao = new StockDaoJdbcImpl();
//		StockInfo stockInfo1 = stockDao.getOneInfoFromDBDAO("GOOG");
//		Assert.assertNotNull(stockInfo1);
//		StockInfo stockInfo2 = stockDao.getOneInfoFromDBDAO("GOOG");
//		Assert.assertNotNull(stockInfo2);
//	}
//
//	@Test
//	public void testFetchAddNewtoDB() {
//		String symbol = "GOOG";
//		StockService stockService = new StockService();
//		stockService.fetchAddNewtoDB(symbol);
//		StockDao stockDao = new StockDaoJdbcImpl();
//		StockInfo stockInfo = stockDao.getOneInfoFromDBDAO("GOOG");
//		Assert.assertNotNull(stockInfo);
//	}

}
