package vts.vtsbackend.protocol;

public enum DiksuchiProtocolStatus {

	PROTOCOL_ACK_IGNORE(-1),
	PROTOCOL_SUCCESS(1),
	PROTOCOL_INVALID_PACKET_TYPE(2),
	PROTOCOL_INVALID_PACKET_FORMAT(3),
	PROTOCOL_INVALID_DATA_LENGTH(4);
	
	private final int value;
	private DiksuchiProtocolStatus(int inValue) {
		this.value = inValue;
	}
	
	public int getValue() {
		return this.value;
	}
}
