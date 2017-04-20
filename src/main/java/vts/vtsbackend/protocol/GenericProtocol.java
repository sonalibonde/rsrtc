package vts.vtsbackend.protocol;

import java.sql.Timestamp;

public class GenericProtocol {

	// Default parameters to be scanned through 
	Timestamp dataTimestamp;
	String packetType;
	String IMEINO;
	String vehicleID;
	String gpsStatus;
	String dateField;
	String timeField;
	Double latitude;
	String latitudeDirection;
	Double longitude;
	String longitudeDirection;
	Double vehicleSpeed;
	Double vehicleDirection;
	int IGN;
	String tamperStatus;
	int panic;
	String networkoperator;
	String packetstatus;
	
	
	public GenericProtocol() { }
	
	public GenericProtocol(String IMEINumber, Double latitude, Double longitude, Timestamp packetTimestamp) {
		this.IMEINO = IMEINumber;
		this.latitude = latitude;
		this.longitude = longitude;
		this.dataTimestamp = packetTimestamp;
		vehicleID = "";
		gpsStatus = "";
		dateField = "";
		timeField = "";
		latitudeDirection = "";
		longitudeDirection = "";
		vehicleSpeed = 0.0;
		vehicleDirection = 0.0;
		IGN = 0;
		tamperStatus = "";
		this.panic = 0;
		networkoperator = "";
		packetstatus = "";
	}
	
	public Timestamp getDataTimestamp() {
		return dataTimestamp;
	}
	public String getPacketType() {
		return packetType;
	}
	public String getIMEINO() {
		return IMEINO;
	}
	public String getVehicleID() {
		return vehicleID;
	}
	public String getGpsStatus() {
		return gpsStatus;
	}
	public String getDateField() {
		return dateField;
	}
	public String getTimeField() {
		return timeField;
	}
	public Double getLatitude() {
		return latitude;
	}
	public String getLatitudeDirection() {
		return latitudeDirection;
	}
	public Double getLongitude() {
		return longitude;
	}
	public String getLongitudeDirection() {
		return longitudeDirection;
	}
	public Double getVehicleSpeed() {
		return vehicleSpeed;
	}
	public Double getVehicleDirection() {
		return vehicleDirection;
	}
	public int getIGN() {
		return IGN;
	}
	public String getTamperStatus() {
		return tamperStatus;
	}

	public int getPanic() {
		return panic;
	}

	public void setPanic(int panic) {
		this.panic = panic;
	}

	public String getNetworkoperator() {
		return networkoperator;
	}

	public void setNetworkoperator(String networkoperator) {
		this.networkoperator = networkoperator;
	}

	public String getPacketstatus() {
		return packetstatus;
	}

	public void setPacketstatus(String packetstatus) {
		this.packetstatus = packetstatus;
	}

	
	
}
