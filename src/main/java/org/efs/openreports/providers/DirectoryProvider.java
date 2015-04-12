/*
 * Copyright (C) 2003 Erik Swenson - erik@oreports.com
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 *  
 */

package org.efs.openreports.providers;

import org.apache.struts2.ServletActionContext;

import org.apache.log4j.Logger;
import org.efs.openreports.objects.ORProperty;

public class DirectoryProvider 
{
	protected static Logger log = Logger.getLogger(DirectoryProvider.class.getName());

	private String reportDirectory;
	private String reportImageDirectory;
	private String reportGenerationDirectory;
	private String tempDirectory;
	private String separator = System.getProperty("file.separator");	

	private PropertiesProvider propertiesProvider;	
	
	public DirectoryProvider(PropertiesProvider propertiesProvider) throws ProviderException
	{
		this.propertiesProvider = propertiesProvider;		
		init();
	}	
	
	protected void init() throws ProviderException
	{
		log.info("Loading BaseDirectory from OR_PROPERTIES table.");

		String baseDirectory = null;

		ORProperty property = propertiesProvider.getProperty(ORProperty.BASE_DIRECTORY);		
		if (property != null) baseDirectory = property.getValue(); 
		
		if (baseDirectory == null || baseDirectory.trim().length() < 1)
		{
			log.info("BaseDirectory not set in OR_PROPERTIES table. Trying to get path from ServletContext.");

			try
			{
				baseDirectory = ServletActionContext.getServletContext().getRealPath("");
				baseDirectory = baseDirectory + separator + "reports";
			}
			catch (NullPointerException npe)
			{
				log.info("ServletActionContext not available.");
				baseDirectory = ".";
			}
		}
				
		reportDirectory = baseDirectory;
		if (!reportDirectory.endsWith(separator)) reportDirectory +=separator;		
		log.info("Report Directory: " + reportDirectory);
			
		reportImageDirectory = reportDirectory + "images" + separator;		
		log.info("Report Image Directory: " + reportImageDirectory);
		
		//set temp directory path for report virtualization and image generation
		property = propertiesProvider.getProperty(ORProperty.TEMP_DIRECTORY);		
		if (property != null && property.getValue() != null && property.getValue().trim().length() > 0)
		{
			tempDirectory = property.getValue(); 
			if (!tempDirectory.endsWith(separator)) tempDirectory +=separator;			
			log.info("TempDirectory: " + tempDirectory);
		}
		
		//set report generation directory path for storing generated reports
		property = propertiesProvider.getProperty(ORProperty.REPORT_GENERATION_DIRECTORY);		
		if (property != null && property.getValue() != null && property.getValue().trim().length() > 0)
		{
			reportGenerationDirectory = property.getValue();
			if (!reportGenerationDirectory.endsWith(separator)) reportGenerationDirectory +=separator;			
			log.info("ReportGenerationDirectory: " + reportGenerationDirectory);
		}		
		
		log.info("Created");
	}

	public String getReportDirectory()
	{
		return reportDirectory;
	}

	public void setReportDirectory(String reportDirectory)
	{
		this.reportDirectory = reportDirectory;
		if (!reportDirectory.endsWith(separator)) reportDirectory +=separator;
		
		reportImageDirectory = reportDirectory + "images" + separator;	
		
		log.info("Report Directory Changed To: " + reportDirectory); 
	}
	
	public String getReportImageDirectory()
	{
		return reportImageDirectory;
	}
	
	public String getTempDirectory()
	{
		return tempDirectory;
	}
	
	public void setTempDirectory(String tempDirectory)
	{
		this.tempDirectory = tempDirectory;
		if (!tempDirectory.endsWith(separator)) tempDirectory +=separator;		
		
		log.info("TempDirectory Changed To: " + tempDirectory); 
	}

	public String getReportGenerationDirectory()
	{
		return reportGenerationDirectory;
	}

	public void setReportGenerationDirectory(String reportGenerationDirectory)
	{
		this.reportGenerationDirectory = reportGenerationDirectory;
		if (!reportGenerationDirectory.endsWith(separator)) reportGenerationDirectory +=separator;
		
		log.info("ReportGenerationDirectory Changed To: " + reportGenerationDirectory); 
	}

	public void setPropertiesProvider(PropertiesProvider propertiesProvider)
	{
		this.propertiesProvider = propertiesProvider;
	}

}