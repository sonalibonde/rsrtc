package vts.vtsbackend.core;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.databind.ObjectMapper;

import vts.vtsbackend.VTSCommunicationManager;
import vts.vtsbackend.VTSDataIOMessage;
import vts.vtsbackend.VTSUtilityHandler;
import vts.vtsbackend.rabbitmq.VTSRabbitMQClient;

public class VTSDataIOProcessor implements Runnable {

	private int MAX_SIZE_READER = 1500; //Just making standard MTU size to read the packet 
	private SelectionKey clientKey;
	public final static Logger logger = LoggerFactory.getLogger(VTSDataIOProcessor.class);
	public ClientConnectionReactor clientConnectionReactor;
	private final VTSRabbitMQClient mqPublisherClient;
	
	public VTSDataIOProcessor(SelectionKey inKey, ClientConnectionReactor clientReactor, VTSRabbitMQClient publisherClient) {
		// TODO Auto-generated constructor stub
		this.clientKey = inKey;
		clientConnectionReactor = clientReactor;
		mqPublisherClient = publisherClient;
	}
		
	public void run() {
		// TODO Auto-generated method stub
		SocketChannel clientChannel = (SocketChannel)clientKey.attachment();
		String channelKey = VTSUtilityHandler.getClientAddressKey(clientChannel);
		
		//lets read the data first 
		ByteBuffer localByteBuffer = ByteBuffer.allocateDirect(MAX_SIZE_READER);
		int bytesRead;
		try {
			bytesRead = clientChannel.read(localByteBuffer);
			if (bytesRead == -1) {
				// close this clientChannel from our side too
				clientConnectionReactor.removeClosedClientChannel(channelKey);
				clientChannel.close();
			} else if (bytesRead > 0) {
				localByteBuffer.clear(); // reset the write pointer ? 
				byte[] recvBytes = new byte[bytesRead];
				localByteBuffer.get(recvBytes); // copies bytes into recvBytes buffer 
				localByteBuffer.flip();
				
				String clientMessage = (new String(recvBytes)).trim();
				
				//Now construct the JSON message with three fields 
				//reactorID/QueueName (subscriptionQueue), 
				//clientKey
				//Message received
				
				VTSDataIOMessage dataIOMessage = new VTSDataIOMessage(
						clientConnectionReactor.getReactorQueueName(), channelKey, clientMessage, String.valueOf(VTSCommunicationManager.serverPortNumber));
				ObjectMapper mapper = new ObjectMapper();
				String jsonMessage = mapper.writeValueAsString(dataIOMessage);
				
				/* At the end enable the watcher for this selectionKey Channel */
				clientKey.interestOps(SelectionKey.OP_READ);
				clientConnectionReactor.wakeUpConnectionSelector();
				
				logger.info("Message Published=>{}", jsonMessage);
				mqPublisherClient.publishMessage(jsonMessage);
			}
			// bytesRead == 0; just ignore ? 
		} catch(Exception ex) {
			logger.error("Selector[{}] VTSDataIOProcessor - Exception={} ", clientConnectionReactor.getReactorID(), ex.getMessage());
			logger.error("Exception={}", ex);
			clientConnectionReactor.removeClosedClientChannel(channelKey);
			try {
				clientChannel.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				logger.error("Exception came during closing the clientChannel ex={}", e);
				//e.printStackTrace();
			}
		}
	}
}
