package vts.vtsbackend.protocol;

public class DiksuchiDisplayBoardStatus {

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
	private String articleNumberSignLevel;
	private String productionDate;
	private String endCustomer;
	
	private String orderNumber;
	private String busVehicleType;
	
	private int busBuilderNumber;
	private String languageCode;
	private String boardTempSensor;
	private int internalCPUTemp;
	private int minCPUTemp;
	private int maxCPUTemp;
	private int minBoardTemp;
	private int maxBoardTemp;
	private int maxPowerVoltage;
	private int minPowerVoltage;
	
	private long operatingHours;
	private int numberOfResets;
	
	@Override
	public String toString() {
		return "DiksuchiDisplayBoardStatus [hardwareRevision=" + hardwareRevision + ", serialNumber=" + serialNumber
				+ ", bootLoaderSWVersion=" + bootLoaderSWVersion + ", applicationSWVersion=" + applicationSWVersion
				+ ", frontLibraryRevision=" + frontLibraryRevision + ", cpuPartNumber=" + cpuPartNumber
				+ ", cpuQualification=" + cpuQualification + ", cpuTempRange=" + cpuTempRange
				+ ", compilationFirmwareDateTime=" + compilationFirmwareDateTime + ", flashUpdateStatusDateTime="
				+ flashUpdateStatusDateTime + ", testDateTime=" + testDateTime + ", articleNumberSignLevel="
				+ articleNumberSignLevel + ", productionDate=" + productionDate + ", endCustomer=" + endCustomer
				+ ", orderNumber=" + orderNumber + ", busVehicleType=" + busVehicleType + ", busBuilderNumber="
				+ busBuilderNumber + ", languageCode=" + languageCode + ", boardTempSensor=" + boardTempSensor
				+ ", internalCPUTemp=" + internalCPUTemp + ", minCPUTemp=" + minCPUTemp + ", maxCPUTemp=" + maxCPUTemp
				+ ", minBoardTemp=" + minBoardTemp + ", maxBoardTemp=" + maxBoardTemp + ", maxPowerVoltage="
				+ maxPowerVoltage + ", minPowerVoltage=" + minPowerVoltage + ", operatingHours=" + operatingHours
				+ ", numberOfResets=" + numberOfResets + "]";
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
	public String getArticleNumberSignLevel() {
		return articleNumberSignLevel;
	}
	public void setArticleNumberSignLevel(String articleNumberSignLevel) {
		this.articleNumberSignLevel = articleNumberSignLevel;
	}
	public String getProductionDate() {
		return productionDate;
	}
	public void setProductionDate(String productionDate) {
		this.productionDate = productionDate;
	}
	public String getEndCustomer() {
		return endCustomer;
	}
	public void setEndCustomer(String endCustomer) {
		this.endCustomer = endCustomer;
	}
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	public String getBusVehicleType() {
		return busVehicleType;
	}
	public void setBusVehicleType(String busVehicleType) {
		this.busVehicleType = busVehicleType;
	}
	public int getBusBuilderNumber() {
		return busBuilderNumber;
	}
	public void setBusBuilderNumber(int busBuilderNumber) {
		this.busBuilderNumber = busBuilderNumber;
	}
	public String getLanguageCode() {
		return languageCode;
	}
	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}
	public String getBoardTempSensor() {
		return boardTempSensor;
	}
	public void setBoardTempSensor(String boardTempSensor) {
		this.boardTempSensor = boardTempSensor;
	}
	public int getInternalCPUTemp() {
		return internalCPUTemp;
	}
	public void setInternalCPUTemp(int internalCPUTemp) {
		this.internalCPUTemp = internalCPUTemp;
	}
	public int getMinCPUTemp() {
		return minCPUTemp;
	}
	public void setMinCPUTemp(int minCPUTemp) {
		this.minCPUTemp = minCPUTemp;
	}
	public int getMaxCPUTemp() {
		return maxCPUTemp;
	}
	public void setMaxCPUTemp(int maxCPUTemp) {
		this.maxCPUTemp = maxCPUTemp;
	}
	public int getMinBoardTemp() {
		return minBoardTemp;
	}
	public void setMinBoardTemp(int minBoardTemp) {
		this.minBoardTemp = minBoardTemp;
	}
	public int getMaxBoardTemp() {
		return maxBoardTemp;
	}
	public void setMaxBoardTemp(int maxBoardTemp) {
		this.maxBoardTemp = maxBoardTemp;
	}
	public int getMaxPowerVoltage() {
		return maxPowerVoltage;
	}
	public void setMaxPowerVoltage(int maxPowerVoltage) {
		this.maxPowerVoltage = maxPowerVoltage;
	}
	public int getMinPowerVoltage() {
		return minPowerVoltage;
	}
	public void setMinPowerVoltage(int minPowerVoltage) {
		this.minPowerVoltage = minPowerVoltage;
	}
	public long getOperatingHours() {
		return operatingHours;
	}
	public void setOperatingHours(long operatingHours) {
		this.operatingHours = operatingHours;
	}
	public int getNumberOfResets() {
		return numberOfResets;
	}
	public void setNumberOfResets(int numberOfResets) {
		this.numberOfResets = numberOfResets;
	}
}
