package com.logicmonitor.msp.dao;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.LinkedList;
import java.util.logging.Logger;

import javax.sql.DataSource;

public class MyDataSource implements DataSource{
	private static String url = "jdbc:mysql://localhost:8889/data?allowMultiQueries=true";
	private static String user = "root";
	private static String password = "root";
//	private static String url = "jdbc:mysql://us-cdbr-iron-east-03.cleardb.net/heroku_6073056c4088c94?reconnect=true&allowMultiQueries=true";
//	private static String user = "bc98a7f3cc2573";
//	private static String password = "58ed6fcf";

	private static int initCount = 2;
	private static int maxCount = 10;
	int currentCount = 0;

//	LinkedList<Connection> connectionsPool = new LinkedList<Connection>();
	LinkedList<MyConnection> connectionsPool = new LinkedList<MyConnection>();

	public MyDataSource() {
		try {
			for (int i = 0; i < initCount; i++) {
//				this.connectionsPool.addLast(this.createConnection());
				this.connectionsPool.addLast((MyConnection)this.createConnection());
				this.currentCount++;
			}
		} catch (SQLException e) {
			throw new ExceptionInInitializerError(e);
		}
	}

	public MyConnection getConnection() throws SQLException {
		synchronized (connectionsPool) {
//			System.out.println("current connection count is"+ currentCount);
			if (this.connectionsPool.size() > 0) {
				return this.connectionsPool.removeFirst();
			}
			if (this.currentCount < maxCount) {
				this.currentCount++;
				return this.createConnection();
			}
			throw new SQLException("no connection available");
		}
	}

	public void free(Connection conn) {
		this.connectionsPool.addLast((MyConnection)conn);
//		if (this.currentUserCount < this.maxUseCount)
//			connectionsPool.addLast(conn);
//		else {
//			this.realConnection.close();
//			this.dataSource.currentCount--;
//		}
	}

	private MyConnection createConnection() throws SQLException {
		Connection realConn = DriverManager.getConnection(url, user, password);
		MyConnection myConnection = new MyConnection(realConn, this);
		return myConnection;
//		return realConn;
	}

	public Connection getConnection(String username, String password)
			throws SQLException {
		return null;
	}

	public PrintWriter getLogWriter() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public int getLoginTimeout() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setLogWriter(PrintWriter out) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void setLoginTimeout(int seconds) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
}
