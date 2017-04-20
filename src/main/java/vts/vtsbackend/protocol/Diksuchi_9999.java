package vts.vtsbackend.protocol;


import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vts.vtsbackend.VTSUtilityHandler;

public class Diksuchi_9999 extends GenericProtocol{
	
	
	
	String clientMessage;
	
	String pkt;
	String packetType = "1";
	String IMEINO;
	String MSGTYPE;
	String timeField;
	String dateField;
	String gpsStatus;
	Double latitude;
	Double longitude;
	Double vehicleSpeed;
	Double vehicleDirection;
	String nosatellite;
	String gsmlac;
	String gsmcellid;
	String gsmsignalpower;
	Double batteryVoltage;
	int battstate;
	int IGN;
	int tamperStatus;
	int digitalip1;
	int digitalop1;
	double analogueip1;
	double odo;
	String firmVers;
	Timestamp dataTimestamp;
	Timestamp packetDate;
	Timestamp packetTime;
	long uniqueDeviceID = -1;
	int ServerPort;
	String vehicleID;
	String latitudeDirection;
	String longitudeDirection;
	String digitalip1name;
	String digitalip2name;
	String digitalip3name;
	String digitalip4name;
	String digitalop1name;
	String digitalop2name;
	String digitalop3name;
	String digitalop4name;
	String analogueip1name;
	String analogueip11filed;
	String analogueip12filed;
	String analogueip13filed;
	String analogueip14filed;
	String analogueip2name;
	String analogueip21filed;
	String analogueip22filed;
	String analogueip23filed;
	String analogueip24filed;
	String analogueip3name;
	String analogueip31filed;
	String analogueip32filed;
	String analogueip33filed;
	String analogueip34filed;
	String analogueip4name;
	String analogueip41filed;
	String analogueip42filed;
	String analogueip43filed;
	String analogueip44filed;
	
	
	
	public final static Logger logger = LoggerFactory.getLogger(Diksuchi_9999.class);
	
	public Diksuchi_9999() {
		// TODO Auto-generated constructor stub
	}
	public String getClientMessage() {
		return clientMessage;
	}

	public void setClientMessage(String clientMessage) {
		this.clientMessage = clientMessage;
	}

	public String getPkt() {
		return pkt;
	}

	public void setPkt(String pkt) {
		this.pkt = pkt;
	}

	public String getPacketType() {
		return packetType;
	}

	public void setPacketType(String packetType) {
		this.packetType = packetType;
	}

	public String getMSGTYPE() {
		return MSGTYPE;
	}

	public void setMSGTYPE(String mSGTYPE) {
		MSGTYPE = mSGTYPE;
	}

	public String getGpsStatus() {
		return gpsStatus;
	}

	public void setGpsStatus(String gpsStatus) {
		this.gpsStatus = gpsStatus;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getVehicleSpeed() {
		return vehicleSpeed;
	}

	public void setVehicleSpeed(Double vehicleSpeed) {
		this.vehicleSpeed = vehicleSpeed;
	}

	public Double getVehicleDirection() {
		return vehicleDirection;
	}

	public void setVehicleDirection(Double vehicleDirection) {
		this.vehicleDirection = vehicleDirection;
	}

	public String getNosatellite() {
		return nosatellite;
	}

	public void setNosatellite(String nosatellite) {
		this.nosatellite = nosatellite;
	}

	public String getGsmlac() {
		return gsmlac;
	}

	public void setGsmlac(String gsmlac) {
		this.gsmlac = gsmlac;
	}

	public String getGsmcellid() {
		return gsmcellid;
	}

	public void setGsmcellid(String gsmcellid) {
		this.gsmcellid = gsmcellid;
	}

	public String getGsmsignalpower() {
		return gsmsignalpower;
	}

	public void setGsmsignalpower(String gsmsignalpower) {
		this.gsmsignalpower = gsmsignalpower;
	}

	public Double getBatteryVoltage() {
		return batteryVoltage;
	}

	public void setBatteryVoltage(Double batteryVoltage) {
		this.batteryVoltage = batteryVoltage;
	}

	public int getBattstate() {
		return battstate;
	}

	public void setBattstate(int battstate) {
		this.battstate = battstate;
	}

	public int getIGN() {
		return IGN;
	}

	public void setIGN(int iGN) {
		IGN = iGN;
	}

//	public int getTamperStatus() {
//		return tamperStatus;
//	}

	public void setTamperStatus(int tamperStatus) {
		this.tamperStatus = tamperStatus;
	}

	public int getDigitalip1() {
		return digitalip1;
	}

	public void setDigitalip1(int digitalip1) {
		this.digitalip1 = digitalip1;
	}

	public int getDigitalop1() {
		return digitalop1;
	}

	public void setDigitalop1(int digitalop1) {
		this.digitalop1 = digitalop1;
	}

	public double getAnalogueip1() {
		return analogueip1;
	}

	public void setAnalogueip1(double analogueip1) {
		this.analogueip1 = analogueip1;
	}

	public double getOdo() {
		return odo;
	}

	public void setOdo(double odo) {
		this.odo = odo;
	}

	public String getFirmVers() {
		return firmVers;
	}

	public void setFirmVers(String firmVers) {
		this.firmVers = firmVers;
	}

	public Timestamp getPacketDate() {
		return packetDate;
	}

	public void setPacketDate(Timestamp packetDate) {
		this.packetDate = packetDate;
	}

	public Timestamp getPacketTime() {
		return packetTime;
	}

	public void setPacketTime(Timestamp packetTime) {
		this.packetTime = packetTime;
	}

	public long getUniqueDeviceID() {
		return uniqueDeviceID;
	}

	public void setUniqueDeviceID(long uniqueDeviceID) {
		this.uniqueDeviceID = uniqueDeviceID;
	}

	public int getServerPort() {
		return ServerPort;
	}

	public void setServerPort(int serverPort) {
		ServerPort = serverPort;
	}

	public String getVehicleID() {
		return vehicleID;
	}

	public void setVehicleID(String vehicleID) {
		this.vehicleID = vehicleID;
	}

	public String getLatitudeDirection() {
		return latitudeDirection;
	}

	public void setLatitudeDirection(String latitudeDirection) {
		this.latitudeDirection = latitudeDirection;
	}

	public String getLongitudeDirection() {
		return longitudeDirection;
	}

	public void setLongitudeDirection(String longitudeDirection) {
		this.longitudeDirection = longitudeDirection;
	}

	public String getDigitalip1name() {
		return digitalip1name;
	}

	public void setDigitalip1name(String digitalip1name) {
		this.digitalip1name = digitalip1name;
	}

	public String getDigitalip2name() {
		return digitalip2name;
	}

	public void setDigitalip2name(String digitalip2name) {
		this.digitalip2name = digitalip2name;
	}

	public String getDigitalip3name() {
		return digitalip3name;
	}

	public void setDigitalip3name(String digitalip3name) {
		this.digitalip3name = digitalip3name;
	}

	public String getDigitalip4name() {
		return digitalip4name;
	}

	public void setDigitalip4name(String digitalip4name) {
		this.digitalip4name = digitalip4name;
	}

	public String getDigitalop1name() {
		return digitalop1name;
	}

	public void setDigitalop1name(String digitalop1name) {
		this.digitalop1name = digitalop1name;
	}

	public String getDigitalop2name() {
		return digitalop2name;
	}

	public void setDigitalop2name(String digitalop2name) {
		this.digitalop2name = digitalop2name;
	}

	public String getDigitalop3name() {
		return digitalop3name;
	}

	public void setDigitalop3name(String digitalop3name) {
		this.digitalop3name = digitalop3name;
	}

	public String getDigitalop4name() {
		return digitalop4name;
	}

	public void setDigitalop4name(String digitalop4name) {
		this.digitalop4name = digitalop4name;
	}

	public String getAnalogueip1name() {
		return analogueip1name;
	}

	public void setAnalogueip1name(String analogueip1name) {
		this.analogueip1name = analogueip1name;
	}

	public String getAnalogueip11filed() {
		return analogueip11filed;
	}

	public void setAnalogueip11filed(String analogueip11filed) {
		this.analogueip11filed = analogueip11filed;
	}

	public String getAnalogueip12filed() {
		return analogueip12filed;
	}

	public void setAnalogueip12filed(String analogueip12filed) {
		this.analogueip12filed = analogueip12filed;
	}

	public String getAnalogueip13filed() {
		return analogueip13filed;
	}

	public void setAnalogueip13filed(String analogueip13filed) {
		this.analogueip13filed = analogueip13filed;
	}

	public String getAnalogueip14filed() {
		return analogueip14filed;
	}

	public void setAnalogueip14filed(String analogueip14filed) {
		this.analogueip14filed = analogueip14filed;
	}

	public String getAnalogueip2name() {
		return analogueip2name;
	}

	public void setAnalogueip2name(String analogueip2name) {
		this.analogueip2name = analogueip2name;
	}

	public String getAnalogueip21filed() {
		return analogueip21filed;
	}

	public void setAnalogueip21filed(String analogueip21filed) {
		this.analogueip21filed = analogueip21filed;
	}

	public String getAnalogueip22filed() {
		return analogueip22filed;
	}

	public void setAnalogueip22filed(String analogueip22filed) {
		this.analogueip22filed = analogueip22filed;
	}

	public String getAnalogueip23filed() {
		return analogueip23filed;
	}

	public void setAnalogueip23filed(String analogueip23filed) {
		this.analogueip23filed = analogueip23filed;
	}

	public String getAnalogueip24filed() {
		return analogueip24filed;
	}

	public void setAnalogueip24filed(String analogueip24filed) {
		this.analogueip24filed = analogueip24filed;
	}

	public String getAnalogueip3name() {
		return analogueip3name;
	}

	public void setAnalogueip3name(String analogueip3name) {
		this.analogueip3name = analogueip3name;
	}

	public String getAnalogueip31filed() {
		return analogueip31filed;
	}

	public void setAnalogueip31filed(String analogueip31filed) {
		this.analogueip31filed = analogueip31filed;
	}

	public String getAnalogueip32filed() {
		return analogueip32filed;
	}

	public void setAnalogueip32filed(String analogueip32filed) {
		this.analogueip32filed = analogueip32filed;
	}

	public String getAnalogueip33filed() {
		return analogueip33filed;
	}

	public void setAnalogueip33filed(String analogueip33filed) {
		this.analogueip33filed = analogueip33filed;
	}

	public String getAnalogueip34filed() {
		return analogueip34filed;
	}

	public void setAnalogueip34filed(String analogueip34filed) {
		this.analogueip34filed = analogueip34filed;
	}

	public String getAnalogueip4name() {
		return analogueip4name;
	}

	public void setAnalogueip4name(String analogueip4name) {
		this.analogueip4name = analogueip4name;
	}

	public String getAnalogueip41filed() {
		return analogueip41filed;
	}

	public void setAnalogueip41filed(String analogueip41filed) {
		this.analogueip41filed = analogueip41filed;
	}

	public String getAnalogueip42filed() {
		return analogueip42filed;
	}

	public void setAnalogueip42filed(String analogueip42filed) {
		this.analogueip42filed = analogueip42filed;
	}

	public String getAnalogueip43filed() {
		return analogueip43filed;
	}

	public void setAnalogueip43filed(String analogueip43filed) {
		this.analogueip43filed = analogueip43filed;
	}

	public String getAnalogueip44filed() {
		return analogueip44filed;
	}

	public void setAnalogueip44filed(String analogueip44filed) {
		this.analogueip44filed = analogueip44filed;
	}

	public static Logger getLogger() {
		return logger;
	}

	public void setIMEINO(String iMEINO) {
		IMEINO = iMEINO;
	}

	public void setTimeField(String timeField) {
		this.timeField = timeField;
	}

	public void setDateField(String dateField) {
		this.dateField = dateField;
	}

	public void setDataTimestamp(Timestamp dataTimestamp) {
		this.dataTimestamp = dataTimestamp;
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
	
	
	public int processClientMessage(String message) throws Exception {
		logger.info("MESSAGE={}", message);
		this.clientMessage = message;
		String delimitor = ",";
		String[] messageToken = clientMessage.split(delimitor);
		
		int index = 0;
		int returnValue = -1;
		
		pkt = messageToken[index++];  //^K
		IMEINO = messageToken[index++];	//866104025461396
		//sequenceno = messageToken[index++];
		if(messageToken.length <= 5){
			System.out.println("***********Acknowledgment Packet***********");
		}
		else{	
			if(packetType.equals("56")){
				 digitalip1name = messageToken[index++];
				 digitalip2name = messageToken[index++];
				 digitalip3name = messageToken[index++];
				 digitalip4name = messageToken[index++];
				 digitalop1name = messageToken[index++];
				 digitalop2name = messageToken[index++];
				 digitalop3name = messageToken[index++];
				 digitalop4name = messageToken[index++];
				 analogueip1name = messageToken[index++];
				 analogueip11filed = messageToken[index++];
				 analogueip12filed = messageToken[index++];
				 analogueip13filed = messageToken[index++];
				 analogueip14filed = messageToken[index++];
				 analogueip2name = messageToken[index++];
				 analogueip21filed = messageToken[index++];
				 analogueip22filed = messageToken[index++];
				 analogueip23filed = messageToken[index++];
				 analogueip24filed = messageToken[index++];
				 analogueip3name = messageToken[index++];
				 analogueip31filed = messageToken[index++];
				 analogueip32filed = messageToken[index++];
				 analogueip33filed = messageToken[index++];
				 analogueip34filed = messageToken[index++];
				 analogueip4name = messageToken[index++];
				 analogueip41filed = messageToken[index++];
				 analogueip42filed = messageToken[index++];
				 analogueip43filed = messageToken[index++];
				 analogueip44filed = messageToken[index++];
			}else{
	//			^K,866104025461396,L,17:51:17,01/01/04,V,21.17514166,79.10098166,0,306.76,3,,,19,4.21,1,1,1,0,0,0,0,2,2,2,2,0,0.00,0.00,0.00,0.00,0.00,10*
				packetType = messageToken[index++];	//L
				//MSGTYPE = messageToken[index++];
				timeField = messageToken[index++];	//17:51:17
				dateField = messageToken[index++];	//01/01/04
				gpsStatus = messageToken[index++];	//v
				latitude = Double.parseDouble(messageToken[index++]);	//21.17514166
				longitude = Double.parseDouble(messageToken[index++]);	//79.10098166
				vehicleSpeed = Double.parseDouble(messageToken[index++]);	//0
				vehicleDirection = Double.parseDouble(messageToken[index++]);	//306.76
				nosatellite = messageToken[index++];	//3
	//			gsmlac = messageToken[index++];	//
	//			gsmcellid = messageToken[index++]; //
				index++; index++;
				gsmsignalpower = messageToken[index++];	//19
				batteryVoltage = Double.parseDouble(messageToken[index++]);	//4.21
				battstate = Integer.parseInt(messageToken[index++]);	//1
				IGN = Integer.parseInt(messageToken[index++]);	//1
				tamperStatus = Integer.parseInt(messageToken[index++]);	//1	
				digitalip1 = Integer.parseInt(messageToken[index++]);	//0
				index++;index++;index++;	//0,0,0
				digitalop1 = Integer.parseInt(messageToken[index++]);	//2
				index++;index++;index++;index++;	//2,2,2,0
				analogueip1 = Double.parseDouble(messageToken[index++]);	//0.00
				index++;index++;index++;	//0.00	//0.00	//0.00
				odo = Double.parseDouble(messageToken[index++]);	//0.00
				firmVers = messageToken[index++];	//10
			}
			
			
			if(gpsStatus.equals("A")){
				gpsStatus = "1";
			}else{
				gpsStatus = "0";
			}
			
			dataTimestamp = VTSUtilityHandler.getParserDateTimestampDIKSUCHI(dateField, timeField);
			SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yy");
			SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
			java.util.Date parseTimestamp1 = sdf1.parse(dateField);
			java.util.Date parseTimestamp2 = sdf2.parse(timeField);
			packetDate = new Timestamp(parseTimestamp1.getTime());
			packetTime = new Timestamp(parseTimestamp2.getTime());
			Calendar cal = new GregorianCalendar();
		    cal.set(Calendar.YEAR, 1901 - 1);
		    cal.set(Calendar.MONTH, 1 - 1);
		    cal.set(Calendar.DATE, 1);
			cal.set(Calendar.HOUR_OF_DAY, packetTime.getHours());
			cal.set(Calendar.MINUTE, packetTime.getMinutes());
			cal.set(Calendar.SECOND, packetTime.getSeconds());
			packetTime = new Timestamp(cal.getTimeInMillis());
			returnValue = 0;
		}
		
		
		return returnValue;
	}
	
public void insertParsedDeviceRecord(PreparedStatement insertDeviceRecordStmt) {
		
		try {
			int parameterIndex = 1;
			insertDeviceRecordStmt.setTimestamp(parameterIndex++, new Timestamp(new java.util.Date().getTime()));
			insertDeviceRecordStmt.setString(parameterIndex++, this.packetType);
			insertDeviceRecordStmt.setString(parameterIndex++, this.IMEINO);
			insertDeviceRecordStmt.setString(parameterIndex++, this.vehicleID);
			insertDeviceRecordStmt.setString(parameterIndex++, this.gpsStatus);
			
			SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yy");
			java.util.Date parsedDate;
			try {
				parsedDate = sdf1.parse(this.dateField);
				Date packetDate = new Date(parsedDate.getTime());
				insertDeviceRecordStmt.setDate(parameterIndex++, packetDate);
			} catch (ParseException e) {
				logger.error("Exception caught during date conversion : message={} and ex={}", e.getMessage(), e);
			}
			
		
			try {
				Timestamp packetTime = VTSUtilityHandler.getParserDateTimestampDIKSUCHI(this.dateField, this.timeField);
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
			insertDeviceRecordStmt.setInt(parameterIndex++, 0);
			insertDeviceRecordStmt.setString(parameterIndex++, String.valueOf(this.tamperStatus));
			insertDeviceRecordStmt.setInt(parameterIndex++, 0);
			insertDeviceRecordStmt.setDouble(parameterIndex++, this.batteryVoltage);
			insertDeviceRecordStmt.setString(parameterIndex++, null);
			insertDeviceRecordStmt.setString(parameterIndex++, null);
			insertDeviceRecordStmt.setInt(parameterIndex++, this.IGN);
			insertDeviceRecordStmt.setString(parameterIndex++, null);
			
			insertDeviceRecordStmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			logger.error("Exception in insertParsedDeviceRecord message={} and exception={}", e.getMessage(), e);
		}	
	} 
	
public String getResponseAckMessage(int ackResponseCode) {
	return pkt+", "+
			IMEINO+", 250, 255, 1*";//^Z, 123456789012345, 250, 255, 1*
}
}
