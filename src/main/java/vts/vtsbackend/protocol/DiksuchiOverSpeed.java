package vts.vtsbackend.protocol;

public class DiksuchiOverSpeed {
	
	private long scheduleID;
	private int currentTripNumber;
	private int routeNumberLength;
	private String routeNumber;
	private double longitude;
	private double latitude;
	private long overSpeedStartTime;
	private int maximumSpeed;
	private int overSpeedDuration;
	
	@Override
	public String toString() {
		return "DiksuchiOverSpeed [scheduleID=" + scheduleID + ", currentTripNumber=" + currentTripNumber
				+ ", routeNumberLength=" + routeNumberLength + ", routeNumber=" + routeNumber + ", longitude="
				+ longitude + ", latitude=" + latitude + ", overSpeedStartTime=" + overSpeedStartTime
				+ ", maximumSpeed=" + maximumSpeed + ", overSpeedDuration=" + overSpeedDuration + "]";
	}
	public long getScheduleID() {
		return scheduleID;
	}
	public void setScheduleID(long scheduleID) {
		this.scheduleID = scheduleID;
	}
	public int getCurrentTripNumber() {
		return currentTripNumber;
	}
	public void setCurrentTripNumber(int currentTripNumber) {
		this.currentTripNumber = currentTripNumber;
	}
	public int getRouteNumberLength() {
		return routeNumberLength;
	}
	public void setRouteNumberLength(int routeNumberLength) {
		this.routeNumberLength = routeNumberLength;
	}
	public String getRouteNumber() {
		return routeNumber;
	}
	public void setRouteNumber(String routeNumber) {
		this.routeNumber = routeNumber;
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
	public long getOverSpeedStartTime() {
		return overSpeedStartTime;
	}
	public void setOverSpeedStartTime(long overSpeedStartTime) {
		this.overSpeedStartTime = overSpeedStartTime;
	}
	public int getMaximumSpeed() {
		return maximumSpeed;
	}
	public void setMaximumSpeed(int maximumSpeed) {
		this.maximumSpeed = maximumSpeed;
	}
	public int getOverSpeedDuration() {
		return overSpeedDuration;
	}
	public void setOverSpeedDuration(int overSpeedDuration) {
		this.overSpeedDuration = overSpeedDuration;
	}	
}
