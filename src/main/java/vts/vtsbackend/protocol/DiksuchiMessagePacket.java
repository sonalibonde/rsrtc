package vts.vtsbackend.protocol;

public class DiksuchiMessagePacket {

	private int messageCodeNumber;
	/* 
	1 Power-Reset OR Reset
	2 Main ON
	3 Main OFF On Battery
	4 Battery Low
	5 Emergency
	6 Break Down
	7 Fire
	8 Tamper ON
	9 Tamper OFF
	*/
	private double longitude;
	private double latitude;
	private long epochTime;
	
	@Override
	public String toString() {
		return "DiksuchiMessagePacket [messageCodeNumber=" + messageCodeNumber + ", longitude=" + longitude
				+ ", latitude=" + latitude + ", epochTime=" + epochTime + "]";
	}
	public int getMessageCodeNumber() {
		return messageCodeNumber;
	}
	public void setMessageCodeNumber(int messageCodeNumber) {
		this.messageCodeNumber = messageCodeNumber;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public long getEpochTime() {
		return epochTime;
	}
	public void setEpochTime(long epochTime) {
		this.epochTime = epochTime;
	}
	
}
