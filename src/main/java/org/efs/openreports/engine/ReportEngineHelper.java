/*
 * Copyright (C) 2006 Erik Swenson - erik@oreports.com
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
 */

package org.efs.openreports.engine;

import org.apache.log4j.Logger;
import org.efs.openreports.objects.Report;
import org.efs.openreports.providers.DataSourceProvider;
import org.efs.openreports.providers.DirectoryProvider;
import org.efs.openreports.providers.PropertiesProvider;
import org.efs.openreports.providers.ProviderException;


/**
 * ReportEngine base class. Delegates report generation to the specific ReportEngine
 * implementation for each report type.  
 * 
 * @author Erik Swenson
 * 
 */

public class ReportEngineHelper
{
	protected static Logger log = Logger.getLogger(ReportEngineHelper.class.getName());
		
	public static ReportEngine getReportEngine(Report report, DataSourceProvider dataSourceProvider, DirectoryProvider directoryProvider, PropertiesProvider propertiesProvider) throws ProviderException
	{		
		ReportEngine reportEngine = null;		
		
		if (report.isChartReport())
		{
			reportEngine = new ChartReportEngine(dataSourceProvider, directoryProvider, propertiesProvider);			
		}
		else if (report.isJXLSReport())
		{
			reportEngine = new JXLSReportEngine(dataSourceProvider, directoryProvider, propertiesProvider);						
		}
		else if (report.isJFreeReport())
		{
			reportEngine = new JFreeReportEngine(dataSourceProvider, directoryProvider, propertiesProvider);					
		}
		else if (report.isBirtReport())
		{
			reportEngine = new BirtReportEngine(dataSourceProvider, directoryProvider, propertiesProvider);						
		}
		else if (report.isJasperReport())
		{
			reportEngine = new JasperReportEngine(dataSourceProvider, directoryProvider, propertiesProvider);						
		}
		else if (report.isQueryReport())
		{
			reportEngine = new QueryReportEngine(dataSourceProvider, directoryProvider, propertiesProvider);			
		}
        else if (report.isVelocityReport())
        {
            reportEngine = new VelocityReportEngine(dataSourceProvider, directoryProvider, propertiesProvider);
        }
		
		if (reportEngine == null)
		{
			String message = report.getName() + " is invalid. Please verify report definition.";
			
			log.error(message);
			throw new ProviderException(message);
		}		
		
		return reportEngine;
	}	
}
