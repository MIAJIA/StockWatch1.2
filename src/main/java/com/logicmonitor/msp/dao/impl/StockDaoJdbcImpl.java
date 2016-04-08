package com.logicmonitor.msp.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;


import com.logicmonitor.msp.dao.MyConnection;
import com.logicmonitor.msp.dao.DaoException;
import com.logicmonitor.msp.dao.JdbcUtils;
import com.logicmonitor.msp.dao.StockDao;
import com.logicmonitor.msp.domain.StockInfo;
import com.logicmonitor.msp.domain.StockPrice;
/**
 * StockDao implementation class StockDaoJdbcImpl
 */
public class StockDaoJdbcImpl implements StockDao {
	private MyConnection conn = null;
	PreparedStatement ps = null;
	Statement st = null;
	private ResultSet rs = null;


	
	public synchronized void addStockInfo(StockInfo info){
		try {
			conn = JdbcUtils.getConnection();
//			conn = JdbcUtilsSingleton.getInstance().getConnection();
			String sql = "insert into stock_info(symbol,stock_name,currency,stock_exchange) values (?,?,?,?) ";
			ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, info.getSymbol());
			ps.setString(2, info.getName());
			ps.setString(3, info.getCurrency());
			ps.setString(4, info.getStockExchange());
			ps.executeUpdate();
			rs = ps.getGeneratedKeys();
		} catch (SQLException e) {
			throw new DaoException(e.getMessage(), e);
		} finally {
			JdbcUtils.free(rs, ps, conn);
//			JdbcUtilsSingleton.getInstance().free(rs, ps, conn);
		}
	}

	
	public synchronized void addRealTimeData(StockPrice realTimeStock) {
		
		try {
			conn = JdbcUtils.getConnection();
//			conn = JdbcUtilsSingleton.getInstance().getConnection();
			String sql = "insert into realtime_stock_data(symbol,price,high,low,open,close,timestamp,volume) values (?,?,?,?,?,?,?,?) ";
			ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			mappingPs(ps,realTimeStock);
			ps.executeUpdate();
			rs = ps.getGeneratedKeys();
		} catch (SQLException e) {
			throw new DaoException(e.getMessage(), e);
		} finally {
			JdbcUtils.free(rs, ps, conn);
//			JdbcUtilsSingleton.getInstance().free(rs, ps, conn);
		}
	}
	
	
	public synchronized void addDailyData(StockPrice dailyStock){
		try {
			conn = JdbcUtils.getConnection();			
//			conn = JdbcUtilsSingleton.getInstance().getConnection();
			String sql = "insert into daily_stock_data(symbol,price,high,low,open,close,timestamp, volume) values (?,?,?,?,?,?,?,?) ";
			ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			mappingPs(ps,dailyStock);
			ps.executeUpdate();
			rs = ps.getGeneratedKeys();
		} catch (SQLException e) {
			throw new DaoException(e.getMessage(), e);
		} finally {
			JdbcUtils.free(rs, ps, conn);
//			JdbcUtilsSingleton.getInstance().free(rs, ps, conn);
		}
	}
	
	
	public synchronized void addWeeklyData(StockPrice weeklyStock){
		try {
			conn = JdbcUtils.getConnection();
//			conn = JdbcUtilsSingleton.getInstance().getConnection();
			String sql = "insert into weekly_stock_data(symbol,price,high,low,open,close,timestamp,volume) values (?,?,?,?,?,?,?,?) ";
			ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			mappingPs(ps,weeklyStock);
			ps.executeUpdate();
			rs = ps.getGeneratedKeys();
		} catch (SQLException e) {
			throw new DaoException(e.getMessage(), e);
		} finally {
			JdbcUtils.free(rs, ps, conn);
//			JdbcUtilsSingleton.getInstance().free(rs, ps, conn);
		}
	}
	
	
	private void mappingPs(PreparedStatement ps, StockPrice stock) throws SQLException {
		ps.setString(1, stock.getSymbol());
		ps.setBigDecimal(2, stock.getPrice());
		ps.setBigDecimal(3, stock.getHigh());
		ps.setBigDecimal(4, stock.getLow());
		ps.setBigDecimal(5, stock.getOpen());
		ps.setBigDecimal(6, stock.getClose());
		ps.setTimestamp(7, stock.getTimestamp());
		ps.setLong(8, stock.getVolume());
	}

	
	public List<StockPrice> getRealTimeData(String symbol) {
		List<StockPrice> realTimePriceList = new ArrayList<StockPrice>();
		StockPrice sp = null;
		try {
			conn = JdbcUtils.getConnection();
//			conn = JdbcUtilsSingleton.getInstance().getConnection();
			String sql = "select symbol,price,high,low,open,close,timestamp, volume from realtime_stock_data where symbol=?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, symbol);
			rs = ps.executeQuery();
			for(int i = 0; i < 100 && rs.next(); i++) {
				//			while(rs.next()){
				sp = new StockPrice();
				mappingStockPrice(rs, sp);
				realTimePriceList.add(sp);
			}
		} catch (SQLException e) {
			throw new DaoException(e.getMessage(), e);
		} finally {
//			JdbcUtilsSingleton.getInstance().free(rs, ps, conn);
			JdbcUtils.free(rs, ps, conn);
		}
		return realTimePriceList;
	}

	
	public List<StockPrice> getDailyData(String symbol) {
		List<StockPrice> dailyPriceList = new ArrayList<StockPrice>();
		StockPrice sp = null;
		try {
			conn = JdbcUtils.getConnection();
//			conn = JdbcUtilsSingleton.getInstance().getConnection();
			String sql = "select symbol,price,high,low,open,close,timestamp,volume from daily_stock_data where symbol=?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, symbol);
			rs = ps.executeQuery();
			//			while(rs.next()){
			for(int i = 0; i < 30 && rs.next(); i++) {
				sp = new StockPrice();
				mappingStockPrice(rs, sp);
				dailyPriceList.add(sp);
			}
		} catch (SQLException e) {
			throw new DaoException(e.getMessage(), e);
		} finally {
			JdbcUtils.free(rs, ps, conn);
//			JdbcUtilsSingleton.getInstance().free(rs, ps, conn);
		}
		return dailyPriceList;

	}
	
	
	public List<StockPrice> getWeeklyData(String symbol) {
		List<StockPrice> weeklyPriceList = new ArrayList<StockPrice>();
		StockPrice sp = null;
		try {
			conn = JdbcUtils.getConnection();
			System.out.println("get the connection!");
			System.out.println(conn);

//			conn = JdbcUtilsSingleton.getInstance().getConnection();
			String sql = "select symbol,price,high,low,open,close,timestamp,volume from weekly_stock_data where symbol=?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, symbol);
			rs = ps.executeQuery();
			for(int i = 0; i < 52 && rs.next(); i++) {
				sp = new StockPrice();
				mappingStockPrice(rs,sp);
				weeklyPriceList.add(sp);
			}
		} catch (SQLException e) {
			throw new DaoException(e.getMessage(), e);
		} finally {
			JdbcUtils.free(rs, ps, conn);
//			JdbcUtilsSingleton.getInstance().free(rs, ps, conn);
		}
		return weeklyPriceList;
	}


	public StockInfo getOneInfoFromDBDAO(String symbol){
		StockInfo stockInfo = null;
		List<StockInfo> stockInfoList = new ArrayList<StockInfo>();
		try {
			conn = JdbcUtils.getConnection();
//			conn = JdbcUtilsSingleton.getInstance().getConnection();
			String sql = "select symbol, stock_name,currency,stock_exchange from stock_info where symbol = ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, symbol);
			rs = ps.executeQuery();
			//			mappingStockInfo(rs, stockInfo);
			while(rs.next()){
				stockInfo = new StockInfo();
				mappingStockInfo(rs, stockInfo);
				stockInfoList.add(stockInfo);
			}
		} catch (SQLException e) {
			throw new DaoException(e.getMessage(), e);
		} finally {
			JdbcUtils.free(rs, ps, conn);
//			JdbcUtilsSingleton.getInstance().free(rs, ps, conn);
		}
		return stockInfoList.get(0);
	}
	
	
	public List<StockInfo> getAllInfoFromDBDAO() { 
		StockInfo stockInfo = null;
		List<StockInfo> stockInfoList = new ArrayList<StockInfo>();
		String sql = "select symbol,stock_name,currency,stock_exchange from stock_info";
		try{
			conn = JdbcUtils.getConnection();
//			conn = JdbcUtilsSingleton.getInstance().getConnection();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()){
				stockInfo = new StockInfo();
				mappingStockInfo(rs, stockInfo);
				stockInfoList.add(stockInfo);
			}
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			JdbcUtils.free(rs, ps, conn);
//			JdbcUtilsSingleton.getInstance().free(rs, ps, conn);
		}
		return stockInfoList;
	}
	
	
//	public List<StockInfo> getSelectInfoFromDBDAO(List<String> symbolList) {
//		StockInfo stockInfo = null;
//		Map<String, StockInfo> stockInfoMap = new HashMap<String, StockInfo>();
//		String sql = "select symbol,stock_name,currency,stock_exchange from stock_info";
//		try{
//			conn = JdbcUtilsSingleton.getInstance().getConnection();
//			ps = conn.prepareStatement(sql);
//			rs = ps.executeQuery();
//			while(rs.next()){
//				stockInfo = new StockInfo();
//				mappingStockInfo(rs, stockInfo);
//				stockInfoMap.put(stockInfo.getSymbol(), stockInfo);
//			}
//		}catch(SQLException e){
//			e.printStackTrace();
//		}finally{
//			JdbcUtilsSingleton.getInstance().free(rs, ps, conn);
//		}
//		for(String symbol: symbolList) {
//			if(stockInfoMap.containsKey(symbol)) {
//				stockInfo = new StockInfo();
//			}
//		}
//		return new ArrayList<StockInfo>(stockInfoMap.values());
//	}

	
 	public  void delRealTimeData(String symbol) {
		try {
			conn = JdbcUtils.getConnection();
//			conn = JdbcUtilsSingleton.getInstance().getConnection();
			String multiQuerySqlString = "SET SQL_SAFE_UPDATES = 0;";
			multiQuerySqlString += "delete from realtime_stock_data where symbol='" + symbol + "' AND timestamp < (NOW() - INTERVAL 7 DAY);";
			multiQuerySqlString += "SET SQL_SAFE_UPDATES = 1;";
			ps = conn.prepareStatement(multiQuerySqlString);
			ps.executeUpdate(multiQuerySqlString);
		} catch (SQLException e) {
			throw new DaoException(e.getMessage(), e);
		} finally {
			JdbcUtils.free(rs, ps, conn);
//			JdbcUtilsSingleton.getInstance().free(rs, ps, conn);
		}
	}
 	
 	
	public  void delInfolFromDBDAO(String symbol){
		try {
			conn = JdbcUtils.getConnection();
//			conn = JdbcUtilsSingleton.getInstance().getConnection();
			String multiQuerySqlString = "SET SQL_SAFE_UPDATES = 0;";
			multiQuerySqlString += "delete from stock_info where symbol=" + symbol + ";";
			multiQuerySqlString += "SET SQL_SAFE_UPDATES = 1;";
			ps = conn.prepareStatement(multiQuerySqlString);
			ps.executeUpdate(multiQuerySqlString);
		} catch (SQLException e) {
			throw new DaoException(e.getMessage(), e);
		} finally {
			JdbcUtils.free(rs, ps, conn);
//			JdbcUtilsSingleton.getInstance().free(rs, ps, conn);
		}

	}

	
 	public  void delDailyData(String symbol) {
		try {
			conn = JdbcUtils.getConnection();
//			conn = JdbcUtilsSingleton.getInstance().getConnection();
			String multiQuerySqlString = "SET SQL_SAFE_UPDATES = 0;";
			multiQuerySqlString += "delete from daily_stock_data where symbol='" + symbol + "' AND timestamp < (NOW() - INTERVAL 30 DAY);";
			multiQuerySqlString += "SET SQL_SAFE_UPDATES = 1;";		
			ps = conn.prepareStatement(multiQuerySqlString);
			ps.executeUpdate(multiQuerySqlString);
		} catch (SQLException e) {
			throw new DaoException(e.getMessage(), e);
		} finally {
			JdbcUtils.free(rs, ps, conn);
//			JdbcUtilsSingleton.getInstance().free(rs, ps, conn);
		}
	}


 	public  void delWeeklyData(String symbol) {
		try {
			conn = JdbcUtils.getConnection();
//			conn = JdbcUtilsSingleton.getInstance().getConnection();
			String multiQuerySqlString = "SET SQL_SAFE_UPDATES = 0;";
			multiQuerySqlString += "delete from weekly_stock_data where symbol='" + symbol + "' AND timestamp < (NOW() - INTERVAL 52 WEEK);";
			multiQuerySqlString += "SET SQL_SAFE_UPDATES = 1;";		
			ps = conn.prepareStatement(multiQuerySqlString);
			ps.executeUpdate(multiQuerySqlString);
		} catch (SQLException e) {
			throw new DaoException(e.getMessage(), e);
		} finally {
			JdbcUtils.free(rs, ps, conn);
//			JdbcUtilsSingleton.getInstance().free(rs, ps, conn);
		}
	}

 	
	public  void delAllFromDBDAO(String symbol) {
		try {
			conn = JdbcUtils.getConnection();
//			conn = JdbcUtilsSingleton.getInstance().getConnection();
			String multiQuerySqlString = "SET SQL_SAFE_UPDATES = 0;";
			multiQuerySqlString += "delete from stock_info where symbol ='" + symbol + "';";
			multiQuerySqlString += "delete from realtime_stock_data where symbol='" + symbol + "';";
			multiQuerySqlString += "delete from daily_stock_data where symbol='" + symbol + "';";
			multiQuerySqlString += "delete from weekly_stock_data where symbol='" + symbol+ "';";
			multiQuerySqlString += "SET SQL_SAFE_UPDATES = 1;";		
			ps = conn.prepareStatement(multiQuerySqlString);
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new DaoException(e.getMessage(), e);
		} finally {
			JdbcUtils.free(rs, ps, conn);
//			JdbcUtilsSingleton.getInstance().free(rs, ps, conn);
		}

	}
	
	
	private void mappingStockPrice(ResultSet rs2, StockPrice stockPrice) throws SQLException{
		stockPrice.setSymbol(rs.getString("symbol"));
		stockPrice.setPrice(rs.getBigDecimal("price"));
		stockPrice.setHigh(rs.getBigDecimal("high"));
		stockPrice.setLow(rs.getBigDecimal("low"));
		stockPrice.setOpen(rs.getBigDecimal("open"));
		stockPrice.setClose(rs.getBigDecimal("close"));
		stockPrice.setVolume(rs.getLong("volume"));
		stockPrice.setTimestamp(rs.getTimestamp("timestamp"));
	}

	
	private void mappingStockInfo(ResultSet rs, StockInfo stockInfo) throws SQLException {
		stockInfo.setSymbol(rs.getString("symbol"));
		stockInfo.setName(rs.getString("stock_name"));
		stockInfo.setCurrency(rs.getString("currency"));
		stockInfo.setStockExchange(rs.getString("stock_exchange"));
	}
	public static void main(String[] argus) {
		StockDaoJdbcImpl sd = new StockDaoJdbcImpl();

//		long start = System.currentTimeMillis();
		for (int i = 0; i < 100; i++){
			sd.getRealTimeData("GOOG");
		}
//		long end = System.currentTimeMillis();
//				System.out.println("old version"+ (end - start));
	}

}

