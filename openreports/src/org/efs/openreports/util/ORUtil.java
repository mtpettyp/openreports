/*
 * Copyright (C) 2004 Erik Swenson - erik@oreports.com
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

package org.efs.openreports.util;

import java.util.*;

import org.efs.openreports.ORException;
import org.efs.openreports.ORStatics;
import org.efs.openreports.objects.ReportParameter;
import org.efs.openreports.objects.ReportParameterMap;
import org.efs.openreports.objects.ReportUser;
import org.efs.openreports.providers.ParameterProvider;

import net.sf.jasperreports.engine.design.JRDesignParameter;

public class ORUtil
{
	public static Map buildJRDesignParameters(Map parameters)
	{
		// convert parameters to JRDesignParameters so they can be parsed
		HashMap<String,JRDesignParameter> jrParameters = new HashMap<String,JRDesignParameter>();

		Iterator iterator = parameters.keySet().iterator();
		while (iterator.hasNext())
		{
			String key = (String) iterator.next();
			Object value = parameters.get(key);
			
			if (value != null)
			{
				JRDesignParameter jrParameter = new JRDesignParameter();
				jrParameter.setName(key);
				jrParameter.setValueClass(value.getClass());
				
				jrParameters.put(jrParameter.getName(), jrParameter);
			}			
		}

		return jrParameters;
	}
	
	/*
	 * Build map containing the parameter name and a test value in order to validate
	 * queries with parameters. 
	 */	
	public static Map buildQueryParameterMap(ReportUser reportUser, String queryString, ParameterProvider parameterProvider) throws ORException
	{
		HashMap<Object,Object> map = new HashMap<Object,Object>();
		
		String name = queryString.substring(queryString.indexOf("{") + 1, queryString.indexOf("}"));
		
		//handle standard report parameters
		if (name.equals(ORStatics.EXTERNAL_ID))
		{	
			map.put(ORStatics.EXTERNAL_ID, reportUser.getExternalId());			
			return map;
		}
		else if (name.equals(ORStatics.USER_ID))
		{			
			map.put(ORStatics.USER_ID, reportUser.getId());			
			return map;
		}
		else if (name.equals(ORStatics.USER_NAME))
		{			
			map.put(ORStatics.USER_NAME, reportUser.getName());			
			return map;
		}
		//
		
		ReportParameter queryParameter = parameterProvider.getReportParameter(name);						
		if (queryParameter == null)
		{			
			throw new ORException(LocalStrings.ERROR_PARAMETER_NOTFOUND);			
		}		
		
		ReportParameterMap rpMap = new ReportParameterMap();
		rpMap.setReportParameter(queryParameter);
		
		ArrayList<ReportParameterMap> queryParameters = new ArrayList<ReportParameterMap>();
		queryParameters.add(rpMap);		
		
		Map parameterMap = new HashMap();
		if (queryParameter.getData().toUpperCase().indexOf("$P") > -1)
		{
			parameterMap = buildQueryParameterMap(reportUser, queryParameter.getData(), parameterProvider);
		}
		
		parameterProvider.loadReportParameterValues(queryParameters, parameterMap);					
		
		String testValue = queryParameter.getValues()[0].getId().toString();
		if (queryParameter.isMultipleSelect())
		{
			testValue = "'" + testValue + "'";
		}
				
		map.put(queryParameter.getName(), testValue);		
		return map;
	}
	
	/**
     * Parse String and replace all parameter names with values. Parameter syntax is the same
     * as JasperReports: $P{ParameterName}
     * 
     */
	public static String parseStringWithParameters(String text, Map parameters) 
	{
		if (text == null) return null;

		while (text.indexOf("$P{") > 0) 
		{
			int beginIndex = text.indexOf("$P{");
			int endIndex = text.indexOf("}", beginIndex);

			String key = text.substring(beginIndex + 3, endIndex);
			String value = parameters.get(key).toString();

			text = text.substring(0, beginIndex) + value
					+ text.substring(endIndex + 1, text.length());
		}

		return text;
	}

}