package vts.vtsbackend.protocol;

public class DiksuchiPIDStatus {

	private int hardwareRevision;
	private int serialNumber;
	private int bootLoaderSWVersion;
	private int applicationSWVersion;
	private int frontLibraryRevision;
	private String cpuPartNumber;
	private String cpuQualification;
	private int cpuTempRange;
	private String compilationFirmwareDateTime;
	private String flashUpdateStatusDateTime;
	private String testDateTime;
	@Override
	public String toString() {
		return "DiksuchiPIDStatus [hardwareRevision=" + hardwareRevision + ", serialNumber=" + serialNumber
				+ ", bootLoaderSWVersion=" + bootLoaderSWVersion + ", applicationSWVersion=" + applicationSWVersion
				+ ", frontLibraryRevision=" + frontLibraryRevision + ", cpuPartNumber=" + cpuPartNumber
				+ ", cpuQualification=" + cpuQualification + ", cpuTempRange=" + cpuTempRange
				+ ", compilationFirmwareDateTime=" + compilationFirmwareDateTime + ", flashUpdateStatusDateTime="
				+ flashUpdateStatusDateTime + ", testDateTime=" + testDateTime + "]";
	}
	public int getHardwareRevision() {
		return hardwareRevision;
	}
	public void setHardwareRevision(int hardwareRevision) {
		this.hardwareRevision = hardwareRevision;
	}
	public int getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(int serialNumber) {
		this.serialNumber = serialNumber;
	}
	public int getBootLoaderSWVersion() {
		return bootLoaderSWVersion;
	}
	public void setBootLoaderSWVersion(int bootLoaderSWVersion) {
		this.bootLoaderSWVersion = bootLoaderSWVersion;
	}
	public int getApplicationSWVersion() {
		return applicationSWVersion;
	}
	public void setApplicationSWVersion(int applicationSWVersion) {
		this.applicationSWVersion = applicationSWVersion;
	}
	public int getFrontLibraryRevision() {
		return frontLibraryRevision;
	}
	public void setFrontLibraryRevision(int frontLibraryRevision) {
		this.frontLibraryRevision = frontLibraryRevision;
	}
	public String getCpuPartNumber() {
		return cpuPartNumber;
	}
	public void setCpuPartNumber(String cpuPartNumber) {
		this.cpuPartNumber = cpuPartNumber;
	}
	public String getCpuQualification() {
		return cpuQualification;
	}
	public void setCpuQualification(String cpuQualification) {
		this.cpuQualification = cpuQualification;
	}
	public int getCpuTempRange() {
		return cpuTempRange;
	}
	public void setCpuTempRange(int cpuTempRange) {
		this.cpuTempRange = cpuTempRange;
	}
	public String getCompilationFirmwareDateTime() {
		return compilationFirmwareDateTime;
	}
	public void setCompilationFirmwareDateTime(String compilationFirmwareDateTime) {
		this.compilationFirmwareDateTime = compilationFirmwareDateTime;
	}
	public String getFlashUpdateStatusDateTime() {
		return flashUpdateStatusDateTime;
	}
	public void setFlashUpdateStatusDateTime(String flashUpdateStatusDateTime) {
		this.flashUpdateStatusDateTime = flashUpdateStatusDateTime;
	}
	public String getTestDateTime() {
		return testDateTime;
	}
	public void setTestDateTime(String testDateTime) {
		this.testDateTime = testDateTime;
	}
}
