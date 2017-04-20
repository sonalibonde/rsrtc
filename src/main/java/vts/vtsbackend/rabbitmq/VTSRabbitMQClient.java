package vts.vtsbackend.rabbitmq;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.MessageProperties;

import vts.vtsbackend.VTSRabbitMQInfo;


/**
 * Create the object of this class 
 ** We will bring the message configuration into this constructure later
 *  VTSRabbitMQClient vtsRabbitMQClient = new VTSRabbitMQClient();
 *  while (1) {
 *  	vtsRabbitMQClient.publishMessage(publishMessageJsonString)
 *  }
 *  done...No closing of the client until the client is dead 
 * @author tchahand
 *
 */

public class VTSRabbitMQClient {

	public final static Logger logger = LoggerFactory.getLogger(VTSRabbitMQClient.class);
	private Channel clientChannel;
	private Connection clientConnection;
	private final String defaultExchangeName = "defaultExchangeName";
	private final String defaultRoutingKey = "deviceIncomingData";
	private String queueName;
	
	/* 
	 * serverHost - provide the server host nmae/ip so that client can connect to it
	 * queueName - provide the queue name where we are going to publish the message or read the message from 
	 */
	public VTSRabbitMQClient() {
		queueName = null;
	}
	
	public boolean initializeClient(VTSRabbitMQInfo rabbitMQInfo, String queueName, boolean autoDelete) {
		return initializeClient(rabbitMQInfo.getBrokerServer(), rabbitMQInfo.getBrokerPort(), 
				rabbitMQInfo.getUserName(), rabbitMQInfo.getPassword(), rabbitMQInfo.getVirtualHost(), queueName, autoDelete);
	}
	
	public Channel getClientChannel() {
		return clientChannel;
	}
	
	public boolean initializeClient(String brokerHost, int brokerPort, 
			String userName, String password, String virtualHost, String queueName, boolean autoDelete) {
		
		logger.info("Initializing RabbitMQClient for QueueName = {}", queueName);
		ConnectionFactory factory = new ConnectionFactory();
		factory.setRequestedHeartbeat(5);
		
		/* Set all the parameters */
		factory.setHost(brokerHost);
		factory.setPort(brokerPort);
		factory.setUsername(userName);
		factory.setPassword(password);
		factory.setVirtualHost(virtualHost);
		
		factory.setAutomaticRecoveryEnabled(true);
		factory.setNetworkRecoveryInterval(10000);
		
		try {
			clientConnection = factory.newConnection(); // Each client will have connection to server 
			clientChannel = clientConnection.createChannel();
			
			//Bind this channel with the queue name provided by caller
			//clientChannel.exchangeDeclare(defaultExchangeName, "direct", true);
			clientChannel.queueDeclare(queueName, true, false, autoDelete, null);
			//clientChannel.queueBind(queueName, defaultExchangeName, defaultRoutingKey);
		
			this.queueName = queueName;
		} catch (Exception ex) {
			logger.error("Couldn't established the RabbitMQClient connection !!! ex={} ", ex.getMessage(), ex);
			return false;
		}
	
		return true;
	}
	
	/* Publish message to RabbitMQ on the connected queue 
	 * 
	 */
	public int publishMessage(String jsonMessage) {
		try {
			clientChannel.basicPublish(""/* No Exchange*/, queueName,  
					MessageProperties.PERSISTENT_TEXT_PLAIN, jsonMessage.getBytes());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("Couldn't publish message to RabbitMQClient connection !!! ex={} ", e.getMessage(), e);
		}
		return 0;
	}
	
	public int publishMessageToReactorQueue(String jsonMessage, String queueName) {
		try {
			clientChannel.basicPublish(""/* No Exchange*/, queueName,  
					MessageProperties.PERSISTENT_TEXT_PLAIN, jsonMessage.getBytes());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("Couldn't publish message to RabbitMQClient connection !!! ex={} ", e.getMessage(), e);
		}
		return 0;
	}
	
	public int startConsumer(Consumer consumer) {
		if (queueName == null || clientChannel == null) {
			logger.error("Please initiate the client initialization with queueName before consuming Messages !!!");
			return -1;
		}
		
		try {
			clientChannel.basicConsume(queueName, false, consumer);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("Couldn't Consume Message from RabbitMQClient connection !!! ex={} ", e.getMessage(), e);
		}
		return 0;
	}
}
