package com.logicmonitor.msp.YahooFetcher;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.logicmonitor.msp.dao.impl.StockDaoJdbcImpl;
import com.logicmonitor.msp.domain.StockInfo;
import com.logicmonitor.msp.domain.StockPrice;


@RunWith(PowerMockRunner.class)
@PrepareForTest({DataFetcher.class})
public class DataFetcherTest {

	private DataFetcher dataFetcher = null;


	private String stock1_symbol = "GOOG";
	private String stock1_name = "Alphabet Inc.";
	private String stock1_currency = "USD";

	private String stock2_symbol = "VRNG";
	private String stock2_name = "Vringo, Inc.";
	private String stock2_currency = "USD";

	@Before
	public void beforeTest() throws Exception {
		dataFetcher = new DataFetcher();
	}

	@Test
	public void test_IBM_getYear() {
		List<String> stock_symbols = new ArrayList<String>();
		List<StockPrice> ibmStockList = new ArrayList<StockPrice>();
		dataFetcher.getMonthLongDailyDataFromYahoo("IBM", ibmStockList);

		assertEquals(2, ibmStockList.size());
		assertTrue(ibmStockList.get(0) instanceof StockPrice);
	}
	
	@Test
	public void test_GetStockInfoFromYahoo_Should_Return_Values() {
		StockInfo stockInfo = null;
		stockInfo = new StockInfo();
		dataFetcher.getStockInfoFromYahoo(stock1_symbol, stockInfo);

		assertEquals(stock1_symbol, stockInfo.getSymbol());
		assertEquals(stock1_name, stockInfo.getName());
		assertEquals(stock1_currency, stockInfo.getCurrency());
		assertNotNull(stockInfo.getStockExchange());

		stockInfo = new StockInfo();
		dataFetcher.getStockInfoFromYahoo(stock2_symbol, stockInfo);

		assertEquals(stock2_symbol, stockInfo.getSymbol());
		assertEquals(stock2_name, stockInfo.getName());
		assertEquals(stock2_currency, stockInfo.getCurrency());
		assertNotNull(stockInfo.getStockExchange());
	}

	@Test
	public void test_GetRealTimeDataFromYahoo_Should_Return_Stock_List() {
		List<String> stock_symbols = new ArrayList<String>();
		stock_symbols.add(stock1_symbol);
		stock_symbols.add(stock2_symbol);
		List<StockPrice> myStockList = new ArrayList<StockPrice>();
		dataFetcher.getRealTimeDataFromYahoo(stock_symbols, myStockList);

		assertEquals(2, myStockList.size());
		assertTrue(myStockList.get(0) instanceof StockPrice);
		assertTrue(myStockList.get(1) instanceof StockPrice);
		assertEquals(stock1_symbol, myStockList.get((0)).getSymbol());
		assertEquals(stock2_symbol, myStockList.get((1)).getSymbol());
	}

	@Test
	public void test_GetRealTimeDataFromYahoo_Should_Return_Null() {
		List<String> stock_symbols = new ArrayList<String>();
		List<StockPrice> myStockList = new ArrayList<StockPrice>();
		dataFetcher.getRealTimeDataFromYahoo(stock_symbols, myStockList);

		assertEquals(0, myStockList.size());
}
   @Test
    public void test_GetOneRealTimeDataFromYahoo_Should_Return_Stock_List() {
        StockPrice myStockPrice = new StockPrice();
        dataFetcher.getOneRealTimeDataFromYahoo(stock1_symbol, myStockPrice);

        assertEquals(stock1_symbol, myStockPrice.getSymbol());
    }

    @Test
    public void test_GetOneRealTimeDataFromYahoo_Should_Return_Null() {
        StockPrice myStockPrice = new StockPrice();
        dataFetcher.getOneRealTimeDataFromYahoo("", myStockPrice);

        assertEquals("", myStockPrice.getSymbol());
    }

	@Test
	public void test_GetDailyDataFromYahoo_Should_Return_Stock_List() {
		List<String> stock_symbols = new ArrayList<String>();
		stock_symbols.add(stock1_symbol);
		stock_symbols.add(stock2_symbol);
		List<StockPrice> myStockList = new ArrayList<StockPrice>();
		dataFetcher.getRealTimeDataFromYahoo(stock_symbols, myStockList);

		assertEquals(2, myStockList.size());
		assertTrue(myStockList.get(0) instanceof StockPrice);
		assertTrue(myStockList.get(1) instanceof StockPrice);
		assertEquals(stock1_symbol, myStockList.get((0)).getSymbol());
		assertEquals(stock2_symbol, myStockList.get((1)).getSymbol());
	}

	@Test
	public void test_GetDailyDataFromYahoo_Should_Return_Null() {
		List<String> stock_symbols = new ArrayList<String>();
		List<StockPrice> myStockList = new ArrayList<StockPrice>();
		dataFetcher.getRealTimeDataFromYahoo(stock_symbols, myStockList);

		assertEquals(0, myStockList.size());
	}

	@Test
	public void test_GetWeeklyDataFromYahoo_Should_Return_Stock_List() {
		List<String> stock_symbols = new ArrayList<String>();
		stock_symbols.add(stock1_symbol);
		stock_symbols.add(stock2_symbol);
		List<StockPrice> myStockList = new ArrayList<StockPrice>();
		dataFetcher.getRealTimeDataFromYahoo(stock_symbols, myStockList);

		assertEquals(2, myStockList.size());
		assertTrue(myStockList.get(0) instanceof StockPrice);
		assertTrue(myStockList.get(1) instanceof StockPrice);
		assertEquals(stock1_symbol, myStockList.get((0)).getSymbol());
		assertEquals(stock2_symbol, myStockList.get((1)).getSymbol());
	}

	@Test
	public void test_GetWeeklyDataFromYahoo_Should_Return_Null() {
		List<String> stock_symbols = new ArrayList<String>();
		List<StockPrice> myStockList = new ArrayList<StockPrice>();
		dataFetcher.getRealTimeDataFromYahoo(stock_symbols, myStockList);

		assertEquals(0, myStockList.size());
	}
	@Test
	public void test_updateWeeklyDataFromYahoo() {
	    List<String> stock_symbols = new ArrayList<String>();
	    stock_symbols.add(stock1_symbol);
	    stock_symbols.add(stock2_symbol);
	    List<StockPrice> myStockList = new ArrayList<StockPrice>();
	    dataFetcher.updateWeeklyDataFromYahoo(stock_symbols, myStockList);

	    assertNotEquals(0, myStockList.size());

	}
	
	@Test
	public void test_updateDailyDataFromYahoo() {
	    List<String> stock_symbols = new ArrayList<String>();
	    stock_symbols.add(stock1_symbol);
	    stock_symbols.add(stock2_symbol);
	    List<StockPrice> myStockList = new ArrayList<StockPrice>();
	    dataFetcher.updateDailyDataFromYahoo(stock_symbols, myStockList);

	    assertNotEquals(0, myStockList.size());
	}

}
