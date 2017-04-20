package vts.vtsbackend.mssql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MSSQLBackendHandler {

	public final static Logger logger = LoggerFactory.getLogger(MSSQLBackendHandler.class);
	Connection connection = null;
	
	private final String userName = "sa";
	//private final String password = "sql2012";
	private final String password = "(@sa!)2Bmp"; 
	//private final String url = "jdbc:sqlserver://180.151.100.243:1433;databaseName=VTSDB";
	private final String url = "jdbc:sqlserver://180.151.100.243:1433;databaseName=VTSDB";
	
	
//	private final String userName = "sa";
//	//private final String password = "sql2012";
//	private final String password = "vts@123"; 
//	//private final String url = "jdbc:sqlserver://182.74.188.186:1433;databaseName=dbQuikRyde";
//	private final String url = "jdbc:sqlserver://209.190.15.26:1433;databaseName=VTSDB";
	
	public MSSQLBackendHandler(){
		connectMSSQLServer();
	}
   
	public Connection getConnection(){
		if (connection == null) {
			return connectMSSQLServer();
		}
		return connection;
	}
	
	public void closeConnection() {
		try {
			connection.close();
			connection = null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
//    public static void main(String args[]){
//    	MSSQLBackendHandler  obj = new MSSQLBackendHandler();
//    	obj.getConnection();
//    }
	public Connection connectMSSQLServer() {
		try{
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
			connection = DriverManager.getConnection(url, userName, password);
			System.out.println("MSSQL Connected Successfully");
		}catch(Exception e){
			logger.error("Exception occured during MSSQL getConnection message={} ex={}", e.getMessage(), e);
			connection = null;
		}
		
		return connection;
	}
}