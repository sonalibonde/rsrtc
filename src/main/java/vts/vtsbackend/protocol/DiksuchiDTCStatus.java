package vts.vtsbackend.protocol;

public class DiksuchiDTCStatus {

	private int watchDogReset;
	private int lowVoltageReset;
	private int lostCommunicationWithGPS;
	private int gpsSignalInvalid;
	private int gpsAntennaError;
	private int invalidStoageDevice;
	private int unknownUSBDeviceConnected;
	private int usbInvalidFS;
	private int usbOverCurrent;
	private int overVoltageCount;
	private int overVoltageValue;
	private int lowVoltageCount;
	private int lowVoltageValue;
	
	private int overHeatCount;
	private int overHeatValue;
	@Override
	public String toString() {
		return "DiksuchiDTCStatus [watchDogReset=" + watchDogReset + ", lowVoltageReset=" + lowVoltageReset
				+ ", lostCommunicationWithGPS=" + lostCommunicationWithGPS + ", gpsSignalInvalid=" + gpsSignalInvalid
				+ ", gpsAntennaError=" + gpsAntennaError + ", invalidStoageDevice=" + invalidStoageDevice
				+ ", unknownUSBDeviceConnected=" + unknownUSBDeviceConnected + ", usbInvalidFS=" + usbInvalidFS
				+ ", usbOverCurrent=" + usbOverCurrent + ", overVoltageCount=" + overVoltageCount
				+ ", overVoltageValue=" + overVoltageValue + ", lowVoltageCount=" + lowVoltageCount
				+ ", lowVoltageValue=" + lowVoltageValue + ", overHeatCount=" + overHeatCount + ", overHeatValue="
				+ overHeatValue + "]";
	}
	public int getWatchDogReset() {
		return watchDogReset;
	}
	public void setWatchDogReset(int watchDogReset) {
		this.watchDogReset = watchDogReset;
	}
	public int getLowVoltageReset() {
		return lowVoltageReset;
	}
	public void setLowVoltageReset(int lowVoltageReset) {
		this.lowVoltageReset = lowVoltageReset;
	}
	public int getLostCommunicationWithGPS() {
		return lostCommunicationWithGPS;
	}
	public void setLostCommunicationWithGPS(int lostCommunicationWithGPS) {
		this.lostCommunicationWithGPS = lostCommunicationWithGPS;
	}
	public int getGpsSignalInvalid() {
		return gpsSignalInvalid;
	}
	public void setGpsSignalInvalid(int gpsSignalInvalid) {
		this.gpsSignalInvalid = gpsSignalInvalid;
	}
	public int getGpsAntennaError() {
		return gpsAntennaError;
	}
	public void setGpsAntennaError(int gpsAntennaError) {
		this.gpsAntennaError = gpsAntennaError;
	}
	public int getInvalidStoageDevice() {
		return invalidStoageDevice;
	}
	public void setInvalidStoageDevice(int invalidStoageDevice) {
		this.invalidStoageDevice = invalidStoageDevice;
	}
	public int getUnknownUSBDeviceConnected() {
		return unknownUSBDeviceConnected;
	}
	public void setUnknownUSBDeviceConnected(int unknownUSBDeviceConnected) {
		this.unknownUSBDeviceConnected = unknownUSBDeviceConnected;
	}
	public int getUsbInvalidFS() {
		return usbInvalidFS;
	}
	public void setUsbInvalidFS(int usbInvalidFS) {
		this.usbInvalidFS = usbInvalidFS;
	}
	public int getUsbOverCurrent() {
		return usbOverCurrent;
	}
	public void setUsbOverCurrent(int usbOverCurrent) {
		this.usbOverCurrent = usbOverCurrent;
	}
	public int getOverVoltageCount() {
		return overVoltageCount;
	}
	public void setOverVoltageCount(int overVoltageCount) {
		this.overVoltageCount = overVoltageCount;
	}
	public int getOverVoltageValue() {
		return overVoltageValue;
	}
	public void setOverVoltageValue(int overVoltageValue) {
		this.overVoltageValue = overVoltageValue;
	}
	public int getLowVoltageCount() {
		return lowVoltageCount;
	}
	public void setLowVoltageCount(int lowVoltageCount) {
		this.lowVoltageCount = lowVoltageCount;
	}
	public int getLowVoltageValue() {
		return lowVoltageValue;
	}
	public void setLowVoltageValue(int lowVoltageValue) {
		this.lowVoltageValue = lowVoltageValue;
	}
	public int getOverHeatCount() {
		return overHeatCount;
	}
	public void setOverHeatCount(int overHeatCount) {
		this.overHeatCount = overHeatCount;
	}
	public int getOverHeatValue() {
		return overHeatValue;
	}
	public void setOverHeatValue(int overHeatValue) {
		this.overHeatValue = overHeatValue;
	}
}
