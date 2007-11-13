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

import com.opensymphony.xwork2.ActionContext;
import java.util.*;

import org.apache.commons.io.FilenameUtils;
import org.efs.openreports.ORException;
import org.efs.openreports.ORStatics;
import org.efs.openreports.engine.output.ReportEngineOutput;
import org.efs.openreports.objects.ReportParameter;
import org.efs.openreports.objects.ReportParameterMap;
import org.efs.openreports.objects.ReportUser;
import org.efs.openreports.providers.ParameterProvider;

import net.sf.jasperreports.engine.design.JRDesignParameter;

public class ORUtil
{
	public static Map<String,JRDesignParameter> buildJRDesignParameters(Map<String,Object> parameters)
	{
		// convert parameters to JRDesignParameters so they can be parsed
		HashMap<String,JRDesignParameter> jrParameters = new HashMap<String,JRDesignParameter>();

		Iterator<String> iterator = parameters.keySet().iterator();
		while (iterator.hasNext())
		{
			String key = iterator.next();
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
	public static Map<String,Object> buildQueryParameterMap(ReportUser reportUser, String queryString, ParameterProvider parameterProvider) throws ORException
	{
		HashMap<String,Object> map = new HashMap<String,Object>();
		
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
		
		Map<String,Object> parameterMap = new HashMap<String,Object>();
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
	public static String parseStringWithParameters(String text, Map<String,Object> parameters) 
	{
		if (text == null) return null;

		while (text.indexOf("$P{") > 0) 
		{
			int beginIndex = text.indexOf("$P{");
			int endIndex = text.indexOf("}", beginIndex);

			String key = text.substring(beginIndex + 3, endIndex);
			String value = key;
			
			if (parameters.get(key) != null)
			value = parameters.get(key).toString();

			text = text.substring(0, beginIndex) + value
					+ text.substring(endIndex + 1, text.length());
		}

		return text;
	}
    
    /*
     * All JPivot objects must be removed from session each time a new JPivot report is run.
     */
    public static void resetOlapContext(ActionContext context)
    {
        Iterator<?> i = context.getSession().keySet().iterator();
        while(i.hasNext())
        {
            String key = (String) i.next();
            if (key.indexOf("tonbeller") > -1 || key.indexOf("01") > -1)
            {                    
                context.getSession().remove(key);               
            }
        }
    }
    
    public static Locale getLocale(String locale)
    {
    	if (locale == null) return Locale.getDefault();
    	
    	StringTokenizer localeTokenizer = new StringTokenizer(locale,"_");
    	if (localeTokenizer.countTokens() == 1)
    	{
    		return new Locale(locale);
    	}
    	else
    	{
    		return new Locale(localeTokenizer.nextToken(), localeTokenizer.nextToken());
    	}
    }
    
    public static String getContentType(String fileName)
    {
    	String extension = FilenameUtils.getExtension(fileName);
    	
    	if (".pdf".equalsIgnoreCase(extension))
    	{
    		return ReportEngineOutput.CONTENT_TYPE_PDF;
    	}
    	else if (".xls".equalsIgnoreCase(extension))
    	{
    		return ReportEngineOutput.CONTENT_TYPE_XLS;
    	}
    	else if (".html".equalsIgnoreCase(extension))
    	{
    		return ReportEngineOutput.CONTENT_TYPE_HTML;
    	}
    	else if (".csv".equalsIgnoreCase(extension))
    	{
    		return ReportEngineOutput.CONTENT_TYPE_CSV;
    	}
    	else if (".rtf".equalsIgnoreCase(extension))
    	{
    		return ReportEngineOutput.CONTENT_TYPE_RTF;
    	}
    	else if (".txt".equalsIgnoreCase(extension))
    	{
    		return ReportEngineOutput.CONTENT_TYPE_TEXT;
    	}
    	else if (".xml".equalsIgnoreCase(extension))
    	{
    		return ReportEngineOutput.CONTENT_TYPE_XML;
    	}
    	else if (".jpg".equalsIgnoreCase(extension))
    	{
    		return ReportEngineOutput.CONTENT_TYPE_JPEG;
    	}
    	else if (".png".equalsIgnoreCase(extension))
    	{
    		return ReportEngineOutput.CONTENT_TYPE_PNG;
    	}
    	
    	//default content type
    	return "application/octet-stream";
    }


}