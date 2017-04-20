package vts.vtsbackend.core;

import java.io.IOException;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vts.vtsbackend.VTSUtilityHandler;
import vts.vtsbackend.rabbitmq.VTSRabbitMQClient;

// This class is responsible of following 
// -- Take the ClientReactor object and watch out of all the available 
// connections to see if something is there or not and it has its own thread pool to schedule the task
public class ClientReactorThread implements Runnable {

	public final static Logger logger = LoggerFactory.getLogger(ClientReactorThread.class);
	
	private ThreadPoolExecutor nioDataReaderPool;
	private int MAX_WORKER_THREADS = 5; /* 50 worker thread */
	private int MIN_WORKER_THREADS = 5; /* Minimum it would start in first place */
	private int MAX_WORKER_IDLE_TIME = 60; //60 seconds
	private int currentThreadIndex = 0;
	
	//As this object has threadPool who is responsible to read the data 
	//This guy should hold the rabbitMQClient connections....
	private VTSRabbitMQClient[] publisherQueue = new VTSRabbitMQClient[MAX_WORKER_THREADS];
	
	private final ClientConnectionReactor clientReactor;
	public ClientReactorThread(ClientConnectionReactor reactor) {
		this.clientReactor = reactor;
		
		// Initialize the workerThreadPool for publishing messages
		BlockingQueue<Runnable> unBoundedThreadBackLogQueue = new LinkedBlockingQueue<Runnable>();
		nioDataReaderPool = new ThreadPoolExecutor(MIN_WORKER_THREADS, MAX_WORKER_THREADS, MAX_WORKER_IDLE_TIME,
				TimeUnit.SECONDS, unBoundedThreadBackLogQueue);
		
		//Register for incoming messages for this reactor 
		VTSRabbitMQClient mqClient = new VTSRabbitMQClient();
		mqClient.initializeClient(clientReactor.getRabbitMQInfo(), clientReactor.getReactorQueueName(), true /*autoDelete*/);
		mqClient.startConsumer(new ClientOutgoingThread(mqClient.getClientChannel(), clientReactor));
		
		// Initialize the pool of rabbitMQ Publisher client which will be used by the thread pool
		for (int index = 0; index < MAX_WORKER_THREADS; index++) {
			publisherQueue[index] = new VTSRabbitMQClient();
			publisherQueue[index].initializeClient(clientReactor.getRabbitMQInfo(), clientReactor.getDataPublisherQueueName(), false /*autoDelete*/);
		}
	}
	
	public void cleanupClientChannel(SelectionKey foundKey) {
		
		try {
			SocketChannel clientChannel = (SocketChannel)foundKey.attachment();
			String channelKey = VTSUtilityHandler.getClientAddressKey(clientChannel);
			this.clientReactor.removeClosedClientChannel(channelKey);
			clientChannel.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			logger.info("SELECTOR[{}] CLEANING UP KEY EXCEPTION ....ex={}", clientReactor.getReactorID(), e);
		}
	}
	
	public void run() {
		try 
		{
			
			
			while (!Thread.interrupted()) {
				
				//Get everytime the latest object from clientReactor 
				Selector clientConnectionSelector = clientReactor.getClientConnectionSelector();
				int keyCount = clientConnectionSelector.select();
				
				logger.info("SELECTOR[{}] total Keys= {}", clientConnectionSelector.keys().size());
				logger.info("SELECTOR[{}] found KEYCOUNT={}", clientReactor.getReactorID(), keyCount);
				
				/*
				if (keyCount <= 0) {
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					continue;
				}*/
				
				Set<SelectionKey> selectedKeys = clientConnectionSelector.selectedKeys();
				
				//Iterate through all the set of keys which has something to process now
				Iterator<SelectionKey> keyIterator = selectedKeys.iterator();
				while (keyIterator.hasNext()) {
					
					SelectionKey foundClientChannelKey = keyIterator.next();
					keyIterator.remove();
					
					try {
					
						if (foundClientChannelKey.isValid() && foundClientChannelKey.isReadable()) {
							
							foundClientChannelKey.interestOps(0); //Ask selector not to watch this 
							
							//TODO: Schedule the FOUND Channel for Data IO and sending it to RabbitMQ
							//Do IO processing ? 
							//Shall we give him two things ? clientChannel and rabbitMQClient ? 
							nioDataReaderPool.execute(new VTSDataIOProcessor(foundClientChannelKey, 
									clientReactor, publisherQueue[currentThreadIndex]));
							
							if (currentThreadIndex >= MAX_WORKER_THREADS-1) {
								currentThreadIndex = 0;
							} else {
								currentThreadIndex++;
							
							}
							SocketChannel clientChannel = (SocketChannel)foundClientChannelKey.attachment();
							logger.info("SELECTOR[{}] Found Channel Key to read Data from {}", clientReactor.getReactorID(), 
									VTSUtilityHandler.getClientAddressKey(clientChannel));
							
						}
						else if (foundClientChannelKey.isValid() && foundClientChannelKey.isWritable()) {
							logger.info("SELECTOR[{}] Something to WRITEEEEEEEEEEEE here has DONE....", clientReactor.getReactorID());
							foundClientChannelKey.interestOps(SelectionKey.OP_READ);
							clientConnectionSelector.wakeup();
						} else {
							// If the key is not valid and not readable or writable then it means 
							// the client has been cancelled 
							logger.info("SELECTOR[{}] CANCELLED KEY ??? ...", clientReactor.getReactorID());
							cleanupClientChannel(foundClientChannelKey);
						}
					} catch (CancelledKeyException exp) {
						logger.error("NEW SELECTOR[{}] CANCELLED Key Exception ex:{}", clientReactor.getReactorID(), exp);
						cleanupClientChannel(foundClientChannelKey);
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
