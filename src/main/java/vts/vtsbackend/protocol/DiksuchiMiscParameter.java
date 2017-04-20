package vts.vtsbackend.protocol;

public class DiksuchiMiscParameter {

	private int gprsUpdateRateActive;
	private int gprsUpdateRateNormal;
	private int gprsUpdateRateStandby;
	
	private int smsUpdateRate;
	
	private int positionStoringDuration;
	private int overSpeedLimit;
	private int overSPeedLimitDuration;
	private int harshBreakDuration1;
	private int harshBreakSpeed1;
	private int harshBreakDuration2;
	private int harshBreakSpeed2;
	
	private int harshAccelerationDuration1;
	private int harshAccelerationSpeed1;
	private int harshAccelerationDuration2;
	private int harshAccelerationSpeed2;

	private int routeDeactivationTime;
	private int routeDeactiveationDistance;
	private int tripStartDetectionDistance;
	private int keepAliveTimerValue;
	@Override
	public String toString() {
		return "DiksuchiMiscParameter [gprsUpdateRateActive=" + gprsUpdateRateActive + ", gprsUpdateRateNormal="
				+ gprsUpdateRateNormal + ", gprsUpdateRateStandby=" + gprsUpdateRateStandby + ", smsUpdateRate="
				+ smsUpdateRate + ", positionStoringDuration=" + positionStoringDuration + ", overSpeedLimit="
				+ overSpeedLimit + ", overSPeedLimitDuration=" + overSPeedLimitDuration + ", harshBreakDuration1="
				+ harshBreakDuration1 + ", harshBreakSpeed1=" + harshBreakSpeed1 + ", harshBreakDuration2="
				+ harshBreakDuration2 + ", harshBreakSpeed2=" + harshBreakSpeed2 + ", harshAccelerationDuration1="
				+ harshAccelerationDuration1 + ", harshAccelerationSpeed1=" + harshAccelerationSpeed1
				+ ", harshAccelerationDuration2=" + harshAccelerationDuration2 + ", harshAccelerationSpeed2="
				+ harshAccelerationSpeed2 + ", routeDeactivationTime=" + routeDeactivationTime
				+ ", routeDeactiveationDistance=" + routeDeactiveationDistance + ", tripStartDetectionDistance="
				+ tripStartDetectionDistance + ", keepAliveTimerValue=" + keepAliveTimerValue + "]";
	}
	public int getGprsUpdateRateActive() {
		return gprsUpdateRateActive;
	}
	public void setGprsUpdateRateActive(int gprsUpdateRateActive) {
		this.gprsUpdateRateActive = gprsUpdateRateActive;
	}
	public int getGprsUpdateRateNormal() {
		return gprsUpdateRateNormal;
	}
	public void setGprsUpdateRateNormal(int gprsUpdateRateNormal) {
		this.gprsUpdateRateNormal = gprsUpdateRateNormal;
	}
	public int getGprsUpdateRateStandby() {
		return gprsUpdateRateStandby;
	}
	public void setGprsUpdateRateStandby(int gprsUpdateRateStandby) {
		this.gprsUpdateRateStandby = gprsUpdateRateStandby;
	}
	public int getSmsUpdateRate() {
		return smsUpdateRate;
	}
	public void setSmsUpdateRate(int smsUpdateRate) {
		this.smsUpdateRate = smsUpdateRate;
	}
	public int getPositionStoringDuration() {
		return positionStoringDuration;
	}
	public void setPositionStoringDuration(int positionStoringDuration) {
		this.positionStoringDuration = positionStoringDuration;
	}
	public int getOverSpeedLimit() {
		return overSpeedLimit;
	}
	public void setOverSpeedLimit(int overSpeedLimit) {
		this.overSpeedLimit = overSpeedLimit;
	}
	public int getOverSPeedLimitDuration() {
		return overSPeedLimitDuration;
	}
	public void setOverSPeedLimitDuration(int overSPeedLimitDuration) {
		this.overSPeedLimitDuration = overSPeedLimitDuration;
	}
	public int getHarshBreakDuration1() {
		return harshBreakDuration1;
	}
	public void setHarshBreakDuration1(int harshBreakDuration1) {
		this.harshBreakDuration1 = harshBreakDuration1;
	}
	public int getHarshBreakSpeed1() {
		return harshBreakSpeed1;
	}
	public void setHarshBreakSpeed1(int harshBreakSpeed1) {
		this.harshBreakSpeed1 = harshBreakSpeed1;
	}
	public int getHarshBreakDuration2() {
		return harshBreakDuration2;
	}
	public void setHarshBreakDuration2(int harshBreakDuration2) {
		this.harshBreakDuration2 = harshBreakDuration2;
	}
	public int getHarshBreakSpeed2() {
		return harshBreakSpeed2;
	}
	public void setHarshBreakSpeed2(int harshBreakSpeed2) {
		this.harshBreakSpeed2 = harshBreakSpeed2;
	}
	public int getHarshAccelerationDuration1() {
		return harshAccelerationDuration1;
	}
	public void setHarshAccelerationDuration1(int harshAccelerationDuration1) {
		this.harshAccelerationDuration1 = harshAccelerationDuration1;
	}
	public int getHarshAccelerationSpeed1() {
		return harshAccelerationSpeed1;
	}
	public void setHarshAccelerationSpeed1(int harshAccelerationSpeed1) {
		this.harshAccelerationSpeed1 = harshAccelerationSpeed1;
	}
	public int getHarshAccelerationDuration2() {
		return harshAccelerationDuration2;
	}
	public void setHarshAccelerationDuration2(int harshAccelerationDuration2) {
		this.harshAccelerationDuration2 = harshAccelerationDuration2;
	}
	public int getHarshAccelerationSpeed2() {
		return harshAccelerationSpeed2;
	}
	public void setHarshAccelerationSpeed2(int harshAccelerationSpeed2) {
		this.harshAccelerationSpeed2 = harshAccelerationSpeed2;
	}
	public int getRouteDeactivationTime() {
		return routeDeactivationTime;
	}
	public void setRouteDeactivationTime(int routeDeactivationTime) {
		this.routeDeactivationTime = routeDeactivationTime;
	}
	public int getRouteDeactiveationDistance() {
		return routeDeactiveationDistance;
	}
	public void setRouteDeactiveationDistance(int routeDeactiveationDistance) {
		this.routeDeactiveationDistance = routeDeactiveationDistance;
	}
	public int getTripStartDetectionDistance() {
		return tripStartDetectionDistance;
	}
	public void setTripStartDetectionDistance(int tripStartDetectionDistance) {
		this.tripStartDetectionDistance = tripStartDetectionDistance;
	}
	public int getKeepAliveTimerValue() {
		return keepAliveTimerValue;
	}
	public void setKeepAliveTimerValue(int keepAliveTimerValue) {
		this.keepAliveTimerValue = keepAliveTimerValue;
	}	
}