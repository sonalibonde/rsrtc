package vts.vtsbackend.protocol;

public class DiksuchiModeParameter {

	private int pollStatus;
	private int communicationMode;
	private int smsPhoneNumberLength;
	private String smsPhoneNumber;
	
	private int primaryIPAddressLength;
	private String primaryIPAddress;
	
	private int secondaryIPAddressLength;
	private String secondaryIPAddress;
	private int tcpPortNumber;
	private int apnLength;
	private String apnValue;
	private int urlLength;
	private String urlValue;
	
	private int dnsEnabled;
	private int dnsLength;
	private String dnsValue;
	@Override
	public String toString() {
		return "DiksuchiModeParameter [pollStatus=" + pollStatus + ", communicationMode=" + communicationMode
				+ ", smsPhoneNumberLength=" + smsPhoneNumberLength + ", smsPhoneNumber=" + smsPhoneNumber
				+ ", primaryIPAddressLength=" + primaryIPAddressLength + ", primaryIPAddress=" + primaryIPAddress
				+ ", secondaryIPAddressLength=" + secondaryIPAddressLength + ", secondaryIPAddress="
				+ secondaryIPAddress + ", tcpPortNumber=" + tcpPortNumber + ", apnLength=" + apnLength + ", apnValue="
				+ apnValue + ", urlLength=" + urlLength + ", urlValue=" + urlValue + ", dnsEnabled=" + dnsEnabled
				+ ", dnsLength=" + dnsLength + ", dnsValue=" + dnsValue + "]";
	}
	public int getPollStatus() {
		return pollStatus;
	}
	public void setPollStatus(int pollStatus) {
		this.pollStatus = pollStatus;
	}
	public int getCommunicationMode() {
		return communicationMode;
	}
	public void setCommunicationMode(int communicationMode) {
		this.communicationMode = communicationMode;
	}
	public int getSmsPhoneNumberLength() {
		return smsPhoneNumberLength;
	}
	public void setSmsPhoneNumberLength(int smsPhoneNumberLength) {
		this.smsPhoneNumberLength = smsPhoneNumberLength;
	}
	public String getSmsPhoneNumber() {
		return smsPhoneNumber;
	}
	public void setSmsPhoneNumber(String smsPhoneNumber) {
		this.smsPhoneNumber = smsPhoneNumber;
	}
	public int getPrimaryIPAddressLength() {
		return primaryIPAddressLength;
	}
	public void setPrimaryIPAddressLength(int primaryIPAddressLength) {
		this.primaryIPAddressLength = primaryIPAddressLength;
	}
	public String getPrimaryIPAddress() {
		return primaryIPAddress;
	}
	public void setPrimaryIPAddress(String primaryIPAddress) {
		this.primaryIPAddress = primaryIPAddress;
	}
	public int getSecondaryIPAddressLength() {
		return secondaryIPAddressLength;
	}
	public void setSecondaryIPAddressLength(int secondaryIPAddressLength) {
		this.secondaryIPAddressLength = secondaryIPAddressLength;
	}
	public String getSecondaryIPAddress() {
		return secondaryIPAddress;
	}
	public void setSecondaryIPAddress(String secondaryIPAddress) {
		this.secondaryIPAddress = secondaryIPAddress;
	}
	public int getTcpPortNumber() {
		return tcpPortNumber;
	}
	public void setTcpPortNumber(int tcpPortNumber) {
		this.tcpPortNumber = tcpPortNumber;
	}
	public int getApnLength() {
		return apnLength;
	}
	public void setApnLength(int apnLength) {
		this.apnLength = apnLength;
	}
	public String getApnValue() {
		return apnValue;
	}
	public void setApnValue(String apnValue) {
		this.apnValue = apnValue;
	}
	public int getUrlLength() {
		return urlLength;
	}
	public void setUrlLength(int urlLength) {
		this.urlLength = urlLength;
	}
	public String getUrlValue() {
		return urlValue;
	}
	public void setUrlValue(String urlValue) {
		this.urlValue = urlValue;
	}
	public int getDnsEnabled() {
		return dnsEnabled;
	}
	public void setDnsEnabled(int dnsEnabled) {
		this.dnsEnabled = dnsEnabled;
	}
	public int getDnsLength() {
		return dnsLength;
	}
	public void setDnsLength(int dnsLength) {
		this.dnsLength = dnsLength;
	}
	public String getDnsValue() {
		return dnsValue;
	}
	public void setDnsValue(String dnsValue) {
		this.dnsValue = dnsValue;
	}
}
