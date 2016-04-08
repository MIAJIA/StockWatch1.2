package com.logicmonitor.msp.dao.impl;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.logicmonitor.msp.YahooFetcher.DataFetcher;
import com.logicmonitor.msp.dao.StockDao;
import com.logicmonitor.msp.domain.StockInfo;
import com.logicmonitor.msp.domain.StockPrice;

@RunWith(PowerMockRunner.class)
@PrepareForTest({StockDao.class})
public class DaoTest {

	private StockDao stockDao = null;
	@Before
	public void beforeTest() throws Exception {
		stockDao = new StockDaoJdbcImpl();		
	}
	@Test
	public void test_addStockInfo_getAllInfoFromDBDAO_getOneInfoFromDBDAO() {
		StockInfo si = new StockInfo();
		si.setCurrency("USD");
		si.setName("google");
		si.setStockExchange("NSQ");
		si.setSymbol("GOOG");
		stockDao.addStockInfo(si);

		StockInfo si2 = new StockInfo();
		si2.setCurrency("USD");
		si2.setName("intel");
		si2.setStockExchange("NSQ");
		si2.setSymbol("INSQ");
		stockDao.addStockInfo(si2);

		List<StockInfo> StockInfoList = stockDao.getAllInfoFromDBDAO();
		Assert.assertEquals(2, StockInfoList.size());
		
		StockInfo sitest = StockInfoList.get(0);
        Assert.assertEquals("google", sitest.getName());
        Assert.assertEquals("USD", sitest.getCurrency());
        Assert.assertEquals("NSQ", sitest.getStockExchange());
        Assert.assertEquals("GOOG", sitest.getSymbol());

		StockInfo stockInfoReturn = stockDao.getOneInfoFromDBDAO("INNNNNSQ");
		Assert.assertNull(stockInfoReturn);
		
		stockDao.delAllFromDBDAO("INSQ");
		stockDao.delAllFromDBDAO("GOOG");
		Assert.assertNull(stockDao.getOneInfoFromDBDAO("INSQ"));
		Assert.assertNull(stockDao.getOneInfoFromDBDAO("GOOG"));
	}

	@Test
	public void test_addDailyData_getDailyData_delDailyData() {
		StockPrice sp = new StockPrice();
		sp.setClose(new BigDecimal(112.5));
		sp.setHigh(new BigDecimal(112.5));
		sp.setOpen(new BigDecimal(90));
		sp.setPrice(new BigDecimal(115.53));
		sp.setSymbol("GAIOD");
		
		stockDao.addDailyData(sp);
		List<StockPrice> stockPriceReturn = stockDao.getDailyData("GAIOD");
		List<StockPrice> stockPriceReturnNull = stockDao.getDailyData("NULLTEST");
		
		Assert.assertEquals(0, stockPriceReturnNull.size());
		Assert.assertEquals("GAIOD", stockPriceReturn.get(0).getSymbol());
		Assert.assertTrue(stockPriceReturn.get(0).getClose().equals(new BigDecimal(112.5)));

		stockDao.delDailyData("GAIOD");
		Assert.assertEquals(0, stockDao.getDailyData("GAIOD").size());
	}

	@Test
	public void test_addWeeklyData_getWeeklyData_delWeeklyData() {
		StockPrice sp = new StockPrice();
		sp.setClose(new BigDecimal(112.5));
		sp.setHigh(new BigDecimal(112.5));
		sp.setOpen(new BigDecimal(90));
		sp.setPrice(new BigDecimal(115.53));
		sp.setSymbol("GAIOD");

		stockDao.addWeeklyData(sp);
		List<StockPrice> stockPriceReturn = stockDao.getWeeklyData("GAIOD");
		List<StockPrice> stockPriceReturnNull = stockDao.getWeeklyData("NULLTEST");
		Assert.assertEquals(0, stockPriceReturnNull.size());
		Assert.assertEquals("GAIOD", stockPriceReturn.get(0).getSymbol());
        Assert.assertTrue(stockPriceReturn.get(0).getClose().equals(new BigDecimal(112.5)));
        
		stockDao.delWeeklyData("GAIOD");
		Assert.assertEquals(0, stockDao.getWeeklyData("GAIOD").size());
	}
	
	@Test
	public void test_addRealTimeData_getRealTimeData_delRealTimeData() {
		StockPrice sp = new StockPrice();
		sp.setClose(new BigDecimal(112.5));
		sp.setHigh(new BigDecimal(112.5));
		sp.setOpen(new BigDecimal(90));
		sp.setPrice(new BigDecimal(115.53));
		sp.setSymbol("GAIOD");

		stockDao.addRealTimeData(sp);
		List<StockPrice> stockPriceReturn = stockDao.getRealTimeData("GAIOD");
		List<StockPrice> stockPriceReturnNull = stockDao.getRealTimeData("NULLTEST");
		Assert.assertEquals(0, stockPriceReturnNull.size());
		Assert.assertEquals("GAIOD", stockPriceReturn.get(0).getSymbol());
		Assert.assertTrue(stockPriceReturn.get(0).getClose().equals(new BigDecimal(112.5)));

		stockDao.delRealTimeData("GAIOD");
		Assert.assertEquals(0, stockDao.getRealTimeData("GAIOD").size());
	}

}
