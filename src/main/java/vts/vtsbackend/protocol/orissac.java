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

public class orissac extends GenericProtocol {
	
	private String cellId;
	private String gsmSignal;
	private String satellite;
	private Double batteryVoltage;
	private String digitalIPStatus;
	
	private Double analogIPVoltage;
	private String swVersion;
	private Long distanceKM;
	private String address;
	private String RFIDHeader;
	private String RFIDSerialNumber;
	private String vendorid;		
	private String firmwareversion;			
	private String packetstatus;			
	private String altitude;			
	private String pdop;			
	private String networkoperator;			
	private String emergency;			
	private String mcc;			
	private String mnc;	
	private String lac;			
	private String nmr;			
	private String digitalopstatus;			
	private String frameno;			

	
	public final static Logger logger = LoggerFactory.getLogger(LocBackProtocol.class);
	
	public orissac() {
		// TODO Auto-generated constructor stub
	}
	
	public String getPacketstatus() {
		return packetstatus;
	}

	public void setPacketstatus(String packetstatus) {
		this.packetstatus = packetstatus;
	}

	public String getNetworkoperator() {
		return networkoperator;
	}

	public void setNetworkoperator(String networkoperator) {
		this.networkoperator = networkoperator;
	}

	public int processClientMessage(String[] message, String dataType) {
		int processValue = 0;
		
		int startIndex = 1;
		String[] tokenizedMessage = message;
		
		try {
			
//			$rsm,8558,1.0.0,NR,L,868324028938558,DL1RTA9935,1,22092016,074428,20.307960,N,085.810760,E,000,238,08,66.0,1.04,000000.025,airtel,1,1,0,O,20,405,53,00aa,895b,9d3d,00aa,e864,00aa,9011,00aa,794d,00aa,0,0,0,0,1,1,000769,*
			vendorid = tokenizedMessage[startIndex++];
			firmwareversion = tokenizedMessage[startIndex++];
			packetType = tokenizedMessage[startIndex++];
			packetstatus = tokenizedMessage[startIndex++]; 
//			System.out.println("packetstatus "+packetstatus);
			IMEINO = tokenizedMessage[startIndex++];
			vehicleID = tokenizedMessage[startIndex++];
			gpsStatus = tokenizedMessage[startIndex++]; 
			dateField = tokenizedMessage[startIndex++]; 
			timeField = tokenizedMessage[startIndex++];
			latitude = Double.parseDouble(tokenizedMessage[startIndex++]);
			latitudeDirection = tokenizedMessage[startIndex++];
			longitude = Double.parseDouble(tokenizedMessage[startIndex++]);
			longitudeDirection = tokenizedMessage[startIndex++];
			vehicleSpeed = Double.parseDouble(tokenizedMessage[startIndex++]);
			vehicleDirection = Double.parseDouble(tokenizedMessage[startIndex++]);
			satellite = tokenizedMessage[startIndex++];
			altitude = tokenizedMessage[startIndex++];
			pdop = tokenizedMessage[startIndex++];
			distanceKM = Math.round(Double.parseDouble(tokenizedMessage[startIndex++]));
			networkoperator = tokenizedMessage[startIndex++];
//			System.out.println("networkoperator "+networkoperator);
			IGN = Integer.parseInt(tokenizedMessage[startIndex++]);
			batteryVoltage = Double.parseDouble(tokenizedMessage[startIndex++]);
			emergency = tokenizedMessage[startIndex++];
			tamperStatus = tokenizedMessage[startIndex++];
			gsmSignal = tokenizedMessage[startIndex++];
			mcc = tokenizedMessage[startIndex++];
			mnc = tokenizedMessage[startIndex++];
			lac = tokenizedMessage[startIndex++];
			cellId = tokenizedMessage[startIndex++];
			nmr = tokenizedMessage[startIndex++];
			digitalIPStatus = tokenizedMessage[startIndex++];
			digitalopstatus = tokenizedMessage[startIndex++];
			frameno = tokenizedMessage[startIndex++];
			analogIPVoltage = 0.00;
			swVersion = "";
			dataTimestamp = VTSUtilityHandler.getLocParserDateTimestamp(dateField, timeField);
			if(Math.abs(VTSUtilityHandler.compareTwoTimeStamps(new Timestamp(new java.util.Date().getTime()), dataTimestamp)) > 10){
				 dataTimestamp.setHours(dataTimestamp.getHours() + 5);
				 dataTimestamp.setMinutes(dataTimestamp.getMinutes() + 30);
			 }
	
		} catch (Exception exp) {
			logger.error("Exception while extracting packet fields message={} ex={}", exp.getMessage(), exp);
			processValue = -1;
		}
			
		return processValue;
	}
	
	
	public int processClientMessagef(String[] message, String dataType) {
		int processValue = 0;
		
		int startIndex = 1;
		String[] tokenizedMessage = message;
		//System.out.println("lengthf "+tokenizedMessage.length);
		try {
			
//			$rsm,8558,1.0.0,NR,L,868324028938558,DL1RTA9935,1,22092016,074428,20.307960,N,085.810760,E,000,238,08,66.0,1.04,000000.025,airtel,1,1,0,O,20,405,53,00aa,895b,9d3d,00aa,e864,00aa,9011,00aa,794d,00aa,0,0,0,0,1,1,000769,*
//		   "$(F,1.0,NR,L,862151031488354,3148835,1,191216,160212,21.907493,N,85.247873,E,0,42,9,579,0,304,airtel,0,1,0,C,29,195,35,ad,6edf,ad,6edd,0,0,0,0,0,0,0000,00,152,49*"
			
			//"$VTU,ATL,EX.1.0.0,NR,L,861693031528706,VRN_NS,1,191216,084552,21.875786,N,85.438072,E,00.00,263,7,502,0.0,000000.5,405753,0,1,0,O,25,405,75,1601,62902,80,14832,21,14831,8,14833,70,62903,0000,00,516*
			
			vendorid = "";
			firmwareversion = tokenizedMessage[startIndex++];
			packetType = tokenizedMessage[startIndex++];
			packetstatus = tokenizedMessage[startIndex++]; 
//			System.out.println("packetstatus "+packetstatus);
			IMEINO = tokenizedMessage[startIndex++];
			vehicleID = tokenizedMessage[startIndex++];
			gpsStatus = tokenizedMessage[startIndex++]; 
			dateField = tokenizedMessage[startIndex++]; 
			timeField = tokenizedMessage[startIndex++];
			latitude = Double.parseDouble(tokenizedMessage[startIndex++]);
			latitudeDirection = tokenizedMessage[startIndex++];
			longitude = Double.parseDouble(tokenizedMessage[startIndex++]);
			longitudeDirection = tokenizedMessage[startIndex++];
			vehicleSpeed = Double.parseDouble(tokenizedMessage[startIndex++]);
			vehicleDirection = Double.parseDouble(tokenizedMessage[startIndex++]);
			satellite = tokenizedMessage[startIndex++];
			altitude = tokenizedMessage[startIndex++];
			pdop = tokenizedMessage[startIndex++];
			distanceKM = Math.round(Double.parseDouble(tokenizedMessage[startIndex++]));
			networkoperator = tokenizedMessage[startIndex++];
//			System.out.println("networkoperator "+networkoperator);
			IGN = Integer.parseInt(tokenizedMessage[startIndex++]);
			batteryVoltage = Double.parseDouble(tokenizedMessage[startIndex++]);
			emergency = tokenizedMessage[startIndex++];
			tamperStatus = tokenizedMessage[startIndex++];
			gsmSignal = tokenizedMessage[startIndex++];
			mcc = tokenizedMessage[startIndex++];
			mnc = tokenizedMessage[startIndex++];
			lac = tokenizedMessage[startIndex++];
			cellId = tokenizedMessage[startIndex++];
			nmr = tokenizedMessage[startIndex++];
			digitalIPStatus = tokenizedMessage[startIndex++];
			digitalopstatus = tokenizedMessage[startIndex++];
			frameno = tokenizedMessage[startIndex++];
			analogIPVoltage = 0.00;
			swVersion = "";
			dataTimestamp = VTSUtilityHandler.getOrsacTimestamp(dateField, timeField);
			if(Math.abs(VTSUtilityHandler.compareTwoTimeStamps(new Timestamp(new java.util.Date().getTime()), dataTimestamp)) > 10){
				 dataTimestamp.setHours(dataTimestamp.getHours() + 5);
				 dataTimestamp.setMinutes(dataTimestamp.getMinutes() + 30);
			 }
			
		} catch (Exception exp) {
			logger.error("Exception while extracting packet fields message={} ex={}", exp.getMessage(), exp);
			processValue = -1;
		}
			
		return processValue;
	}
	
	
	
	public int processClientMessage1(String[] message, String dataType) {
		int processValue = 0;
		
		int startIndex = 1;
		String[] tokenizedMessage = message;
		//System.out.println("length "+tokenizedMessage.length);
		try {
			
//			$rsm,8558,1.0.0,NR,L,868324028938558,DL1RTA9935,1,22092016,074428,20.307960,N,085.810760,E,000,238,08,66.0,1.04,000000.025,airtel,1,1,0,O,20,405,53,00aa,895b,9d3d,00aa,e864,00aa,9011,00aa,794d,00aa,0,0,0,0,1,1,000769,*
		//  $(F,1.0,NR,L,862151031526732,3152673,0,010100,000030,0.000000,N,0.000000,E,0,0,0,0,0,0,airtel,0,0,0,O,26,194,a,152,2e39,0,0,0,0,0,0,0,0,0000,00,2,7E*"
			
			//"$VTU,ATL,EX.1.0.0,NR,L,861693031528706,VRN_NS,1,191216,084552,21.875786,N,85.438072,E,00.00,263,7,502,0.0,000000.5,405753,0,1,0,O,25,405,75,1601,62902,80,14832,21,14831,8,14833,70,62903,0000,00,516*
//$VTU,ATL,EX.1.0.0,NR,H,861693031524424,VRN_NS,1,211216,141110,21.875790,N,85.438019,E,00.00,9,7,491,0.0,000223.3,405753,0,1,0,O,23,405,75,1601,14832,21,14831,8,62902,8,14833,70,62903,0000,00,7220*$VTU,ATL,EX.1.0.0,NR,H,861693031524424,VRN_NS,1,211216,14114 (...)"		
			vendorid = tokenizedMessage[startIndex++];
			firmwareversion = tokenizedMessage[startIndex++];
			packetType = tokenizedMessage[startIndex++];
			packetstatus = tokenizedMessage[startIndex++]; 
//			System.out.println("packetstatus "+packetstatus);
			IMEINO = tokenizedMessage[startIndex++];
			vehicleID = tokenizedMessage[startIndex++];
			gpsStatus = tokenizedMessage[startIndex++]; 
			dateField = tokenizedMessage[startIndex++]; 
			timeField = tokenizedMessage[startIndex++];
			latitude = Double.parseDouble(tokenizedMessage[startIndex++]);
			latitudeDirection = tokenizedMessage[startIndex++];
			longitude = Double.parseDouble(tokenizedMessage[startIndex++]);
			longitudeDirection = tokenizedMessage[startIndex++];
			vehicleSpeed = Double.parseDouble(tokenizedMessage[startIndex++]);
			vehicleDirection = Double.parseDouble(tokenizedMessage[startIndex++]);
			satellite = tokenizedMessage[startIndex++];
			altitude = tokenizedMessage[startIndex++];
			pdop = tokenizedMessage[startIndex++];
			distanceKM = Math.round(Double.parseDouble(tokenizedMessage[startIndex++]));
			networkoperator = tokenizedMessage[startIndex++];
//			System.out.println("networkoperator "+networkoperator);
			IGN = Integer.parseInt(tokenizedMessage[startIndex++]);
			batteryVoltage = Double.parseDouble(tokenizedMessage[startIndex++]);
			emergency = tokenizedMessage[startIndex++];
			tamperStatus = tokenizedMessage[startIndex++];
			gsmSignal = tokenizedMessage[startIndex++];
			mcc = tokenizedMessage[startIndex++];
			mnc = tokenizedMessage[startIndex++];
			lac = tokenizedMessage[startIndex++];
			cellId = tokenizedMessage[startIndex++];
			nmr = tokenizedMessage[startIndex++];
			digitalIPStatus = tokenizedMessage[startIndex++];
			digitalopstatus = tokenizedMessage[startIndex++];
			frameno = tokenizedMessage[startIndex++];
			analogIPVoltage = 0.00;
			swVersion = "";
			dataTimestamp = VTSUtilityHandler.getOrsacTimestamp(dateField, timeField);
			if(Math.abs(VTSUtilityHandler.compareTwoTimeStamps(new Timestamp(new java.util.Date().getTime()), dataTimestamp)) > 10){
				 dataTimestamp.setHours(dataTimestamp.getHours() + 5);
				 dataTimestamp.setMinutes(dataTimestamp.getMinutes() + 30);
			 }
	
		} catch (Exception exp) {
			logger.error("Exception while extracting packet fields message={} ex={}", exp.getMessage(), exp);
			processValue = -1;
		}
			
		return processValue;
	}
	
	
	
	
	public void insertLocDeviceRecord(PreparedStatement insertLocRecordStmt) {
		
		try {
			int parameterIndex = 1;
			insertLocRecordStmt.setTimestamp(parameterIndex++, this.dataTimestamp);
			insertLocRecordStmt.setString(parameterIndex++, this.packetType);
			insertLocRecordStmt.setString(parameterIndex++, this.IMEINO);
			insertLocRecordStmt.setString(parameterIndex++, this.cellId);
			insertLocRecordStmt.setString(parameterIndex++, this.gpsStatus);
			
			
			try {
				
				Date packetDate = new Date(dataTimestamp.getTime());
				insertLocRecordStmt.setDate(parameterIndex++, packetDate);
			} catch (Exception e) {
				logger.error("Exception caught during date conversion : message={} and ex={}", e.getMessage(), e);
			}
			
			
			
				insertLocRecordStmt.setTimestamp(parameterIndex++, dataTimestamp);
			
	
//			System.out.println("half processed");
			
			insertLocRecordStmt.setDouble(parameterIndex++, this.latitude);
			insertLocRecordStmt.setString(parameterIndex++, this.latitudeDirection);
			insertLocRecordStmt.setDouble(parameterIndex++, this.longitude);
			insertLocRecordStmt.setString(parameterIndex++, this.longitudeDirection);
			insertLocRecordStmt.setDouble(parameterIndex++, this.vehicleSpeed);
			insertLocRecordStmt.setDouble(parameterIndex++, this.vehicleDirection);
			
			insertLocRecordStmt.setString(parameterIndex++, this.gsmSignal);
			insertLocRecordStmt.setString(parameterIndex++, this.satellite);
			insertLocRecordStmt.setDouble(parameterIndex++, this.batteryVoltage);
			insertLocRecordStmt.setInt(parameterIndex++, this.IGN);
			
			insertLocRecordStmt.setString(parameterIndex++, this.digitalIPStatus);
			insertLocRecordStmt.setString(parameterIndex++, this.tamperStatus);
			insertLocRecordStmt.setDouble(parameterIndex++, this.analogIPVoltage);
			insertLocRecordStmt.setString(parameterIndex++, this.swVersion);
			
			insertLocRecordStmt.setLong(parameterIndex++, this.distanceKM);
			insertLocRecordStmt.setString(parameterIndex++, this.address);
			insertLocRecordStmt.setString(parameterIndex++, this.RFIDHeader);
			insertLocRecordStmt.setString(parameterIndex++, this.RFIDSerialNumber);

			insertLocRecordStmt.setString(parameterIndex++, this.vendorid);
			insertLocRecordStmt.setString(parameterIndex++, this.firmwareversion);
			insertLocRecordStmt.setString(parameterIndex++, this.packetstatus);
			insertLocRecordStmt.setString(parameterIndex++, this.altitude);
			insertLocRecordStmt.setString(parameterIndex++, this.pdop);
			insertLocRecordStmt.setString(parameterIndex++, this.networkoperator);
			insertLocRecordStmt.setString(parameterIndex++, this.emergency);
			insertLocRecordStmt.setString(parameterIndex++, this.mcc);
			insertLocRecordStmt.setString(parameterIndex++, this.mnc);
			insertLocRecordStmt.setString(parameterIndex++, this.nmr);
			insertLocRecordStmt.setString(parameterIndex++, this.digitalopstatus);
			
			
			insertLocRecordStmt.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error("Exception caught in insertLocDeviceRecord  : message={} and ex={}", e.getMessage(), e);
		}	
	}
	
	
public void insertParsedDataSQL(PreparedStatement insertParsedDataSQLStatement, String clientMessage) throws SQLException{
		
			int parameterIndex = 0;
			//System.out.println("@DeviceID int,");
			insertParsedDataSQLStatement.setInt(++parameterIndex, 8383);
			//System.out.println("@GPSStatus char(1),");
			insertParsedDataSQLStatement.setString(++parameterIndex, clientMessage);
			//System.out.println("@Lat float,");
			insertParsedDataSQLStatement.executeUpdate();
	}

}
