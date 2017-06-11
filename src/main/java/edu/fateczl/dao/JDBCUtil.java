package edu.fateczl.dao;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCUtil {
	
	/*
	private static String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	private static String JDBC_URL = "jdbc:mysql://localhost/eventor";
	private static String JDBC_USER = "root";
	private static String JDBC_PASSWORD = "";
	private static Driver driver = null;
	*/
	/*
	private static String JDBC_DRIVER = "org.postgresql.Driver";
	private static String JDBC_URL = "postgresql://localhost:5432/eventor";
	private static String JDBC_USER = "root";
	private static String JDBC_PASSWORD = "";
	private static Driver driver = null;
	*/
	
	private static String JDBC_DRIVER = "org.postgresql.Driver";
	private static String JDBC_URL = "postgresql://localhost:5432/eventor";
	private static String JDBC_USER = "root";
	private static String JDBC_PASSWORD = "";
	private static Driver driver = null;
	
	
	private static String dbUrl = System.getenv("JDBC_DATABASE_URL");

	public static synchronized Connection getConnection() throws SQLException {
		if (dbUrl == null) {
			try {
				Class jdbcDriverClass = Class.forName(JDBC_DRIVER);
				driver = (Driver) jdbcDriverClass.newInstance();
				DriverManager.registerDriver(driver);
			} catch (Exception e) {
				System.out.println("Failed to initialise JDBC driver");
				e.printStackTrace();
			}
		}
		return DriverManager.getConnection(dbUrl);
	}

	public static void close(Connection conn) {
		try {
			if (conn != null)
				conn.close();
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
	}

}
