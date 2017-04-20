package vts.vtsbackend.core;

import java.io.IOException;
import java.net.StandardSocketOptions;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Map;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vts.vtsbackend.VTSRabbitMQInfo;
import vts.vtsbackend.VTSUtilityHandler;

public class ClientConnectionReactor {

	// self identity of the clientConnectionReactor
	private final UUID reactorUUID = UUID.randomUUID();
	public final static Logger logger = LoggerFactory.getLogger(ClientConnectionReactor.class);
	
	private final String DATAIO_PUBLISHER_QUEUE;
	// Binding queue name or key for each reactor to wait for the outgoing message.
	private final String reactorOutgoingQueueName = "QUEUE_"+reactorUUID.toString();  
	private VTSRabbitMQInfo rabbitMQInfo;

	// MOST IMPORTANT DataStructure [0,1,2,3.........25K] client watcher 
	private Selector clientConnectionSelector;
	private Map<String, SocketChannel> clientKeyToChannelMapping;
	
	public ClientConnectionReactor(VTSRabbitMQInfo rabbitMQInfo) {
		this.rabbitMQInfo = rabbitMQInfo;
		DATAIO_PUBLISHER_QUEUE = rabbitMQInfo.getConsumerQueueName();
		clientKeyToChannelMapping = new ConcurrentHashMap<String, SocketChannel>();
	}
	
	public void initializeClientReactor() {
		try 
		{
			// Initialize the clientSelector and keep it open to add/monitor client channels on this
			clientConnectionSelector = Selector.open();				
			logger.info("Selector[{}] has been Initialized !!!", reactorUUID);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Selector getClientConnectionSelector() {
		return clientConnectionSelector;
	}
	
	// Add new client to the selector reactor
	public void addAcceptedClientChannel(SocketChannel clientChannel) {
		try 
		{
			clientChannel.configureBlocking(false);
			try {
				logger.info("DEBUGGING Client Socket Options = {}", clientChannel.getOption(StandardSocketOptions.SO_KEEPALIVE));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//Assuming that the same clientConnectionSelector could be accessible by 
			//various other threads 
			synchronized(this) {	
				clientConnectionSelector.wakeup();
				SelectionKey sk = clientChannel.register(clientConnectionSelector, SelectionKey.OP_READ);
				sk.attach(clientChannel);	
			}
			
			logger.info("ADDFUNCTION Reactor={} selector total keys = {}", reactorUUID, clientConnectionSelector.keys().size());
			
			String clientKey = VTSUtilityHandler.getClientAddressKey(clientChannel);
			//This may override the existing clientKey's channel in case it was there earlier and didn't cleaned up. 
			clientKeyToChannelMapping.put(clientKey, clientChannel);
			
			logger.info("Reactor={} has ADDED Client Channel key={} and total Size={}", new Object[] {reactorUUID.toString(), clientKey, clientKeyToChannelMapping.size()});
		} catch (ClosedChannelException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void removeClosedClientChannel(String clientKey) {
		synchronized(this) {
			clientKeyToChannelMapping.remove(clientKey);
			//Also remove this client from the selector ? 
			logger.info("Reactor={} has REMOVED Client Channel key={} and total Size={}", new Object[] {reactorUUID.toString(), clientKey, clientKeyToChannelMapping.size()});
		}
	}
	
	public VTSRabbitMQInfo getRabbitMQInfo() {
		return rabbitMQInfo;
	}
	
	public UUID getReactorID() {
		return reactorUUID;
	}

	public String getReactorQueueName() {
		// TODO Auto-generated method stub
		return reactorOutgoingQueueName;
	}
	
	public String getDataPublisherQueueName() {
		return DATAIO_PUBLISHER_QUEUE;
	}

	public SocketChannel getClientChannel(String clientKey) {
		return clientKeyToChannelMapping.get(clientKey);
	}

	public void wakeUpConnectionSelector() {
		// TODO Auto-generated method stub
		clientConnectionSelector.wakeup();
	}
}
