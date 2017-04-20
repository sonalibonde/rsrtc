package vts.vtsbackend.protocol;

public class DiksuchiPositionPacket {

	private double latitude;
	private double longitude;
	private int altitude;
	private long epochTime;
	private int velocity;
	private double coarseOverGround;
	private int trackedSattelite;
	
	@Override
	public String toString() {
		return "DiksuchiPositionPacket [latitude=" + latitude + ", longitude=" + longitude + ", altitude=" + altitude
				+ ", epochTime=" + epochTime + ", velocity=" + velocity + ", coarseOverGround=" + coarseOverGround
				+ ", trackedSattelite=" + trackedSattelite + "]";
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public int getAltitude() {
		return altitude;
	}

	public void setAltitude(int altitude) {
		this.altitude = altitude;
	}

	public long getEpochTime() {
		return epochTime;
	}

	public void setEpochTime(long epochTime) {
		this.epochTime = epochTime;
	}

	public int getVelocity() {
		return velocity;
	}

	public void setVelocity(int velocity) {
		this.velocity = velocity;
	}

	public double getCoarseOverGround() {
		return coarseOverGround;
	}

	public void setCoarseOverGround(double coarseOverGround) {
		this.coarseOverGround = coarseOverGround;
	}

	public int getTrackedSattelite() {
		return trackedSattelite;
	}

	public void setTrackedSattelite(int trackedSattelite) {
		this.trackedSattelite = trackedSattelite;
	}
	
}
