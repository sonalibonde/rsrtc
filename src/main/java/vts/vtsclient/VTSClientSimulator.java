package vts.vtsclient;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vts.vtsbackend.rabbitmq.VTSRabbitMQClient;


public class VTSClientSimulator implements Runnable {

	private String message = "@LIVE,353301056510030,DL3CBD3302,C,10/03/2014,18:44:55,2830.59665,N,07704.77513,E,"
			+ "35.1,123.20,1,0,0,4.2,SW_V1.1,355233054665904-10/03/2014-18:44:45,1,2A";
	
	private int totalNumberOfClients = 1000;
	private Selector clientSelector;
	private String serverHost;
	private int serverPort;
	private int reactorIndex;
	public final static Logger logger = LoggerFactory.getLogger(VTSClientSimulator.class);
		
	public VTSClientSimulator(int totalClients, String hostName, int portNumber, int reactorIndex) {
		this.serverHost = hostName;
		this.serverPort = portNumber;
		this.totalNumberOfClients = totalClients;
		this.reactorIndex = reactorIndex;
		
		initializeClients();
	}
	
	class ClientHandler {
		private String clientID;
		
		public ClientHandler(String clientID) {
			this.clientID = clientID;
		}
	}
	
	public void initializeClients() {
	
		try {
			clientSelector = Selector.open();
		
			for (int clientIndex = 0; clientIndex < totalNumberOfClients; clientIndex++) {
				SocketChannel clientSocketChannel;
				clientSocketChannel = SocketChannel.open();
				clientSocketChannel.configureBlocking(false);
				
				/* Connect this channel with Server and register with Selector */
				clientSocketChannel.connect(new java.net.InetSocketAddress(serverHost, serverPort));
				
				SelectionKey clientKey = clientSocketChannel.register(clientSelector, SelectionKey.OP_CONNECT);
				clientKey.attach(new ClientHandler(reactorIndex+"-"+clientIndex));
				Thread.sleep(1);
				logger.debug("Slept and now going to execute run..!");
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run() {
		System.out.println("Thead Client Started =>"+Thread.currentThread().getName());
		try {
			while (!Thread.interrupted()) {
				
					System.out.println("Came here to SELECT !!!!"+Thread.currentThread().getName());
				
					int keyCount = clientSelector.select();
					
					
					logger.info("SELECTOR[{}] found KEYCOUNT={}", Thread.currentThread().getName(), keyCount);
					if (keyCount <= 0) {
						try {
							Thread.sleep(10000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						continue;
					}
					
					
					System.out.println("CameOUT : "+Thread.currentThread().getName());
					//
					/** get the keys **/
					Set<SelectionKey> clientKeys = clientSelector.selectedKeys();
					Iterator<SelectionKey> clientIterator = clientKeys.iterator();
					while (clientIterator.hasNext()) {
					
						SelectionKey clientKey = clientIterator.next();
						SocketChannel clientChannel = (SocketChannel)clientKey.channel();
					
						clientIterator.remove();
						String clientID = null;
						
						/** Attempt a connection **/
						if (clientKey.isConnectable()) {
							ClientHandler clientHandler = (ClientHandler)clientKey.attachment();
							if (clientChannel.isConnectionPending()) {
								clientChannel.finishConnect();
								try {
									Thread.sleep(100);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} //1 seconds
								clientID = clientHandler.clientID;
								System.out.println("Client["+clientHandler.clientID+"] Connected");
								clientKey.interestOps(clientKey.interestOps() ^ SelectionKey.OP_CONNECT);
								clientKey.interestOps(clientKey.interestOps() | SelectionKey.OP_READ);
								
								ByteBuffer buffer = null;
								buffer = ByteBuffer.wrap(new String("Client["+clientHandler.clientID+"] # "+message).getBytes());
								
								clientChannel.write(buffer);
								System.out.println("1st SENT =>"+buffer.toString());
							}
						}
						
						if (clientKey.isReadable()) {
							
							/** We will read the data and then dump the data */
							
							/* Connection established; send some message **/
							ClientHandler clientHandler = (ClientHandler)clientKey.attachment();
							ByteBuffer buffer = null;
							buffer = ByteBuffer.wrap(new String("Client["+clientHandler.clientID+"] # "+message).getBytes());
							
							clientChannel.write(buffer);
							System.out.println("2nd SENT =>"+buffer.toString());
						} else {
							
							clientKey.interestOps(clientKey.interestOps() ^ SelectionKey.OP_CONNECT);
							clientKey.interestOps(clientKey.interestOps() | SelectionKey.OP_READ);
							
							ByteBuffer buffer = null;
							buffer = ByteBuffer.wrap(new String("Client["+clientID+"] # "+message).getBytes());
							
							clientChannel.write(buffer);
							System.out.println("3rd SENT =>"+buffer.toString());
						}
					}
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		System.out.println("Thread Client Ends Here....!!!"+Thread.currentThread().getName());
	}
	
	public static void main(String[] args) {
		int numberOfClients = Integer.parseInt(args[0]);
		String serverHost = args[1];
		int serverPort = Integer.parseInt(args[2]);
		
		int numberOfClientThreads = 4;
		Executor clientHandlerExecutor = Executors.newFixedThreadPool(numberOfClientThreads);
		for(int clientIndex = 0; clientIndex < numberOfClientThreads; clientIndex++) {
			clientHandlerExecutor.execute(new VTSClientSimulator(numberOfClients, serverHost, serverPort, clientIndex+1));
		}
	}
}