package vts.vtsbackend.protocol;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vts.vtsbackend.VTSUtilityHandler;

public class RosMertaProtocol extends GenericProtocol {
	
	// Default parameters to be scanned through 
	private int vehicleStatus;
	private int panicStatus;
	private Double batteryVoltage;
	private String swVersion;
	
	private String tripID;
	private String tripMode;
	
	private Timestamp tripStartDate;
	private Timestamp tripEndDate;
	private int meterStatus;
	private int speedSensorStatus;	
	private double tripFare;;
	private double tripDistance;
	
	private String checksum;
	
	//health related 
	private int numberOfSatellite;
	private Double powerVoltage;
	
	public final static Logger logger = LoggerFactory.getLogger(RosMertaProtocol.class);

	public RosMertaProtocol() {
		// TODO Auto-generated constructor stub
	}
	
	public Timestamp getDataTimestamp() {
		return dataTimestamp;
	}
	
	public String getIMEINO() {
		return IMEINO;
	}
	
	public String getTimeField() {
		return timeField;
	}
	
	public String getDateField() {
		return dateField;
	}
	
	public int processHealthMessage(String[] message, String dataType) {
		int processValue = 0;
		int startIndex = 0;
		String[] tokenizedMessage = message;
		
		try {
			packetType = tokenizedMessage[startIndex++];
			IMEINO = tokenizedMessage[startIndex++];
	
			gpsStatus = tokenizedMessage[startIndex++]; 
			panicStatus = Integer.parseInt(tokenizedMessage[startIndex++]);
			dateField = tokenizedMessage[startIndex++]; 
			timeField = tokenizedMessage[startIndex++];
			numberOfSatellite = Integer.parseInt(tokenizedMessage[startIndex++]);
			powerVoltage = Double.parseDouble(tokenizedMessage[startIndex++]);
			batteryVoltage = Double.parseDouble(tokenizedMessage[startIndex++]);
			swVersion = tokenizedMessage[startIndex++];
			checksum = tokenizedMessage[startIndex++];
		
			dataTimestamp = VTSUtilityHandler.getParserDateTimestamp(dateField, timeField);
		} catch (Exception ex) {
			logger.error("Exception in processHealthMessage with message={} and ex={}", ex.getMessage(), ex);
		}
		return processValue;
	}
	
	public int processClientMessage(String[] message, String dataType) {
		int processValue = 0;
		
		int startIndex = 0;
		String[] tokenizedMessage = message;
		
		try {
		
			packetType = tokenizedMessage[startIndex++];
			IMEINO = tokenizedMessage[startIndex++];
			vehicleID = tokenizedMessage[startIndex++];
			gpsStatus = tokenizedMessage[startIndex++]; 
			dateField = tokenizedMessage[startIndex++]; 
			timeField = tokenizedMessage[startIndex++];
			
			latitude = Double.parseDouble(tokenizedMessage[startIndex++]);
			latitude = Double.parseDouble(VTSUtilityHandler.getVTSLat(latitude));
			latitudeDirection = tokenizedMessage[startIndex++];
			
			longitude = Double.parseDouble(tokenizedMessage[startIndex++]);
			longitude = Double.parseDouble(VTSUtilityHandler.getVTSLong(longitude));
			longitudeDirection = tokenizedMessage[startIndex++];
			
			vehicleSpeed = Double.parseDouble(tokenizedMessage[startIndex++]);
			vehicleDirection = Double.parseDouble(tokenizedMessage[startIndex++]);
			vehicleStatus = Integer.parseInt(tokenizedMessage[startIndex++]);
			tamperStatus = tokenizedMessage[startIndex++];
			panicStatus = Integer.parseInt(tokenizedMessage[startIndex++]);
			batteryVoltage = Double.parseDouble(tokenizedMessage[startIndex++]);
			swVersion = tokenizedMessage[startIndex++];
			
			dataTimestamp = VTSUtilityHandler.getParserDateTimestamp(dateField, timeField);
			
			String startDate = null, endDate = null;
			String startTime = null, endTime = null;
			
			if (dataType.contains("LIVE")) {
				if (tokenizedMessage.length == 20) {
					tripID = tokenizedMessage[startIndex++];
				}
				IGN = Integer.parseInt(tokenizedMessage[startIndex++]);
			} else if (dataType.contains("HISTORY")) {
				IGN = Integer.parseInt(tokenizedMessage[startIndex++]);
				tripID = tokenizedMessage[startIndex++];
			} else if (dataType.contains("ALERT")) {
				
				// Alert has two types; liveAlert and historyAlert
				
				if (packetType.contains("TRIPSTART")) {
					if (packetType.equals("@HISTORY-TRIPSTART")) {
						meterStatus = Integer.parseInt(tokenizedMessage[startIndex++]);
					}
					tripID = tokenizedMessage[startIndex++];
					startDate = tokenizedMessage[startIndex++];
					startTime = tokenizedMessage[startIndex++];
					IGN = Integer.parseInt(tokenizedMessage[startIndex++]);
				} 
				else if (packetType.contains("TRIPSTOP")) {
					if (packetType.equals("@HISTORY-TRIPSTOP")) {
						meterStatus = Integer.parseInt(tokenizedMessage[startIndex++]);
						speedSensorStatus = Integer.parseInt(tokenizedMessage[startIndex++]);
						
					}	
					
					tripID = tokenizedMessage[startIndex++]; //TODO: Verify this..
					startDate = tokenizedMessage[startIndex++];
					startTime = tokenizedMessage[startIndex++];
					endDate = tokenizedMessage[startIndex++];
					endTime = tokenizedMessage[startIndex++];
					
					tripFare = Double.parseDouble(tokenizedMessage[startIndex++]);
					tripDistance = Double.parseDouble(tokenizedMessage[startIndex++]);
					tripMode = tokenizedMessage[startIndex++];
					IGN = Integer.parseInt(tokenizedMessage[startIndex++]);
				}
			}
			// last element of all the packets 
			checksum = tokenizedMessage[startIndex++];
			
			if (startDate != null && startTime != null) {
				tripStartDate = VTSUtilityHandler.getParserDateTimestamp(startDate, startTime);
			}
			
			if (endDate != null && endTime != null) {
				tripEndDate = VTSUtilityHandler.getParserDateTimestamp(endDate, endTime);
			}
		} catch (Exception e) {
			logger.debug("Exception in processClientMessage message={} and exception={}", e.getMessage(), e);
		}
		// Hopefully parsing is done :) 
		return processValue;
	}

	public void insertParsedDeviceRecord(PreparedStatement insertDeviceRecordStmt) {
		
		try {
			int parameterIndex = 1;
			insertDeviceRecordStmt.setTimestamp(parameterIndex++, this.dataTimestamp);
			insertDeviceRecordStmt.setString(parameterIndex++, this.packetType);
			insertDeviceRecordStmt.setString(parameterIndex++, this.IMEINO);
			insertDeviceRecordStmt.setString(parameterIndex++, this.vehicleID);
			insertDeviceRecordStmt.setString(parameterIndex++, this.gpsStatus);
			
			SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
			java.util.Date parsedDate;
			try {
				parsedDate = sdf1.parse(this.dateField);
				Date packetDate = new Date(parsedDate.getTime());
				insertDeviceRecordStmt.setDate(parameterIndex++, packetDate);
			} catch (ParseException e) {
				logger.error("Exception caught during date conversion : message={} and ex={}", e.getMessage(), e);
			}
			
		
			try {
				Timestamp packetTime = VTSUtilityHandler.getParserDateTimestamp(this.dateField, this.timeField);
				//int h = packetTime.getHours() + 5;
				//packetTime.setHours(h);
				//int m = packetTime.getMinutes() + 30;
				//packetTime.setMinutes(m);
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
			insertDeviceRecordStmt.setInt(parameterIndex++, this.vehicleStatus);
			insertDeviceRecordStmt.setString(parameterIndex++, this.tamperStatus);
			insertDeviceRecordStmt.setInt(parameterIndex++, this.panicStatus);
			insertDeviceRecordStmt.setDouble(parameterIndex++, this.batteryVoltage);
			insertDeviceRecordStmt.setString(parameterIndex++, this.swVersion);
			insertDeviceRecordStmt.setString(parameterIndex++, this.tripID);
			insertDeviceRecordStmt.setInt(parameterIndex++, this.IGN);
			insertDeviceRecordStmt.setString(parameterIndex++, this.checksum);
			
			insertDeviceRecordStmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			logger.error("Exception in insertParsedDeviceRecord message={} and exception={}", e.getMessage(), e);
		}	
	}

	public void insertParsedDeviceAlertRecord(PreparedStatement insertDeviceAlertRecordStmt) {
		
		try {
			int parameterIndex = 1;
			insertDeviceAlertRecordStmt.setTimestamp(parameterIndex++, this.dataTimestamp);
			insertDeviceAlertRecordStmt.setString(parameterIndex++, this.packetType);
			insertDeviceAlertRecordStmt.setString(parameterIndex++, this.IMEINO);
			insertDeviceAlertRecordStmt.setString(parameterIndex++, this.vehicleID);
			insertDeviceAlertRecordStmt.setString(parameterIndex++, this.gpsStatus);
			
			SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
			java.util.Date parsedDate;
			try {
				parsedDate = sdf1.parse(this.dateField);
				Date packetDate = new Date(parsedDate.getTime());
				insertDeviceAlertRecordStmt.setDate(parameterIndex++, packetDate);
			} catch (ParseException e) {
				logger.error("Exception caught during date conversion : message={} and ex={}", e.getMessage(), e);
			}
			
		
			try {
				Timestamp packetTime = VTSUtilityHandler.getParserDateTimestamp(this.dateField, this.timeField);
				//int h = packetTime.getHours() + 5;
				//packetTime.setHours(h);
				//int m = packetTime.getMinutes() + 30;
				//packetTime.setMinutes(m);
				insertDeviceAlertRecordStmt.setTimestamp(parameterIndex++, packetTime);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.error("Exception caught during time conversion : message={} and ex={}", e.getMessage(), e);
			}
			
			//insertDeviceAlertRecordStmt.setString(parameterIndex++, this.dateField);
			//insertDeviceAlertRecordStmt.setString(parameterIndex++, this.timeField);
			insertDeviceAlertRecordStmt.setDouble(parameterIndex++, this.latitude);
			insertDeviceAlertRecordStmt.setString(parameterIndex++, this.latitudeDirection);
			insertDeviceAlertRecordStmt.setDouble(parameterIndex++, this.longitude);
			insertDeviceAlertRecordStmt.setString(parameterIndex++, this.longitudeDirection);
			
			insertDeviceAlertRecordStmt.setDouble(parameterIndex++, this.vehicleSpeed);
			insertDeviceAlertRecordStmt.setDouble(parameterIndex++, this.vehicleDirection);
			insertDeviceAlertRecordStmt.setInt(parameterIndex++, this.vehicleStatus);
			insertDeviceAlertRecordStmt.setString(parameterIndex++, this.tamperStatus);
			insertDeviceAlertRecordStmt.setInt(parameterIndex++, this.panicStatus);
			insertDeviceAlertRecordStmt.setDouble(parameterIndex++, this.batteryVoltage);
			insertDeviceAlertRecordStmt.setString(parameterIndex++, this.swVersion);
			insertDeviceAlertRecordStmt.setString(parameterIndex++, this.tripID);
			insertDeviceAlertRecordStmt.setInt(parameterIndex++, this.IGN);
			insertDeviceAlertRecordStmt.setString(parameterIndex++, this.checksum);
			
			insertDeviceAlertRecordStmt.setTimestamp(parameterIndex++, this.tripStartDate);
			insertDeviceAlertRecordStmt.setTimestamp(parameterIndex++, this.tripEndDate);
			insertDeviceAlertRecordStmt.setDouble(parameterIndex++, this.tripFare);
			insertDeviceAlertRecordStmt.setDouble(parameterIndex++, this.tripDistance);
			insertDeviceAlertRecordStmt.setString(parameterIndex++, this.tripMode);
			insertDeviceAlertRecordStmt.setInt(parameterIndex++, this.meterStatus);
			insertDeviceAlertRecordStmt.setInt(parameterIndex++, this.speedSensorStatus);
			
			insertDeviceAlertRecordStmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			logger.error("Exception in insertParsedDeviceAlertRecord message={} and exception={}", e.getMessage(), e);
		}	
	}

	public void insertParsedHealthDeviceRecord(PreparedStatement insertDeviceHealthRecordStmt) {
		
		try {
			int parameterIndex = 1;
			insertDeviceHealthRecordStmt.setTimestamp(parameterIndex++, this.dataTimestamp);
			insertDeviceHealthRecordStmt.setString(parameterIndex++, this.packetType);
			insertDeviceHealthRecordStmt.setString(parameterIndex++, this.IMEINO);
			insertDeviceHealthRecordStmt.setString(parameterIndex++, this.gpsStatus);
			insertDeviceHealthRecordStmt.setInt(parameterIndex++,  this.panicStatus);
			
			//packetdatetime
			insertDeviceHealthRecordStmt.setTimestamp(parameterIndex++, this.dataTimestamp);
			
			insertDeviceHealthRecordStmt.setInt(parameterIndex++, this.numberOfSatellite);
			insertDeviceHealthRecordStmt.setDouble(parameterIndex++, this.powerVoltage);
			insertDeviceHealthRecordStmt.setDouble(parameterIndex++, this.batteryVoltage);
			
			insertDeviceHealthRecordStmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			logger.error("Exception in insertParsedHealthDeviceRecord message={} and exception={}", e.getMessage(), e);
		}	
	}

}
