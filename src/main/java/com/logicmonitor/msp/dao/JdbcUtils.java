package com.logicmonitor.msp.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;

public final class JdbcUtils {
	//	private static String url = "jdbc:mysql://localhost:8889/data?allowMultiQueries=true";
	//	private static String user = "root";
	//	private static String password = "123";
	private static MyDataSource myDataSource;

	private JdbcUtils() {
	}

	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			myDataSource = new MyDataSource();
		} catch (Exception e) {
			throw new ExceptionInInitializerError(e);
		}
	}

	//	public static DataSource getDataSource() {
	//		return myDataSource.getConnection();
	//	}

	public static MyConnection getConnection() throws SQLException {
		//		 return DriverManager.getConnection(url, user, password);
//		System.out.println("JUtils get the connection!");
		return myDataSource.getConnection();
	}

	public static void free(ResultSet rs, PreparedStatement st, MyConnection conn) {
		try {
			if (rs != null)
				rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (st != null)
					st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				if (conn != null)
					try {
//						System.out.println("JUtils mydatasource free!");
						conn.close();
//												myDataSource.free(conn);
					} catch (Exception e) {
						e.printStackTrace();
					}
			}
		}
	}
}

