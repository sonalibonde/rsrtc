package vts.vtsbackend;

public class VTSDataIOMessage {

	private String reactorKey;
	private String clientKey;
	private String messageReceived;
	private String serverPort;
	
	public VTSDataIOMessage() {
		// TODO Auto-generated constructor stub
	}
	
	public VTSDataIOMessage(String reactorKey, String clientKey, String dataMessage, String serverPort) {
		// TODO Auto-generated constructor stub
		this.reactorKey = reactorKey;
		this.clientKey = clientKey;
		this.messageReceived = dataMessage;
		this.serverPort = serverPort;
	}

	
	public String getServerPort() {
		return serverPort;
	}

	public void setServerPort(String serverPort) {
		this.serverPort = serverPort;
	}

	public String getReactorKey() {
		return reactorKey;
	}

	public String getClientKey() {
		return clientKey;
	}

	public String getMessageReceived() {
		return messageReceived;
	}

	public void setReactorKey(String reactorKey) {
		this.reactorKey = reactorKey;
	}

	public void setClientKey(String clientKey) {
		this.clientKey = clientKey;
	}

	public void setMessageReceived(String messageReceived) {
		this.messageReceived = messageReceived;
	}
}
