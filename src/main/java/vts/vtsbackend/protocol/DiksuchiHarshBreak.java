package vts.vtsbackend.protocol;

public class DiksuchiHarshBreak {
	
	private long scheduleID;
	private int currentTripNumber;
	private int routeNumberLength;
	private String routeNumber;
	private double longitude;
	private double latitude;
	private long exceptionStartTime;
	private int startingSpeed;
	private int endingSpeed;
	private int duration;
	@Override
	public String toString() {
		return "DiksuchiHarshBreak [scheduleID=" + scheduleID + ", currentTripNumber=" + currentTripNumber
				+ ", routeNumberLength=" + routeNumberLength + ", routeNumber=" + routeNumber + ", longitude="
				+ longitude + ", latitude=" + latitude + ", exceptionStartTime=" + exceptionStartTime
				+ ", startingSpeed=" + startingSpeed + ", endingSpeed=" + endingSpeed + ", duration=" + duration + "]";
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
	public long getExceptionStartTime() {
		return exceptionStartTime;
	}
	public void setExceptionStartTime(long exceptionStartTime) {
		this.exceptionStartTime = exceptionStartTime;
	}
	public int getStartingSpeed() {
		return startingSpeed;
	}
	public void setStartingSpeed(int startingSpeed) {
		this.startingSpeed = startingSpeed;
	}
	public int getEndingSpeed() {
		return endingSpeed;
	}
	public void setEndingSpeed(int endingSpeed) {
		this.endingSpeed = endingSpeed;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
}
