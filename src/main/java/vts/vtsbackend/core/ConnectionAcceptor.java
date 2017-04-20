package vts.vtsbackend.core;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vts.vtsbackend.VTSRabbitMQInfo;


public class ConnectionAcceptor {
	
	public static final int MAX_SELECTORS = 4;

	private ClientConnectionReactor clientReactor[];
	private ServerSocketChannel serverChannel;
	private VTSRabbitMQInfo rabbitMQInfo;
	private Selector mainSelector;
	
	public final static Logger logger = LoggerFactory.getLogger(ConnectionAcceptor.class);
	
	public ConnectionAcceptor(ServerSocketChannel serverSocket) {
		serverChannel = serverSocket;
	}
	
	public void initializeClientChannelSelector(VTSRabbitMQInfo vtsRabbitMQInfo) {
		rabbitMQInfo = vtsRabbitMQInfo;
		
		//Create selector to accept connections
		try {
			mainSelector = Selector.open();
			SelectionKey sk = serverChannel.register(mainSelector, SelectionKey.OP_ACCEPT);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		clientReactor = new ClientConnectionReactor[MAX_SELECTORS];
		for (int selectorIndex = 0; selectorIndex < MAX_SELECTORS; selectorIndex++) {
			clientReactor[selectorIndex] = new ClientConnectionReactor(rabbitMQInfo);
			clientReactor[selectorIndex].initializeClientReactor();
		
			// Lets start reactor threads and pass each reactor to them :) 
			Thread reactorThread = new Thread(new ClientReactorThread(clientReactor[selectorIndex]));
			reactorThread.start();
		}
	}
	
	public ClientConnectionReactor getClientReactor(int reactorIndex) {
		if (reactorIndex >= 0 && reactorIndex <= 3) {
			return clientReactor[reactorIndex];
		}
		System.out.println("Somethin problematic....");
		return null;
	}
	
	public Selector getMainSelector() {
		return mainSelector;
	}
	
	public ServerSocketChannel getServerSocketChannel() {
		return serverChannel;
	}
}