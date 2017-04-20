package vts.vtsbackend.protocol;


import java.nio.ByteBuffer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import vts.vtsbackend.VTSUtilityHandler;
import vts.vtsbackend.pgsql.PGSQLBackendHandler;

public class DiksuchiProtocol {

	public final static Logger logger = LoggerFactory.getLogger(DiksuchiProtocol.class);
	
	
	private final Connection pgsqlConnection;
	/* Incoming Data */
	String clientMessage;
	/* Outgoing Data */
	ByteBuffer clientResponseBuffer = null;
	
	/* Header Information */
	public int communicationServerID;
	public String sourceIMEINumber;
	public int sequenceNumber;
	public int packetType;
	public static int ackPacketType = 255;
	public DiksuchiProtocolStatus protocolStatus;
	
	//Some more attributes for connected_device_master
	private Timestamp packetTimestamp;
	public Double latitude;
	public String latitudeDirection;
	public Double longitude;
	public String longitudeDirection;
	public Double vehicleSpeed;
	
	public DiksuchiProtocol(Connection inConnection) {
		pgsqlConnection = inConnection;
		sequenceNumber = 0;
	}
	
	public int getStatus() {
		return protocolStatus.getValue();
	}
	
	public String getDeviceIMEI() {
		return sourceIMEINumber;
	}
	
	public int getPacketType() {
		return packetType;
	}
	
	public Timestamp getPacketTimestamp() {
		return packetTimestamp;
	}
	
	public Double getLatitude() {
		return latitude;
	}
	
	public Double getLongitude() {
		return longitude;
	}
	
	private final String DIKSUCHI_POSITION_PACKET_QUERY = "INSERT INTO db_nrda_vtsdb.DIKSUCHI_POSITION_PACKET(packetType, IMEINO, sequenceNo, gpsDateTime, latitude, "
			+ "longitude, deviceSpeed, coarseOverGround, trackSatellite) values(?, ?, ?, ?, ?, ?, ?, ?, ?)" ;
	
	
	private final String DIKSUCHI_ROUTE_TRIP_PACKET_QUERY = "INSERT INTO db_nrda_vtsdb.DIKSUCHI_ROUTE_TRIP_PACKET(packetType, IMEINO, sequenceNo, scheduleID, "
			+ "routeNumberLength, routeNumber, tripNo, tripStatus, latitude, longitude, gpsDateTime) VALUES(?, ?, ?,"
			+ "?, ?, ?, ?, ?, ?, ?, ?)";
	
	
	private final String DIKSUCHI_BUSSTOP_PACKET_QUERY = "INSERT INTO db_nrda_vtsdb.DIKSUCHI_BUSSTOP_PACKET(packetType, IMEINO, sequenceNo, scheduleID, routeNumberLength, "
			+ "routeNumber, tripNo, busStopNameLength, busStopName, reachTime, stoppageDuration, deviationFromBusStop) "
			+ "VALUES(?, ? , ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	private final String DIKSUCHI_SCHEDULE_REQUEST_PACKET_QUERY = "INSERT INTO db_nrda_vtsdb.DIKSUCHI_SCHEDULE_REQUEST_PACKET(packetType, IMEINO, sequenceNo, gpsDateTime, "
			+ "scheduleID, latitude, longitude) VALUES(?, ?, ?, ?, ?, ?, ?)";
	
	private final String DIKSUCHI_MESSAGE_PACKET_QUERY = "INSERT INTO db_nrda_vtsdb.DIKSUCHI_MESSAGE_PACKET(packetType, IMEINO, sequenceNo,"
			+ "messageCodeNumber, gpsDateTime, latitude, longitude) VALUES(?, ?, ?, ?, ?, ?, ?)";
			
	private final String DIKSUCHI_KEEPALIVE_PACKET_QUERY = "INSERT INTO db_nrda_vtsdb.DIKSUCHI_KEEPALIVE_PACKET(packetType, IMEINO, sequenceNo, data) VALUES(?, ?, ?, ?)";
	
	private final String DIKSUCHI_DRIVING_PACKET_QUERY = "INSERT INTO db_nrda_vtsdb.DIKSUCHI_DRIVING_PACKET(packetType, IMEINO, sequenceNo, scheduleID, "
			+ "currentTripNumber, routeNumberLength, routeNumber, latitude, longitude, overSpeedStartTime, maximumSpeed, overSpeedDuration, "
			+ "exceptionStartTime, startingSpeed, endingSpeed, speedDuration) VALUES(?, ?, ?,"
			+ "?, ?, ?, ?, ?, ?,"
			+ "?, ?, ?, ?, ?, ?, ?)";
			
	private final String DIKSUCHI_ROUTE_DEVIATION_PACKET_QUERY = "INSERT INTO db_nrda_vtsdb.DIKSUCHI_ROUTE_DEVIATION_PACKET(packetType, IMEINO, sequenceNo, "
			+ "scheduleID, currentTripNumber, routeNumberLength, routeNumber, latitude, longitude, stopNameLength, "
			+ "firstViolatedStopName, distanceTraveledFromVioleted) VALUES(?, ? , ?"
			+ "?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
			
	private final String DIKSUCHI_DIGITAL_INPUT_PACKET_QUERY = "INSERT INTO db_nrda_vtsdb.DIKSUCHI_DIGITAL_INPUT_PACKET(packetType, IMEINO, sequenceNo, "
			+ "digitalInputType, digitalInputUpdateMode, digitalInputValue) VALUES(?, ?, ?,	?, ?, ?)";
					
	private final String DIKSUCHI_ANALOG_INPUT_PACKET_QUERY = "INSERT INTO db_nrda_vtsdb.DIKSUCHI_ANALOG_INPUT_PACKET(packetType, IMEINO, sequenceNo, "
			+ "analogInput, analogInputLowLevel, analogInputHighLevel, analogInputValue1, analogInputValue0)"
			+ " VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
	
							
	private final String DIKSUCHI_PID_STATUS_PACKET_QUERY = "INSERT INTO db_nrda_vtsdb.DIKSUCHI_PID_STATUS_PACKET(packetType, IMEINO, sequenceNo, "
			+ "hardwareRevision, serialNumber, bootLoaderSWVersion, applicationSWVersion, "
			+ "frontLibraryRevision, cpuPartNumber, cpuQualification, cpuTempRange, "
			+ "compilationFirmwareDateTime, flashUpdateStatusDateTime, testDateTime,"
			+ "articleNumberSignLevel, productionDate, endCustomer, orderNumber, "
			+ "busVehicleType, busBuilderNumber, languageCode, boardTempSensor, "
			+ "internalCPUTemp, minCPUTemp, maxCPUTemp, minBoardTemp, maxBoardTemp,"
			+ "maxPowerVoltage, minPowerVoltage, operatingHours, numberOfResets) "
			+ "VALUES (?, ?, ?,"
			+ "?, ?, ?, ?,"
			+ "?, ?, ?, ?,"
			+ "?, ?, ?,"
			+ "?, ?, ?, ?,"
			+ "?, ?, ?, ?,"
			+ "?, ?, ?, ?, ?,"
			+ "?, ?, ?, ?)";
	
	private final String DIKSUCHI_BOARD_DTC_STATUS_PACKET_QUERY = "INSERT INTO db_nrda_vtsdb.DIKSUCHI_DTC_STATUS_PACKET(packetType, IMEINO, sequenceNo,"
			+ "overVoltageCount, overVoltageValue, lowVoltageCount, lowVoltageValue, "
			+ "overHeatCount, overHeatValue) VALUES(?, ?, ?, "
			+ "?, ?, ?, ?, "
			+ "?, ?)";
									
	private final String DIKSUCHI_DTC_STATUS_PACKET_QUERY = "INSERT INTO db_nrda_vtsdb.DIKSUCHI_DTC_STATUS_PACKET(packetType, IMEINO, sequenceNo,"
			+ "overVoltageCount, overVoltageValue, lowVoltageCount, lowVoltageValue, "
			+ "overHeatCount, overHeatValue, watchDogReset, lowVoltageReset, "
			+ "lostCommunicationWithGPS, gpsSignalInvalid, gpsAntennaError,"
			+ "invalidStoageDevice, unknownUSBDeviceConnected, usbInvalidFS, usbOverCurrent)"
			+ "	VALUES(?, ?, ?,"
			+ "?, ?, ?, ?,"
			+ "?, ?, ?, ?,"
			+ "?, ?, ?,"
			+ "?, ?, ?, ?)";
	
																						
	private final String DIKSUCHI_MODE_PARAMETER_PACKET_QUERY = "INSERT INTO db_nrda_vtsdb.DIKSUCHI_MODE_PARAMETER_PACKET(packetType, IMEINO, sequenceNo,"
			+ "pollStatus, communicationMode, smsPhoneNumberLength, smsPhoneNumber,"
			+ "primaryIPAddressLength, primaryIPAddress, secondaryIPAddressLength, secondaryIPAddress, tcpPortNumber,"
			+ "apnLength, apnValue, urlLength, urlValue, dnsEnabled, dnsLength, dnsValue)"
			+ "	VALUES(?, ?, ?,"
			+ "	?, ?, ?, ?,"
			+ "	?, ?, ?, ?, ?"
			+ " ?, ?, ?, ?, ?, ?, ?)";
	
	private final String DIKSUCHI_DIGITALIO_PARAMETER_PACKET_QUERY = "INSERT INTO db_nrda_vtsdb.DIKSUCHI_DIGITALIO_PARAMETER_PACKET(packetType, IMEINO, sequenceNo,"
			+ "	digitalInputType1, digitalInputMode1, digitalInputValue1,"
			+ "	digitalInputType2, digitalInputMode2, digitalInputValue2,"
			+ "	digitalInputType3, digitalInputMode3, digitalInputValue3,"
			+ "	analogInput1, analogInput1LowLevelValue, analogInput1HighLevelValue, analogInput1Value1, analogInput1Value0,"
			+ "	analogInput2, analogInput2LowLevelValue, analogInput2HighLevelValue, analogInput2Value1, analogInput2Value0,"
			+ "	digitalOutput1Type, digitalOutput2Type)"
			+ "	VALUES(?, ?, ?,"
			+ "	?, ?, ?,"
			+ "	?, ?, ?,"
			+ "	?, ?, ?"
			+ " ?, ?, ?, ?, ?,"
			+ "	?, ?, ?, ?, ?,"
			+ "	?, ?)";
	
															
															
	private final String DIKSUCHI_DISPLAY_PARAMETER_PACKET_QUERY = "INSERT INTO db_nrda_vtsdb.DIKSUCHI_DISPLAY_PARAMETER_PACKET(packetType, IMEINO, sequenceNo,"
			+ "	frontDisplayBoard, frontDisplayBoardIntensityMode, frontDisplayBoardIntensity,"
			+ "	sideDisplayBoard, sideDisplayBoardIntensityMode, sideDisplayBoardIntensity,"
			+ "	rearDisplayBoard, rearDisplayBoardIntensityMode, rearDisplayBoardIntensity,"
			+ "	internalDisplayBoard, internalDisplayBoardIntensityMode, internalDisplayBoardIntensity)"
			+ "	VALUES(?, ?, ?, "
			+ "	?, ?, ?,"
			+ "	?, ?, ?"
			+ "	?, ?, ?"
			+ "	?, ?, ?)";
																	
	private final String DIKSUCHI_MISC_PARAMETER_PACKET_QUERY = "INSERT INTO db_nrda_vtsdb.DIKSUCHI_MISC_PARAMETER_PACKET(packetType, IMEINO, sequenceNo,"
			+ "	gprsUpdateRateActive, gprsUpdateRateNormal, gprsUpdateRateStandby, smsUpdateRate,"
			+ "	positionStoringDuration, overSPeedLimitDuration, overSpeedLimit,"
			+ "	harshBreakDuration1, harshBreakSpeed1, harshBreakDuration2, harshBreakSpeed2,"
			+ "	harshAccelerationDuration1, harshAccelerationSpeed1,"
			+ "	harshAccelerationDuration2, harshAccelerationSpeed2,"
			+ "	routeDeactivationTime, routeDeactiveationDistance, tripStartDetectionDistance, keepAliveTimerValue)"
			+ "	VALUES(?, ?, ?,"
			+ "	?, ? , ?, ?,"
			+ "	?, ?, ?,"
			+ "	?, ?, ?, ?,"
			+ "	?, ?,"
			+ "	?, ?,"
			+ "	?, ?, ?, ?)";
	
	
	public int processClientMessageV3(String message) throws Exception {
		
		logger.info("MESSAGE={}", message);
		this.clientMessage = message;
		String delimitor = ",";
		String[] messageToken = clientMessage.split(delimitor);
		
		int returnValue = DiksuchiProtocolStatus.PROTOCOL_SUCCESS.getValue();
		
		communicationServerID = Integer.parseInt(messageToken[0]);
		sourceIMEINumber = messageToken[1];
		sequenceNumber = Integer.parseInt(messageToken[2]);
		packetType = Integer.parseInt(messageToken[3]);
		
		Connection connection = null;
		PreparedStatement prepStmt = null;
		try 
		{
			
		
		connection = pgsqlConnection;
		
	
		/** TAKSHAK TODO: validate this IMEINumber belongs to us **/
		
		
		logger.info("********* INFORMATION STARTS *****");
		logger.info("CommunicationServerID="+communicationServerID);
		logger.info("sourceIMEINumber="+sourceIMEINumber);
		logger.info("sequenceNumber="+sequenceNumber);
		logger.info("packetType="+packetType);
		logger.info("***** INFORMATION ENDS *****");
		
		int fieldIndex = 0;
		
		int startingIndex = 4;
		logger.info("FOUND packetType={}", packetType);
		
		/* Now Depending on the packetType take an action */
		switch(packetType) {
		
		case 255:	/* Acknowledgement packet */
			returnValue = DiksuchiProtocolStatus.PROTOCOL_ACK_IGNORE.getValue();
			logger.info("ACK RECV FROM DEVICE sourceIMEINumber={} and sequenceNumber={}", sourceIMEINumber, sequenceNumber);
			break;
			
		case 1: /* Position Packet */
			
			int numberOfSamples = Integer.parseInt(messageToken[startingIndex++]);
			
			for (int sampleIndex = 0; sampleIndex < numberOfSamples; sampleIndex++) {
				DiksuchiPositionPacket positionPacket = new DiksuchiPositionPacket();
				latitude = Double.parseDouble(messageToken[startingIndex++]);
				positionPacket.setLatitude(latitude);
				longitude = Double.parseDouble(messageToken[startingIndex++]);
				positionPacket.setLongitude(longitude);
				positionPacket.setAltitude(Integer.parseInt(messageToken[startingIndex++]));
				
				String timeField = messageToken[startingIndex++];
				String dateField = messageToken[startingIndex++];
				packetTimestamp = VTSUtilityHandler.getParserDateTimestampDIKSUCHI(dateField, timeField);
				
				positionPacket.setEpochTime(packetTimestamp.getTime());
				positionPacket.setVelocity(Integer.parseInt(messageToken[startingIndex++]));
				positionPacket.setCoarseOverGround(Double.parseDouble(messageToken[startingIndex++]));
				positionPacket.setTrackedSattelite(Integer.parseInt(messageToken[startingIndex++]));
				
				/** Decide what to do with the data */
				logger.info(positionPacket.toString());
				
				fieldIndex = 0;
				//Timestamp gpsDateTime = new Timestamp(positionPacket.getEpochTime());
				//packetTimestamp = gpsDateTime;
				
				prepStmt = connection.prepareStatement(DIKSUCHI_POSITION_PACKET_QUERY);
				prepStmt.setInt(++fieldIndex, packetType);
				prepStmt.setString(++fieldIndex, sourceIMEINumber);
				prepStmt.setInt(++fieldIndex, sequenceNumber);
				prepStmt.setTimestamp(++fieldIndex, packetTimestamp);
				prepStmt.setDouble(++fieldIndex,  positionPacket.getLatitude());
				prepStmt.setDouble(++fieldIndex,  positionPacket.getLongitude());
				prepStmt.setInt(++fieldIndex, positionPacket.getVelocity());
				prepStmt.setDouble(++fieldIndex, positionPacket.getCoarseOverGround());
				prepStmt.setInt(++fieldIndex, positionPacket.getTrackedSattelite());
				
				returnValue = prepStmt.executeUpdate();
			}
			
			break;
		case 2:	/* Route Select information */
		
			DiksuchiRouteSelect routePacket = new DiksuchiRouteSelect();
			routePacket.setScheduleID(Long.parseLong(messageToken[startingIndex++]));
			routePacket.setRouteNumberLength(Integer.parseInt(messageToken[startingIndex++]));
			routePacket.setRouteNumber(messageToken[startingIndex++]);
			routePacket.setCurrentTripNumber(Integer.parseInt(messageToken[startingIndex++]));
			routePacket.setLatitude(Double.parseDouble(messageToken[startingIndex++]));
			routePacket.setLongitude(Double.parseDouble(messageToken[startingIndex++]));
			routePacket.setEpochTime(Long.parseLong(messageToken[startingIndex++]));
			
			logger.info(routePacket.toString());
			
			prepStmt = connection.prepareStatement(DIKSUCHI_ROUTE_TRIP_PACKET_QUERY);
			prepStmt.setInt(++fieldIndex, packetType);
			prepStmt.setString(++fieldIndex, sourceIMEINumber);
			prepStmt.setInt(++fieldIndex, sequenceNumber);
			prepStmt.setLong(++fieldIndex, routePacket.getScheduleID());
			prepStmt.setInt(++fieldIndex, routePacket.getRouteNumberLength());
			prepStmt.setString(++fieldIndex, routePacket.getRouteNumber());
			prepStmt.setInt(++fieldIndex, routePacket.getCurrentTripNumber());
			prepStmt.setInt(++fieldIndex, 0); /* Trip Status will be false here */
			
			prepStmt.setDouble(++fieldIndex,  routePacket.getLatitude());
			prepStmt.setDouble(++fieldIndex,  routePacket.getLongitude());
			
			Timestamp routeGPSDataTime = new Timestamp(routePacket.getEpochTime());
			prepStmt.setTimestamp(++fieldIndex, routeGPSDataTime);
			
			returnValue = prepStmt.executeUpdate();
			
			break; 
		case 3: /* Trip Select Information */
			
			DiksuchiTripSelect tripPacket = new DiksuchiTripSelect();
			tripPacket.setScheduleID(Long.parseLong(messageToken[startingIndex++]));
			tripPacket.setCurrentTripNumber(Integer.parseInt(messageToken[startingIndex++]));
			tripPacket.setTripStatus(Integer.parseInt(messageToken[startingIndex++]));
			
			tripPacket.setRouteNumberLength(Integer.parseInt(messageToken[startingIndex++]));
			tripPacket.setRouteNumber(messageToken[startingIndex++]);
			
			tripPacket.setLatitude(Double.parseDouble(messageToken[startingIndex++]));
			tripPacket.setLongitude(Double.parseDouble(messageToken[startingIndex++]));
			tripPacket.setEpochTime(Long.parseLong(messageToken[startingIndex++]));
			
			logger.info(tripPacket.toString());
			
			prepStmt = connection.prepareStatement(DIKSUCHI_ROUTE_TRIP_PACKET_QUERY);
			prepStmt.setInt(++fieldIndex, packetType);
			prepStmt.setString(++fieldIndex, sourceIMEINumber);
			prepStmt.setInt(++fieldIndex, sequenceNumber);
			
			prepStmt.setLong(++fieldIndex, tripPacket.getScheduleID());
			prepStmt.setInt(++fieldIndex, tripPacket.getRouteNumberLength());
			prepStmt.setString(++fieldIndex, tripPacket.getRouteNumber());
			prepStmt.setInt(++fieldIndex, tripPacket.getCurrentTripNumber());
			prepStmt.setInt(++fieldIndex, tripPacket.getTripStatus());
			
			prepStmt.setDouble(++fieldIndex,  tripPacket.getLatitude());
			prepStmt.setDouble(++fieldIndex,  tripPacket.getLongitude());
			
			Timestamp gpsDateTime = new Timestamp(tripPacket.getEpochTime());
			prepStmt.setTimestamp(++fieldIndex, gpsDateTime);
			
			returnValue = prepStmt.executeUpdate();
			
			break;
		case 4:	/* Bus Stop Crossed */
			
			DiksuchiBusStopCrossed busStopCrossed = new DiksuchiBusStopCrossed();
			
			busStopCrossed.setScheduleID(Long.parseLong(messageToken[startingIndex++]));
			busStopCrossed.setCurrentTripNumber(Integer.parseInt(messageToken[startingIndex++]));
			
			busStopCrossed.setRouteNumberLength(Integer.parseInt(messageToken[startingIndex++]));
			busStopCrossed.setRouteNumber(messageToken[startingIndex++]);
			
			busStopCrossed.setBusStopNameLength(Integer.parseInt(messageToken[startingIndex++]));
			busStopCrossed.setBusStopName(messageToken[startingIndex++]);
			busStopCrossed.setReachTime(Long.parseLong(messageToken[startingIndex++]));
			
			busStopCrossed.setStoppageDuration(Integer.parseInt(messageToken[startingIndex++]));
			busStopCrossed.setDeviationFromBusStop(Integer.parseInt(messageToken[startingIndex++]));
			
			logger.info(busStopCrossed.toString());
			
			prepStmt = connection.prepareStatement(DIKSUCHI_BUSSTOP_PACKET_QUERY);
			prepStmt.setInt(++fieldIndex, packetType);
			prepStmt.setString(++fieldIndex, sourceIMEINumber);
			prepStmt.setInt(++fieldIndex, sequenceNumber);
			
			prepStmt.setLong(++fieldIndex, busStopCrossed.getScheduleID());
			prepStmt.setInt(++fieldIndex, busStopCrossed.getRouteNumberLength());
			prepStmt.setString(++fieldIndex, busStopCrossed.getRouteNumber());
			prepStmt.setInt(++fieldIndex, busStopCrossed.getCurrentTripNumber());
			prepStmt.setInt(++fieldIndex, busStopCrossed.getBusStopNameLength());
			prepStmt.setString(++fieldIndex, busStopCrossed.getBusStopName());
			
			Timestamp reachDateTime = new Timestamp(busStopCrossed.getReachTime());
			prepStmt.setTimestamp(++fieldIndex, reachDateTime);
			
			prepStmt.setInt(++fieldIndex, busStopCrossed.getStoppageDuration());
			prepStmt.setInt(++fieldIndex, busStopCrossed.getDeviationFromBusStop());
			
			returnValue = prepStmt.executeUpdate();
			
			break;
		case 5: /* Schedule Request */
			
			DiksuchiScheduleRequest scheduleRequest = new DiksuchiScheduleRequest();
			scheduleRequest.setScheduleID(Long.parseLong(messageToken[startingIndex++]));
			scheduleRequest.setLatitude(Double.parseDouble(messageToken[startingIndex++]));
			scheduleRequest.setLongitude(Double.parseDouble(messageToken[startingIndex++]));
			scheduleRequest.setEpochTime(Long.parseLong(messageToken[startingIndex++]));
			
			logger.info(scheduleRequest.toString());
			
			prepStmt = connection.prepareStatement(DIKSUCHI_SCHEDULE_REQUEST_PACKET_QUERY);
			prepStmt.setInt(++fieldIndex, packetType);
			prepStmt.setString(++fieldIndex, sourceIMEINumber);
			prepStmt.setInt(++fieldIndex, sequenceNumber);
			
			prepStmt.setLong(++fieldIndex, scheduleRequest.getScheduleID());
			Timestamp scheduleGpsDataTime = new Timestamp(scheduleRequest.getEpochTime());
			prepStmt.setTimestamp(++fieldIndex, scheduleGpsDataTime);
			prepStmt.setDouble(++fieldIndex,  scheduleRequest.getLatitude());
			prepStmt.setDouble(++fieldIndex,  scheduleRequest.getLongitude());
			
			returnValue = prepStmt.executeUpdate();
			
			break;
		
		case 6: /* Message Packet */
			
			DiksuchiMessagePacket messagePacket = new DiksuchiMessagePacket();
		
			messagePacket.setMessageCodeNumber(Integer.parseInt(messageToken[startingIndex++]));
			messagePacket.setLatitude(Double.parseDouble(messageToken[startingIndex++]));
			messagePacket.setLongitude(Double.parseDouble(messageToken[startingIndex++]));
			messagePacket.setEpochTime(Long.parseLong(messageToken[startingIndex++]));
			
			logger.info(messagePacket.toString());
			
			prepStmt = connection.prepareStatement(DIKSUCHI_MESSAGE_PACKET_QUERY);
			prepStmt.setInt(++fieldIndex, packetType);
			prepStmt.setString(++fieldIndex, sourceIMEINumber);
			prepStmt.setInt(++fieldIndex, sequenceNumber);
			
			prepStmt.setInt(++fieldIndex, messagePacket.getMessageCodeNumber());
			Timestamp messageGpsDataTime = new Timestamp(messagePacket.getEpochTime());
			prepStmt.setTimestamp(++fieldIndex, messageGpsDataTime);
			prepStmt.setDouble(++fieldIndex,  messagePacket.getLatitude());
			prepStmt.setDouble(++fieldIndex,  messagePacket.getLongitude());
			
			returnValue = prepStmt.executeUpdate();
			
			break;
			
		case 7: /* Keep Alive Packet */
			String dataPacket = messageToken[startingIndex++];
			
			logger.info("Keep Alive Packet with Data="+dataPacket);
			
			prepStmt = connection.prepareStatement(DIKSUCHI_KEEPALIVE_PACKET_QUERY);
			prepStmt.setInt(++fieldIndex, packetType);
			prepStmt.setString(++fieldIndex, sourceIMEINumber);
			prepStmt.setInt(++fieldIndex, sequenceNumber);
			
			prepStmt.setString(++fieldIndex, dataPacket);
			
			returnValue = prepStmt.executeUpdate();
			
			break;
		
		case 8:	/* Over Speed */
			
			DiksuchiOverSpeed overSpeed = new DiksuchiOverSpeed();
			overSpeed.setScheduleID(Long.parseLong(messageToken[startingIndex++]));
			overSpeed.setCurrentTripNumber(Integer.parseInt(messageToken[startingIndex++]));
			
			overSpeed.setRouteNumberLength(Integer.parseInt(messageToken[startingIndex++]));
			overSpeed.setRouteNumber(messageToken[startingIndex++]);
			
			overSpeed.setLatitude(Double.parseDouble(messageToken[startingIndex++]));
			overSpeed.setLongitude(Double.parseDouble(messageToken[startingIndex++]));
			overSpeed.setOverSpeedStartTime(Long.parseLong(messageToken[startingIndex++]));
			overSpeed.setOverSpeedDuration(Integer.parseInt(messageToken[startingIndex++]));
			
			logger.info(overSpeed.toString());
			
			prepStmt = connection.prepareStatement(DIKSUCHI_DRIVING_PACKET_QUERY);
			prepStmt.setInt(++fieldIndex, packetType);
			prepStmt.setString(++fieldIndex, sourceIMEINumber);
			prepStmt.setInt(++fieldIndex, sequenceNumber);
			
			prepStmt.setLong(++fieldIndex, overSpeed.getScheduleID());
			prepStmt.setInt(++fieldIndex, overSpeed.getCurrentTripNumber());
			prepStmt.setInt(++fieldIndex, overSpeed.getRouteNumberLength());
			prepStmt.setString(++fieldIndex,  overSpeed.getRouteNumber());

			prepStmt.setDouble(++fieldIndex,  overSpeed.getLatitude());
			prepStmt.setDouble(++fieldIndex,  overSpeed.getLongitude());
			
			Timestamp overSpeedStartTime = new Timestamp(overSpeed.getOverSpeedStartTime());
			prepStmt.setTimestamp(++fieldIndex, overSpeedStartTime);
			
			prepStmt.setInt(++fieldIndex,  0); /* Maximum Speed */
			prepStmt.setInt(++fieldIndex,  overSpeed.getOverSpeedDuration());
			prepStmt.setTimestamp(++fieldIndex, overSpeedStartTime); /* Exception Start Time */
			prepStmt.setInt(++fieldIndex,  0); /* Starting Speed */
			prepStmt.setInt(++fieldIndex,  0); /* Ending Speed */
			prepStmt.setInt(++fieldIndex,  0); /* Speed Duration */
		
			returnValue = prepStmt.executeUpdate();
			
			break;
			
		case 9:	/* Harsh Break */
			
			DiksuchiHarshBreak harshBreak = new DiksuchiHarshBreak();
			harshBreak.setScheduleID(Integer.parseInt(messageToken[startingIndex++]));
			harshBreak.setCurrentTripNumber(Integer.parseInt(messageToken[startingIndex++]));
			
			harshBreak.setRouteNumberLength(Integer.parseInt(messageToken[startingIndex++]));
			harshBreak.setRouteNumber(messageToken[startingIndex++]);
			
			harshBreak.setLatitude(Double.parseDouble(messageToken[startingIndex++]));
			harshBreak.setLongitude(Double.parseDouble(messageToken[startingIndex++]));
			harshBreak.setExceptionStartTime(Long.parseLong(messageToken[startingIndex++]));
			harshBreak.setStartingSpeed(Integer.parseInt(messageToken[startingIndex++]));
			harshBreak.setEndingSpeed(Integer.parseInt(messageToken[startingIndex++]));
			harshBreak.setDuration(Integer.parseInt(messageToken[startingIndex++]));
			
			logger.info(harshBreak.toString());
			
			prepStmt = connection.prepareStatement(DIKSUCHI_DRIVING_PACKET_QUERY);
			prepStmt.setInt(++fieldIndex, packetType);
			prepStmt.setString(++fieldIndex, sourceIMEINumber);
			prepStmt.setInt(++fieldIndex, sequenceNumber);
			
			prepStmt.setLong(++fieldIndex, harshBreak.getScheduleID());
			prepStmt.setInt(++fieldIndex, harshBreak.getCurrentTripNumber());
			prepStmt.setInt(++fieldIndex, harshBreak.getRouteNumberLength());
			prepStmt.setString(++fieldIndex,  harshBreak.getRouteNumber());

			prepStmt.setDouble(++fieldIndex,  harshBreak.getLatitude());
			prepStmt.setDouble(++fieldIndex,  harshBreak.getLongitude());
			
			Timestamp exceptionStartTime = new Timestamp(harshBreak.getExceptionStartTime());
			prepStmt.setTimestamp(++fieldIndex, exceptionStartTime); /* OverSpeed Time Filler than empty */
			
			prepStmt.setInt(++fieldIndex,  0); /* Maximum Speed */
			prepStmt.setInt(++fieldIndex,  0); /* OveSpeed Duration */
			prepStmt.setTimestamp(++fieldIndex, exceptionStartTime); /* Exception Start Time */
			prepStmt.setInt(++fieldIndex,  harshBreak.getStartingSpeed()); /* Starting Speed */
			prepStmt.setInt(++fieldIndex,  harshBreak.getEndingSpeed()); /* Ending Speed */
			prepStmt.setInt(++fieldIndex,  harshBreak.getDuration()); /* Speed Duration */
			
			returnValue = prepStmt.executeUpdate();
			
			break;
		case 10: /* Harsh Acceleration */
			
			DiksuchiHarshAcceleration harshAcceleration = new DiksuchiHarshAcceleration();
			harshAcceleration.setScheduleID(Integer.parseInt(messageToken[startingIndex++]));
			harshAcceleration.setCurrentTripNumber(Integer.parseInt(messageToken[startingIndex++]));
			
			harshAcceleration.setRouteNumberLength(Integer.parseInt(messageToken[startingIndex++]));
			harshAcceleration.setRouteNumber(messageToken[startingIndex++]);
			
			harshAcceleration.setLatitude(Double.parseDouble(messageToken[startingIndex++]));
			harshAcceleration.setLongitude(Double.parseDouble(messageToken[startingIndex++]));
			harshAcceleration.setExceptionStartTime(Long.parseLong(messageToken[startingIndex++]));
			harshAcceleration.setStartingSpeed(Integer.parseInt(messageToken[startingIndex++]));
			harshAcceleration.setEndingSpeed(Integer.parseInt(messageToken[startingIndex++]));
			harshAcceleration.setDuration(Integer.parseInt(messageToken[startingIndex++]));
			
			logger.info(harshAcceleration.toString());
			
			prepStmt = connection.prepareStatement(DIKSUCHI_DRIVING_PACKET_QUERY);
			prepStmt.setInt(++fieldIndex, packetType);
			prepStmt.setString(++fieldIndex, sourceIMEINumber);
			prepStmt.setInt(++fieldIndex, sequenceNumber);
			
			
			prepStmt.setLong(++fieldIndex, harshAcceleration.getScheduleID());
			prepStmt.setInt(++fieldIndex, harshAcceleration.getCurrentTripNumber());
			prepStmt.setInt(++fieldIndex, harshAcceleration.getRouteNumberLength());
			prepStmt.setString(++fieldIndex,  harshAcceleration.getRouteNumber());

			prepStmt.setDouble(++fieldIndex,  harshAcceleration.getLatitude());
			prepStmt.setDouble(++fieldIndex,  harshAcceleration.getLongitude());
			
			Timestamp exceptionStartTime1 = new Timestamp(harshAcceleration.getExceptionStartTime());
			prepStmt.setTimestamp(++fieldIndex, exceptionStartTime1); /* OverSpeed Time Filler than empty */
			
			prepStmt.setInt(++fieldIndex,  0); /* Maximum Speed */
			prepStmt.setInt(++fieldIndex,  0); /* OveSpeed Duration */
			prepStmt.setTimestamp(++fieldIndex, exceptionStartTime1); /* Exception Start Time */
			prepStmt.setInt(++fieldIndex,  harshAcceleration.getStartingSpeed()); /* Starting Speed */
			prepStmt.setInt(++fieldIndex,  harshAcceleration.getEndingSpeed()); /* Ending Speed */
			prepStmt.setInt(++fieldIndex,  harshAcceleration.getDuration()); /* Speed Duration */
			
			
			returnValue = prepStmt.executeUpdate();
			
			break;
		case 11: /* Route Deviation */
			
			DiksuchiRouteDeviation routeDeviation = new DiksuchiRouteDeviation();
			routeDeviation.setScheduleID(Integer.parseInt(messageToken[startingIndex++]));
			routeDeviation.setRouteNumberLength(Integer.parseInt(messageToken[startingIndex++]));
			routeDeviation.setRouteNumber(messageToken[startingIndex++]);
			
			routeDeviation.setCurrentTripNumber(Integer.parseInt(messageToken[startingIndex++]));
			routeDeviation.setStopNameLength(Integer.parseInt(messageToken[startingIndex++]));
			routeDeviation.setFirstViolatedStopName(messageToken[startingIndex++]);
			routeDeviation.setDistanceTraveledFromVioleted(Integer.parseInt(messageToken[startingIndex++]));
			
			routeDeviation.setLatitude(Double.parseDouble(messageToken[startingIndex++]));
			routeDeviation.setLongitude(Double.parseDouble(messageToken[startingIndex++]));
			
			
			logger.info(routeDeviation.toString());
			prepStmt = connection.prepareStatement(DIKSUCHI_ROUTE_DEVIATION_PACKET_QUERY);
			prepStmt.setInt(++fieldIndex, packetType);
			prepStmt.setString(++fieldIndex, sourceIMEINumber);
			prepStmt.setInt(++fieldIndex, sequenceNumber);
			
			prepStmt.setLong(++fieldIndex, routeDeviation.getScheduleID());
			prepStmt.setInt(++fieldIndex, routeDeviation.getCurrentTripNumber());
			prepStmt.setInt(++fieldIndex, routeDeviation.getRouteNumberLength());
			prepStmt.setString(++fieldIndex,  routeDeviation.getRouteNumber());

			prepStmt.setDouble(++fieldIndex,  routeDeviation.getLatitude());
			prepStmt.setDouble(++fieldIndex,  routeDeviation.getLongitude());
			
			prepStmt.setInt(++fieldIndex, routeDeviation.getStopNameLength());
			prepStmt.setString(++fieldIndex,  routeDeviation.getFirstViolatedStopName());
			prepStmt.setInt(++fieldIndex, routeDeviation.getDistanceTraveledFromVioleted());
			
			returnValue = prepStmt.executeUpdate();
			
			break;
		case 12:	/* Status tracking - No GPS */
			
			String gpsData = messageToken[startingIndex++];
			
			logger.info("Status tracking - No GPS="+gpsData);
			
			prepStmt = connection.prepareStatement(DIKSUCHI_KEEPALIVE_PACKET_QUERY);
			prepStmt.setInt(++fieldIndex, packetType);
			prepStmt.setString(++fieldIndex, sourceIMEINumber);
			prepStmt.setInt(++fieldIndex, sequenceNumber);
			
			prepStmt.setString(++fieldIndex, gpsData);
			
			returnValue = prepStmt.executeUpdate();
			
			break;
		
		case 13:	/* Digital Input 1 Status */
			
			int digitalInput1Type = Integer.parseInt(messageToken[startingIndex++]);
			int digitalInput1UpdateMode = Integer.parseInt(messageToken[startingIndex++]);
			int digitalInput1Value = Integer.parseInt(messageToken[startingIndex++]);
			
			prepStmt = connection.prepareStatement(DIKSUCHI_DIGITAL_INPUT_PACKET_QUERY);
			prepStmt.setInt(++fieldIndex, packetType);
			prepStmt.setString(++fieldIndex, sourceIMEINumber);
			prepStmt.setInt(++fieldIndex, sequenceNumber);
			
			prepStmt.setInt(++fieldIndex, digitalInput1Type);
			prepStmt.setInt(++fieldIndex, digitalInput1UpdateMode);
			prepStmt.setInt(++fieldIndex, digitalInput1Value);
			
			returnValue = prepStmt.executeUpdate();
			
			break;
		
		case 14:	/* Digital Input 2 Status */
			
			int digitalInput2Type = Integer.parseInt(messageToken[startingIndex++]);
			int digitalInput2UpdateMode = Integer.parseInt(messageToken[startingIndex++]);
			int digitalInput2Value = Integer.parseInt(messageToken[startingIndex++]);
			
			prepStmt = connection.prepareStatement(DIKSUCHI_DIGITAL_INPUT_PACKET_QUERY);
			prepStmt.setInt(++fieldIndex, packetType);
			prepStmt.setString(++fieldIndex, sourceIMEINumber);
			prepStmt.setInt(++fieldIndex, sequenceNumber);
			
			prepStmt.setInt(++fieldIndex, digitalInput2Type);
			prepStmt.setInt(++fieldIndex, digitalInput2UpdateMode);
			prepStmt.setInt(++fieldIndex, digitalInput2Value);
		
			returnValue = prepStmt.executeUpdate();
			
			
			break;
			
		case 15:	/* Digital Input 3 Status */
			
			int digitalInput3Type = Integer.parseInt(messageToken[startingIndex++]);
			int digitalInput3UpdateMode = Integer.parseInt(messageToken[startingIndex++]);
			int digitalInput3Value = Integer.parseInt(messageToken[startingIndex++]);
			
			prepStmt = connection.prepareStatement(DIKSUCHI_DIGITAL_INPUT_PACKET_QUERY);
			prepStmt.setInt(++fieldIndex, packetType);
			prepStmt.setString(++fieldIndex, sourceIMEINumber);
			prepStmt.setInt(++fieldIndex, sequenceNumber);
			
			prepStmt.setInt(++fieldIndex, digitalInput3Type);
			prepStmt.setInt(++fieldIndex, digitalInput3UpdateMode);
			prepStmt.setInt(++fieldIndex, digitalInput3Value);
			
			returnValue = prepStmt.executeUpdate();
			
			
			break;
			
		case 16: /* Analog Input status 1 */
			
			int analogInput1 = Integer.parseInt(messageToken[startingIndex++]);
			int analogInput1LowLevel = Integer.parseInt(messageToken[startingIndex++]); 
			int analogInput1HighLevel = Integer.parseInt(messageToken[startingIndex++]);
			int analogInput1Value1 = Integer.parseInt(messageToken[startingIndex++]);
			int analogInput1Value0 = Integer.parseInt(messageToken[startingIndex++]);
			
			prepStmt = connection.prepareStatement(DIKSUCHI_ANALOG_INPUT_PACKET_QUERY);
			prepStmt.setInt(++fieldIndex, packetType);
			prepStmt.setString(++fieldIndex, sourceIMEINumber);
			prepStmt.setInt(++fieldIndex, sequenceNumber);
			
			prepStmt.setInt(++fieldIndex, analogInput1);
			prepStmt.setInt(++fieldIndex, analogInput1LowLevel);
			prepStmt.setInt(++fieldIndex, analogInput1HighLevel);
			prepStmt.setInt(++fieldIndex, analogInput1Value1);
			prepStmt.setInt(++fieldIndex, analogInput1Value0);
			
			returnValue = prepStmt.executeUpdate();
			
			
			break;
		
		case 17: /* Analog Input status 2 */
			
			int analogInput2 = Integer.parseInt(messageToken[startingIndex++]);
			int analogInput2LowLevel = Integer.parseInt(messageToken[startingIndex++]); 
			int analogInput2HighLevel = Integer.parseInt(messageToken[startingIndex++]);
			int analogInput2Value1 = Integer.parseInt(messageToken[startingIndex++]);
			int analogInput2Value0 = Integer.parseInt(messageToken[startingIndex++]);
			
			prepStmt = connection.prepareStatement(DIKSUCHI_ANALOG_INPUT_PACKET_QUERY);
			prepStmt.setInt(++fieldIndex, packetType);
			prepStmt.setString(++fieldIndex, sourceIMEINumber);
			prepStmt.setInt(++fieldIndex, sequenceNumber);
			
			prepStmt.setInt(++fieldIndex, analogInput2);
			prepStmt.setInt(++fieldIndex, analogInput2LowLevel);
			prepStmt.setInt(++fieldIndex, analogInput2HighLevel);
			prepStmt.setInt(++fieldIndex, analogInput2Value1);
			prepStmt.setInt(++fieldIndex, analogInput2Value0);
					
			returnValue = prepStmt.executeUpdate();
			
			break;
		
		case 18:	/*Display Board PID Status */
			
			DiksuchiDisplayBoardStatus displayBoardStatus = new DiksuchiDisplayBoardStatus();
			
			displayBoardStatus.setHardwareRevision(Integer.parseInt(messageToken[startingIndex++]));
			displayBoardStatus.setSerialNumber(Integer.parseInt(messageToken[startingIndex++]));
			
			displayBoardStatus.setBootLoaderSWVersion(Integer.parseInt(messageToken[startingIndex++]));
			displayBoardStatus.setApplicationSWVersion(Integer.parseInt(messageToken[startingIndex++]));
			displayBoardStatus.setFrontLibraryRevision(Integer.parseInt(messageToken[startingIndex++]));

			displayBoardStatus.setCpuPartNumber(messageToken[startingIndex++]);
			displayBoardStatus.setCpuQualification(messageToken[startingIndex++]);
			
			displayBoardStatus.setCpuTempRange(Integer.parseInt(messageToken[startingIndex++]));
			displayBoardStatus.setCompilationFirmwareDateTime(messageToken[startingIndex++]);
			displayBoardStatus.setFlashUpdateStatusDateTime(messageToken[startingIndex++]);
			displayBoardStatus.setTestDateTime(messageToken[startingIndex++]);
			displayBoardStatus.setArticleNumberSignLevel(messageToken[startingIndex++]);
			displayBoardStatus.setProductionDate(messageToken[startingIndex++]);
			displayBoardStatus.setEndCustomer(messageToken[startingIndex++]);
			
			displayBoardStatus.setOrderNumber(messageToken[startingIndex++]);
			displayBoardStatus.setBusVehicleType(messageToken[startingIndex++]);
			displayBoardStatus.setBusBuilderNumber(Integer.parseInt(messageToken[startingIndex++]));
			
			displayBoardStatus.setLanguageCode(messageToken[startingIndex++]);
			displayBoardStatus.setBoardTempSensor(messageToken[startingIndex++]);
			
			displayBoardStatus.setInternalCPUTemp(Integer.parseInt(messageToken[startingIndex++]));
			displayBoardStatus.setMinCPUTemp(Integer.parseInt(messageToken[startingIndex++]));
			
			displayBoardStatus.setMaxCPUTemp(Integer.parseInt(messageToken[startingIndex++]));
			displayBoardStatus.setMaxBoardTemp(Integer.parseInt(messageToken[startingIndex++]));
			displayBoardStatus.setMinBoardTemp(Integer.parseInt(messageToken[startingIndex++]));
			
			displayBoardStatus.setMaxPowerVoltage(Integer.parseInt(messageToken[startingIndex++]));
			displayBoardStatus.setMinPowerVoltage(Integer.parseInt(messageToken[startingIndex++]));
			displayBoardStatus.setOperatingHours(Long.parseLong(messageToken[startingIndex++]));
			
			displayBoardStatus.setNumberOfResets(Integer.parseInt(messageToken[startingIndex++]));
			
			logger.info(displayBoardStatus.toString());
			
			prepStmt = connection.prepareStatement(DIKSUCHI_PID_STATUS_PACKET_QUERY);
			prepStmt.setInt(++fieldIndex, packetType);
			prepStmt.setString(++fieldIndex, sourceIMEINumber);
			prepStmt.setInt(++fieldIndex, sequenceNumber);
			
			prepStmt.setInt(++fieldIndex, displayBoardStatus.getHardwareRevision());
			prepStmt.setInt(++fieldIndex, displayBoardStatus.getSerialNumber());
			prepStmt.setInt(++fieldIndex, displayBoardStatus.getBootLoaderSWVersion());
			prepStmt.setInt(++fieldIndex, displayBoardStatus.getApplicationSWVersion());
			prepStmt.setInt(++fieldIndex, displayBoardStatus.getFrontLibraryRevision());
			prepStmt.setString(++fieldIndex, displayBoardStatus.getCpuPartNumber());
			prepStmt.setString(++fieldIndex, displayBoardStatus.getCpuQualification());
			prepStmt.setInt(++fieldIndex, displayBoardStatus.getCpuTempRange());
			
			prepStmt.setString(++fieldIndex, displayBoardStatus.getCompilationFirmwareDateTime());
			prepStmt.setString(++fieldIndex, displayBoardStatus.getFlashUpdateStatusDateTime());
			prepStmt.setString(++fieldIndex, displayBoardStatus.getTestDateTime());
			
			prepStmt.setString(++fieldIndex,  displayBoardStatus.getArticleNumberSignLevel());
			prepStmt.setString(++fieldIndex,  displayBoardStatus.getProductionDate());
			prepStmt.setString(++fieldIndex,  displayBoardStatus.getEndCustomer());
			prepStmt.setString(++fieldIndex,  displayBoardStatus.getOrderNumber());
			prepStmt.setString(++fieldIndex,  displayBoardStatus.getBusVehicleType());
			prepStmt.setInt(++fieldIndex, displayBoardStatus.getBusBuilderNumber());
			prepStmt.setString(++fieldIndex,  displayBoardStatus.getLanguageCode());
			prepStmt.setString(++fieldIndex,  displayBoardStatus.getBoardTempSensor());
			
			prepStmt.setInt(++fieldIndex, displayBoardStatus.getInternalCPUTemp());
			prepStmt.setInt(++fieldIndex, displayBoardStatus.getMinCPUTemp());
			prepStmt.setInt(++fieldIndex, displayBoardStatus.getMaxCPUTemp());
			prepStmt.setInt(++fieldIndex, displayBoardStatus.getMinBoardTemp());
			prepStmt.setInt(++fieldIndex, displayBoardStatus.getMaxBoardTemp());
			prepStmt.setInt(++fieldIndex, displayBoardStatus.getMaxPowerVoltage());
			prepStmt.setInt(++fieldIndex, displayBoardStatus.getMinPowerVoltage());
			prepStmt.setLong(++fieldIndex, displayBoardStatus.getOperatingHours());
			prepStmt.setInt(++fieldIndex, displayBoardStatus.getNumberOfResets());
			
			returnValue = prepStmt.executeUpdate();
			
			break;
			
		case 19: /* Display Board DTC Status */
			
			int overVoltageCount = Integer.parseInt(messageToken[startingIndex++]);
			int overVoltageValue = Integer.parseInt(messageToken[startingIndex++]); 
			int lowVoltageCount = Integer.parseInt(messageToken[startingIndex++]);
			int lowVoltageValue = Integer.parseInt(messageToken[startingIndex++]);
			int overHeatCount = Integer.parseInt(messageToken[startingIndex++]);
			int overHeatValue = Integer.parseInt(messageToken[startingIndex++]);
			
			
			prepStmt = connection.prepareStatement(DIKSUCHI_BOARD_DTC_STATUS_PACKET_QUERY);
			prepStmt.setInt(++fieldIndex, packetType);
			prepStmt.setString(++fieldIndex, sourceIMEINumber);
			prepStmt.setInt(++fieldIndex, sequenceNumber);
			
			prepStmt.setInt(++fieldIndex, overVoltageCount);
			prepStmt.setInt(++fieldIndex, overVoltageValue);
			prepStmt.setInt(++fieldIndex, lowVoltageCount);
			prepStmt.setInt(++fieldIndex, lowVoltageValue);
			prepStmt.setInt(++fieldIndex, overHeatCount);
			prepStmt.setInt(++fieldIndex, overHeatValue);
			
			returnValue = prepStmt.executeUpdate();
			
			
			break;
			
		case 20: /* Diksuchi PID Status */
			
			DiksuchiPIDStatus pidStatus = new DiksuchiPIDStatus();
			
			pidStatus.setHardwareRevision(Integer.parseInt(messageToken[startingIndex++]));
			pidStatus.setSerialNumber(Integer.parseInt(messageToken[startingIndex++]));
			
			pidStatus.setBootLoaderSWVersion(Integer.parseInt(messageToken[startingIndex++]));
			pidStatus.setApplicationSWVersion(Integer.parseInt(messageToken[startingIndex++]));
			pidStatus.setFrontLibraryRevision(Integer.parseInt(messageToken[startingIndex++]));

			pidStatus.setCpuPartNumber(messageToken[startingIndex++]);
			pidStatus.setCpuQualification(messageToken[startingIndex++]);
			
			pidStatus.setCpuTempRange(Integer.parseInt(messageToken[startingIndex++]));
			pidStatus.setCompilationFirmwareDateTime(messageToken[startingIndex++]);
			pidStatus.setFlashUpdateStatusDateTime(messageToken[startingIndex++]);
			pidStatus.setTestDateTime(messageToken[startingIndex++]);
			
			logger.info(pidStatus.toString());
			
			prepStmt = connection.prepareStatement(DIKSUCHI_PID_STATUS_PACKET_QUERY);
			prepStmt.setInt(++fieldIndex, packetType);
			prepStmt.setString(++fieldIndex, sourceIMEINumber);
			prepStmt.setInt(++fieldIndex, sequenceNumber);
			
			prepStmt.setInt(++fieldIndex, pidStatus.getHardwareRevision());
			prepStmt.setInt(++fieldIndex, pidStatus.getSerialNumber());
			prepStmt.setInt(++fieldIndex, pidStatus.getBootLoaderSWVersion());
			prepStmt.setInt(++fieldIndex, pidStatus.getApplicationSWVersion());
			prepStmt.setInt(++fieldIndex, pidStatus.getFrontLibraryRevision());
			prepStmt.setString(++fieldIndex, pidStatus.getCpuPartNumber());
			prepStmt.setString(++fieldIndex, pidStatus.getCpuQualification());
			prepStmt.setInt(++fieldIndex, pidStatus.getCpuTempRange());
			
			prepStmt.setString(++fieldIndex, pidStatus.getCompilationFirmwareDateTime());
			prepStmt.setString(++fieldIndex, pidStatus.getFlashUpdateStatusDateTime());
			prepStmt.setString(++fieldIndex, pidStatus.getTestDateTime());
			
			prepStmt.setString(++fieldIndex,  "");
			prepStmt.setString(++fieldIndex,  "");
			prepStmt.setString(++fieldIndex,  "");
			prepStmt.setString(++fieldIndex,  "");
			prepStmt.setString(++fieldIndex,  "");
			prepStmt.setInt(++fieldIndex, 0);
			prepStmt.setString(++fieldIndex,  "");
			prepStmt.setString(++fieldIndex,  "");
			
			prepStmt.setInt(++fieldIndex, 0);
			prepStmt.setInt(++fieldIndex, 0);
			prepStmt.setInt(++fieldIndex, 0);
			prepStmt.setInt(++fieldIndex, 0);
			prepStmt.setInt(++fieldIndex, 0);
			prepStmt.setInt(++fieldIndex, 0);
			prepStmt.setInt(++fieldIndex, 0);
			prepStmt.setLong(++fieldIndex, 0);
			prepStmt.setInt(++fieldIndex, 0);
			
			
			returnValue = prepStmt.executeUpdate();
			
			
			break;
		
		case 21:
			
			DiksuchiDTCStatus dtcStatus = new DiksuchiDTCStatus();
			
			dtcStatus.setWatchDogReset(Integer.parseInt(messageToken[startingIndex++]));
			dtcStatus.setLowVoltageReset(Integer.parseInt(messageToken[startingIndex++]));
			
			dtcStatus.setLostCommunicationWithGPS(Integer.parseInt(messageToken[startingIndex++]));
			dtcStatus.setGpsSignalInvalid(Integer.parseInt(messageToken[startingIndex++]));
			dtcStatus.setGpsAntennaError(Integer.parseInt(messageToken[startingIndex++]));
			
			
			dtcStatus.setInvalidStoageDevice(Integer.parseInt(messageToken[startingIndex++]));
			dtcStatus.setUsbInvalidFS(Integer.parseInt(messageToken[startingIndex++]));
			dtcStatus.setUnknownUSBDeviceConnected(Integer.parseInt(messageToken[startingIndex++]));
			
			dtcStatus.setUsbOverCurrent(Integer.parseInt(messageToken[startingIndex++]));
			dtcStatus.setUsbInvalidFS(Integer.parseInt(messageToken[startingIndex++]));
			dtcStatus.setOverVoltageCount(Integer.parseInt(messageToken[startingIndex++]));
			dtcStatus.setOverVoltageValue(Integer.parseInt(messageToken[startingIndex++]));
			
			dtcStatus.setLowVoltageCount(Integer.parseInt(messageToken[startingIndex++]));
			dtcStatus.setLowVoltageValue(Integer.parseInt(messageToken[startingIndex++]));
			
			dtcStatus.setOverHeatCount(Integer.parseInt(messageToken[startingIndex++]));
			dtcStatus.setOverHeatValue(Integer.parseInt(messageToken[startingIndex++]));
			
			logger.info(dtcStatus.toString());
			
			prepStmt = connection.prepareStatement(DIKSUCHI_DTC_STATUS_PACKET_QUERY);
			prepStmt.setInt(++fieldIndex, packetType);
			prepStmt.setString(++fieldIndex, sourceIMEINumber);
			prepStmt.setInt(++fieldIndex, sequenceNumber);
			
			prepStmt.setInt(++fieldIndex, dtcStatus.getOverVoltageCount());
			prepStmt.setInt(++fieldIndex, dtcStatus.getOverVoltageValue());
			prepStmt.setInt(++fieldIndex, dtcStatus.getLowVoltageCount());
			prepStmt.setInt(++fieldIndex, dtcStatus.getLowVoltageValue());
			prepStmt.setInt(++fieldIndex, dtcStatus.getOverHeatCount());
			prepStmt.setInt(++fieldIndex, dtcStatus.getOverHeatValue());
			
			prepStmt.setInt(++fieldIndex, dtcStatus.getWatchDogReset());
			prepStmt.setInt(++fieldIndex, dtcStatus.getLowVoltageReset());
			prepStmt.setInt(++fieldIndex, dtcStatus.getLostCommunicationWithGPS());
			prepStmt.setInt(++fieldIndex, dtcStatus.getGpsSignalInvalid());
			prepStmt.setInt(++fieldIndex, dtcStatus.getGpsAntennaError());
			prepStmt.setInt(++fieldIndex, dtcStatus.getInvalidStoageDevice());
			prepStmt.setInt(++fieldIndex, dtcStatus.getUnknownUSBDeviceConnected());
			prepStmt.setInt(++fieldIndex, dtcStatus.getUsbInvalidFS());
			prepStmt.setInt(++fieldIndex, dtcStatus.getUsbOverCurrent());
			
			returnValue = prepStmt.executeUpdate();
			
			break;
		
		case 50: /* MODE/GSM/GPRS Parameters */
			
			DiksuchiModeParameter modeParameter = new DiksuchiModeParameter();
			
			modeParameter.setPollStatus(Integer.parseInt(messageToken[startingIndex++]));
			modeParameter.setCommunicationMode(Integer.parseInt(messageToken[startingIndex++]));
			modeParameter.setSmsPhoneNumberLength(Integer.parseInt(messageToken[startingIndex++]));
			modeParameter.setSmsPhoneNumber(messageToken[startingIndex++]);
			
			
			modeParameter.setPrimaryIPAddressLength(Integer.parseInt(messageToken[startingIndex++]));
			modeParameter.setPrimaryIPAddress(messageToken[startingIndex++]);
			modeParameter.setSecondaryIPAddressLength(Integer.parseInt(messageToken[startingIndex++]));
			modeParameter.setSecondaryIPAddress(messageToken[startingIndex++]);
			modeParameter.setTcpPortNumber(Integer.parseInt(messageToken[startingIndex++]));
			modeParameter.setApnLength(Integer.parseInt(messageToken[startingIndex++]));
			modeParameter.setApnValue(messageToken[startingIndex++]);
			modeParameter.setUrlLength(Integer.parseInt(messageToken[startingIndex++]));
			modeParameter.setUrlValue(messageToken[startingIndex++]);
			modeParameter.setDnsEnabled(Integer.parseInt(messageToken[startingIndex++]));
			modeParameter.setDnsLength(Integer.parseInt(messageToken[startingIndex++]));
			modeParameter.setDnsValue(messageToken[startingIndex++]);
			
			logger.info(modeParameter.toString());
			
			prepStmt = connection.prepareStatement(DIKSUCHI_MODE_PARAMETER_PACKET_QUERY);
			prepStmt.setInt(++fieldIndex, packetType);
			prepStmt.setString(++fieldIndex, sourceIMEINumber);
			prepStmt.setInt(++fieldIndex, sequenceNumber);
			
			prepStmt.setInt(++fieldIndex, modeParameter.getPollStatus());
			prepStmt.setInt(++fieldIndex, modeParameter.getCommunicationMode());
			prepStmt.setInt(++fieldIndex, modeParameter.getSmsPhoneNumberLength());
			prepStmt.setString(++fieldIndex, modeParameter.getSmsPhoneNumber());
			prepStmt.setInt(++fieldIndex, modeParameter.getPrimaryIPAddressLength());
			prepStmt.setString(++fieldIndex, modeParameter.getPrimaryIPAddress());
			
			prepStmt.setInt(++fieldIndex, modeParameter.getSecondaryIPAddressLength());
			prepStmt.setString(++fieldIndex, modeParameter.getSecondaryIPAddress());
			
			prepStmt.setInt(++fieldIndex, modeParameter.getTcpPortNumber());
			prepStmt.setInt(++fieldIndex, modeParameter.getApnLength());
			prepStmt.setString(++fieldIndex, modeParameter.getApnValue());
			prepStmt.setInt(++fieldIndex, modeParameter.getUrlLength());
			prepStmt.setString(++fieldIndex, modeParameter.getUrlValue());
			
			prepStmt.setInt(++fieldIndex, modeParameter.getDnsEnabled());
			prepStmt.setInt(++fieldIndex, modeParameter.getDnsLength());
			prepStmt.setString(++fieldIndex, modeParameter.getDnsValue());
			
			returnValue = prepStmt.executeUpdate();
			
			break;
		
		case 51: /* Digital Input/Output/Analogue Parameters */
			
			DiksuchiDigitalIOParameter ioParam = new DiksuchiDigitalIOParameter();
			
			ioParam.setDigitalInputType1(Integer.parseInt(messageToken[startingIndex++]));
			ioParam.setDigitalInputMode1(Integer.parseInt(messageToken[startingIndex++]));
			ioParam.setDigitalInputValue1(Integer.parseInt(messageToken[startingIndex++]));
			
			ioParam.setDigitalInputType2(Integer.parseInt(messageToken[startingIndex++]));
			ioParam.setDigitalInputMode2(Integer.parseInt(messageToken[startingIndex++]));
			ioParam.setDigitalInputValue2(Integer.parseInt(messageToken[startingIndex++]));
			
			ioParam.setDigitalInputType3(Integer.parseInt(messageToken[startingIndex++]));
			ioParam.setDigitalInputMode3(Integer.parseInt(messageToken[startingIndex++]));
			ioParam.setDigitalInputValue3(Integer.parseInt(messageToken[startingIndex++]));
			
			ioParam.setAnalogInput1(Integer.parseInt(messageToken[startingIndex++]));
			ioParam.setAnalogInput1LowLevelValue(Integer.parseInt(messageToken[startingIndex++]));
			ioParam.setAnalogInput1HighLevelValue(Integer.parseInt(messageToken[startingIndex++]));
			ioParam.setAnalogInput1Value1(Integer.parseInt(messageToken[startingIndex++]));
			ioParam.setAnalogInput1Value0(Integer.parseInt(messageToken[startingIndex++]));
			
			ioParam.setAnalogInput2(Integer.parseInt(messageToken[startingIndex++]));
			ioParam.setAnalogInput2LowLevelValue(Integer.parseInt(messageToken[startingIndex++]));
			ioParam.setAnalogInput2HighLevelValue(Integer.parseInt(messageToken[startingIndex++]));
			ioParam.setAnalogInput2Value1(Integer.parseInt(messageToken[startingIndex++]));
			ioParam.setAnalogInput2Value0(Integer.parseInt(messageToken[startingIndex++]));
			
			ioParam.setDigitalOutput1Type(Integer.parseInt(messageToken[startingIndex++]));
			ioParam.setDigitalOutput2Type(Integer.parseInt(messageToken[startingIndex++]));
			
			System.out.println(ioParam.toString());
			
			prepStmt = connection.prepareStatement(DIKSUCHI_DIGITALIO_PARAMETER_PACKET_QUERY);
			prepStmt.setInt(++fieldIndex, packetType);
			prepStmt.setString(++fieldIndex, sourceIMEINumber);
			prepStmt.setInt(++fieldIndex, sequenceNumber);
			
			prepStmt.setInt(++fieldIndex, ioParam.getDigitalInputType1());
			prepStmt.setInt(++fieldIndex, ioParam.getDigitalInputMode1());
			prepStmt.setInt(++fieldIndex, ioParam.getDigitalInputValue1());
			prepStmt.setInt(++fieldIndex, ioParam.getDigitalInputType2());
			prepStmt.setInt(++fieldIndex, ioParam.getDigitalInputMode2());
			prepStmt.setInt(++fieldIndex, ioParam.getDigitalInputValue2());
			prepStmt.setInt(++fieldIndex, ioParam.getDigitalInputType3());
			prepStmt.setInt(++fieldIndex, ioParam.getDigitalInputMode3());
			prepStmt.setInt(++fieldIndex, ioParam.getDigitalInputValue2());
			
			prepStmt.setInt(++fieldIndex, ioParam.getAnalogInput1());
			prepStmt.setInt(++fieldIndex, ioParam.getAnalogInput1LowLevelValue());
			prepStmt.setInt(++fieldIndex, ioParam.getAnalogInput1HighLevelValue());
			prepStmt.setInt(++fieldIndex, ioParam.getAnalogInput1Value1());
			prepStmt.setInt(++fieldIndex, ioParam.getAnalogInput1Value0());
			
			prepStmt.setInt(++fieldIndex, ioParam.getAnalogInput2());
			prepStmt.setInt(++fieldIndex, ioParam.getAnalogInput2LowLevelValue());
			prepStmt.setInt(++fieldIndex, ioParam.getAnalogInput2HighLevelValue());
			prepStmt.setInt(++fieldIndex, ioParam.getAnalogInput2Value1());
			prepStmt.setInt(++fieldIndex, ioParam.getAnalogInput2Value0());
			
			prepStmt.setInt(++fieldIndex, ioParam.getDigitalOutput1Type());
			prepStmt.setInt(++fieldIndex, ioParam.getDigitalOutput2Type());
			
			returnValue = prepStmt.executeUpdate();
			
			break;
		
		case 52:
			DiksuchiDisplayBoardParameter boardParam = new DiksuchiDisplayBoardParameter();
			
			boardParam.setFrontDisplayBoard(Integer.parseInt(messageToken[startingIndex++]));
			boardParam.setFrontDisplayBoardIntensityMode(Integer.parseInt(messageToken[startingIndex++]));
			boardParam.setFrontDisplayBoardIntensity(Integer.parseInt(messageToken[startingIndex++]));
			
			boardParam.setSideDisplayBoard(Integer.parseInt(messageToken[startingIndex++]));
			boardParam.setSideDisplayBoardIntensityMode(Integer.parseInt(messageToken[startingIndex++]));
			boardParam.setSideDisplayBoardIntensity(Integer.parseInt(messageToken[startingIndex++]));
			
			boardParam.setRearDisplayBoard(Integer.parseInt(messageToken[startingIndex++]));
			boardParam.setRearDisplayBoardIntensityMode(Integer.parseInt(messageToken[startingIndex++]));
			boardParam.setRearDisplayBoardIntensity(Integer.parseInt(messageToken[startingIndex++]));
			
			boardParam.setInternalDisplayBoard(Integer.parseInt(messageToken[startingIndex++]));
			boardParam.setInternalDisplayBoardIntensityMode(Integer.parseInt(messageToken[startingIndex++]));
			boardParam.setInternalDisplayBoardIntensity(Integer.parseInt(messageToken[startingIndex++]));
			
			logger.info(boardParam.toString());
			
			prepStmt = connection.prepareStatement(DIKSUCHI_DISPLAY_PARAMETER_PACKET_QUERY);
			prepStmt.setInt(++fieldIndex, packetType);
			prepStmt.setString(++fieldIndex, sourceIMEINumber);
			prepStmt.setInt(++fieldIndex, sequenceNumber);
			
			prepStmt.setInt(++fieldIndex, boardParam.getFrontDisplayBoard());
			prepStmt.setInt(++fieldIndex, boardParam.getFrontDisplayBoardIntensityMode());
			prepStmt.setInt(++fieldIndex, boardParam.getFrontDisplayBoardIntensity());
			
			prepStmt.setInt(++fieldIndex, boardParam.getSideDisplayBoard());
			prepStmt.setInt(++fieldIndex, boardParam.getSideDisplayBoardIntensityMode());
			prepStmt.setInt(++fieldIndex, boardParam.getSideDisplayBoardIntensity());
			
			
			prepStmt.setInt(++fieldIndex, boardParam.getRearDisplayBoard());
			prepStmt.setInt(++fieldIndex, boardParam.getRearDisplayBoardIntensityMode());
			prepStmt.setInt(++fieldIndex, boardParam.getRearDisplayBoardIntensity());
			
			prepStmt.setInt(++fieldIndex, boardParam.getInternalDisplayBoard());
			prepStmt.setInt(++fieldIndex, boardParam.getInternalDisplayBoardIntensityMode());
			prepStmt.setInt(++fieldIndex, boardParam.getInternalDisplayBoardIntensity());
			
			returnValue = prepStmt.executeUpdate();
			
			
			break;
		
		case 53:
			
			DiksuchiMiscParameter miscParam = new DiksuchiMiscParameter();
			miscParam.setGprsUpdateRateActive(Integer.parseInt(messageToken[startingIndex++]));
			miscParam.setGprsUpdateRateNormal(Integer.parseInt(messageToken[startingIndex++]));
			miscParam.setGprsUpdateRateStandby(Integer.parseInt(messageToken[startingIndex++]));
			
			miscParam.setSmsUpdateRate(Integer.parseInt(messageToken[startingIndex++]));
			miscParam.setPositionStoringDuration(Integer.parseInt(messageToken[startingIndex++]));
			miscParam.setOverSpeedLimit(Integer.parseInt(messageToken[startingIndex++]));
			miscParam.setOverSPeedLimitDuration(Integer.parseInt(messageToken[startingIndex++]));
			
			miscParam.setHarshBreakDuration1(Integer.parseInt(messageToken[startingIndex++]));
			miscParam.setHarshBreakSpeed1(Integer.parseInt(messageToken[startingIndex++]));
			
			miscParam.setHarshBreakDuration2(Integer.parseInt(messageToken[startingIndex++]));
			miscParam.setHarshBreakSpeed2(Integer.parseInt(messageToken[startingIndex++]));
			
			miscParam.setHarshAccelerationDuration1(Integer.parseInt(messageToken[startingIndex++]));
			miscParam.setHarshAccelerationSpeed1(Integer.parseInt(messageToken[startingIndex++]));
			
			miscParam.setHarshAccelerationDuration2(Integer.parseInt(messageToken[startingIndex++]));
			miscParam.setHarshAccelerationSpeed2(Integer.parseInt(messageToken[startingIndex++]));
			
			miscParam.setRouteDeactivationTime(Integer.parseInt(messageToken[startingIndex++]));
			miscParam.setRouteDeactiveationDistance(Integer.parseInt(messageToken[startingIndex++]));
			
			miscParam.setKeepAliveTimerValue(Integer.parseInt(messageToken[startingIndex++]));
			
			logger.info(miscParam.toString());
			
			prepStmt = connection.prepareStatement(DIKSUCHI_MISC_PARAMETER_PACKET_QUERY);
			prepStmt.setInt(++fieldIndex, packetType);
			prepStmt.setString(++fieldIndex, sourceIMEINumber);
			prepStmt.setInt(++fieldIndex, sequenceNumber);
			
			prepStmt.setInt(++fieldIndex, miscParam.getGprsUpdateRateActive());
			prepStmt.setInt(++fieldIndex, miscParam.getGprsUpdateRateNormal());
			prepStmt.setInt(++fieldIndex, miscParam.getGprsUpdateRateStandby());
			
			prepStmt.setInt(++fieldIndex, miscParam.getSmsUpdateRate());
			prepStmt.setInt(++fieldIndex, miscParam.getPositionStoringDuration());
			prepStmt.setInt(++fieldIndex, miscParam.getOverSPeedLimitDuration());
			prepStmt.setInt(++fieldIndex, miscParam.getOverSpeedLimit());
			prepStmt.setInt(++fieldIndex, miscParam.getHarshBreakDuration1());
			prepStmt.setInt(++fieldIndex, miscParam.getHarshBreakSpeed1());
			prepStmt.setInt(++fieldIndex, miscParam.getHarshBreakDuration2());
			prepStmt.setInt(++fieldIndex, miscParam.getHarshBreakSpeed2());
			prepStmt.setInt(++fieldIndex, miscParam.getHarshAccelerationDuration1());
			prepStmt.setInt(++fieldIndex, miscParam.getHarshAccelerationSpeed1());
			prepStmt.setInt(++fieldIndex, miscParam.getHarshAccelerationDuration2());
			prepStmt.setInt(++fieldIndex, miscParam.getHarshAccelerationSpeed2());
			prepStmt.setInt(++fieldIndex, miscParam.getRouteDeactivationTime());
			prepStmt.setInt(++fieldIndex, miscParam.getRouteDeactiveationDistance());
			prepStmt.setInt(++fieldIndex, miscParam.getTripStartDetectionDistance());
			prepStmt.setInt(++fieldIndex, miscParam.getKeepAliveTimerValue());
			
			returnValue = prepStmt.executeUpdate();
			
			break;
	
		case 54:
			int testMessage = Integer.parseInt(messageToken[startingIndex++]);
			if (testMessage == 0)
				logger.info("TEST Message : FAIL !!!");
			else 
				logger.info("TEST Message : SUCCESS !!!");
			
			
			prepStmt = connection.prepareStatement(DIKSUCHI_KEEPALIVE_PACKET_QUERY);
			prepStmt.setInt(++fieldIndex, packetType);
			prepStmt.setString(++fieldIndex, sourceIMEINumber);
			prepStmt.setInt(++fieldIndex, sequenceNumber);
			
			prepStmt.setString(++fieldIndex, testMessage+"");
			
			returnValue = prepStmt.executeUpdate();
			
			break;
			
		default:
			logger.info("UNKNOWN Packet Type => "+packetType);
			returnValue = DiksuchiProtocolStatus.PROTOCOL_INVALID_PACKET_TYPE.getValue();
		}
		
		/* After extracting header; based on the packet type; take an action */
		//System.out.println("PROTOCOL V3 PROCESSING ACTION DONE !!!");
		} catch (SQLException exp) {
			logger.error("DIKSUCHI SQLException: message={} and ex={}", exp.getMessage(), exp);
		} finally {
            try {
         
            	if (prepStmt != null) {
            		prepStmt.close();
                }
            	if (connection != null) {
            		connection.close();
            	}
            } catch (SQLException ex) {
                logger.error("SQLException Finally message={} and ex={}", ex.getMessage(), ex);
            }
        }
		
		return returnValue;
	}
	
	public String getResponseAckMessage(int ackResponseCode) {
		return "$"+
				sourceIMEINumber+","+
				"3,"+
				sequenceNumber+","+ackPacketType+","+ackResponseCode+"@";
	}
	
	
	

	
	/** Below is USELESS code; due to previous USELESS version document V2 */
	
	public void extractHeaderInformationV2() throws ArrayIndexOutOfBoundsException {
		communicationServerID = (int)clientMessage.charAt(1);
		sourceIMEINumber = String.copyValueOf(clientMessage.toCharArray(), 3, 15);
		
		sequenceNumber = (int)clientMessage.charAt(19); 
		byte[] packetTypeBytes = new byte[] {(byte) clientMessage.charAt(22), (byte) clientMessage.charAt(21) };
		packetType = VTSUtilityHandler.byte2short(packetTypeBytes[1], packetTypeBytes[0]);
		
	}
	
	public void constructAckResponseV2(int ackResponseCode) {
	
		/* Ack can be sent */
		int ackPacketSize = 1/*$*/+16/*DestinationID*/+2/*SourceID*/+2/*SequenceNum*/+3/*PacketType*/+1/*AckResponse*/+1/*@*/;
		/* Construct an ack and keep it ready to send it back to the client */
		clientResponseBuffer = ByteBuffer.allocateDirect(ackPacketSize);
		clientResponseBuffer.clear();
		
		clientResponseBuffer.put((byte) '$');
		clientResponseBuffer.put(sourceIMEINumber.getBytes(), 0, 15);
		clientResponseBuffer.put((byte)',');
		clientResponseBuffer.put((byte)communicationServerID);
		clientResponseBuffer.put((byte)',');
		clientResponseBuffer.put((byte)sequenceNumber);
		clientResponseBuffer.put((byte)',');
		clientResponseBuffer.put((byte)0); /* additional higher byte as per protocol */
		clientResponseBuffer.put((byte)packetType);
		clientResponseBuffer.put((byte)',');
		clientResponseBuffer.put((byte)ackResponseCode);
		clientResponseBuffer.put((byte)'@');
		clientResponseBuffer.flip();
	}
	
	public ByteBuffer getClientResponseBuffer() {
		return clientResponseBuffer;
	}
	
	/** Below function for VERSION 2 */
	public int processClientMessage(String message) {
		this.clientMessage = message;
		int headerLength = 1/*$*/+2/*destinationID,*/+16/*IMEINumber,*/+2/*Sequence,*/+3/*Type,*/;
		int packetTailerLength = 1;/*@*/
		
		/* Verify the header length first */
		if (clientMessage.length() < headerLength+packetTailerLength) {
			System.out.println("Invalid Packet Length = "+clientMessage.length() + " And expected ="+headerLength+packetTailerLength);
			return -1;
		}
		
		try {
			extractHeaderInformationV2();
		} catch (ArrayIndexOutOfBoundsException exp) {
			System.out.println("Header Parsing Error !!!");
			return -1;
		}
		
		/* After extracting header; based on the packet type; take an action */
		System.out.println("PROTOCOL PROCESSING ACTION DONE !!!");
		
		return DiksuchiProtocolStatus.PROTOCOL_SUCCESS.getValue();
	}
}
