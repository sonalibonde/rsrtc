package vts.vtsbackend.core;

import java.io.IOException;
import java.net.StandardSocketOptions;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// This thread class is responsible on watching out any incoming connections for accept 
// Pass that incoming connection to the Reactor -- Round Robine Order Now :) 
public class ConnectionAcceptorThread implements Runnable {

	private final ConnectionAcceptor connectionAcceptor;
	private int currentChannelSelector = 0;
	public final static Logger logger = LoggerFactory.getLogger(ConnectionAcceptorThread.class);
	
	public ConnectionAcceptorThread(ConnectionAcceptor acceptor) {
		connectionAcceptor = acceptor;
	}
	
	public void run() {
		
		// TODO Auto-generated method stub
		ServerSocketChannel serverChannel = connectionAcceptor.getServerSocketChannel();
		Selector mainSelector = connectionAcceptor.getMainSelector();
		
		while (!Thread.interrupted()) {
			logger.info("Inside MainAcceptor Thread And Waiting for InComing Connections !!!");
			// Now keep accepting the incoming requests 
			try {
				//This is a blocking call :) 		
				mainSelector.select();
				
				// We registered only one "key" so we shall be getting back only one key 
				Set<SelectionKey> selectedConnections = mainSelector.selectedKeys();
				Iterator<SelectionKey> selectionKey = selectedConnections.iterator();
				
				while (selectionKey.hasNext()) {
					SelectionKey currentSelectionKey = selectionKey.next();
					if (currentSelectionKey.isAcceptable()) {
								
						SocketChannel newClientChannel = serverChannel.accept();
						if (newClientChannel != null) {
							newClientChannel.setOption(StandardSocketOptions.SO_KEEPALIVE, true);
							newClientChannel.setOption(StandardSocketOptions.TCP_NODELAY, true);
							// Extract the channel and add to the Reactor's object so that
							// the thread who is watching that Reactor can get it to watch :) 
							dispatchClientChannelToReactor(newClientChannel);
						}
					}		
					selectionKey.remove();
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void dispatchClientChannelToReactor(SocketChannel clientChannel) {
		// TODO: Takshak - do you really think to have thread pool for this 
		// functionalities ? I don't think so :) 
		
		logger.info("Selecting clientConnectionReactor=[{}]", currentChannelSelector);
		ClientConnectionReactor clientReactor = connectionAcceptor.getClientReactor(currentChannelSelector);
		if (clientReactor != null) {
			clientReactor.addAcceptedClientChannel(clientChannel);
		}
		
		if (++currentChannelSelector == ConnectionAcceptor.MAX_SELECTORS) 
			currentChannelSelector = 0;
		
		Thread.yield();
	}	
}
