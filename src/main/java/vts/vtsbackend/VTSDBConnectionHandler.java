package vts.vtsbackend;

import java.sql.Connection;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vts.vtsbackend.mssql.MSSQLBackendHandler;
import vts.vtsbackend.pgsql.PGSQLBackendHandler;


public class VTSDBConnectionHandler {

	public final static Logger logger = LoggerFactory.getLogger(VTSDBConnectionHandler.class);
	private PGSQLBackendHandler pgsqlHandler;
	private MSSQLBackendHandler mssqlHandler;
	
	private Connection pgsqlConnection = null;
	private Connection mssqlConnection = null;
	
	public VTSDBConnectionHandler() {
		//Nothing doing here
	}
	
	public boolean initializeDBHandler() {
		
		pgsqlHandler = new PGSQLBackendHandler();
		mssqlHandler = new MSSQLBackendHandler();
		
		// Get PGSQL Connection 
		if (getPgsqlConnection() == null) {
			return false;
		}
		
		//Get MSSQL Conenction -- Can be Removed....
		if (getMssqlConnection() == null) {
			return false;
		}
		
		return true;
	}	
	
	public MSSQLBackendHandler getMssqlHandler() {
		return mssqlHandler;
	}
	
	public PGSQLBackendHandler getPgsqlHandler() {
		return pgsqlHandler;
	}
	
	public Connection getPgsqlConnection() {
		if (pgsqlConnection == null) {
			pgsqlConnection = pgsqlHandler.getConnection();
		}
		return pgsqlConnection;
	}
	
	public Connection getMssqlConnection() {
		if (mssqlConnection == null) {
			mssqlConnection = mssqlHandler.getConnection();
		}
		return mssqlConnection;
	}
	
	public void closeMssqlConnection() {
		try {
			pgsqlHandler.closeConnection();
			mssqlHandler.closeConnection();
			mssqlConnection.close();
			mssqlConnection = null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
}
