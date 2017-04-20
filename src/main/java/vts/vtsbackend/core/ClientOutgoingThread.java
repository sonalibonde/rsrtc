package vts.vtsbackend.core;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import vts.vtsbackend.VTSDataIOMessage;

import com.rabbitmq.client.AMQP.BasicProperties;

// This class is responsible for sending data back to the client
public class ClientOutgoingThread extends DefaultConsumer {

	public final static Logger logger = LoggerFactory.getLogger(ClientOutgoingThread.class);
	
	private final Channel clientChannel;
	private final ClientConnectionReactor clientReactor;
	public ClientOutgoingThread(Channel channel, ClientConnectionReactor reactor) {
		super(channel);
		clientChannel = channel;
		clientReactor = reactor;
	}
	
	@Override
	public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] body)
			throws IOException {
		// TODO Auto-generated method stub
		String message = new String(body, "UTF-8");
		logger.info("Selector[{}] has Received {} !!!", clientReactor.getReactorID(), message);
		handleOutgoingMessage(message);
		
		//This will release the message from the queue
		clientChannel.basicAck(envelope.getDeliveryTag(), false);
	}
	
	private void handleOutgoingMessage(String message) {
		//TODO we will be handling this message and sending back to the client channel here...
		// Okay we got the message and now post it back to the client :) 
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			VTSDataIOMessage ioMessage = mapper.readValue(message, VTSDataIOMessage.class);

			SocketChannel clientChannel = clientReactor.getClientChannel(ioMessage.getClientKey());
			if (clientChannel != null) {
				/* Client connected found; so lets send message */
				ByteBuffer sendBuffer = ByteBuffer.wrap(ioMessage.getMessageReceived().getBytes());
				clientChannel.write(sendBuffer);
				logger.info("handleOutgoingMessage - message={} sent", ioMessage.getMessageReceived());
			} else {
				logger.warn("IGNORE: ClientChannel NOT found for the outgoing request clientChannelKey="+ioMessage.getClientKey());
			}
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Get the channel for this clientKey 
	}
}
