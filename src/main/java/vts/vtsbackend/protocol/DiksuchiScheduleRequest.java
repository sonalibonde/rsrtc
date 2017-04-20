package vts.vtsbackend.protocol;

public class DiksuchiScheduleRequest {
	
	private long scheduleID;
	private double longitude;
	private double latitude;
	private long epochTime;

	@Override
	public String toString() {
		return "DiksuchiScheduleRequest [scheduleID=" + scheduleID + ", longitude=" + longitude + ", latitude="
				+ latitude + ", epochTime=" + epochTime + "]";
	}
	public long getScheduleID() {
		return scheduleID;
	}
	public void setScheduleID(long scheduleID) {
		this.scheduleID = scheduleID;
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
