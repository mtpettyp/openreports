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

package org.efs.openreports.services;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.efs.openreports.objects.ReportParameter;
import org.efs.openreports.objects.ReportParameterValue;
import org.efs.openreports.providers.ParameterProvider;
import org.efs.openreports.services.info.ParameterValueInfo;

/**
 * ParameterService implementation using standard OpenReports providers. 
 * 
 * @author Erik Swenson
 */

public class ParameterServiceImpl implements ParameterService
{
	private static Logger log = Logger.getLogger(ParameterServiceImpl.class.getName());
	
	private ParameterProvider parameterProvider;	
	
	public ParameterServiceImpl()
	{
		log.info("ParameterServiceImpl: Started");
	}		
	
	/*
	 * Returns an array of parameter values for a given Query, List or
	 * Boolean parameter.
	 * 		
	 */
	public ParameterValueInfo[] getParameterValues(String paramName)
	{
		if (paramName == null) return null;

		ParameterValueInfo[] paramValues = null;

		try
		{
			ReportParameter parameter = parameterProvider.getReportParameter(paramName);
			
			if (parameter != null
					&& (parameter.getType().equals(ReportParameter.QUERY_PARAM)
							|| parameter.getType().equals(ReportParameter.LIST_PARAM) || parameter
							.getType().equals(ReportParameter.BOOLEAN_PARAM)))
			{
				//TODO support multi-step parameters
				ReportParameterValue[] reportParamValues = parameterProvider
						.getParamValues(parameter, new HashMap());
				
				paramValues = new ParameterValueInfo[reportParamValues.length];

				for (int i = 0; i < reportParamValues.length; i++)
				{
					ParameterValueInfo paramInfo = new ParameterValueInfo();
					paramInfo.setId(reportParamValues[i].getId().toString());
					paramInfo.setDescription(reportParamValues[i].getDescription());
					
					paramValues[i] = paramInfo;
				}
			}
		}
		catch (Exception e)
		{
			log.warn(e);
		}

		return paramValues;
	}

	public void setParameterProvider(ParameterProvider parameterProvider)
	{
		this.parameterProvider = parameterProvider;
	}
}
