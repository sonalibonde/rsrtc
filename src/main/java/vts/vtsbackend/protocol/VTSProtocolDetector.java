package vts.vtsbackend.protocol;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vts.vtsbackend.VTSDBConnectionHandler;
import vts.vtsbackend.VTSPacketConsumerManager;
import vts.vtsbackend.VTSRabbitMQInfo;
import vts.vtsbackend.rabbitmq.VTSRabbitMQClient;

// This class is responsible in reading the RabbitMQ messages 
// if he can detect the protocol consume it or leave it for future protocol detectors 
public class VTSProtocolDetector implements Runnable {

	public final static Logger logger = LoggerFactory.getLogger(VTSProtocolDetector.class);
	private	VTSRabbitMQClient mqClient = null;
	private final VTSRabbitMQInfo rabbitMQInfo;
	
	public VTSProtocolDetector(VTSRabbitMQInfo rabbitMQInfo) {
		this.rabbitMQInfo = rabbitMQInfo;
	}
	
	// May be just connect to the rabbitMQ server and be ready for consumption ? 
	public void run() {
		
		mqClient = new VTSRabbitMQClient();
		boolean initRabbitMQClient = mqClient.initializeClient(rabbitMQInfo, rabbitMQInfo.getConsumerQueueName(), false /*autoDelete*/);
		if (!initRabbitMQClient) {
			logger.error("ProtocolDetector Thread Could not create RabbitMQ Connection -- Check connection parameters !!!");
			//Kill this thread
			Thread.currentThread().stop();
			return;
		}
		
		VTSDBConnectionHandler dbHandler = new VTSDBConnectionHandler();
		if (!dbHandler.initializeDBHandler()) {
			logger.error("Could not establish DB Connection -- Please check the Connection Parameters !!!");
			//kill this thread 
			Thread.currentThread().stop();
			return;
		}
		
		//Register only when all the things are good to go before start consuming the packet 
		mqClient.startConsumer(new VTSPacketConsumer(mqClient, dbHandler));
	}

}
