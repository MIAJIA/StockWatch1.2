package com.logicmonitor.msp.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import com.logicmonitor.msp.dao.DaoException;
import com.logicmonitor.msp.dao.JdbcUtils;
import com.logicmonitor.msp.dao.MyConnection;
import com.logicmonitor.msp.dao.UserDao;
import com.logicmonitor.msp.domain.User;

public class UserDaoImpl implements UserDao {

	//  private List<User> users = new ArrayList<>();  
	//  private static int index = 0;  
	private MyConnection conn = null;
	PreparedStatement ps = null;
	Statement st = null;
	private ResultSet rs = null;

	public UserDaoImpl() {}

	public User findUserByAccountAndPsw(String username, String password) {
		try {
//		  System.out.println("findUserByAccountAndPsw");
			conn = JdbcUtils.getConnection();
			
			String sql = "select password from user_login where username=?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, username);
			rs = ps.executeQuery();
			if (rs!=null && rs.next() && rs.getString("password").equals(password)) {
			  System.out.println("rs is not null, password correct");
				User user = new User();
				user.setUsername(username);
				user.setPassword(password);
				return user;
			}
		} catch (SQLException e) {
			throw new DaoException(e.getMessage(), e);
		} finally {
			JdbcUtils.free(rs, ps, conn);
		}
		return null;
	}
//	public String getUserPassword(String username) {
//		String dbPassword = null;
//		try {
//			conn = JdbcUtils.getConnection();
//			String sql = "select password from user_login where username=?";
//			ps = conn.prepareStatement(sql);
//			ps.setString(1, username);
//			rs = ps.executeQuery();
//			while(rs.next()){
//				dbPassword = rs.getString("password");
//			}
//		} catch (SQLException e) {
//			throw new DaoException(e.getMessage(), e);
//		} finally {
//			JdbcUtils.free(rs, ps, conn);
//		}
//		return dbPassword;
//	}
	//  @Override
	//  public User getUser(String username) {
	//    User curUser = null;
	//    try {
	//      conn = JdbcUtils.getConnection();
	//      String sql = "select username,watchlist from XXXXXX where username=?";
	//      ps = conn.prepareStatement(sql);
	//      ps.setString(1, username);
	//      rs = ps.executeQuery();
	//      while(rs.next()){
	//        curUser = new User();
	//        mappingUser(rs, curUser);
	//      }
	//    } catch (SQLException e) {
	//      throw new DaoException(e.getMessage(), e);
	//    } finally {
	//      JdbcUtils.free(rs, ps, conn);
	//    }
	//    return curUser;
	//  }
	//  private void mappingUser(ResultSet rs2, User curUser) {
	//    try {
	//      curUser.setEmail(rs.getString("username"));
	//      curUser.setPassword(rs.getString("password"));
	//    } catch (SQLException e) {
	//      e.printStackTrace();
	//    }
	//    
	//  }
	@Override
	public void getUserWatchList(String username, List<String> watchlist) {
		try {
			conn = JdbcUtils.getConnection();
			String sql = "select symbol from user_watchlist where username=?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, username);
			rs = ps.executeQuery();
			while(rs.next()){
				watchlist.add(rs.getString("symbol"));
			}
		} catch (SQLException e) {
			throw new DaoException(e.getMessage(), e);
		} finally {
			JdbcUtils.free(rs, ps, conn);
		}
	}

	@Override
	public void addOneToWatchList(String username, String symbol) {
		try {
			conn = JdbcUtils.getConnection();
			String sql = "insert into user_watchlist(username,symbol) values (?,?) ";
			ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, username);
			ps.setString(2, symbol);
			ps.executeUpdate();
			rs = ps.getGeneratedKeys();
		} catch (SQLException e) {
			throw new DaoException(e.getMessage(), e);
		} finally {
			JdbcUtils.free(rs, ps, conn);
		}

	}
	@Override
	public void delOneFromWatchList(String username, String symbol) {
		try {
			conn = JdbcUtils.getConnection();
			String multiQuerySqlString = "SET SQL_SAFE_UPDATES = 0;";
			multiQuerySqlString += "delete from user_watchlist where username='" + username +"' AND symbol = '"+symbol+"';";
			multiQuerySqlString += "SET SQL_SAFE_UPDATES = 1;";
			ps = conn.prepareStatement(multiQuerySqlString);
			ps.executeUpdate(multiQuerySqlString);
		} catch (SQLException e) {
			throw new DaoException(e.getMessage(), e);
		} finally {
			JdbcUtils.free(rs, ps, conn);
		}

	}
}
