package vts.vtsbackend.protocol;

public class DiksuchiBusStopCrossed {

	private long scheduleID;
	private int currentTripNumber;
	
	private int routeNumberLength;
	private String routeNumber;
	
	private int busStopNameLength;
	private String busStopName;
	private long reachTime;
	
	private int stoppageDuration;
	private int deviationFromBusStop;
	
	@Override
	public String toString() {
		return "DiksuchiBusStopCrossed [scheduleID=" + scheduleID + ", currentTripNumber=" + currentTripNumber
				+ ", routeNumberLength=" + routeNumberLength + ", routeNumber=" + routeNumber + ", busStopNameLength="
				+ busStopNameLength + ", busStopName=" + busStopName + ", reachTime=" + reachTime
				+ ", stoppageDuration=" + stoppageDuration + ", deviationFromBusStop=" + deviationFromBusStop + "]";
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

	public int getBusStopNameLength() {
		return busStopNameLength;
	}

	public void setBusStopNameLength(int busStopNameLength) {
		this.busStopNameLength = busStopNameLength;
	}

	public String getBusStopName() {
		return busStopName;
	}

	public void setBusStopName(String busStopName) {
		this.busStopName = busStopName;
	}

	public long getReachTime() {
		return reachTime;
	}

	public void setReachTime(long reachTime) {
		this.reachTime = reachTime;
	}

	public int getStoppageDuration() {
		return stoppageDuration;
	}

	public void setStoppageDuration(int stoppageDuration) {
		this.stoppageDuration = stoppageDuration;
	}

	public int getDeviationFromBusStop() {
		return deviationFromBusStop;
	}

	public void setDeviationFromBusStop(int deviationFromBusStop) {
		this.deviationFromBusStop = deviationFromBusStop;
	}
	
}
