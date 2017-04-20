package vts.vtsbackend.protocol;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.AMQP.BasicProperties;

import vts.vtsbackend.VTSClientID;
import vts.vtsbackend.VTSDBConnectionHandler;
import vts.vtsbackend.VTSDataIOMessage;
import vts.vtsbackend.VTSUtilityHandler;
import vts.vtsbackend.mssql.MSSQLBackendHandler;
import vts.vtsbackend.pgsql.PGSQLBackendHandler;
import vts.vtsbackend.rabbitmq.VTSRabbitMQClient;


public class VTSPacketConsumer extends DefaultConsumer {

	public final static Logger logger = LoggerFactory.getLogger(VTSPacketConsumer.class);
	public final VTSRabbitMQClient rabbitMQClient;
	private final Channel serverChannel;
	private final VTSDBConnectionHandler vtsDBHandler;
	
	
	private PreparedStatement logPreparedStatementSQL;
	
	private final String insertDeviceRecordQuerySQL = 
			"INSERT INTO RawDataAFM7878(stringstatus,StringText,ReceivedDateTime) VALUES "+
            "(?,?,?)";
	
	private final String devicedata_exceptionlogInsertQuery = "INSERT INTO devicedata_exception_log(dataMessage, clientFQDN, clientPort) VALUES(?, ?, ?)";
	private PreparedStatement exceptionLogPreparedStatement;
	
	private final String deviceMasterQuery = "INSERT INTO devicemaster(imeino, datatimestamp, serverport)"+
										"VALUES (?, ?, ?)";
	private PreparedStatement deviceMasterStatement;
	
	private final String devicedata_logsInsertQuery = "INSERT INTO devicedata_logs(dataMessage, clientFQDN, clientPort) VALUES(?, ?, ?)";
	private PreparedStatement logPreparedStatement;
	
//	[insertrawdata_Process_sansation] 6565 select [IndiaLocation].dbo.[getLocationbyLonLat_fun] (21.00,78.00)
	 
	private PreparedStatement locationstatement;
	private final String locationSqlserverSQL = "select [IndiaLocation].dbo.[getLocationbyLonLat_fun] (?, ?)";
	
	private PreparedStatement rsrtcSqlserverstatement;
	private final String rsrtcSqlserverSQL = "EXEC insertrawdata_Process ?, ?";
	
	private PreparedStatement rsrtcSqlserverstatement2;
	private final String rsrtcSqlserverSQL2 = "EXEC insertrawdata_Process ?, ?";
	
	private PreparedStatement selectDeviceMasterStatement2;
	private PreparedStatement selectDeviceMasterStatement;
	private final String selectDeviceMasterSQL = "select deviceid from devicemaster where imeino = ?"; 
	
	private PreparedStatement selectVehicleMasterStatement2;
	private PreparedStatement selectVehicleMasterStatement;
	private final String selectVehicleMasterSQL = "select vehicleid, clientid, vehicleregno, vehicletype, orgid from vehicle where deviceid = ?"; 
	
	private PreparedStatement selectCurrentVehicleStatement2;
	private PreparedStatement selectCurrentVehicleStatement;
	private final String selectCurrentVehicleSQL = "select StringDateTime from CurrentVehicleString where deviceid = ?";
	
	
	private PreparedStatement insertRawDataStmt1;
	private PreparedStatement insertRawDataStmt2;
	private final String insertRawDataSQL = "insert into RawDataNew_sansation(stringstatus,StringText,ReceivedDateTime) values(?, ?, ?) "; 
	
	private PreparedStatement insertCurrentDataStatetments2;
	private PreparedStatement insertCurrentDataStatetments;
	private final String insertCurrentDataSQL = "INSERT INTO [dbo].[CurrentVehicleString] "+
           "([VehicleID] ,[VehicleRegNo] ,[VehicleType] ,[OrgID] ,[ClientID] ,[HubID] ,[DeviceID] ,[GPSStatus] "+
           ",[Lat] ,[Lon] ,[NoOfSatellite] ,[Alt] ,[Heading] ,[Speed] ,[UTCDate] ,[UTCTime] ,[TimeZone] "+
           ",[CellID] ,[OdoMeter] ,[DeviceTamperingStatus] ,[MainBatteryStatus] ,[IgnitionStatus] "+
           ",[DigitalInput1] ,[DigitalInput2] ,[DigitalInput3] ,[DigitalInput4] ,[DigitalOutput1] "+
           ",[DigitalOutput2] ,[AnalogInput1] ,[AnalogInput2] ,[InternalBatteryChargingStatus] "+
           ",[InternalBatteryVoltage] ,[InternalBatteryChargingCondition] ,[TypeOfData] "+
           ",[AlertType] ,[GSMSignalStrength] ,[Version] ,[RecievedDateTime] ,[Panic] "+
           ",[StringDateTime] ,[LocationFlag] "+
           ",[GeoFenceID] ,[GeofenceStatus] ,[TripStatus] ,[TripStatusChangeTime] ,[PickTripStartTime] "+
           ",[PickTripEndTime] ,[DropTripStartTime] ,[DropTripEndTime] ,[OverSpeedFlag] ,[RouteID] "+
           ",[ETATrip] ,[ETATripflag] ,[processstatus] "+
           ",[SequenceNo] ,[tripid] ,[routedeviation] ,[orgfuelvalue]) "+
           "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"
           		 + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?"
           		 + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	
	private PreparedStatement updateCurrentDataStatetments2;
	private PreparedStatement updateCurrentDataStatetments;
	private final String updateCurrentDataSQL = "update [dbo].[CurrentVehicleString] set "+
           "[VehicleID] = ?,[VehicleRegNo]  = ?,[VehicleType]  = ?,[OrgID]  = ?,[ClientID]  = ?,[HubID]  = ?,[DeviceID]  = ?,[GPSStatus] = ? "+
           ",[Lat]  = ?,[Lon]  = ?,[NoOfSatellite]  = ?,[Alt]  = ?,[Heading]  = ?,[Speed]  = ? "+
           ",[CellID]  = ?,[DeviceTamperingStatus]  = ?,[MainBatteryStatus]  = ?,[IgnitionStatus] = ? "+
           ",[DigitalInput1]  = ?,[DigitalInput2]  = ?,[DigitalInput3]  = ?,[DigitalInput4]  = ?,[DigitalOutput1] = ? "+
           ",[DigitalOutput2]  = ?,[AnalogInput1]  = ?,[AnalogInput2]  = ?,[InternalBatteryChargingStatus] = ? "+
           ",[InternalBatteryVoltage]  = ?,[InternalBatteryChargingCondition]  = ?,[TypeOfData] = ? "+
           ",[AlertType]  = ?,[GSMSignalStrength]  = ?,[Version]  = ?,[RecievedDateTime]  = ?,[Panic] = ? "+
           ",[StringDateTime]  = ? where DeviceID = ?";
	
	private PreparedStatement insertProcessDataStatetments2;
	private PreparedStatement insertProcessDataStatetments;
	private final String insertProcessDataSQL = "INSERT INTO [dbo].[ProcessedRawData] "+
           "([VehicleID]  ,[VehicleRegNo] ,[OrgID],[DeviceID] ,[HubID] ,[GPSStatus],[Lat], "+
        	"[Lon],[NoOfSatellite],[Alt],[Heading],[Speed],[TimeZone],[CellID], "+
        	"[OdoMeter],[DeviceTamperingStatus],[MainBatteryStatus],[IgnitionStatus],[DigitalInput1], "+
        	"[DigitalInput2],[DigitalInput3],[DigitalInput4],[DigitalOutput1],[DigitalOutput2], "+
        	"[AnalogInput1],[AnalogInput2],[InternalBatteryChargingStatus],[InternalBatteryVoltage], "+
        	"[InternalBatteryChargingCondition],[TypeOfData],[AlertType],[GSMSignalStrength], "+
        	"[Version],[RecievedDateTime],[Panic],[StringDateTime], [ClientID]) "+
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
            	  + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	private PreparedStatement insertHistoryProcessDataStatetments2;
	private PreparedStatement insertHistoryProcessDataStatetments;
	private final String insertHistoryProcessDataSQL =  "INSERT INTO [dbo].[tbl_historydata] "+
	           "([VehicleID]  ,[VehicleRegNo] ,[OrgID],[DeviceID] ,[HubID] ,[GPSStatus],[Lat], "+
	        	"[Lon],[NoOfSatellite],[Alt],[Heading],[Speed],[TimeZone],[CellID], "+
	        	"[OdoMeter],[DeviceTamperingStatus],[MainBatteryStatus],[IgnitionStatus],[DigitalInput1], "+
	        	"[DigitalInput2],[DigitalInput3],[DigitalInput4],[DigitalOutput1],[DigitalOutput2], "+
	        	"[AnalogInput1],[AnalogInput2],[InternalBatteryChargingStatus],[InternalBatteryVoltage], "+
	        	"[InternalBatteryChargingCondition],[TypeOfData],[AlertType],[GSMSignalStrength], "+
	        	"[Version],[RecievedDateTime],[Panic],[StringDateTime], [ClientID]) "+
	            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
	            	  + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	
	
	private PreparedStatement parsedProcedureSQLStatetments;
	private final String parsedProcedureSQL = "EXEC insertrawdata_Process_Sansation_GVK ?, ?";
	
	private final String insertDeviceRecordQuery = 
			"INSERT INTO parsed_device_record(datatimestamp, packettype, imeino, vehicleid, gpsstatus, packetdate, "+
			"packettime, latitude, latitudedirection, longitude, longitudedirection, vehiclespeed, vehicledirection, "+ 
			"vehiclestatus, tamperstatus, panicstatus, batteryvoltage, swversion, tripid, ignumber, checksum) " +
			"VALUES(?, ?, ?, ?, ?, ?, " +
			"?, ?, ?, ?, ?, ?, ?, " +
			"?, ?, ?, ?, ?, ?, ?, ?) ";
	private PreparedStatement insertDeviceRecordStmt;
	
	private final String insertDeviceAlertRecordQuery = 
			"INSERT INTO parsed_device_alert_record(datatimestamp, packettype, imeino, vehicleid, gpsstatus, packetdate, "+
			"packettime, latitude, latitudedirection, longitude, longitudedirection, vehiclespeed, vehicledirection, "+ 
			"vehiclestatus, tamperstatus, panicstatus, batteryvoltage, swversion, tripid, ignumber, checksum, "+
			"tripstart, tripend, tripfare, tripdistance, tripmode, meterstatus, speedsensorstatus) " +
			"VALUES(?, ?, ?, ?, ?, ?, " +
			"?, ?, ?, ?, ?, ?, ?, " +
			"?, ?, ?, ?, ?, ?, ?, ?, " +
			"?, ?, ?, ?, ?, ?, ?)";
	private PreparedStatement insertDeviceAlertRecordStmt;
	
	private final String insertDeviceHealthRecordQuery = 
			"INSERT INTO parsed_device_health_record(datatimestamp, packettype, imeino, gpsstatus, panicstatus, "
			+ "packetdatetime, numberOfSatellite, mainvoltage, internalvoltage) "
			+ "VALUES(?, ?, ?, ?, ?, "
			+ "?, ?, ?, ?)";
	
	
	private PreparedStatement insertDeviceHealthRecordStmt;
	
	private final String insertLocDeviceRecordQuery = 
			"INSERT INTO parsed_loc_device_record(datatimestamp, packettype, imeino, cellid, gpsstatus, packetdate, "+
			"packettime, latitude, latitudedirection, longitude, longitudedirection, vehiclespeed, vehicledirection, "+ 
			"gsmsignal, satellite, batteryvoltage, ignumber, digitalipstatus, tamperstatus, analogipvoltage, swversion, "+
			"distanceKM, address, rfidheader, rfidserialnumber) "+
			"VALUES(?, ?, ?, ?, ?, ?, " +
			"?, ?, ?, ?, ?, ?, ?, " +
			"?, ?, ?, ?, ?, ?, ?, ?, "+
			"?, ?, ?, ?) ";
	private PreparedStatement insertLocDeviceRecordStmt;
	
	private final String verifyConnectedDeviceRecord = 
			"SELECT * from connected_device_master where IMEINO = ?";
	private PreparedStatement verifyConnectedDeviceRecordStmt;
	
	private final String updateConnectedDeviceRecord =
			"UPDATE connected_device_master SET reactorkey = ?, "
			+ "clientKey = ?, packetdate = ?, packettime = ?, updatedTimeStamp = ? , "
			+ "vehicleid = ?, latitude = ?, latitudedirection = ?, "
			+ "longitude = ?, longitudedirection = ?, "
			+ "vehiclespeed = ?, vehicledirection = ?, "
			+ "gpsstatus = ? , ignumber = ?, tamperstatus = ?,"
			+ "packettype = ?, networkoperator = ? WHERE IMEINO = ?";
	private PreparedStatement updateConnectedDeviceRecordStmt;
	
	private final String updateConnectedDeviceRecordwhen0 =
			"UPDATE connected_device_master SET reactorkey = ?, "
			+ "clientKey = ?, packetdate = ?, packettime = ?, updatedTimeStamp = ? , "
			+ "vehicleid = ?, latitude = ?, latitudedirection = ?, longitude = ?, "
			+ "longitudedirection = ?, vehiclespeed = ?, vehicledirection = ?, "
			+ "gpsstatus = ? , ignumber = ?, tamperstatus = ?, panicstatus = ?, "
			+ "validlat=?, validlong=?, validtime=?, packettype=?, networkoperator=? "
			+ "WHERE IMEINO = ?";
	private PreparedStatement updateConnectedDeviceRecordStmtwhen0;
	
	private final String insertConnectedDeviceRecord = 
			"INSERT INTO connected_device_master(imeino, reactorKey, clientKey, packetdate, packettime, updatedTimeStamp,"
			+ "vehicleid, latitude, latitudedirection, longitude, longitudedirection, vehiclespeed, vehicledirection, "
			+ "gpsstatus, ignumber, tamperstatus) "
			+ "VALUES(?, ?, ?, ?, ?, ?,"
			+ "?, ?, ?, ?, ?, ?, ?, "
			+ "?, ?, ?)";
	private PreparedStatement insertConnectedDeviceRecordStmt;
	
	private Connection pgsqlConnection;
	private Connection sqlConnection;
	private String serverport;
	
	public VTSPacketConsumer(VTSRabbitMQClient mqClient, VTSDBConnectionHandler dbHandler) {
		super(mqClient.getClientChannel());
		rabbitMQClient = mqClient;
		
		this.serverChannel = mqClient.getClientChannel();
		vtsDBHandler = dbHandler;
		
		//Here assumed that initialization happened and hence we are in this constructor
		//So no retry logic for re-creating the connection
		initializePGSQLConnectionAndStatements();
		initializeMSSQLConnectionsAndStatements();
	}
	
	public void initializePGSQLConnectionAndStatements() {
		boolean retryConnection = false;
		pgsqlConnection = vtsDBHandler.getPgsqlConnection();
		
		if (pgsqlConnection == null) {
			retryConnection = true;
		} else {
		
			try {
				
				insertRawDataStmt2 = pgsqlConnection.prepareStatement(insertRawDataSQL);
//				logPreparedStatement = pgsqlConnection.prepareStatement(devicedata_logsInsertQuery);
//				exceptionLogPreparedStatement = pgsqlConnection.prepareStatement(devicedata_exceptionlogInsertQuery);
//				insertConnectedDeviceRecordStmt = pgsqlConnection.prepareStatement(insertConnectedDeviceRecord);
//				updateConnectedDeviceRecordStmt = pgsqlConnection.prepareStatement(updateConnectedDeviceRecord);
//				verifyConnectedDeviceRecordStmt = pgsqlConnection.prepareStatement(verifyConnectedDeviceRecord);
//				insertDeviceAlertRecordStmt = pgsqlConnection.prepareStatement(insertDeviceAlertRecordQuery);
//				insertDeviceRecordStmt = pgsqlConnection.prepareStatement(insertDeviceRecordQuery);
//				insertDeviceHealthRecordStmt = pgsqlConnection.prepareStatement(insertDeviceHealthRecordQuery);
//				insertLocDeviceRecordStmt = pgsqlConnection.prepareStatement(insertLocDeviceRecordQuery);
//				deviceMasterStatement = pgsqlConnection.prepareStatement(deviceMasterQuery);
			
			} catch (SQLException e) {
				logger.error("Exception in initializePGSQLConnectionAndStatements message={} and exception={}", e.getMessage(), e);
				logPreparedStatementSQL = null;
				parsedProcedureSQLStatetments = null;
				rsrtcSqlserverstatement = null;
				selectDeviceMasterStatement = null;
				insertCurrentDataStatetments = null;
				insertProcessDataStatetments = null;
				selectVehicleMasterStatement = null;
				updateCurrentDataStatetments = null;
				selectCurrentVehicleStatement = null;
				insertHistoryProcessDataStatetments = null;
				try {
					retryConnection = handlePGSQLException(e);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					//Thread.currentThread().stop();
				}
			}
		}
			
		if (retryConnection) {
			if (pgsqlConnection != null) {
				try {
					pgsqlConnection.close();
				} catch (SQLException e) {
					logger.error("sqlLogRawRecord MSSQL not null connection closing issue ");
				}
			}
			try {
				Thread.sleep(30000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			initializePGSQLConnectionAndStatements();
		}
	}
	
	public void initializeMSSQLConnectionsAndStatements() {
		boolean retryConnection = false;
		logger.info("In initializeMSSQLConnectionsAndStatements !!");
		sqlConnection = vtsDBHandler.getMssqlConnection();
		
		if (sqlConnection == null) {
			retryConnection = true;
		} else {
		
			try {
				
//				selectDeviceMasterStatement = sqlConnection.prepareStatement(selectDeviceMasterSQL);
//				selectVehicleMasterStatement = sqlConnection.prepareStatement(selectVehicleMasterSQL);
//				selectCurrentVehicleStatement = sqlConnection.prepareStatement(selectCurrentVehicleSQL);
//				updateCurrentDataStatetments = sqlConnection.prepareStatement(updateCurrentDataSQL);
//				insertProcessDataStatetments = sqlConnection.prepareStatement(insertProcessDataSQL);
//				insertHistoryProcessDataStatetments = sqlConnection.prepareStatement(insertHistoryProcessDataSQL);
//				locationstatement = sqlConnection.prepareStatement(locationSqlserverSQL);
				
				
				selectDeviceMasterStatement2 = sqlConnection.prepareStatement(selectDeviceMasterSQL);
				selectVehicleMasterStatement2 = sqlConnection.prepareStatement(selectVehicleMasterSQL);
				selectCurrentVehicleStatement2 = sqlConnection.prepareStatement(selectCurrentVehicleSQL);
				updateCurrentDataStatetments2 = sqlConnection.prepareStatement(updateCurrentDataSQL);
				insertProcessDataStatetments2 = sqlConnection.prepareStatement(insertProcessDataSQL);
				insertHistoryProcessDataStatetments2 = sqlConnection.prepareStatement(insertHistoryProcessDataSQL);
				rsrtcSqlserverstatement2 = sqlConnection.prepareStatement(rsrtcSqlserverSQL2);
				
				
			} catch (SQLException e) {
				logger.error("Exception in initializeMSSQLConnectionsAndStatements message={} and exception={}", e.getMessage(), e);
				try {
					retryConnection = handleMSSQLException(e);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					//Thread.currentThread().stop();
				}
			}
		}
		
		if (retryConnection) {
			if (sqlConnection != null) {
				try {
					sqlConnection.close();
					vtsDBHandler.closeMssqlConnection();
				} catch (SQLException e) {
					logger.error("sqlLogRawRecord MSSQL not null connection closing issue ");
				}
			}
			try {
				Thread.sleep(30000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			initializeMSSQLConnectionsAndStatements();
		}
	}
	
	@Override
	public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body)
			throws IOException {
		String message = new String(body, "UTF-8");
		//Consume the packet and detect the protocol
		logger.info("Received {} !!!", message);
		boolean packetConsumed = handlePacketProcessing(message);
		if (packetConsumed && serverChannel.isOpen())
			serverChannel.basicAck(envelope.getDeliveryTag(), false);
		else 
			logger.error("ERROR: Ack didn't go to RabbitMQ Server packetConsumed={}", packetConsumed);
	}
	
	private boolean handlePacketProcessing(String message) {
		boolean packetConsumed = false;
		
		ObjectMapper mapper = new ObjectMapper();
		
		try {
			VTSDataIOMessage devicePacketMessage = mapper.readValue(message, VTSDataIOMessage.class);
			
			//Log Raw packet to PGSQL Server 
//			packetConsumed = logRawRecord(devicePacketMessage, devicePacketMessage.getClientKey());
//			if (!packetConsumed)
//				return packetConsumed;
			
			// Log raw packet to MSSQL Server
			packetConsumed = sqlLogRawRecord(devicePacketMessage.getMessageReceived(), devicePacketMessage.getClientKey());
			if (!packetConsumed)
				return packetConsumed;
			
			int protocolDetected = identifyProtocolVendorAndProcessIt(devicePacketMessage);
			if (protocolDetected == 0)
				packetConsumed = true;
			
		} catch (JsonParseException e) {	
			logger.error("Exception in handlePacketProcessing message={} and exception={}", e.getMessage(), e);			
		} catch (JsonMappingException e) {
			logger.error("Exception in handlePacketProcessing message={} and exception={}", e.getMessage(), e);
		} catch (IOException e) {
			logger.error("Exception in handlePacketProcessing message={} and exception={}", e.getMessage(), e);
		} catch (Exception e) {
			logger.error("Exception in handlePacketProcessing message={} and exception={}", e.getMessage(), e);
		}
		
		return packetConsumed;
	}
	
	private int delhiProtocolHandler(VTSDataIOMessage ioMessage) {
		String clientMessage = ioMessage.getMessageReceived();
		serverport = ioMessage.getServerPort();
		String commaDelimitor = ",";
		int returnValue = 0;
		Pattern delhiLivePattern = Pattern.compile("@LIVE|@PWR-OUT|@PWR-IN|@TAMPERED|@TAMPER-RESET|@LOBAT|@BATOK|@PANIC");
		Pattern delhiHistoryPattern = Pattern.compile("@HISTORY-LIVE|@HISTORY-PWR-OUT|@HISTORY-PWR-IN|@HISTORY-TAMPERED|"
				+ "@HISTORY-TAMPER-RESET|@HISTORY-LOBAT|@HISTORY-BATOK|@HISTORY-PANIC");
		Pattern delhiAlertPattern = Pattern.compile("@TRIPSTART|@HISTORY-TRIPSTART|@TRIPSTOP|@HISTORY-TRIPSTOP");
		Pattern delhiHealthPattern = Pattern.compile("@HEALTH|@HISTORY-HEALTH");
		
		/* Now just tokenize it with COMMA and see if you see any of the matching word from DELHI */
		String[] messages = clientMessage.split("\n");
		
		for (String receivedMessage : messages) {
			
			if (receivedMessage == null || receivedMessage.isEmpty() || receivedMessage.length() == 0) {
				logger.info("EMPTY PACKET while processing Delhi Message !");
				continue;
			}
			
			boolean updateConnectedDeviceMaster = true;
			String[] messageToken = receivedMessage.split(commaDelimitor);
			RosMertaProtocol protocolHandler = new RosMertaProtocol();
			if (delhiLivePattern.matcher(messageToken[0]).find()) {
				logger.info("DELHI PATTERN -- LIVE DATA");				
				returnValue = protocolHandler.processClientMessage(messageToken, "LIVE");
				// Insert into parsed_device_record with live tag ?
				if (VTSUtilityHandler.validDateFromDevice(protocolHandler.getDataTimestamp()))
					protocolHandler.insertParsedDeviceRecord(insertDeviceRecordStmt);
				else {
					updateConnectedDeviceMaster = false;
					logExceptionRecord(ioMessage.getMessageReceived(), ioMessage.getClientKey());
				}
			} else if (delhiHistoryPattern.matcher(messageToken[0]).find()) {
				logger.info("DELHI PATTERN -- HISTORY DATA");
				returnValue = protocolHandler.processClientMessage(messageToken, "HISTORY");
				// Insert into parsed_device_record with history tag ? 
				if (VTSUtilityHandler.validDateFromDevice(protocolHandler.getDataTimestamp()))
					protocolHandler.insertParsedDeviceRecord(insertDeviceRecordStmt);
				else {
					updateConnectedDeviceMaster = false;
					logExceptionRecord(ioMessage.getMessageReceived(), ioMessage.getClientKey());
				}
				
			} else if (delhiAlertPattern.matcher(messageToken[0]).find()) {
				logger.info("DELHI PATTERN -- ALERT DATA");
				returnValue = protocolHandler.processClientMessage(messageToken, "ALERT");
				// Insert into parsed_device_alert_record with alert data tag ? 
				if (VTSUtilityHandler.validDateFromDevice(protocolHandler.getDataTimestamp()))
					protocolHandler.insertParsedDeviceAlertRecord(insertDeviceAlertRecordStmt);
				else {
					logExceptionRecord(ioMessage.getMessageReceived(), ioMessage.getClientKey());
					updateConnectedDeviceMaster = false;
				}
				
			} else if (delhiHealthPattern.matcher(messageToken[0]).find()) {
				logger.info("DELHI PATTERN -- HEALTH DATA");
				returnValue = protocolHandler.processHealthMessage(messageToken, "HEALTH");
				// Just insert into healthLive and healthHistory table 
				if (VTSUtilityHandler.validDateFromDevice(protocolHandler.getDataTimestamp()))
					protocolHandler.insertParsedHealthDeviceRecord(insertDeviceHealthRecordStmt);
				else 
					logExceptionRecord(ioMessage.getMessageReceived(), ioMessage.getClientKey());
				
				updateConnectedDeviceMaster = false;
			} else {
				updateConnectedDeviceMaster = false;
			}
			
			// updateConnectedDeviceMaster as record has been successfully parsed and we know IMEI number 
			if (updateConnectedDeviceMaster)
				updateConnectedDeviceMaster(ioMessage.getReactorKey(), ioMessage.getClientKey(), protocolHandler);
			else {
				logger.info("This time not updating connected_device_master");
			}
		}
		
		logger.info("DELHI PATTERN PROCESSING DONE returnValue={} ", returnValue);
		return returnValue;
	}
	
	public int diksuchiProtocolHandler(VTSDataIOMessage ioMessage) {
		
		String clientMessage = ioMessage.getMessageReceived();
		serverport = ioMessage.getServerPort();
		int returnValue = 0;
		logger.info("DIKSUCHI PROTOCOL FOUND !!!");
		DiksuchiProtocol protocolHandler = new DiksuchiProtocol(vtsDBHandler.getPgsqlConnection());
		int ackResponseCode = 0;
		try {
			logger.info("Going to process the packet ?");
			ackResponseCode = protocolHandler.processClientMessageV3(clientMessage.substring(1, clientMessage.length()-1));
			logger.info("Came out with ackResponseCode = {}", ackResponseCode);
		} catch (Exception exp) {
			logger.error("Exception while processing packet {}", exp);
			return -1;
		}
		
		if (ackResponseCode != -1) {
			String clientResponseMessage = protocolHandler.getResponseAckMessage(ackResponseCode);
			//Okay we have the constructed response command which we need to send back to the device ? 
			//Construct the message and dump into the reactorQueue
			String responseMessage = "{"+
					"\"reactorKey\":\""+ ioMessage.getReactorKey() + "\"," +
					"\"clientKey\":\""+ ioMessage.getClientKey() + "\"," +
					"\"messageReceived\":\""+ clientResponseMessage +"\"" +
					"}";
			
			logger.info("Posting message={} at queue={}", responseMessage, ioMessage.getReactorKey());
			rabbitMQClient.publishMessageToReactorQueue(responseMessage, ioMessage.getReactorKey());
			if (protocolHandler.getPacketType() == 1) { //Position Packet 
				GenericProtocol genericProtocol = new GenericProtocol(protocolHandler.getDeviceIMEI(), protocolHandler.getLatitude(), 
					protocolHandler.getLongitude(), protocolHandler.getPacketTimestamp());
				updateConnectedDeviceMaster(ioMessage.getReactorKey(), ioMessage.getClientKey(), genericProtocol);
				logger.info("Updated connection Master !!!");
			}
		} else {
			logger.info("No Response would be sent back to device !!! {}", ackResponseCode);
		}
		return returnValue;
	}
	
	private int locBackProtocolHandler(VTSDataIOMessage ioMessage) {
		boolean retryConnection = false;
		String clientMessage = ioMessage.getMessageReceived();
		serverport = ioMessage.getServerPort();
		int returnValue = 0;
		String commaDelimitor = ",";
		String[] messageToken = clientMessage.split(commaDelimitor);
		LocBackProtocol locBackProtocol = new LocBackProtocol();
		
		try{
			//System.out.println("changes applied");
//			locBackProtocol.insertParsedDataSQL(rsrtcSqlserverstatement2, clientMessage, serverport);
//			locBackProtocol.insertParsedDataSQL(rsrtcSqlserverstatement, clientMessage, serverport);
			if(messageToken[0].contains("$loc") || messageToken[0].contains("$bak") || messageToken[0].contains("$rmv") ||
					messageToken[0].contains("$btl") || messageToken[0].contains("$ion") || messageToken[0].contains("$iof") ||
					messageToken[0].contains("$rid") || messageToken[0].contains("$cam")) {
				
				returnValue = locBackProtocol.processClientMessage(messageToken, "LOCATION");
				long k = VTSUtilityHandler.compareTwoTimeStamps(new Timestamp(new java.util.Date().getTime()), locBackProtocol.getDataTimestamp());
				
//				System.out.println("mins = "+k);
				if(k < 20){
//					try{
	//					locBackProtocol.selectLocationSQL(locationstatement);
//						locBackProtocol.setLocation(getLocationClass.getLoc(locBackProtocol.latitude, locBackProtocol.longitude));
//					}catch(Exception e){
	//					System.out.println(e);
//					}
					
					int m = locBackProtocol.selectDeviceDataSQL(selectDeviceMasterStatement2);
					if(m==1 && locBackProtocol.latitude>0.00 && locBackProtocol.longitude>0.00){
//	//					System.out.println("deviceid selected from devicemaster");
//						locBackProtocol.selectVehicleDataSQL(selectVehicleMasterStatement);
//						Timestamp h = locBackProtocol.selectCurrentVehicleDataSQL(selectCurrentVehicleStatement);
//						System.out.println(locBackProtocol.dataTimestamp+" "+h);
//						if(locBackProtocol.dataTimestamp.after(h)){
//							System.out.println("deviceid selected from currentvehicle");
//							locBackProtocol.updateCurrentVehicleDataSQL(updateCurrentDataStatetments);
//						}
//						locBackProtocol.insertProcessVehicleDataSQL(insertProcessDataStatetments);
//						locBackProtocol.insertHistoryProcessVehicleDataSQL(insertHistoryProcessDataStatetments);
						
	//		--------------------------------------SQL2------------------------------------------------			
						locBackProtocol.selectVehicleDataSQL(selectVehicleMasterStatement2);
						Timestamp h = locBackProtocol.selectCurrentVehicleDataSQL(selectCurrentVehicleStatement2);
						//System.out.println(locBackProtocol.dataTimestamp+" "+h);
						if(locBackProtocol.dataTimestamp.after(h)){
							//System.out.println("deviceid selected from currentvehicle");
							locBackProtocol.updateCurrentVehicleDataSQL(updateCurrentDataStatetments2);
						}
						locBackProtocol.insertProcessVehicleDataSQL(insertProcessDataStatetments2);
						locBackProtocol.insertHistoryProcessVehicleDataSQL(insertHistoryProcessDataStatetments2);
					}
	//				
	//				if (returnValue == 0 && VTSUtilityHandler.validDateFromDevice(locBackProtocol.getDataTimestamp())) {
	//					locBackProtocol.insertLocDeviceRecord(insertLocDeviceRecordStmt);
	//					// updateConnectedDeviceMaster as record has been successfully parsed and we know IMEI number 
	//					
	//					updateConnectedDeviceMaster(ioMessage.getReactorKey(), ioMessage.getClientKey(), locBackProtocol);
	//					logger.info("LOC2 PATTERN PROCESSING DONE returnValue={} ", returnValue);
	//				} else {
	//					logger.info("LOC2 PATTERN PROCESSING DONE BUT Data didn't store into DB due to parsering error");
	//				}
				}
				
				
//				if (returnValue == 0 && VTSUtilityHandler.validDateFromDevice(locBackProtocol.getDataTimestamp())) {
//					locBackProtocol.insertLocDeviceRecord(insertLocDeviceRecordStmt);
//					// updateConnectedDeviceMaster as record has been successfully parsed and we know IMEI number 
////					System.out.println("In update");
//					updateConnectedDeviceMaster(ioMessage.getReactorKey(), ioMessage.getClientKey(), locBackProtocol);
//					logger.info("LOC2 PATTERN PROCESSING DONE returnValue={} ", returnValue);
//				} else {
//					logger.info("LOC2 PATTERN PROCESSING DONE BUT Data didn't store into DB due to parsering error");
//				}
				
			}else{
				System.out.println("in else");
			}
		} catch (SQLException ex) {
			logger.error("Exception in MSSQL Procedure message={} and exception={}", ex.getMessage(), ex);
			try {
				retryConnection = handleMSSQLException(ex);
			} catch (Exception e) {
				logger.error("Exception in HISTORY DATA selectmaster while handling exception message={} and exception={}", e.getMessage(), e);
				//Kill the thread ??? 
				Thread.currentThread().stop();
				//return false;
			}	
		} 
		finally {
			if (retryConnection) {
				if (sqlConnection != null) {
					try {
						sqlConnection.close();
						vtsDBHandler.closeMssqlConnection();
					} catch (SQLException e) {
						logger.error("LIVE DATA selectmaster MSSQL not null connection closing issue ");
					}
				}
				initializeMSSQLConnectionsAndStatements();
//				initializePGSQLConnectionAndStatements();
			}
		}
		
		
		return returnValue;
	}
	
	
	private int orsacHandler(VTSDataIOMessage ioMessage) {
		boolean retryConnection = false;
		String clientMessage = ioMessage.getMessageReceived();
		serverport = ioMessage.getServerPort();
		int returnValue = 0;
		String commaDelimitor = ",";
//		Pattern location = Pattern.compile("$loc|$bak|$rmv|$btl|$ion|$iof|$rid|$cam|$loc2|$bak2");
//		String location = "$loc";
//		String backlocation = "$bak";
		String[] messageToken = clientMessage.split(commaDelimitor);
		orissac locBackProtocol = new orissac();
		try{
			//System.out.println("changes applied");
			//locBackProtocol.insertParsedDataSQL(rsrtcSqlserverstatement, clientMessage);
			if(messageToken[0].contains("$rsm") || messageToken[0].contains("$LIVE") || messageToken[0].contains("$History") || messageToken[0].contains("$TRMB")) {
				//System.out.println("in if");
				returnValue = locBackProtocol.processClientMessage(messageToken, "LOCATION");
			}else if (messageToken[0].contains("$VTU")) {
				returnValue = locBackProtocol.processClientMessage1(messageToken, "LOCATION");
			}else{
				returnValue = locBackProtocol.processClientMessagef(messageToken, "LOCATION");
			}
				
//				int m = locBackProtocol.selectDeviceDataSQL(selectDeviceMasterStatement);
//				if(m==1){
//					System.out.println("deviceid selected from devicemaster");
//					locBackProtocol.selectVehicleDataSQL(selectVehicleMasterStatement);
//					Timestamp h = locBackProtocol.selectCurrentVehicleDataSQL(selectCurrentVehicleStatement);
//					System.out.println(locBackProtocol.dataTimestamp+" "+h);
//					if(locBackProtocol.dataTimestamp.after(h)){
//						System.out.println("deviceid selected from currentvehicle");
//						locBackProtocol.updateCurrentVehicleDataSQL(updateCurrentDataStatetments);
//					}
//					locBackProtocol.insertProcessVehicleDataSQL(insertProcessDataStatetments);
//					locBackProtocol.insertHistoryProcessVehicleDataSQL(insertHistoryProcessDataStatetments);
//				}
//			System.out.println(returnValue+" date "+locBackProtocol.getDataTimestamp());
				if (returnValue == 0 && VTSUtilityHandler.validDateFromDevice(locBackProtocol.getDataTimestamp())) {
					//System.out.println("in insert");
					locBackProtocol.insertLocDeviceRecord(insertLocDeviceRecordStmt);
					// updateConnectedDeviceMaster as record has been successfully parsed and we know IMEI number 
					
					updateConnectedDeviceMaster(ioMessage.getReactorKey(), ioMessage.getClientKey(), locBackProtocol);
					logger.info("LOC2 PATTERN PROCESSING DONE returnValue={} ", returnValue);
				} else {
					logger.info("LOC2 PATTERN PROCESSING DONE BUT Data didn't store into DB due to parsering error");
				}
//			}else{
//				System.out.println("in else");
//			}
		} catch (Exception ex) {
			logger.error("Exception in MSSQL Procedure message={} and exception={}", ex.getMessage(), ex);
//			try {
//				retryConnection = handlePGSQLException(ex);
//			} catch (Exception e) {
//				logger.error("Exception in HISTORY DATA selectmaster while handling exception message={} and exception={}", e.getMessage(), e);
//				//Kill the thread ??? 
//				Thread.currentThread().stop();
//				//return false;
//			}	
		} 
//		finally {
//			if (retryConnection) {
//				if (sqlConnection != null) {
//					try {
//						sqlConnection.close();
//						vtsDBHandler.closeMssqlConnection();
//					} catch (SQLException e) {
//						logger.error("LIVE DATA selectmaster MSSQL not null connection closing issue ");
//					}
//				}
//				initializeMSSQLConnectionsAndStatements();
//			}
//		}
		
		
		return returnValue;
	}
	
	
	private int diksuchi_9999(VTSDataIOMessage ioMessage) {
		String clientMessage = ioMessage.getMessageReceived();
		serverport = ioMessage.getServerPort();
		int returnValue = 0;
		Diksuchi_9999 dik99 = new Diksuchi_9999();
		try {
			returnValue = dik99.processClientMessage(clientMessage);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Processing done");
		if (returnValue == 0) {
//				locBackProtocol.insertLocDeviceRecord(insertLocDeviceRecordStmt);
			// updateConnectedDeviceMaster as record has been successfully parsed and we know IMEI number 
			try{
			if(dik99.latitude > 0.000 && dik99.longitude > 0.000){
				dik99.insertParsedDeviceRecord(insertDeviceRecordStmt);
				try{
					updateConnectedDeviceMaster(ioMessage.getReactorKey(), ioMessage.getClientKey(), dik99);
				}catch(Exception e){
					
				}
			}
			}catch(Exception e){
				System.out.println(e);
			}
			//System.out.println("About to send message");
			
			//Okay we have the constructed response command which we need to send back to the device ? 
			//Construct the message and dump into the reactorQueue
			//System.out.println(clientResponseMessage);
			
			logger.info("Diksuchi_9999 PATTERN PROCESSING DONE returnValue={} ", returnValue);
		} else {
			logger.info("Diksuchi_9999 PATTERN PROCESSING DONE BUT Data didn't store into DB due to parsering error");
		}
//		int ackResponseCode = 1;
//		String clientResponseMessage = dik99.getResponseAckMessage(ackResponseCode);
//		String responseMessage = "{"+
//				"\"reactorKey\":\""+ ioMessage.getReactorKey() + "\"," +
//				"\"clientKey\":\""+ ioMessage.getClientKey() + "\"," +
//				"\"messageReceived\":\""+ clientResponseMessage +"\"" +
//				"}";
//		
//		logger.info("Posting message={} at queue={}", responseMessage, ioMessage.getReactorKey());
//		rabbitMQClient.publishMessageToReactorQueue(responseMessage, ioMessage.getReactorKey());
		return returnValue;
	}
	
	private int sa200Protocol(VTSDataIOMessage ioMessage) {
		String clientMessage = ioMessage.getMessageReceived();
		serverport = ioMessage.getServerPort();
		int returnValue = 0;
		String delimitor = ";";
		String[] messageToken = clientMessage.split(delimitor);
		SA200Protocol sa200 = new SA200Protocol();
		
		returnValue = sa200.processClientMessage(messageToken, "SA");
		System.out.println("processing done"+returnValue);
		if (returnValue == 0) {
//				locBackProtocol.insertLocDeviceRecord(insertLocDeviceRecordStmt);
			// updateConnectedDeviceMaster as record has been successfully parsed and we know IMEI number 
			sa200.insertParsedDeviceRecord(insertDeviceRecordStmt);
			System.out.println("devicedata inserted");
			updateConnectedDeviceMaster(ioMessage.getReactorKey(), ioMessage.getClientKey(), sa200);
			logger.info("SA200 PATTERN PROCESSING DONE returnValue={} ", returnValue);
		} else {
			logger.info("SA200 PATTERN PROCESSING DONE BUT Data didn't store into DB due to parsering error");
		}
		
		return returnValue;
	}
	
	private int identifyProtocolVendorAndProcessIt(VTSDataIOMessage ioMessage) {
		
		String clientMessage = ioMessage.getMessageReceived();
		
		logger.info("START identifyProtocolVendorAndProcessIt !!!!");
				
		if (clientMessage.charAt(0) == 64 ){ /* Starting is @ */
			return delhiProtocolHandler(ioMessage);
		}
		
		/* Identify 1st ($) and last byte (@)to check DIKSUCHI */
		if (clientMessage.charAt(0) == 36 /* Starting is $ */ && 
				clientMessage.charAt(clientMessage.length()-1) == 64 /* Starting is @ */) {
			return diksuchiProtocolHandler(ioMessage);
		}
		
		if (clientMessage.charAt(0) == 36 ){ /* Starting is $ */
			return locBackProtocolHandler(ioMessage);
		}
		

		if (clientMessage.charAt(0) == 83 ){
			return sa200Protocol(ioMessage);
		}
		
		
		if (clientMessage.charAt(0) == 94 ){
			return diksuchi_9999(ioMessage);
		}
		
		logger.info("UNKNOWN PROTOCOL Message -- STILL LOG IT AND CLOSE THE CONNECTIION");

		return 0;
	}
	
	private void updateConnectedDeviceMaster(String reactorKey, String clientKey, GenericProtocol protocolHandler) {
		int parameterIndex = 0;
		try {
			
			String IMEINO = protocolHandler.getIMEINO();
			
			
			/* OLD CODE -- DO CLEAN UP 
			SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy hh:mm:ss");
			java.util.Date parsedDateTime = sdf.parse(protocolHandler.getDateField() + " " + protocolHandler.getTimeField());
			Timestamp inComingTimestamp = new Timestamp(parsedDateTime.getTime());
			*/
			
			Timestamp inComingTimestamp = protocolHandler.getDataTimestamp();
			try{
				deviceMasterStatement.setString(1, IMEINO);
				deviceMasterStatement.setTimestamp(2, inComingTimestamp);
				deviceMasterStatement.setString(3, serverport);
				deviceMasterStatement.executeUpdate();
			}catch(Exception e){
				//System.out.println(e);
			}
			
			verifyConnectedDeviceRecordStmt.setString(1, IMEINO);			
			ResultSet rs = verifyConnectedDeviceRecordStmt.executeQuery();
			if (rs != null && rs.next()) {
				// record found; just check if that is older than the incoming record 
				Timestamp storedTimestamp = rs.getTimestamp("updatedTimestamp");
				if (inComingTimestamp.after(storedTimestamp)) {
					//update record 
					
//					if(protocolHandler.getLatitude()>0.00 && protocolHandler.getLongitude()>0.00){
//						updateConnectedDeviceRecordStmtwhen0.setString(++parameterIndex, reactorKey);
//						updateConnectedDeviceRecordStmtwhen0.setString(++parameterIndex, clientKey);
//						Date packetDate = new Date(inComingTimestamp.getTime());
//						updateConnectedDeviceRecordStmtwhen0.setDate(++parameterIndex, packetDate);
//						updateConnectedDeviceRecordStmtwhen0.setTimestamp(++parameterIndex, inComingTimestamp); //packettime 
//						updateConnectedDeviceRecordStmtwhen0.setTimestamp(++parameterIndex, inComingTimestamp); //updatedtimestamp 
//						updateConnectedDeviceRecordStmtwhen0.setString(++parameterIndex, protocolHandler.getVehicleID());
//						updateConnectedDeviceRecordStmtwhen0.setDouble(++parameterIndex, protocolHandler.getLatitude());
//						updateConnectedDeviceRecordStmtwhen0.setString(++parameterIndex, protocolHandler.getLatitudeDirection());
//						updateConnectedDeviceRecordStmtwhen0.setDouble(++parameterIndex, protocolHandler.getLongitude());
//						updateConnectedDeviceRecordStmtwhen0.setString(++parameterIndex, protocolHandler.getLongitudeDirection());
//						updateConnectedDeviceRecordStmtwhen0.setDouble(++parameterIndex, protocolHandler.getVehicleSpeed());
//						updateConnectedDeviceRecordStmtwhen0.setDouble(++parameterIndex, protocolHandler.getVehicleDirection());
//						updateConnectedDeviceRecordStmtwhen0.setString(++parameterIndex, protocolHandler.getGpsStatus());
//						updateConnectedDeviceRecordStmtwhen0.setInt(++parameterIndex, protocolHandler.getIGN());
//						updateConnectedDeviceRecordStmtwhen0.setString(++parameterIndex, protocolHandler.getTamperStatus());
//						updateConnectedDeviceRecordStmtwhen0.setInt(++parameterIndex,protocolHandler.getPanic());
//						updateConnectedDeviceRecordStmtwhen0.setDouble(++parameterIndex, protocolHandler.getLatitude());
//						updateConnectedDeviceRecordStmtwhen0.setDouble(++parameterIndex, protocolHandler.getLongitude());
//						updateConnectedDeviceRecordStmtwhen0.setTimestamp(++parameterIndex, inComingTimestamp);
//						//Where Clause
//						updateConnectedDeviceRecordStmtwhen0.setString(++parameterIndex, IMEINO);
//						updateConnectedDeviceRecordStmtwhen0.executeUpdate();
//					}else{
						updateConnectedDeviceRecordStmt.setString(++parameterIndex, reactorKey);
						updateConnectedDeviceRecordStmt.setString(++parameterIndex, clientKey);
						Date packetDate = new Date(inComingTimestamp.getTime());
						updateConnectedDeviceRecordStmt.setDate(++parameterIndex, packetDate);
						updateConnectedDeviceRecordStmt.setTimestamp(++parameterIndex, inComingTimestamp); //packettime 
						updateConnectedDeviceRecordStmt.setTimestamp(++parameterIndex, inComingTimestamp); //updatedtimestamp 
						updateConnectedDeviceRecordStmt.setString(++parameterIndex, protocolHandler.getVehicleID());
						updateConnectedDeviceRecordStmt.setDouble(++parameterIndex, protocolHandler.getLatitude());
						updateConnectedDeviceRecordStmt.setString(++parameterIndex, protocolHandler.getLatitudeDirection());
						updateConnectedDeviceRecordStmt.setDouble(++parameterIndex, protocolHandler.getLongitude());
						updateConnectedDeviceRecordStmt.setString(++parameterIndex, protocolHandler.getLongitudeDirection());
						updateConnectedDeviceRecordStmt.setDouble(++parameterIndex, protocolHandler.getVehicleSpeed());
						updateConnectedDeviceRecordStmt.setDouble(++parameterIndex, protocolHandler.getVehicleDirection());
						updateConnectedDeviceRecordStmt.setString(++parameterIndex, protocolHandler.getGpsStatus());
						updateConnectedDeviceRecordStmt.setInt(++parameterIndex, protocolHandler.getIGN());
						updateConnectedDeviceRecordStmt.setString(++parameterIndex, protocolHandler.getTamperStatus());
						updateConnectedDeviceRecordStmt.setString(++parameterIndex, protocolHandler.getPacketstatus());
						updateConnectedDeviceRecordStmt.setString(++parameterIndex, protocolHandler.getNetworkoperator());
//						System.out.println("in update data "+protocolHandler.getPacketstatus()+" network =  "+protocolHandler.getNetworkoperator());
//						updateConnectedDeviceRecordStmt.setInt(++parameterIndex,protocolHandler.getPanic());
						//Where Clause
						updateConnectedDeviceRecordStmt.setString(++parameterIndex, IMEINO);
						updateConnectedDeviceRecordStmt.executeUpdate();
//					}
				}
			} 
			else {
				// insertRecord
				insertConnectedDeviceRecordStmt.setString(++parameterIndex, IMEINO);
				insertConnectedDeviceRecordStmt.setString(++parameterIndex, reactorKey);
				insertConnectedDeviceRecordStmt.setString(++parameterIndex, clientKey);
				Date packetDate = new Date(inComingTimestamp.getTime());
				insertConnectedDeviceRecordStmt.setDate(++parameterIndex, packetDate);
				insertConnectedDeviceRecordStmt.setTimestamp(++parameterIndex, inComingTimestamp); //packettime 
				insertConnectedDeviceRecordStmt.setTimestamp(++parameterIndex, inComingTimestamp); //updatedtimestamp 
				
				insertConnectedDeviceRecordStmt.setString(++parameterIndex, protocolHandler.getVehicleID());
				insertConnectedDeviceRecordStmt.setDouble(++parameterIndex, protocolHandler.getLatitude());
				insertConnectedDeviceRecordStmt.setString(++parameterIndex, protocolHandler.getLatitudeDirection());
				insertConnectedDeviceRecordStmt.setDouble(++parameterIndex, protocolHandler.getLongitude());
				insertConnectedDeviceRecordStmt.setString(++parameterIndex, protocolHandler.getLongitudeDirection());
				insertConnectedDeviceRecordStmt.setDouble(++parameterIndex, protocolHandler.getVehicleSpeed());
				insertConnectedDeviceRecordStmt.setDouble(++parameterIndex, protocolHandler.getVehicleDirection());
				insertConnectedDeviceRecordStmt.setString(++parameterIndex, protocolHandler.getGpsStatus());
				insertConnectedDeviceRecordStmt.setInt(++parameterIndex, protocolHandler.getIGN());
				insertConnectedDeviceRecordStmt.setString(++parameterIndex, protocolHandler.getTamperStatus());
				
				insertConnectedDeviceRecordStmt.executeUpdate();
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error("Exception in updateConnectedDeviceMaster message={} and exception={}", e.getMessage(), e);
			//e.printStackTrace();
		}	
	}
	
	// No connection opened here;
	// No prepared statement creation here; as it is pre-initialized
	// Just take the message and dump into DB. 
	private boolean logRawRecord(VTSDataIOMessage message, String clientKey) {
		boolean retryConnection = false;
		
		
		VTSClientID clientID = new VTSClientID(clientKey);
		int parameterIndex = 1;
		try {
			logger.info("Going to INSERT INTO logRawRecord");
			logPreparedStatement.setString(parameterIndex++, message.getMessageReceived());
			logPreparedStatement.setString(parameterIndex++, clientID.getHostName());
			logPreparedStatement.setInt(parameterIndex++, Integer.parseInt(message.getServerPort()));
			logPreparedStatement.executeUpdate();
		} catch (SQLException ex) {
			logger.error("Exception in logRawRecord message={} and exception={}", ex.getMessage(), ex);
			try {
				retryConnection = handlePGSQLException(ex);
			} catch (Exception e) {
				logger.error("Exception in logRawRecord while handling exception message={} and exception={}", e.getMessage(), e);
				//Kill the thread ??? 
				//Thread.currentThread().stop();
				return false;
			}			
		} finally {
			if (retryConnection) {
				if (pgsqlConnection != null) {
					try {
						pgsqlConnection.close();
					} catch (SQLException e) {
						logger.error("PGSQL not null connection closing issue ");
					}
				}
				initializePGSQLConnectionAndStatements();
			}
		}
		logger.info("INSERTED INTO logRawRecord");
		return true;
	}
	
	public boolean handlePGSQLException(SQLException ex) throws Exception {
		boolean retry_connection = false;
		final String ss = ex.getSQLState();
		logger.error("Catched SQL Exception State = {}", ss);
		if (ss == null) return true;
		
		if (ss.equals("40001") || ss.equals("40P01")
				|| ss.startsWith("08") || ss.startsWith("53")) {
			/* It is a connection error or resource limit. Reconnect and retry. */
			try {
				pgsqlConnection.close();
		    } catch (SQLException ex1) { 
		        logger.error("Error closing suspected bad connection after SQLState={} ex={}", ss, ex1);
		    }
			pgsqlConnection = null; /* App knows to reconnect if it sees a null connection */
			retry_connection = true;
			Thread.sleep(30000);
		} else {
			throw new Exception(ex);
		}
		
		return retry_connection;
	}
	
	private boolean logExceptionRecord(String message, String clientKey) {

		boolean retryConnection = false;
		
		VTSClientID clientID = new VTSClientID(clientKey);
		int parameterIndex = 1;
		try {
			logger.info("Going to INSERT INTO logExceptionRecord");
			exceptionLogPreparedStatement.setString(parameterIndex++, message);
			exceptionLogPreparedStatement.setString(parameterIndex++, clientID.getHostName());
			exceptionLogPreparedStatement.setInt(parameterIndex++, clientID.getPortNumber());
			exceptionLogPreparedStatement.executeUpdate();
			
		} catch (SQLException ex) {
			logger.error("Exception in logExceptionRecord message={} and exception={}", ex.getMessage(), ex);
			try {
				retryConnection = handlePGSQLException(ex);
			} catch (Exception e) {
				logger.error("Exception in logExceptionRecord while handling exception message={} and exception={}", e.getMessage(), e);
				//Kill the thread ??? 
				//Thread.currentThread().stop();
				return false;
			}				
		} finally {
			if (retryConnection) {
				if (pgsqlConnection != null) {
					try {
						pgsqlConnection.close();
					} catch (SQLException e) {
						logger.error("logExceptionRecord PGSQL not null connection closing issue ");
					}
				}
				initializePGSQLConnectionAndStatements();
			}
		}
		logger.info("INSERTED INTO logExceptionRecord");
		return true;
	}
	
	public boolean handleMSSQLException(SQLException ex) throws Exception {
		boolean retry_connection = false;
		final String ss = ex.getSQLState();
		logger.error("Catched SQL Exception State = {}", ss);
		if (ss == null) return true;
		
		if (ss.equals("40001") || ss.equals("40P01") 
				|| ss.startsWith("08") || ss.startsWith("53")) {
			/* It is a connection error or resource limit. Reconnect and retry. */
			try {
				sqlConnection.close();
				vtsDBHandler.closeMssqlConnection();
		    } catch (SQLException ex1) { 
		        logger.error("Error closing suspected bad connection after SQLState={} ex={}", ss, ex1);
		    }
			sqlConnection = null; /* App knows to reconnect if it sees a null connection */
			retry_connection = true;
			Thread.sleep(30000);
		} else {
			throw new Exception(ex);
		}
		
		return retry_connection;
	}
	
	private boolean sqlLogRawRecord(String message, String clientKey) {
		boolean retryConnection = false;
		
		java.util.Date dd = new java.util.Date(); 
		
		int parameterIndex = 1;
		try {
			logger.info("Going to INSERT INTO sqlLogRawRecord");
			insertRawDataStmt2.setInt(parameterIndex++, 1);
			insertRawDataStmt2.setString(parameterIndex++, message);
			insertRawDataStmt2.setTimestamp(parameterIndex++, new Timestamp(dd.getTime()));
			insertRawDataStmt2.executeUpdate();
			
		} catch (SQLException ex) {
			logger.error("Exception in logRawRecord message={} and exception={}", ex.getMessage(), ex);
			try {
				retryConnection = handlePGSQLException(ex);
			} catch (Exception e) {
				logger.error("Exception in logRawRecord while handling exception message={} and exception={}", e.getMessage(), e);
				//Kill the thread ??? 
				//Thread.currentThread().stop();
				return false;
			}			
		} finally {
			if (retryConnection) {
				if (pgsqlConnection != null) {
					try {
						pgsqlConnection.close();
					} catch (SQLException e) {
						logger.error("PGSQL not null connection closing issue ");
					}
				}
				initializePGSQLConnectionAndStatements();
			}
		}
		logger.info("INSERTED INTO logRawRecord");
		return true;
	}
}
