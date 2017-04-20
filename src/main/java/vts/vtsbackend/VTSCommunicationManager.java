package vts.vtsbackend;

import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.util.Properties;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vts.vtsbackend.config.VTSConfigManager;
import vts.vtsbackend.core.ConnectionAcceptor;
import vts.vtsbackend.core.ConnectionAcceptorThread;

/**
 * VTS Communication Manager 	
 *
 */
public class VTSCommunicationManager  
{
	public final static Logger logger = LoggerFactory.getLogger(VTSCommunicationManager.class);
	
	private VTSConfigManager vtsConfigManager;
	private Properties configProperties;
	private VTSRabbitMQInfo vtsRabbitMQInfo;
	public static int serverPortNumber;
	
	private ConnectionAcceptor connectionAcceptor;
	//private ServerSocketChannel serverChannel;
	private ServerSocketChannel serverChannel;
	private Thread acceptorThread;
	
	public VTSCommunicationManager(int portNumber) {
		//Default constructor 
		serverPortNumber = portNumber;
	}
	
	public int initializeCommunicationServer() {

		// Do configuration related things here
		if (initAndVerifyConfiguration() < 0) {
			//Printing has already done for this function so just returing from here...
			return -1;
		}
		
		// Do Socket initialization part which is important
        int initSocket = initializeConnectionAcceptor();
        if (initSocket == -1) {
        	logger.error("Server Socket Initialization Has been Failed !!!");
        	return -1;
        }
		
		return 0;
	}
	
	public Thread getAcceptorThread() { 
		return acceptorThread;
	}
	
	private int initializeConnectionAcceptor() {
		try 
		{			
			serverChannel = ServerSocketChannel.open();
			//Make server non-blocking 
			serverChannel.configureBlocking(false);
			//serverChannel.setOption(StandardSocketOptions.TCP_NODELAY, true);
			//serverChannel.setOption(StandardSocketOptions.SO_KEEPALIVE, true);
			serverChannel.socket().bind(new InetSocketAddress(serverPortNumber));
			
			
			//Initialize the acceptor which holds client reactor objects 
			connectionAcceptor = new ConnectionAcceptor(serverChannel);
			connectionAcceptor.initializeClientChannelSelector(vtsRabbitMQInfo);

			//So by now there are reactor objects and each has started the thread to react on 
			//added connections/socketChannel to them. 
			acceptorThread = new Thread(new ConnectionAcceptorThread(connectionAcceptor));
			acceptorThread.start();
			
		} catch (Exception ex) {
			logger.error("VTSCommunicationManager - startVTSServer got exception ex={} ", ex.getMessage(), ex);
			return -1;
		}
		return 0;
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

        /* RabbitMQ parameters are available; ; lets construct the client and send some message */
        /** TODO move this from here...
        VTSRabbitMQClient rabbitMQClient = new VTSRabbitMQClient();
        boolean initMessageClient = rabbitMQClient.initializeClient(vtsRabbitMQInfo.getBrokerServer(), 
        		vtsRabbitMQInfo.getBrokerPort(), 
        		vtsRabbitMQInfo.getUserName(), 
        		vtsRabbitMQInfo.getPassword(), 
        		vtsRabbitMQInfo.getVirtualHost(), "InComingMessage_Queue");
        
        
        if (!initMessageClient) {
        	logger.error("Couldn't initialize the RabbitMQClient here..");
        	return -1;
        }
        
        rabbitMQClient.publishMessage("Takshak Here and thisis the 1st message");
        **/
        
        return 0;
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
        logger.info("World's Scalable VTS Communication Manager ");
        
        if (args.length != 1) {
        	logger.error("Usage: VTSConnectionServer.class <portNumber>");
        	System.exit(-1);
        }
        int portNumber = Integer.parseInt(args[0]);
        
        VTSCommunicationManager vtsCommunicationManager = new VTSCommunicationManager(portNumber);
        int initServer = vtsCommunicationManager.initializeCommunicationServer();
        if (initServer == -1) { 
        	logger.error("Communication Server Couldn't be initialized !");
        	System.exit(-1);
        }
        logger.info("!!! Communication Server has been Initialized and Now Starting !!!");
        
        // Wait for acceptor thread to die to SHUTDOWN the server
        try {
            vtsCommunicationManager.getAcceptorThread().join();
            System.out.println("All child threads have completed.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            logger.info("!!! Communication Server ENDED !!!");
        }
    }
}
