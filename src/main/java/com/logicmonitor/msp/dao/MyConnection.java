package com.logicmonitor.msp.dao;


import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;


public class MyConnection implements Connection {
	private Connection realConnection;
	private MyDataSource dataSource;
	private int maxUseCount = Integer.MAX_VALUE;
	private int currentUserCount = 0;

	MyConnection(Connection connection, MyDataSource dataSource) {
		this.realConnection = connection;
		this.dataSource = dataSource;
	}

	public void clearWarnings() throws SQLException {
		this.realConnection.clearWarnings();
	}

	public void close() throws SQLException {
		this.currentUserCount++;
		if (this.currentUserCount < this.maxUseCount)
			this.dataSource.connectionsPool.addLast(this);
		else {
			this.realConnection.close();
			this.dataSource.currentCount--;
		}
	}

	public void commit() throws SQLException {
		this.realConnection.commit();
	}

	public Statement createStatement() throws SQLException {
		return this.realConnection.createStatement();
	}

	public PreparedStatement prepareStatement(String sql) throws SQLException {
		return this.realConnection.prepareStatement(sql);
	}

	public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys)
			throws SQLException {
		return this.realConnection.prepareStatement(sql, autoGeneratedKeys);
	}

	public PreparedStatement prepareStatement(String sql, int[] columnIndexes)
			throws SQLException {
		return this.realConnection.prepareStatement(sql,columnIndexes);
	}

	public PreparedStatement prepareStatement(String sql, String[] columnNames)
			throws SQLException {
		return this.realConnection.prepareStatement(sql,columnNames);
	}

	public PreparedStatement prepareStatement(String sql, int resultSetType,
			int resultSetConcurrency) throws SQLException {
		return this.realConnection.prepareStatement(sql,resultSetType,resultSetConcurrency);
	}

	public PreparedStatement prepareStatement(String sql, int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		return null;
	}
	
	public Statement createStatement(int resultSetType, int resultSetConcurrency)
			throws SQLException {
		return this.realConnection.createStatement(resultSetType,resultSetConcurrency);
	}

	public Statement createStatement(int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		return this.realConnection.createStatement(resultSetType,resultSetConcurrency, resultSetHoldability);
	}

	public boolean getAutoCommit() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public String getCatalog() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public int getHoldability() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	public DatabaseMetaData getMetaData() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public int getTransactionIsolation() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	public Map<String, Class<?>> getTypeMap() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public SQLWarning getWarnings() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isClosed() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isReadOnly() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public String nativeSQL(String sql) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public CallableStatement prepareCall(String sql) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public CallableStatement prepareCall(String sql, int resultSetType,
			int resultSetConcurrency) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public CallableStatement prepareCall(String sql, int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}


	public void releaseSavepoint(Savepoint savepoint) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void rollback() throws SQLException {
		// TODO Auto-generated method stub

	}

	public void rollback(Savepoint savepoint) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void setAutoCommit(boolean autoCommit) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void setCatalog(String catalog) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void setHoldability(int holdability) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void setReadOnly(boolean readOnly) throws SQLException {
		// TODO Auto-generated method stub

	}

	public Savepoint setSavepoint() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Savepoint setSavepoint(String name) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public void setTransactionIsolation(int level) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
		// TODO Auto-generated method stub

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

	@Override
	public Clob createClob() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Blob createBlob() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NClob createNClob() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SQLXML createSQLXML() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isValid(int timeout) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setClientInfo(String name, String value) throws SQLClientInfoException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setClientInfo(Properties properties) throws SQLClientInfoException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getClientInfo(String name) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Properties getClientInfo() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setSchema(String schema) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getSchema() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void abort(Executor executor) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getNetworkTimeout() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

}