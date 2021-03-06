package com.celera.core.configure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface IOverrideConfig
{
	static final Logger logger = LoggerFactory.getLogger(IOverrideConfig.class);

	public default void overrideCxfSpiProvider()
	{
		String CXF_SPI_PROVIDER = ResourceManager.getProperties(IResourceProperties.PROP_CXF_SPI_PROVIDER);
		if (CXF_SPI_PROVIDER != null)
		{
			String oldSpi = System.getProperty("javax.xml.ws.spi.Provider");
			System.setProperty("javax.xml.ws.spi.Provider", CXF_SPI_PROVIDER);
			logger.info("override javax.xml.ws.spi.Provider old[{}] new[{}]", oldSpi, CXF_SPI_PROVIDER);
		}
	}
	
	
	/**
	 * force decide override cfg
	 */
	public void overrideCfg();
}
