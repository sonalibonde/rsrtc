package vts.vtsbackend.protocol;

public class DiksuchiRouteDeviation {
	private long scheduleID;
	private int routeNumberLength;
	private String routeNumber;
	private int currentTripNumber;
	
	private int stopNameLength;
	private String firstViolatedStopName;
	private int distanceTraveledFromVioleted;
	
	private double longitude;
	private double latitude;
	@Override
	public String toString() {
		return "DiksuchiRouteDeviation [scheduleID=" + scheduleID + ", routeNumberLength=" + routeNumberLength
				+ ", routeNumber=" + routeNumber + ", currentTripNumber=" + currentTripNumber + ", stopNameLength="
				+ stopNameLength + ", firstViolatedStopName=" + firstViolatedStopName
				+ ", distanceTraveledFromVioleted=" + distanceTraveledFromVioleted + ", longitude=" + longitude
				+ ", latitude=" + latitude + "]";
	}
	public long getScheduleID() {
		return scheduleID;
	}
	public void setScheduleID(long scheduleID) {
		this.scheduleID = scheduleID;
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
	public int getCurrentTripNumber() {
		return currentTripNumber;
	}
	public void setCurrentTripNumber(int currentTripNumber) {
		this.currentTripNumber = currentTripNumber;
	}
	public int getStopNameLength() {
		return stopNameLength;
	}
	public void setStopNameLength(int stopNameLength) {
		this.stopNameLength = stopNameLength;
	}
	public String getFirstViolatedStopName() {
		return firstViolatedStopName;
	}
	public void setFirstViolatedStopName(String firstViolatedStopName) {
		this.firstViolatedStopName = firstViolatedStopName;
	}
	public int getDistanceTraveledFromVioleted() {
		return distanceTraveledFromVioleted;
	}
	public void setDistanceTraveledFromVioleted(int distanceTraveledFromVioleted) {
		this.distanceTraveledFromVioleted = distanceTraveledFromVioleted;
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
}
