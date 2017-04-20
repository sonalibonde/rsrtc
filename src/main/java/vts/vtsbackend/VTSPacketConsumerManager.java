package vts.vtsbackend;

import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vts.vtsbackend.config.VTSConfigManager;
import vts.vtsbackend.protocol.VTSProtocolDetector;
import vts.vtsbackend.rabbitmq.VTSRabbitMQClient;


public class VTSPacketConsumerManager {
	
	public final static Logger logger = LoggerFactory.getLogger(VTSPacketConsumerManager.class);
	private final int numberOfThreads;
	Thread consumerThreads[];
	
	private VTSConfigManager vtsConfigManager;
	private Properties configProperties;
	private VTSRabbitMQInfo vtsRabbitMQInfo;
	
	public VTSPacketConsumerManager(int threadNumber) {
		numberOfThreads = threadNumber;
	}
	
	public void startConsumerThreads() {
		//Create these many thread instances 
		
		consumerThreads = new Thread[numberOfThreads];
		
		for (int threadIndex = 0; threadIndex < numberOfThreads; threadIndex++) {
			
			VTSProtocolDetector protocolDetectorThread = new VTSProtocolDetector(vtsRabbitMQInfo);
			consumerThreads[threadIndex] = new Thread(protocolDetectorThread);
			consumerThreads[threadIndex].start();
			
		}
		
		//Now in this main thread; wait for all the started threads 
		for (int joinThreadIndex = 0; joinThreadIndex < numberOfThreads; joinThreadIndex++) {
			try {
				consumerThreads[joinThreadIndex].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				logger.error("Exception during JOIN joinThreadIndex={}: ex={}", joinThreadIndex, e);
			}
		}
		
		logger.info("All Consumer Threads are DONE !!!");
	}
	
	public int initAndVerifyConfiguration() { 
		
		// Initialize the configuration from the properties file if everything is good
		// then move further
		vtsConfigManager = new VTSConfigManager();
		try {
        	boolean initConfigManager = vtsConfigManager.initializeConfigProperties();
        	
            if (!initConfigManager) {
            	logger.error("Couldn't initialize the Configuration Manager; application.properties couldn't be loaded ! Fix it ");
            	return -1;
            }
        } catch(Exception exp) {
        	logger.error("Exception while initializing configManager ex={}", exp.getMessage(), exp);
        	return -1;
        }
		
        logger.info("Configuration file Found !!!");
        configProperties = vtsConfigManager.getConfigProperties();

        //Validate the read configuration 
        int validateRabbitMQ = validateAndInitRabbitMQBrokerConfiguration();
        if (validateRabbitMQ == -1) {
        	logger.debug("Validating RabbitMQ Failed for its configuration parameters");
        	return -1;
        }
        
        return 0; //Success
	}
	
	private int validateAndInitRabbitMQBrokerConfiguration() {
		
		if (configProperties == null) {
			logger.error("Config properties is NOT load loaded !!!");
			return -1;
		}
		
		String brokerServer = configProperties.getProperty("brokerServer", null);
		if (brokerServer == null) {
			logger.error("Please specify broker server name or ip address ");
			return -1;
		}
		logger.info("brokerServer={}", brokerServer);
		
		String brokerPortStr = configProperties.getProperty("brokerPort", null);
		if (brokerPortStr == null) {
			logger.error("Please specify the valid BrokerServer port !");
			return -1;
		}
		logger.info("brokerPort={}", Integer.parseInt(brokerPortStr));
		
		String userName = configProperties.getProperty("userName", null);
		if (userName == null) {
			logger.error("Please specify the user name ");
			return -1;
		}
		
		String password = configProperties.getProperty("password", null);
		if (password == null) {
			logger.error("Please specify the password");
			return -1;
		}
		
		String virtualHost = configProperties.getProperty("virtualHost", null);
		if (virtualHost == null) {
			logger.error("Please specify the virtual Host ");
			return -1;
		}
		
		String queueName = configProperties.getProperty("queueName", null);
		if (queueName == null) {
			logger.error("Please specify the Queue name which we need to subscribe !!!");
			return -1;
		}
		
		vtsRabbitMQInfo = new VTSRabbitMQInfo(brokerServer, Integer.parseInt(brokerPortStr), 
				userName, password, virtualHost, queueName);
		
		return 0;
	}
	
	public static void main( String[] args )
    {
		
		logger.info("VTS Message Consumer Manager ");
        
        if (args.length != 1) {
        	logger.error("Usage: VTSPacketConsumerManager.class <NumberOfThreads>");
        	System.exit(-1);
        }
        int numberOfThreads = Integer.parseInt(args[0]);
        
		VTSPacketConsumerManager consumerManager = new VTSPacketConsumerManager(numberOfThreads);
		if (consumerManager.initAndVerifyConfiguration() < 0) {
			logger.error("RabbitMQ setup verification failed !!!");
			return;
		}
		
		consumerManager.startConsumerThreads();
		
    }
}
