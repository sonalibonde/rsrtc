package vts.vtsbackend;

public class VTSClientID {
	private final String hostName;
	private final int portNumber;
	public VTSClientID(String clientKey) {
		String tokens[] = clientKey.split(":");
		hostName = tokens[0];
		portNumber = Integer.parseInt(tokens[1]);
	}
	
	public String getHostName() {
		return hostName;
	}
	
	public int getPortNumber() {
		return portNumber;
	}
}
