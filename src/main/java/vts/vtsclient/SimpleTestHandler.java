package vts.vtsclient;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class SimpleTestHandler {

	public static void main(String[] args) {
		System.out.println("Here we are ....");
		//String message = "@HISTORY-LIVE,911225202410776,XYZ,N,16/05/2016,11:12:48,0.0,N,0.0,E,0.0,0.00,1,1,0,4.0,SW_V1.0,16/05/2016-11:12:12,37.50,0.0,D,1,1,00:00:25,5\n\r\n@HISTORY-TRIPSTART,911225202410776,XYZ,N,16/05/2016,11:12:49,0.0,N,0.0,E,0.0,0.00,1,1,0,4.0,SW_V1.0,16/05/2016-11:12:12,16/05/2016,11:12:12,1,36\n\r\n@HISTORY-BATOKTRIP,911225202410776,XYZ,N,16/05/2016,11:12:50,0.0,N,0.0,E,0.0,0.00,1,1,0,4.0,SW_V1.0,16/05/2016-11:12:12,37.50,0.0,D,1,1,00:00:25,CB\n\r\n@HISTORY-HEALTH,911225202410776,N,0,16/05/2016,11:12:52,0.0,15.0,0.0,SW_V1.0,F7\n\r\n@HISTORY-LIVE,911225202410776,XYZ,N,16/05/2016,11:12:58,0.0,N,0.0,E,0.0,0.00,1,1,0,4.0,SW_V1.0,16/05/2016-11:12:12,37.50,0.0,D,1,1,00:00:25,4\n\r\n@HISTORY-LIVE,911225202410776,XYZ,N,16/05/2016,11:13:08,0.0,N,0.0,E,0.0,0.00,1,1,0,4.0,SW_V1.0,16/05/2016-11:12:12,37.50,0.0,D,1,1,00:00:25,8\n\r\n@HISTORY-LIVE,911225202410776,XYZ,N,16/05/2016,11:13:18,0.0,N,0.0,E,0.0,0.00,1,1,0,4.0,SW_V1.0,16/05/2016-11:12:12,37.50,0.0,D,1,1,00:00:25,7\n\r\n@HISTORY-LIVE,911225202410776,XYZ,N,16/05/2016,11:13:21,2834.354,N,76";
		String message = "@HISTORY-LIVE,911225202781036,XYZ,C,02/07/2016,16:07:23,1303.1094,N,7729.2924,E,16.209632873535156,0.00,0,1,0,4.0,SW_V1.0,1,B0\n\r\n\r\n@HISTORY-LIVE,911225202781036,XYZ,C,02/07/2016,16:08:03,1303.3442,N,7728.6325,E,22.87065315246582,0.00,0,1,0,4.0,SW_V1.0,1,B1\n\r\n\r\n@HISTORY-LIVE,911225202781036,XYZ,C,02/07/2016,16:08:42,1303.5245,N,7727.9882,E,17.762548446655273,0.00,0,1,0,4.0,SW_V1.0,1,AE\n\r\n\r\n@HISTORY-LIVE,911225202781036,XYZ,C,02/07/2016,16:09:22,1303.5245,N,7727.9882,E,17.762548446655273,0.00,0,1,0,4.0,SW_V1.0,1,AF\n\r\n\r\n@HISTORY-LIVE,911225202781036,XYZ,C,02/07/2016,16:10:02,1303.7865,N,7727.4441,E,23.44096565246582,0.00,0,1,0,4.0,SW_V1.0,1,B9\n\r\n\r\n@HISTORY-LIVE,911225202781036,XYZ,C,27/07/2016,16:10:42,1304.1578,N,7726.8222,E,18.674636840820313,0.00,0,1,0,4.0,SW_V1.0,1,AE\n\r\n\r\n@HISTORY-LIVE,911225202781036,XYZ,C,27/07/2016,16:11:22,1304.1578,N,7726.8222,E,18.674636840820313,0.00,0,1,0,4.0,SW_V1.0,1,AF\n\r\n\r\n@HISTORY-LIVE,911225202781036,XYZ,C,27/07/2016,16:12:02,1304.4871,N,7726.2205,E,19.221786499023438,0.00,0,1,0,4.0,SW_V1.0,1,B0\n\r\n\r\n@HISTORY-LIVE,911225202781036,XYZ,C,02/07/2016,16:12:42,1304.726,N,7725.6534,E,17";
		//String message = "@HISTORY-LIVE,911225202410776,XYZ,N,16/05/2016,11:12:48,0.0,N,0.0,E,0.0,0.00,1,1,0,4.0,SW_V1.0,16/05/2016-11:12:12,37.50,0.0,D,1,1,00:00:25,5";
		String[] messages = message.split("\n");
		
		
		SocketChannel clientSocketChannel;
		try {
			//clientSocketChannel = SocketChannel.open();
			//clientSocketChannel.connect(new java.net.InetSocketAddress("localhost", 9898));
			
			//ByteBuffer buffer = null;
		
			for (String receivedMessage: messages) {
				System.out.println("MESSAGE="+ receivedMessage);
				//buffer = ByteBuffer.wrap(new String(receivedMessage).getBytes());
				//clientSocketChannel.write(buffer);
				//Thread.sleep(20000);
			}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		//} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
	}
	
}
