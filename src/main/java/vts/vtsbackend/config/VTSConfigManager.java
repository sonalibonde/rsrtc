package vts.vtsbackend.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/* This class is responsible for reading the configuration 
 * parameters from the application.properties and keep everything 
 * with him 
 */
public class VTSConfigManager {

	public final static Logger logger = LoggerFactory.getLogger(VTSConfigManager.class);
	Properties configProperties = null;
	public final String configFileName = "application.properties";
	
	/* Default configuration path */
	public VTSConfigManager() {
		configProperties = new Properties();
	}
	
	public boolean initializeConfigProperties() throws IOException {
		InputStream configStream = getClass().getClassLoader().getResourceAsStream(configFileName);
		if (configStream != null) {
			logger.info("Found the configuration File {}", configFileName);
			configProperties.load(configStream);
			return true;
		}
		else {
			logger.error("Couldn't find the application configuration File {}", configFileName);
			return false;
		}
	}
	
	public Properties getConfigProperties() {
		return configProperties;
	}
}
