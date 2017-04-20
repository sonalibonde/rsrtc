package vts.vtsbackend.protocol;

public class DiksuchiRouteSelect {

	private long scheduleID;
	private int routeNumberLength;
	private String routeNumber;
	private int currentTripNumber;
	
	private double longitude;
	private double latitude;
	private long epochTime;
	
	@Override
	public String toString() {
		return "DiksuchiRouteSelect [scheduleID=" + scheduleID + ", routeNumberLength=" + routeNumberLength
				+ ", routeNumber=" + routeNumber + ", currentTripNumber=" + currentTripNumber + ", longitude="
				+ longitude + ", latitude=" + latitude + ", epochTime=" + epochTime + "]";
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
