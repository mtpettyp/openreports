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

import com.thoughtworks.xstream.XStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.efs.openreports.ORStatics;
import org.efs.openreports.engine.ReportEngine;
import org.efs.openreports.engine.ReportEngineHelper;
import org.efs.openreports.engine.input.ReportEngineInput;
import org.efs.openreports.engine.output.QueryEngineOutput;
import org.efs.openreports.engine.output.ReportEngineOutput;
import org.efs.openreports.objects.Report;
import org.efs.openreports.objects.ReportLog;
import org.efs.openreports.objects.ReportSchedule;
import org.efs.openreports.objects.ReportUser;
import org.efs.openreports.providers.DataSourceProvider;
import org.efs.openreports.providers.DateProvider;
import org.efs.openreports.providers.DirectoryProvider;
import org.efs.openreports.providers.ParameterProvider;
import org.efs.openreports.providers.PropertiesProvider;
import org.efs.openreports.providers.ProviderException;
import org.efs.openreports.providers.ReportLogProvider;
import org.efs.openreports.providers.ReportProvider;
import org.efs.openreports.providers.SchedulerProvider;
import org.efs.openreports.providers.UserProvider;
import org.efs.openreports.services.info.ReportInfo;
import org.efs.openreports.services.input.ParameterInput;
import org.efs.openreports.services.input.ReportServiceInput;
import org.efs.openreports.services.output.ReportServiceOutput;
import org.efs.openreports.services.util.Converter;
import org.efs.openreports.util.LocalStrings;

/**
 * ReportService implementation using standard OpenReports providers. 
 * 
 * @author Erik Swenson
 */

public class ReportServiceImpl implements ReportService
{
	private static Logger log = Logger.getLogger(ReportServiceImpl.class.getName());

	private ReportProvider reportProvider;
	private ReportLogProvider reportLogProvider;	
	private SchedulerProvider schedulerProvider;
	private UserProvider userProvider;	
	private DirectoryProvider directoryProvider;
	private ParameterProvider parameterProvider;	
	private DataSourceProvider dataSourceProvider;
	private PropertiesProvider propertiesProvider;	
	private DateProvider dateProvider;
    private UserService userService;
    
    private XStream xStream;

	public ReportServiceImpl()
	{
        this.xStream = new XStream();
        
		log.info("ReportService: Started");
	}		
	
	/**
	 * Generate a Report and return a ReportEngineOutput. Contents of the
	 * ReportEngineOutput vary with the deliveryMethod and exportType of the
	 * ReportServiceInput.
	 * 
	 * Returns errors messages in the message field of the ReportServiceOutput.
	 * 
	 * Includes Report Logging functionality.
	 */
	public ReportServiceOutput generateReport(ReportServiceInput reportInput)
	{		
		ReportServiceOutput reportOutput = new ReportServiceOutput();	        
        
        boolean authenticated = userService.authenticate(reportInput.getUser());		
		if (!authenticated || reportInput.getReportName() == null)
		{
			reportOutput.setContentMessage("Invalid ReportInput - Valid User and Report Name required.");
			log.warn("generateReport: " + reportOutput.getContentMessage());
			
			return reportOutput;
		}
		
		ReportLog reportLog = null;
		
		try
		{
			Report report = reportProvider.getReport(reportInput.getReportName());
			if (report == null)
			{
				reportOutput.setContentMessage("Invalid ReportInput - Report not found: " + reportInput.getReportName());
				log.warn("generateReport: " + reportOutput.getContentMessage());
				
				return reportOutput;
			}
			
			ReportUser user = userProvider.getUser(reportInput.getUser().getUserName());
			if (user == null)
			{
				reportOutput.setContentMessage("Invalid ReportInput - User not found: " + reportInput.getUser().getUserName());
				log.warn("generateReport: " + reportOutput.getContentMessage());
				
				return reportOutput;
			}
			
			if (!user.isValidReport(report))
			{
				reportOutput.setContentMessage("Invalid ReportInput - "
						+ user.getName() + " not authorized to run: "
						+ reportInput.getReportName());
				
				log.warn("generateReport: " + reportOutput.getContentMessage());
				
				return reportOutput;	
			}
			
			reportLog = new ReportLog(user, report, new Date());
			reportLog = reportLogProvider.insertReportLog(reportLog);
			
			log.info("generateReport: " + user.getName() + " : " + report.getName() + " : " + reportInput.getDeliveryMethod());
			
			if (reportInput.getDeliveryMethod().equals(ReportService.DELIVERY_EMAIL)
					|| reportInput.getDeliveryMethod()
							.equals(ReportService.DELIVERY_FILE))
			{
				ReportSchedule schedule = new ReportSchedule();
				schedule.setReport(report);
				schedule.setUser(user);
				schedule.setReportParameters(buildParameterMap(reportInput, report));
				schedule.setExportType(reportInput.getExportType());
				schedule.setRecipients(user.getEmail());
				schedule.setScheduleName(report.getId() + "|" + new Date().getTime());
				schedule.setScheduleDescription(reportInput.getScheduleDescription());				
				schedule.setScheduleType(ReportSchedule.ONCE);
				
				// advanced scheduling
				if (reportInput.getStartDate() != null)
				{
					if (!user.isAdvancedScheduler())
					{
						throw new ProviderException("Not Authorized: Advanced Scheduling permission required");					
					}
					
					schedule.setScheduleType(reportInput.getScheduleType());
					schedule.setStartDate(dateProvider.parseDate(reportInput.getStartDate()));
					schedule.setStartHour(reportInput.getStartHour());
					schedule.setStartMinute(reportInput.getStartMinute());
					schedule.setStartAmPm(reportInput.getStartAmPm());
					schedule.setHours(reportInput.getHours());
					schedule.setCronExpression(reportInput.getCronExpression());					
				}
								
				schedulerProvider.scheduleReport(schedule);					
			}
			else
			{
				ReportEngine reportEngine = ReportEngineHelper.getReportEngine(report,
						dataSourceProvider, directoryProvider, propertiesProvider);
				
				ReportEngineInput engineInput = new ReportEngineInput(report, buildParameterMap(reportInput, report));
				engineInput.setExportType(reportInput.getExportType());								 
				
				ReportEngineOutput reportEngineOutput = reportEngine.generateReport(engineInput);
                
                reportOutput.setContent(reportEngineOutput.getContent());
                reportOutput.setContentType(reportEngineOutput.getContentType());
                
                //convert List of Dynabeans to XML so that it can be serialized
                if (reportEngineOutput instanceof QueryEngineOutput)
                {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();           
                                       
                    xStream.toXML(((QueryEngineOutput)reportEngineOutput).getResults(), out);                   
                    
                    reportOutput.setContent(out.toByteArray());
                    reportOutput.setContentType(ReportEngineOutput.CONTENT_TYPE_XML);
                    
                    out.close();                 
                }
			}
			
			reportOutput.setContentMessage(LocalStrings.SERVICE_REQUEST_COMPLETE);
			
			reportLog.setStatus(ReportLog.STATUS_SUCCESS);
			reportLog.setEndTime(new Date());
			reportLogProvider.updateReportLog(reportLog);			
		}
		catch (Exception e)
		{			
			log.error(e);					
			reportOutput.setContentMessage(e.getMessage());		
			
			if (reportLog != null && reportLog.getId() != null)
			{
				reportLog.setStatus(ReportLog.STATUS_FAILURE);
				reportLog.setMessage(e.getMessage());
				reportLog.setEndTime(new Date());
				
				try
				{
					reportLogProvider.updateReportLog(reportLog);
				}
				catch (Exception ex)
				{
					log.error("Unable to update ReportLog: " + ex.getMessage());
				}						
			}
		}
		
		log.info("generateReport: " + reportOutput.getContentMessage());				
		
		return reportOutput;
	}	  
    
	public ReportInfo getReportInfo(String reportName)
	{
		ReportInfo reportInfo = null;
		
		try
		{
			Report report = reportProvider.getReport(reportName);
			if (report != null)
			{
				reportInfo = Converter.convertToReportInfo(report);
			}
		}
		catch(ProviderException pe)
		{
			log.warn(pe);
		}
		
		return reportInfo;		
	}

	/**
	 * Builds report parameter map from incoming ReportServiceInput and adds
	 * standard report parameters.
	 */	
	private Map<String,Object> buildParameterMap(ReportServiceInput reportServiceInput, Report report) throws ProviderException
	{
		Map<Object,Object> inputParameters = new HashMap<Object,Object>();
		
		if (reportServiceInput.getParameters() != null)
		{
            ParameterInput[] parameters = reportServiceInput.getParameters();
            for (int i=0; i < parameters.length; i++)            
            {               
                inputParameters.put(parameters[i].getName(), parameters[i].getValues());
            }
        }
		
		Map<String,Object> parsedParameters = parameterProvider.getReportParametersMap(report.getParameters(), inputParameters);		
		parsedParameters.put(ORStatics.IMAGE_DIR, new File(directoryProvider.getReportImageDirectory()));		
		parsedParameters.put(ORStatics.REPORT_DIR, new File(directoryProvider.getReportDirectory()));		
		
		if (reportServiceInput.getDeliveryMethod().equals(ReportService.DELIVERY_FILE))
		{
			parsedParameters.put(ORStatics.GENERATE_FILE, Boolean.TRUE);
		}
		
		return parsedParameters;
	}

	public void setDirectoryProvider(DirectoryProvider directoryProvider)
	{
		this.directoryProvider = directoryProvider;
	}

	public void setParameterProvider(ParameterProvider parameterProvider)
	{
		this.parameterProvider = parameterProvider;
	}

	public void setReportLogProvider(ReportLogProvider reportLogProvider)
	{
		this.reportLogProvider = reportLogProvider;
	}

	public void setReportProvider(ReportProvider reportProvider)
	{
		this.reportProvider = reportProvider;
	}	

	public void setSchedulerProvider(SchedulerProvider schedulerProvider)
	{
		this.schedulerProvider = schedulerProvider;
	}

	public void setUserProvider(UserProvider userProvider)
	{
		this.userProvider = userProvider;
	}	

	public void setDataSourceProvider(DataSourceProvider dataSourceProvider)
	{
		this.dataSourceProvider = dataSourceProvider;
	}

	public void setPropertiesProvider(PropertiesProvider propertiesProvider)
	{
		this.propertiesProvider = propertiesProvider;
	}
	
	public void setDateProvider(DateProvider dateProvider)
	{
		this.dateProvider = dateProvider;
	}   
    
    public void setUserService(UserService userService) 
    {
        this.userService = userService;
    }	
}
