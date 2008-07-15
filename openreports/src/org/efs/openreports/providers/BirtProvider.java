/*  
 * Copyright (C) 2006 by Open Source Software Solutions, LLC and Contributors
 *  
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option)
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 *   
 * Original Author	:	Roberto Nibali 	- rnibali@pyx.ch 
 * Contributor(s)	:	Erik Swenson 	- erik@oreports.com
 * 
 */

package org.efs.openreports.providers;

import java.util.logging.Level;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.IPlatformContext;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.core.framework.PlatformConfig;
import org.eclipse.birt.core.framework.PlatformFileContext;
import org.eclipse.birt.core.framework.PlatformServletContext;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.HTMLActionHandler;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.HTMLServerImageHandler;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;
import org.springframework.beans.factory.DisposableBean;

/**
 * Provides the init and startup of the birt engine and config. 
 * 
 * @author Roberto Nibali
 * @author Erik Swenson
 * 
 */
public class BirtProvider implements DisposableBean
{
	protected static Logger log = Logger.getLogger(BirtProvider.class);
	
	private static IReportEngine birtEngine = null;		
	
	public BirtProvider()
	{
		//
	}
	
	public BirtProvider(DirectoryProvider directoryProvider)
	{
	    PlatformConfig platformConfig = new PlatformConfig();
        platformConfig.setBIRTHome(directoryProvider.getReportDirectory() + "platform");
        
        IPlatformContext context = new PlatformFileContext(platformConfig);        
		startBirtEngine(context);
	}
	
	public BirtProvider(ServletContext servletContext)
    {
	    log.info(servletContext.toString());
	    IPlatformContext context = new PlatformServletContext(servletContext);	   
        startBirtEngine(context);
    }
		
	public static synchronized IReportEngine getBirtEngine(String birtHome)
	{
		if (birtEngine == null)
		{			
		    PlatformConfig platformConfig = new PlatformConfig();
	        platformConfig.setBIRTHome(birtHome);
	        
	        IPlatformContext context = new PlatformFileContext(platformConfig);	        
			startBirtEngine(context);
		}			
		
		return birtEngine;
	}
	
	@SuppressWarnings("unchecked")
	public static synchronized void startBirtEngine(IPlatformContext context)
	{				
		log.info("Starting BIRT Engine and OSGI Platform using: " + context.getClass().getName());		
				
		HTMLServerImageHandler imageHandler = new HTMLServerImageHandler();
		
		HTMLRenderOption emitterConfig = new HTMLRenderOption();
		emitterConfig.setActionHandler(new HTMLActionHandler());		
		emitterConfig.setImageHandler(imageHandler);

		EngineConfig config = new EngineConfig();
		config.setEngineHome("");
		config.setPlatformContext(context);								
		config.setLogConfig(null, Level.ALL);			
		config.getEmitterConfigs().put("html", emitterConfig);

		try
		{
			Platform.startup(config);
		}
		catch (BirtException e)
		{
			log.error("BirtException", e);
		}

		IReportEngineFactory factory = (IReportEngineFactory) Platform
				.createFactoryObject(IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY);
		
		birtEngine = factory.createReportEngine(config);
		
		log.info("BIRT Engine Started");
			
		birtEngine.changeLogLevel(Level.SEVERE);	
	}
	
	public void destroy()
	{
		if (birtEngine == null)	return;				
		
		birtEngine.destroy();				
		Platform.shutdown();
		birtEngine = null;
		
		log.info("BIRT Engine and OSGI Platform Shutdown");
	}	
}
