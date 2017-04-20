package vts.vtsbackend.protocol;

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

public class LocBackProtocol extends GenericProtocol {
		
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
	private int panic;
	private int deviceid = 0;
	private int vehicleid = 0;
	private int clientid = 0;
	private String vehicleregno;
	private String vehicletype;
	private String orgid;
	private String gps;
	private int tampervalue;
	private int dir;
	private int volt;
	private String location;
	private int camera;
	private int image;
	
	
	
  public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

public int getPanic() {
		return panic;
	}

	public void setPanic(int panic) {
		this.panic = panic;
	}


	public final static Logger logger = LoggerFactory.getLogger(LocBackProtocol.class);
	
	public LocBackProtocol() {
		// TODO Auto-generated constructor stub
	}
	
	public int processClientMessage(String[] message, String dataType) {
		int processValue = 0;
		
		int startIndex = 0;
		String[] tokenizedMessage = message;
		
		try {
//			$loc,861074028631862,29032017,105145,1,2821.1977,N,07926.3297,E,0.0,340.51,2FB5,28,10,85,1,LLLL,NNNN,NORMAL,0.08,SW5.32,29267,88 AT Battalion Rd  Civil Lines  Bareilly  Uttar Pradesh
//			 loc2,868324020323973,19112016,075930,1,2526.8183,N,07537.07982.25,E,054,089,0000,80,09,99,1,LHLL,NNN,00.00,SW-3.10,00000314,NNNN,NRM,1,*
//			"$loc,868324020447327,24082016,140436,1,1302.0774,N,07736.5828,E,000,041,0000,28,09,99,0,LLLL,NNN,ACTIVE,00.00,SW-5.011,0"
//			"$loc2,868324020375130,24082016,133338,1,1257.9720,N,07728.3530,E,000,201,0000,25,07,99,0,LLLL,NNN,00.00,SW-3.00,00057088,NNNN,*"
//			 $loc,868324029858995,13102016,194226,1,1302.8001,N,08014.9538,E,000,125,0000,25,05,99,1,LHLL,NNN,01.00,SW-3.00,000000.000,000000,*
//			 $loc,868324029858995,13102016,231729,1,1302.7729,N,08014.9679,E,000,217,0000,16,06,99,0,LLLL,NNN,00.00,SW-3.00,000013.210,000013,*
			packetType = tokenizedMessage[startIndex++];
			IMEINO = tokenizedMessage[startIndex++];
			
			
			dateField = tokenizedMessage[startIndex++]; 
			timeField = tokenizedMessage[startIndex++];
			
			gpsStatus = tokenizedMessage[startIndex++]; 
			if(gpsStatus.contains("0")){
				gpsStatus = "N";
			}else{
				gpsStatus = "C";
			}
			
			latitude = Double.parseDouble(tokenizedMessage[startIndex++]);
			latitude = Double.parseDouble(VTSUtilityHandler.getVTSLat(latitude));
			latitudeDirection = tokenizedMessage[startIndex++];
			
			longitude = Double.parseDouble(tokenizedMessage[startIndex++]);
			longitude = Double.parseDouble(VTSUtilityHandler.getVTSLong(longitude));
			longitudeDirection = tokenizedMessage[startIndex++];
			
			vehicleSpeed = Double.parseDouble(tokenizedMessage[startIndex++]);
			vehicleDirection = Double.parseDouble(tokenizedMessage[startIndex++]);
			
			cellId = tokenizedMessage[startIndex++];
			vehicleID = cellId; //just to ensure we have the same notion for the generic protocol
			
			gsmSignal = tokenizedMessage[startIndex++];
			satellite = tokenizedMessage[startIndex++];
			
			batteryVoltage = Double.parseDouble(tokenizedMessage[startIndex++]);
			IGN = Integer.parseInt(tokenizedMessage[startIndex++]);
			
			digitalIPStatus = tokenizedMessage[startIndex++];
			//System.out.println("About to proces panic");
			if(digitalIPStatus.charAt(2)=='H'){
				
				this.panic=1;
				//System.out.println("in if "+this.panic);
			}else{
				this.panic=0;
				//System.out.println("in else "+this.panic);
			}
			tamperStatus = tokenizedMessage[startIndex++];
			if(tamperStatus.charAt(2)=='T'){
				//power = 0;
			}
//			System.out.println("packetType "+packetType);
			if(packetType.equals("$loc") || packetType.equals("$bak")){
				String status = tokenizedMessage[startIndex++];
//				System.out.println("status "+status);
			}
			analogIPVoltage = Double.parseDouble(tokenizedMessage[startIndex++]);
			
			swVersion = tokenizedMessage[startIndex++];
			
			distanceKM = Math.round(Double.parseDouble(tokenizedMessage[startIndex++]));
			
			
			if (tokenizedMessage.length >= 23) {
				address = tokenizedMessage[startIndex++];
				RFIDHeader = tokenizedMessage[startIndex++];
				if (tokenizedMessage.length == 24){
					RFIDSerialNumber = tokenizedMessage[startIndex++];
//					System.out.println("RFIDSerialNumber "+RFIDSerialNumber);
				}
				if(tokenizedMessage.length == 25){
					if(tokenizedMessage[startIndex++].equals("NRM")){
						camera = Integer.parseInt(tokenizedMessage[startIndex++]);
					}if(tokenizedMessage[startIndex++].equals("PIC")){
						image = Integer.parseInt(tokenizedMessage[startIndex++]);
					}
				}
			}
			
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
	
	public void insertLocDeviceRecord(PreparedStatement insertLocRecordStmt) {
		
		try {
			int parameterIndex = 1;
			insertLocRecordStmt.setTimestamp(parameterIndex++, this.dataTimestamp);
			insertLocRecordStmt.setString(parameterIndex++, this.packetType);
			insertLocRecordStmt.setString(parameterIndex++, this.IMEINO);
			insertLocRecordStmt.setString(parameterIndex++, this.cellId);
			insertLocRecordStmt.setString(parameterIndex++, this.gpsStatus);
			
			SimpleDateFormat sdf1 = new SimpleDateFormat("ddMMyyyy");
			java.util.Date parsedDate;
			try {
				parsedDate = sdf1.parse(this.dateField);
				Date packetDate = new Date(parsedDate.getTime());
				insertLocRecordStmt.setDate(parameterIndex++, packetDate);
			} catch (ParseException e) {
				logger.error("Exception caught during date conversion : message={} and ex={}", e.getMessage(), e);
			}
			
			
			SimpleDateFormat sdf2 = new SimpleDateFormat("ddMMyyyy HHmmss");
			java.util.Date parseTimestamp;
			try {
				parseTimestamp = sdf2.parse(this.dateField + " " + this.timeField);
				Timestamp packetTime = new Timestamp(parseTimestamp.getTime());
				
				int h = packetTime.getHours() + 5;
				packetTime.setHours(h);
				int m = packetTime.getMinutes() + 30;
				packetTime.setMinutes(m);
				insertLocRecordStmt.setTimestamp(parameterIndex++, packetTime);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				logger.error("Exception caught during time conversion : message={} and ex={}", e.getMessage(), e);
			}
	
			
			
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
			
			insertLocRecordStmt.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error("Exception caught in insertLocDeviceRecord  : message={} and ex={}", e.getMessage(), e);
		}	
	}
	
	
public void insertParsedDataSQL(PreparedStatement insertParsedDataSQLStatement, String clientMessage, String port) throws SQLException{
			try{
			int parameterIndex = 0;
			//System.out.println("@DeviceID int,");
			insertParsedDataSQLStatement.setInt(++parameterIndex, Integer.parseInt(port));
			//System.out.println("@GPSStatus char(1),");
			insertParsedDataSQLStatement.setString(++parameterIndex, clientMessage);
			//System.out.println("@Lat float,");
			insertParsedDataSQLStatement.executeUpdate();
			System.out.println("executed");
			}catch(Exception e){
				System.out.println(e);
			}
	}
 //selectDeviceMasterStatement

public Timestamp selectCurrentVehicleDataSQL(PreparedStatement selectDeviceMasterStatement) throws SQLException{
	Timestamp retvalue = null;
	try{
	int parameterIndex = 0;
	//System.out.println("@DeviceID int,");
	selectDeviceMasterStatement.setInt(++parameterIndex, this.deviceid);
	//System.out.println("@GPSStatus char(1),");pdate();
	ResultSet rs = selectDeviceMasterStatement.executeQuery();
	if(rs.next()){
		//deviceid = rs.getInt(1);
		retvalue = rs.getTimestamp(1);
	}
	System.out.println("selectCurrentVehicleDataSQL Deviceid = "+deviceid);
	}catch(Exception e){
		System.out.println("selectCurrentVehicleDataSQL "+e);
	}
	return retvalue;
}





public int selectDeviceDataSQL(PreparedStatement selectDeviceMasterStatement) throws SQLException{
	int retvalue = 0;
	try{
	int parameterIndex = 0;
	//System.out.println("@DeviceID int,");
	selectDeviceMasterStatement.setString(++parameterIndex, this.IMEINO);
	//System.out.println("@GPSStatus char(1),");pdate();
//	System.out.println()
	ResultSet rs = selectDeviceMasterStatement.executeQuery();
	if(rs.next()){
		deviceid = rs.getInt(1);
		retvalue = 1;
	}
//	System.out.println("selectCurrentVehicleDataSQL Deviceid = "+deviceid);
	}catch(Exception e){
		System.out.println("selectDeviceDataSQL "+e);
	}
	return retvalue;
}


public void selectVehicleDataSQL(PreparedStatement selectVehicleMasterStatement) throws SQLException{
	try{
		int parameterIndex = 0;
		//System.out.println("@DeviceID int,");
		selectVehicleMasterStatement.setString(++parameterIndex, String.valueOf(this.deviceid));
		//System.out.println("@GPSStatus char(1),");pdate();
		ResultSet rs = selectVehicleMasterStatement.executeQuery();
		if(rs.next()){
			vehicleid = rs.getInt(1);
			clientid = rs.getInt(2);
			vehicleregno = rs.getString(3);
			vehicletype = rs.getString(4);
			orgid = rs.getString(5);
		}
		System.out.println("selectCurrentVehicleDataSQL Deviceid = "+deviceid);
	}catch(Exception e){
		System.out.println("selectVehicleDataSQL "+e);
	}
}

public void selectLocationSQL(PreparedStatement selectVehicleMasterStatement) throws SQLException{
	try{
		int parameterIndex = 0;
		//System.out.println("@DeviceID int,");
		
		selectVehicleMasterStatement.setString(++parameterIndex, String.valueOf(this.latitude));
		selectVehicleMasterStatement.setString(++parameterIndex, String.valueOf(this.longitude));
		
		ResultSet rs = selectVehicleMasterStatement.executeQuery();
		if(rs.next()){
			this.location = rs.getString(1);
		}
		System.out.println("selectCurrentVehicleDataSQL Deviceid = "+deviceid);
	}catch(Exception e){
		System.out.println("selectVehicleDataSQL "+e);
	}
}


public void insertCurrentVehicleDataSQL(PreparedStatement insertCurrentDataStatetments) throws SQLException{
	try{
	int parameterIndex = 0;
//	[VehicleID] ,[VehicleRegNo] ,[VehicleType] ,[OrgID] ,[ClientID] ,[HubID] ,[DeviceID] ,[GPSStatus] "+
//   ",[Lat] ,[Lon] ,[NoOfSatellite] ,[Alt] ,[Heading] ,[Speed] ,[UTCDate] ,[UTCTime] ,[TimeZone] "+
//   ",[CellID] ,[OdoMeter] ,[DeviceTamperingStatus] ,[MainBatteryStatus] ,[IgnitionStatus] "+
//   ",[DigitalInput1] ,[DigitalInput2] ,[DigitalInput3] ,[DigitalInput4] ,[DigitalOutput1] "+
//   ",[DigitalOutput2] ,[AnalogInput1] ,[AnalogInput2] ,[InternalBatteryChargingStatus] "+
//   ",[InternalBatteryVoltage] ,[InternalBatteryChargingCondition] ,[TypeOfData] "+
//   ",[AlertType] ,[GSMSignalStrength] ,[Version] ,[RecievedDateTime] ,[Panic] "+
//   ",[StringDateTime] ,[Location] ,[LocationFlag] ,[FuelTankCapacity] ,[MobileNumber] "+
//   ",[GeoFenceID] ,[GeofenceStatus] ,[TripStatus] ,[TripStatusChangeTime] ,[PickTripStartTime] "+
//   ",[PickTripEndTime] ,[DropTripStartTime] ,[DropTripEndTime] ,[OverSpeedFlag] ,[RouteID] "+
//   ",[ETATrip] ,[ETATripflag] ,[GeofenceSMSAlert] ,[onschedulestatus] ,[processstatus] "+
//   ",[SequenceNo] ,[tripid] ,[routedeviation] ,[orgfuelvalue] ,[Scheduletime] ,[routestartcode] "+
//   ",[serviceid] ,[laststop] ,[nextstop] ,[imagedate] ,[Overstop] ,[Overidle] ,[camerastatus] "+
//   ",[drivername] ,[drivermob] ,[vehiclestatus] ,[attandby] ,[Remarks] ,[Remarksdate] ,[entrydate] "+
//   ",[nodatastatus] ,[routename] ,[rautestatus] ,[cameraconnectstatus] ,[imagesentstatus])
	insertCurrentDataStatetments.setString(++parameterIndex, String.valueOf(vehicleid));
	insertCurrentDataStatetments.setString(++parameterIndex, vehicleregno);
	insertCurrentDataStatetments.setString(++parameterIndex, vehicletype);
	insertCurrentDataStatetments.setString(++parameterIndex, orgid);
	insertCurrentDataStatetments.setString(++parameterIndex, String.valueOf(clientid));
	insertCurrentDataStatetments.setString(++parameterIndex, "0");
	insertCurrentDataStatetments.setString(++parameterIndex, gpsStatus);
	insertCurrentDataStatetments.setString(++parameterIndex, String.valueOf(latitude));
	insertCurrentDataStatetments.setString(++parameterIndex, String.valueOf(longitude));
	insertCurrentDataStatetments.setString(++parameterIndex, satellite);
	insertCurrentDataStatetments.setString(++parameterIndex, "0");
	insertCurrentDataStatetments.setString(++parameterIndex, String.valueOf(this.vehicleDirection));
	insertCurrentDataStatetments.setString(++parameterIndex, String.valueOf(this.vehicleSpeed));
	insertCurrentDataStatetments.setString(++parameterIndex, String.valueOf(this.dateField));
	insertCurrentDataStatetments.setString(++parameterIndex, String.valueOf(this.timeField));
	insertCurrentDataStatetments.setString(++parameterIndex, null);
	insertCurrentDataStatetments.setString(++parameterIndex, String.valueOf(this.cellId));
	insertCurrentDataStatetments.setString(++parameterIndex, null);
	insertCurrentDataStatetments.setString(++parameterIndex, String.valueOf(this.tampervalue));
	insertCurrentDataStatetments.setString(++parameterIndex, String.valueOf(this.batteryVoltage));
	insertCurrentDataStatetments.setString(++parameterIndex, String.valueOf(this.IGN));
	insertCurrentDataStatetments.setString(++parameterIndex, "0");
	insertCurrentDataStatetments.setString(++parameterIndex, "0");
	insertCurrentDataStatetments.setString(++parameterIndex, "0");
	insertCurrentDataStatetments.setString(++parameterIndex, "0");
	insertCurrentDataStatetments.setString(++parameterIndex, "0");
	insertCurrentDataStatetments.setString(++parameterIndex, "0");
	insertCurrentDataStatetments.setString(++parameterIndex, "0:00");
	insertCurrentDataStatetments.setString(++parameterIndex, "0:00");
	insertCurrentDataStatetments.setString(++parameterIndex, "0");
	insertCurrentDataStatetments.setString(++parameterIndex, "0:00");
	insertCurrentDataStatetments.setString(++parameterIndex, "0");
	insertCurrentDataStatetments.setString(++parameterIndex, "44");
	insertCurrentDataStatetments.setString(++parameterIndex, "0");
	insertCurrentDataStatetments.setString(++parameterIndex, this.gsmSignal);
	insertCurrentDataStatetments.setString(++parameterIndex, this.swVersion);
	insertCurrentDataStatetments.setString(++parameterIndex, String.valueOf(new Timestamp(new java.util.Date().getTime())));
	insertCurrentDataStatetments.setString(++parameterIndex, String.valueOf(panic));
	insertCurrentDataStatetments.setString(++parameterIndex, String.valueOf(this.dataTimestamp));
//	insertCurrentDataStatetments.setString(++parameterIndex, x);
	insertCurrentDataStatetments.setString(++parameterIndex, null);
//	insertCurrentDataStatetments.setString(++parameterIndex, x);
//	insertCurrentDataStatetments.setString(++parameterIndex, String.valueOf(vehicleid));
	insertCurrentDataStatetments.setString(++parameterIndex, null);
	insertCurrentDataStatetments.setString(++parameterIndex, null);
	insertCurrentDataStatetments.setString(++parameterIndex, null);
	insertCurrentDataStatetments.setString(++parameterIndex, null);
	insertCurrentDataStatetments.setString(++parameterIndex, null);
	insertCurrentDataStatetments.setString(++parameterIndex, null);
	insertCurrentDataStatetments.setString(++parameterIndex, null);
	insertCurrentDataStatetments.setString(++parameterIndex, null);
	insertCurrentDataStatetments.setString(++parameterIndex, null);
	insertCurrentDataStatetments.setString(++parameterIndex, "0");
	insertCurrentDataStatetments.setString(++parameterIndex, "0");
	insertCurrentDataStatetments.setString(++parameterIndex, null);
	insertCurrentDataStatetments.setString(++parameterIndex, null);
//	insertCurrentDataStatetments.setString(++parameterIndex, "");
//	insertCurrentDataStatetments.setString(++parameterIndex, "");
	insertCurrentDataStatetments.setString(++parameterIndex, "0");
	insertCurrentDataStatetments.setString(++parameterIndex, "0");
	insertCurrentDataStatetments.setString(++parameterIndex, null);
	insertCurrentDataStatetments.setString(++parameterIndex, "0");
	insertCurrentDataStatetments.setString(++parameterIndex, null);
//	insertCurrentDataStatetments.setString(++parameterIndex, String.valueOf(this.location));
//	insertCurrentDataStatetments.setString(++parameterIndex, vehicleregno);
//	insertCurrentDataStatetments.setString(++parameterIndex, vehicletype);
//	insertCurrentDataStatetments.setString(++parameterIndex, x);
//	insertCurrentDataStatetments.setString(++parameterIndex, x);
//	insertCurrentDataStatetments.setString(++parameterIndex, x);
//	insertCurrentDataStatetments.setString(++parameterIndex, x);
//	insertCurrentDataStatetments.setString(++parameterIndex, x);
//	insertCurrentDataStatetments.setString(++parameterIndex, x);
//	insertCurrentDataStatetments.setString(++parameterIndex, x);
//	insertCurrentDataStatetments.setString(++parameterIndex, x);
//	insertCurrentDataStatetments.setString(++parameterIndex, x);
//	insertCurrentDataStatetments.setString(++parameterIndex, x);
//	insertCurrentDataStatetments.setString(++parameterIndex, x);
//	insertCurrentDataStatetments.setString(++parameterIndex, x);
//	insertCurrentDataStatetments.setString(++parameterIndex, x);
//	insertCurrentDataStatetments.setString(++parameterIndex, x);
//	insertCurrentDataStatetments.setString(++parameterIndex, x);
//	insertCurrentDataStatetments.setString(++parameterIndex, x);
//	insertCurrentDataStatetments.setString(++parameterIndex, x);
//	insertCurrentDataStatetments.setString(++parameterIndex, x);
	insertCurrentDataStatetments.executeUpdate();
	System.out.println("executed");
	}catch(Exception e){
		System.out.println("selectVehicleDataSQL "+e);
	}
}




public void updateCurrentVehicleDataSQL(PreparedStatement updateCurrentDataStatetments) throws SQLException{
	try{
	int parameterIndex = 0;
//	[VehicleID] = ?,[VehicleRegNo]  = ?,[VehicleType]  = ?,[OrgID]  = ?,[ClientID]  = ?,[HubID]  = ?,[DeviceID]  = ?,[GPSStatus] = ? "+
//   ",[Lat]  = ?,[Lon]  = ?,[NoOfSatellite]  = ?,[Alt]  = ?,[Heading]  = ?,[Speed]  = ? "+
//   ",[CellID]  = ?,[DeviceTamperingStatus]  = ?,[MainBatteryStatus]  = ?,[IgnitionStatus] = ? "+
//   ",[DigitalInput1]  = ?,[DigitalInput2]  = ?,[DigitalInput3]  = ?,[DigitalInput4]  = ?,[DigitalOutput1] = ? "+
//   ",[DigitalOutput2]  = ?,[AnalogInput1]  = ?,[AnalogInput2]  = ?,[InternalBatteryChargingStatus] = ? "+
//   ",[InternalBatteryVoltage]  = ?,[InternalBatteryChargingCondition]  = ?,[TypeOfData] = ? "+
//   ",[AlertType]  = ?,[GSMSignalStrength]  = ?,[Version]  = ?,[RecievedDateTime]  = ?,[Panic] = ? "+
//   ",[StringDateTime]  = ? where DeviceID = ?
	updateCurrentDataStatetments.setString(++parameterIndex, String.valueOf(vehicleid));
	updateCurrentDataStatetments.setString(++parameterIndex, vehicleregno);
	updateCurrentDataStatetments.setString(++parameterIndex, vehicletype);
	updateCurrentDataStatetments.setString(++parameterIndex, orgid);
	updateCurrentDataStatetments.setString(++parameterIndex, String.valueOf(clientid));
	updateCurrentDataStatetments.setString(++parameterIndex, "0");
	updateCurrentDataStatetments.setInt(++parameterIndex, this.deviceid);
	updateCurrentDataStatetments.setString(++parameterIndex, this.gpsStatus);
	updateCurrentDataStatetments.setString(++parameterIndex, String.valueOf(latitude));
	updateCurrentDataStatetments.setString(++parameterIndex, String.valueOf(longitude));
	updateCurrentDataStatetments.setString(++parameterIndex, satellite);
	updateCurrentDataStatetments.setString(++parameterIndex, "0");
	updateCurrentDataStatetments.setInt(++parameterIndex, dir);
	updateCurrentDataStatetments.setString(++parameterIndex, String.valueOf(this.vehicleSpeed));
//	updateCurrentDataStatetments.setString(++parameterIndex, null);
//	updateCurrentDataStatetments.setString(++parameterIndex, null);
//	updateCurrentDataStatetments.setString(++parameterIndex, null);
	updateCurrentDataStatetments.setString(++parameterIndex, String.valueOf(this.cellId));
//	updateCurrentDataStatetments.setString(++parameterIndex, null);
	updateCurrentDataStatetments.setString(++parameterIndex, String.valueOf(this.tampervalue));
	updateCurrentDataStatetments.setString(++parameterIndex, String.valueOf(this.batteryVoltage.intValue()));
	updateCurrentDataStatetments.setString(++parameterIndex, String.valueOf(this.IGN));
	updateCurrentDataStatetments.setString(++parameterIndex, "0");
	updateCurrentDataStatetments.setString(++parameterIndex, "0");
	updateCurrentDataStatetments.setString(++parameterIndex, "0");
	updateCurrentDataStatetments.setString(++parameterIndex, "0");
	updateCurrentDataStatetments.setString(++parameterIndex, "0");
	updateCurrentDataStatetments.setString(++parameterIndex, "0");
	updateCurrentDataStatetments.setString(++parameterIndex, "0");
	updateCurrentDataStatetments.setString(++parameterIndex, "0");
	updateCurrentDataStatetments.setString(++parameterIndex, "0");
	updateCurrentDataStatetments.setString(++parameterIndex, "0");
	updateCurrentDataStatetments.setString(++parameterIndex, "0");
	updateCurrentDataStatetments.setString(++parameterIndex, "44");
	updateCurrentDataStatetments.setString(++parameterIndex, "0");
	updateCurrentDataStatetments.setString(++parameterIndex, this.gsmSignal);
	updateCurrentDataStatetments.setString(++parameterIndex, this.swVersion);
	updateCurrentDataStatetments.setString(++parameterIndex, String.valueOf(new Timestamp(new java.util.Date().getTime())));
	updateCurrentDataStatetments.setString(++parameterIndex, String.valueOf(panic));
	updateCurrentDataStatetments.setString(++parameterIndex, String.valueOf(this.dataTimestamp));
//	updateCurrentDataStatetments.setString(++parameterIndex, null);
//	updateCurrentDataStatetments.setString(++parameterIndex, null);
//	updateCurrentDataStatetments.setString(++parameterIndex, null);
//	updateCurrentDataStatetments.setString(++parameterIndex, null);
//	updateCurrentDataStatetments.setString(++parameterIndex, null);
//	updateCurrentDataStatetments.setString(++parameterIndex, null);
//	updateCurrentDataStatetments.setString(++parameterIndex, null);
//	updateCurrentDataStatetments.setString(++parameterIndex, null);
//	updateCurrentDataStatetments.setString(++parameterIndex, null);
//	updateCurrentDataStatetments.setString(++parameterIndex, null);
//	updateCurrentDataStatetments.setString(++parameterIndex, "0");
//	updateCurrentDataStatetments.setString(++parameterIndex, "0");
//	updateCurrentDataStatetments.setString(++parameterIndex, null);
//	updateCurrentDataStatetments.setString(++parameterIndex, null);
//	updateCurrentDataStatetments.setString(++parameterIndex, "0");
//	updateCurrentDataStatetments.setString(++parameterIndex, "0");
//	updateCurrentDataStatetments.setString(++parameterIndex, null);
//	updateCurrentDataStatetments.setString(++parameterIndex, "0");
//	updateCurrentDataStatetments.setString(++parameterIndex, this.location);
	updateCurrentDataStatetments.setString(++parameterIndex, String.valueOf(this.deviceid));
	updateCurrentDataStatetments.executeUpdate();
	System.out.println("executed");
	}catch(Exception e){
		System.out.println("selectVehicleDataSQL "+e+ " "+updateCurrentDataStatetments);
	}
}





public void insertProcessVehicleDataSQL(PreparedStatement insertProcessDataStatetments) throws SQLException{
	try{
	int parameterIndex = 0;
//	[VehicleID]  ,[VehicleRegNo] ,[OrgID],[DeviceID] ,[HubID] ,[GPSStatus],[Lat], "+
//	"[Lon],[NoOfSatellite],[Alt],[Heading,[Speed],[UTCDate],[UTCTime],[TimeZone],[CellID], "+
//	"[OdoMeter],[DeviceTamperingStatus],[MainBatteryStatus],[IgnitionStatus],[DigitalInput1], "+
//	"[DigitalInput2],[DigitalInput3],[DigitalInput4],[DigitalOutput1],[DigitalOutput2], "+
//	"[AnalogInput1],[AnalogInput2],[InternalBatteryChargingStatus],[InternalBatteryVoltage], "+
//	"[InternalBatteryChargingCondition],[TypeOfData],[AlertType],[GSMSignalStrength], "+
//	"[Version],[RecievedDateTime],[Panic],[StringDateTime]
	//System.out.println(insertProcessDataStatetments);
	insertProcessDataStatetments.setString(++parameterIndex, String.valueOf(vehicleid));
	insertProcessDataStatetments.setString(++parameterIndex, vehicleregno);
	insertProcessDataStatetments.setString(++parameterIndex, orgid);
	insertProcessDataStatetments.setString(++parameterIndex, String.valueOf(deviceid));
	insertProcessDataStatetments.setString(++parameterIndex, "0");
	insertProcessDataStatetments.setString(++parameterIndex, gpsStatus);
	insertProcessDataStatetments.setString(++parameterIndex, String.valueOf(latitude));
	insertProcessDataStatetments.setString(++parameterIndex, String.valueOf(longitude));
	insertProcessDataStatetments.setString(++parameterIndex, satellite);
	insertProcessDataStatetments.setString(++parameterIndex, "0");
	insertProcessDataStatetments.setInt(++parameterIndex, dir);
	insertProcessDataStatetments.setString(++parameterIndex, String.valueOf(this.vehicleSpeed));
//	insertProcessDataStatetments.setString(++parameterIndex, null);
//	insertProcessDataStatetments.setString(++parameterIndex, null);
	insertProcessDataStatetments.setString(++parameterIndex, null);
	insertProcessDataStatetments.setString(++parameterIndex, String.valueOf(this.cellId));
	insertProcessDataStatetments.setString(++parameterIndex, null);
	insertProcessDataStatetments.setString(++parameterIndex, String.valueOf(this.tampervalue));
	insertProcessDataStatetments.setString(++parameterIndex, String.valueOf(this.batteryVoltage.intValue()));
	insertProcessDataStatetments.setString(++parameterIndex, String.valueOf(this.IGN));
	insertProcessDataStatetments.setString(++parameterIndex, "0");
	insertProcessDataStatetments.setString(++parameterIndex, "0");
	insertProcessDataStatetments.setString(++parameterIndex, "0");
	insertProcessDataStatetments.setString(++parameterIndex, "0");
	insertProcessDataStatetments.setString(++parameterIndex, "0");
	insertProcessDataStatetments.setString(++parameterIndex, "0");
	insertProcessDataStatetments.setString(++parameterIndex, "0");
	insertProcessDataStatetments.setString(++parameterIndex, "0");
	insertProcessDataStatetments.setString(++parameterIndex, "0");
	insertProcessDataStatetments.setString(++parameterIndex, "0");
	insertProcessDataStatetments.setString(++parameterIndex, "0");
	insertProcessDataStatetments.setString(++parameterIndex, "44");
	insertProcessDataStatetments.setString(++parameterIndex, "0");
	insertProcessDataStatetments.setString(++parameterIndex, this.gsmSignal);
	insertProcessDataStatetments.setString(++parameterIndex, this.swVersion);
	insertProcessDataStatetments.setString(++parameterIndex, String.valueOf(new Timestamp(new java.util.Date().getTime())));
	insertProcessDataStatetments.setString(++parameterIndex, String.valueOf(panic));
	insertProcessDataStatetments.setString(++parameterIndex, String.valueOf(this.dataTimestamp));
	insertProcessDataStatetments.setString(++parameterIndex, String.valueOf(this.clientid));
//	insertProcessDataStatetments.setString(++parameterIndex, this.location);
	insertProcessDataStatetments.executeUpdate();
	
	
	System.out.println("executed");
	}catch(Exception e){
		System.out.println("insertProcessVehicleDataSQL "+e);
	}
}


public void insertHistoryProcessVehicleDataSQL(PreparedStatement insertProcessDataStatetments) throws SQLException{
	try{
	int parameterIndex = 0;
//	[VehicleID]  ,[VehicleRegNo] ,[OrgID],[DeviceID] ,[HubID] ,[GPSStatus],[Lat], "+
//	"[Lon],[NoOfSatellite],[Alt],[Heading,[Speed],[UTCDate],[UTCTime],[TimeZone],[CellID], "+
//	"[OdoMeter],[DeviceTamperingStatus],[MainBatteryStatus],[IgnitionStatus],[DigitalInput1], "+
//	"[DigitalInput2],[DigitalInput3],[DigitalInput4],[DigitalOutput1],[DigitalOutput2], "+
//	"[AnalogInput1],[AnalogInput2],[InternalBatteryChargingStatus],[InternalBatteryVoltage], "+
//	"[InternalBatteryChargingCondition],[TypeOfData],[AlertType],[GSMSignalStrength], "+
//	"[Version],[RecievedDateTime],[Panic],[StringDateTime]
	//System.out.println(insertProcessDataStatetments);
	insertProcessDataStatetments.setString(++parameterIndex, String.valueOf(vehicleid));
	insertProcessDataStatetments.setString(++parameterIndex, vehicleregno);
	insertProcessDataStatetments.setString(++parameterIndex, orgid);
	insertProcessDataStatetments.setString(++parameterIndex, String.valueOf(deviceid));
	insertProcessDataStatetments.setString(++parameterIndex, "0");
	insertProcessDataStatetments.setString(++parameterIndex, gpsStatus);
	insertProcessDataStatetments.setString(++parameterIndex, String.valueOf(latitude));
	insertProcessDataStatetments.setString(++parameterIndex, String.valueOf(longitude));
	insertProcessDataStatetments.setString(++parameterIndex, satellite);
	insertProcessDataStatetments.setString(++parameterIndex, "0");
	insertProcessDataStatetments.setInt(++parameterIndex, dir);
	insertProcessDataStatetments.setString(++parameterIndex, String.valueOf(this.vehicleSpeed));
//	insertProcessDataStatetments.setString(++parameterIndex, null);
//	insertProcessDataStatetments.setString(++parameterIndex, null);
	insertProcessDataStatetments.setString(++parameterIndex, null);
	insertProcessDataStatetments.setString(++parameterIndex, String.valueOf(this.cellId));
	insertProcessDataStatetments.setString(++parameterIndex, null);
	insertProcessDataStatetments.setString(++parameterIndex, String.valueOf(this.tampervalue));
	insertProcessDataStatetments.setString(++parameterIndex, String.valueOf(this.batteryVoltage.intValue()));
	insertProcessDataStatetments.setString(++parameterIndex, String.valueOf(this.IGN));
	insertProcessDataStatetments.setString(++parameterIndex, "0");
	insertProcessDataStatetments.setString(++parameterIndex, "0");
	insertProcessDataStatetments.setString(++parameterIndex, "0");
	insertProcessDataStatetments.setString(++parameterIndex, "0");
	insertProcessDataStatetments.setString(++parameterIndex, "0");
	insertProcessDataStatetments.setString(++parameterIndex, "0");
	insertProcessDataStatetments.setString(++parameterIndex, "0");
	insertProcessDataStatetments.setString(++parameterIndex, "0");
	insertProcessDataStatetments.setString(++parameterIndex, "0");
	insertProcessDataStatetments.setString(++parameterIndex, "0");
	insertProcessDataStatetments.setString(++parameterIndex, "0");
	insertProcessDataStatetments.setString(++parameterIndex, "44");
	insertProcessDataStatetments.setString(++parameterIndex, "0");
	insertProcessDataStatetments.setString(++parameterIndex, this.gsmSignal);
	insertProcessDataStatetments.setString(++parameterIndex, this.swVersion);
	insertProcessDataStatetments.setString(++parameterIndex, String.valueOf(new Timestamp(new java.util.Date().getTime())));
	insertProcessDataStatetments.setString(++parameterIndex, String.valueOf(panic));
	insertProcessDataStatetments.setString(++parameterIndex, String.valueOf(this.dataTimestamp));
	insertProcessDataStatetments.setString(++parameterIndex, String.valueOf(this.clientid));
//	insertProcessDataStatetments.setString(++parameterIndex, String.valueOf(this.location));
	
	insertProcessDataStatetments.executeUpdate();
	
	
	System.out.println("executed");
	}catch(Exception e){
		System.out.println("insertHistoryProcessVehicleDataSQL "+e);
	}
}



}
