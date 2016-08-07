package com.logicmonitor.msp.YahooFetcher;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.logicmonitor.msp.domain.StockInfo;
import com.logicmonitor.msp.domain.StockList;
import com.logicmonitor.msp.domain.StockPrice;
import com.logicmonitor.msp.service.TimeValidation;
import com.mysql.jdbc.StringUtils;

import yahoofinance.Stock;

import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;
import yahoofinance.quotes.stock.StockQuote;
/**
 * DataFetcher class to fetch real time or history data from YahooAPI
 */
public class DataFetcher {
	TimeValidation timeValid = new TimeValidation();
	// get one stock's static information from YahooAPI 
	public void getStockInfoFromYahoo(String symbol, StockInfo stockInfo) { 
		if(StringUtils.isNullOrEmpty(symbol)) return;
		Stock yStock = null;
		try {
			yStock = YahooFinance.get(symbol);
		} catch (YahooFetchException e) {
			e.printStackTrace();
		}
		stockInfo.setSymbol(yStock.getSymbol());
		stockInfo.setName( yStock.getName());
		stockInfo.setCurrency(yStock.getCurrency());
		stockInfo.setStockExchange(yStock.getStockExchange());
	}
	
	// get one stock's month long daily stock price information from YahooAPI 
	public void getMonthLongDailyDataFromYahoo(String symbol, List<StockPrice> myStockList) {  
		if(symbol == null) return;
		Stock s = null;
		try {
			s  = YahooFinance.get(symbol);
		} catch (YahooFetchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Calendar from = Calendar.getInstance();
		Calendar to = Calendar.getInstance();
		from.add(Calendar.MONTH, -1); // from 1 years ago

		List<HistoricalQuote> yearhistory = s.getHistory(from, to, Interval.DAILY);
		for(HistoricalQuote hq: yearhistory) {
			StockPrice ms = new StockPrice();
			ms.setSymbol(s.getSymbol());
			ms.setPrice(s.getQuote().getPrice());
			ms.setHigh(hq.getHigh());
			ms.setLow(hq.getLow());
			ms.setOpen(hq.getOpen());
			ms.setClose(hq.getClose());
			ms.setVolume(hq.getVolume());
			Date date = hq.getDate().getTime();
			Timestamp timestamp = new Timestamp(date.getTime());
			ms.setTimestamp(timestamp);
			myStockList.add(ms);
		}
	}

	// get one stock's year long weekly stock price information from YahooAPI 
	public void getYearLongWeeklyDataFromYahoo(String symbol, List<StockPrice> myStockList) {  
		if(symbol == null) return;
		Stock s = null;
		try {
			s  = YahooFinance.get(symbol);
		} catch (YahooFetchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Calendar from = Calendar.getInstance();
		Calendar to = Calendar.getInstance();
		from.add(Calendar.YEAR, -1); // from 1 years ago
		List<HistoricalQuote> yearhistory = s.getHistory(from, to, Interval.WEEKLY);
		for(HistoricalQuote hq: yearhistory) {
			StockPrice ms = new StockPrice();
			ms.setSymbol(s.getSymbol());
			ms.setPrice(s.getQuote().getPrice());
			ms.setHigh(hq.getHigh());
			ms.setLow(hq.getLow());
			ms.setOpen(hq.getOpen());
			ms.setClose(hq.getClose());
			ms.setVolume(hq.getVolume());
			Date date = hq.getDate().getTime();
			Timestamp timestamp = new Timestamp(date.getTime());
			ms.setTimestamp(timestamp);
			myStockList.add(ms);
		}
	}

	// update all stock's year long weekly stock price information from YahooAPI 
	public void updateWeeklyDataFromYahoo(StockList symbols, List<StockPrice> myStockList) {
		if(symbols.size() == 0) return;
		String[] symbolsArr = symbols.toArray(new String[symbols.size()]);
		Map<String, Stock> yStocks = null;
		try {
			yStocks  = YahooFinance.get(symbolsArr);
		} catch (YahooFetchException e) {
			e.printStackTrace();
		}
		Calendar from = Calendar.getInstance();
		Calendar to = Calendar.getInstance();
		from.add(Calendar.DATE, -7); // from 1 week ago
		for(Stock s: yStocks.values()) {
			List<HistoricalQuote> yearhistory = s.getHistory(from, to, Interval.WEEKLY);
			for(HistoricalQuote hq: yearhistory) {
				StockPrice ms = new StockPrice();
				ms.setSymbol(s.getSymbol());
				ms.setPrice(s.getQuote().getPrice());
				ms.setHigh(hq.getHigh());
				ms.setLow(hq.getLow());
				ms.setOpen(hq.getOpen());
				ms.setClose(hq.getClose());
				ms.setVolume(hq.getVolume());

				Date date = hq.getDate().getTime();
				Timestamp timestamp = new Timestamp(date.getTime());
				ms.setTimestamp(timestamp);
				myStockList.add(ms);
			}
		}
	}

	// update all stock's month long daily stock price information from YahooAPI 
	public void updateDailyDataFromYahoo(StockList symbols, List<StockPrice> myStockList) {
		if(symbols.size() == 0) return;
		String[] symbolsArr = symbols.toArray(new String[symbols.size()]);
		Map<String, Stock> yStocks = null;
		try {
			yStocks  = YahooFinance.get(symbolsArr);
		} catch (YahooFetchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Calendar from = Calendar.getInstance();
		Calendar to = Calendar.getInstance();
		from.add(Calendar.DATE, -1); // from 1 day ago

		for(Stock s: yStocks.values()) {
			List<HistoricalQuote> yearhistory = s.getHistory(from, to, Interval.DAILY);
			for(HistoricalQuote hq: yearhistory) {
				StockPrice ms = new StockPrice();
				ms.setSymbol(s.getSymbol());
				ms.setPrice(s.getQuote().getPrice());
				ms.setHigh(hq.getHigh());
				ms.setLow(hq.getLow());
				ms.setOpen(hq.getOpen());
				ms.setClose(hq.getClose());
				ms.setVolume(hq.getVolume());

				Date date = hq.getDate().getTime();
				Timestamp timestamp = new Timestamp(date.getTime());
				ms.setTimestamp(timestamp);
				myStockList.add(ms);
			}
		}
	}

	// get all stocks' real time stock price information from YahooAPI 
	public void getRealTimeDataFromYahoo(StockList symbols, List<StockPrice> myStockList ) {  
		if(!timeValid.isValidate()) return;
		if(symbols.size() == 0) return;
		Map<String, Stock> yStocks = null;
		try {
			String[] symbolsArr = symbols.toArray(new String[symbols.size()]);
			yStocks = YahooFinance.get(symbolsArr);
		} catch (YahooFetchException e) {
			e.printStackTrace();
		}
		for(Stock s: yStocks.values()) {
			StockPrice ms = new StockPrice();
			StockQuote sq = s.getQuote();
			ms.setSymbol(s.getSymbol());
			ms.setPrice(sq.getPrice());
			ms.setHigh(sq.getDayHigh());
			ms.setLow(sq.getDayLow());
			ms.setOpen(sq.getOpen());
			ms.setClose(sq.getPreviousClose());
			ms.setVolume(sq.getVolume());
			ms.setPe(s.getStats().getPe());
			ms.setEps(s.getStats().getEps());
			ms.setPeg(s.getStats().getPeg());
			ms.setTimestamp(new Timestamp(sq.getLastTradeTime().getTimeInMillis()));
			myStockList.add(ms);
		}
	}

	// get one stock's real time stock price information from YahooAPI 
	public void getOneRealTimeDataFromYahoo(String symbol, StockPrice stockPrice) {  
		if(!timeValid.isValidate()) {
			System.out.println("market is closed");
			return;
		}
		if(StringUtils.isNullOrEmpty(symbol)) return;
		Stock s = null;
		try {
			s = YahooFinance.get(symbol);
		} catch (YahooFetchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		StockQuote sq = s.getQuote();
		stockPrice.setSymbol(s.getSymbol());
		stockPrice.setPrice(sq.getPrice());
		stockPrice.setHigh(sq.getDayHigh());
		stockPrice.setLow(sq.getDayLow());
		stockPrice.setOpen(sq.getOpen());
		stockPrice.setClose(sq.getPreviousClose());
		stockPrice.setVolume(sq.getVolume());
		stockPrice.setPe(s.getStats().getPe());
		stockPrice.setEps(s.getStats().getEps());
		stockPrice.setPeg(s.getStats().getPeg());
		stockPrice.setTimestamp(new Timestamp(sq.getLastTradeTime().getTimeInMillis()));
	}
}
