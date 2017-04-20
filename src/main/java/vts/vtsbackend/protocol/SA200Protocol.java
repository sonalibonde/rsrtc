package vts.vtsbackend.protocol;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vts.vtsbackend.VTSUtilityHandler;


public class SA200Protocol extends GenericProtocol {
	String[] tokenizedMessage = null;
	String dataType = null;
	String software,servingcell,noofsatellites,distance,voltdec,inpoutstatus,modeact,sos,messagenumber;
	Double batteryVoltage;
	int type = 0;
	
	public final static Logger logger = LoggerFactory.getLogger(SA200Protocol.class);
	
	public SA200Protocol(){
		// TODO Auto-generated constructor stub
	}
	
	
	@SuppressWarnings("resource")
	public int processClientMessage(String[] message, String inDataType) {
		int returnValue = 0;
		dataType = inDataType;
		System.out.println("datatype = "+dataType);
		/*
		 * Check the number of fields in the tok enized message depending on the
		 * dataType -- LIVE or HISTORY
		 */
		tokenizedMessage = message;
		
		
		int startIndex = 0;
		try{
//			SA200STT;863771025463292;111;20150522;13:25:01;26742;+28.573836;+077.2563
//			63;000.000;228.09;0;0;00000000;00.11;4.000;000000;1;0;1;0003
			
			
//			SA200STTH;863771025463292;111;20150522;13:31:14;26742;+28.573766;+077.256
//			296;000.000;228.09;0;0;00000000;00.11;4.000;000000;1;0;1;0014
			
		 packetType = tokenizedMessage[startIndex++];
		 IMEINO = tokenizedMessage[startIndex++];
		 software = tokenizedMessage[startIndex++];
		 dateField = tokenizedMessage[startIndex++];
		 timeField = tokenizedMessage[startIndex++];
		 servingcell = tokenizedMessage[startIndex++];
		 latitude = Double.parseDouble(tokenizedMessage[startIndex++].replace("+", ""));
		 longitude = Double.parseDouble(tokenizedMessage[startIndex++].replace("+", ""));
		 vehicleSpeed = Double.parseDouble(tokenizedMessage[startIndex++])*3.6;
		 vehicleDirection = Double.parseDouble(tokenizedMessage[startIndex++]);
		 noofsatellites = tokenizedMessage[startIndex++];
		 gpsStatus = tokenizedMessage[startIndex++];;
		 distance = tokenizedMessage[startIndex++];
		 batteryVoltage = Double.parseDouble(tokenizedMessage[startIndex++]);
		 voltdec = tokenizedMessage[startIndex++];
		 inpoutstatus = tokenizedMessage[startIndex++];
		 IGN = Integer.parseInt(tokenizedMessage[startIndex++]);
		 try{
		 if(inpoutstatus.charAt(0)=='1' && IGN==2){
			 IGN = 1;
		 }else{
			 IGN = 0;
		 }
		 }catch(Exception e){
			 System.out.println(e);
			 IGN=0;
		 }
		 modeact = tokenizedMessage[startIndex++];
		 sos = tokenizedMessage[startIndex++];
		 messagenumber = tokenizedMessage[startIndex++];
		 
		 
		 dataTimestamp = VTSUtilityHandler.getSA200ParserDateTimestamp(dateField, timeField);
		 if(Math.abs(VTSUtilityHandler.compareTwoTimeStamps(new Timestamp(new java.util.Date().getTime()), dataTimestamp)) > 10){
			 dataTimestamp.setHours(dataTimestamp.getHours() + 5);
			 dataTimestamp.setMinutes(dataTimestamp.getMinutes() + 30);
			 type = 1;
		 }
		/* Try to insert this parsed data into devicedata_parsed table */

		
		} catch (Exception exp) {
			logger.error("Exception while extracting packet fields message={} ex={}", exp.getMessage(), exp);
			returnValue = -1;
		} 

		return returnValue;
	}
	
	
public void insertParsedDeviceRecord(PreparedStatement insertDeviceRecordStmt) {
		
		try {
			int parameterIndex = 1;
			insertDeviceRecordStmt.setTimestamp(parameterIndex++, this.dataTimestamp);
			insertDeviceRecordStmt.setString(parameterIndex++, this.packetType);
			insertDeviceRecordStmt.setString(parameterIndex++, this.IMEINO);
			insertDeviceRecordStmt.setString(parameterIndex++, this.vehicleID);
			insertDeviceRecordStmt.setString(parameterIndex++, this.gpsStatus);
			System.out.println(dataTimestamp);
			
			try {
				Date packetDate = new Date(this.dataTimestamp.getTime());
				System.out.println(packetDate);
				insertDeviceRecordStmt.setDate(parameterIndex++, packetDate);
			} catch (Exception e) {
				logger.error("Exception caught during date conversion : message={} and ex={}", e.getMessage(), e);
			}
			
			
			try {
				Timestamp packetTime = VTSUtilityHandler.getSA200ParserDateTimestamp(this.dateField, this.timeField);
				if(type==1){
					packetTime.setHours(packetTime.getHours()+5);
					packetTime.setMinutes(packetTime.getMinutes()+30);
				}
				System.out.println(packetTime);
				insertDeviceRecordStmt.setTimestamp(parameterIndex++, packetTime);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.error("Exception caught during time conversion : message={} and ex={}", e.getMessage(), e);
			}
			
			
			//insertDeviceRecordStmt.setString(parameterIndex++, this.dateField);
			//insertDeviceRecordStmt.setString(parameterIndex++, this.timeField);
			insertDeviceRecordStmt.setDouble(parameterIndex++, this.latitude);
			insertDeviceRecordStmt.setString(parameterIndex++, this.latitudeDirection);
			insertDeviceRecordStmt.setDouble(parameterIndex++, this.longitude);
			insertDeviceRecordStmt.setString(parameterIndex++, this.longitudeDirection);
			
			insertDeviceRecordStmt.setDouble(parameterIndex++, this.vehicleSpeed);
			insertDeviceRecordStmt.setDouble(parameterIndex++, this.vehicleDirection);
			insertDeviceRecordStmt.setInt(parameterIndex++, 0);
			insertDeviceRecordStmt.setInt(parameterIndex++, 0);
			insertDeviceRecordStmt.setInt(parameterIndex++, 0);
			insertDeviceRecordStmt.setDouble(parameterIndex++, this.batteryVoltage);
			insertDeviceRecordStmt.setString(parameterIndex++, this.software);
			insertDeviceRecordStmt.setString(parameterIndex++, "0");
			insertDeviceRecordStmt.setInt(parameterIndex++, this.IGN);
			insertDeviceRecordStmt.setString(parameterIndex++, "0");
			
			insertDeviceRecordStmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			logger.error("Exception in insertParsedDeviceRecord message={} and exception={}", e.getMessage(), e);
		}	
	}
}
