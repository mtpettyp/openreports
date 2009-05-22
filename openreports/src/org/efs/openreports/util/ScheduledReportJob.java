/*
 * Copyright (C) 2003 Erik Swenson - erik@oreports.com
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

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRVirtualizer;
import net.sf.jasperreports.engine.fill.JRFileVirtualizer;
import org.apache.log4j.Logger;
import org.efs.openreports.ORStatics;
import org.efs.openreports.ReportConstants.ExportType;
import org.efs.openreports.delivery.DeliveryException;
import org.efs.openreports.delivery.DeliveryMethod;
import org.efs.openreports.engine.ChartReportEngine;
import org.efs.openreports.engine.ReportEngine;
import org.efs.openreports.engine.ReportEngineHelper;
import org.efs.openreports.engine.input.ReportEngineInput;
import org.efs.openreports.engine.output.ChartEngineOutput;
import org.efs.openreports.engine.output.ReportEngineOutput;
import org.efs.openreports.objects.Report;
import org.efs.openreports.objects.ReportDeliveryLog;
import org.efs.openreports.objects.ReportLog;
import org.efs.openreports.objects.ReportSchedule;
import org.efs.openreports.objects.ReportUser;
import org.efs.openreports.objects.ReportUserAlert;
import org.efs.openreports.providers.AlertProvider;
import org.efs.openreports.providers.DataSourceProvider;
import org.efs.openreports.providers.DirectoryProvider;
import org.efs.openreports.providers.PropertiesProvider;
import org.efs.openreports.providers.ReportLogProvider;
import org.efs.openreports.scheduler.ScheduledReportCallback;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.springframework.context.ApplicationContext;

public class ScheduledReportJob	implements Job
{
	protected static Logger log = Logger.getLogger(ScheduledReportJob.class.getName());
	
	private ReportLogProvider reportLogProvider;	
	private DirectoryProvider directoryProvider;	
	private AlertProvider alertProvider;
	private DataSourceProvider dataSourceProvider;
	private PropertiesProvider propertiesProvider;
	
	private List<ScheduledReportCallback> callbacks;

	public ScheduledReportJob()
	{
				
	}	

	public void execute(JobExecutionContext context)
		throws JobExecutionException
	{
		log.debug("Scheduled Report Executing....");
        
        ApplicationContext appContext = init(context);
		        
		JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();

		ReportSchedule reportSchedule =
			(ReportSchedule) jobDataMap.get(ORStatics.REPORT_SCHEDULE);
		reportSchedule.setScheduleDescription(context.getJobDetail().getDescription());

		Report report = reportSchedule.getReport();
		ReportUser user = reportSchedule.getUser();
		Map<String,Object> reportParameters = reportSchedule.getReportParameters();

		log.debug("Report: " + report.getName());
		log.debug("User: " + user.getName());		

		JRVirtualizer virtualizer = null;
       		
		ReportLog reportLog = new ReportLog(user, report, new Date());
        reportLog.setExportType(reportSchedule.getExportType());
        reportLog.setRequestId(reportSchedule.getRequestId());
        
		try
		{
			//
			ReportUserAlert alert = reportSchedule.getAlert();			
			
			if (alert != null)
			{
				log.debug("Executing Alert Condition");
				
				alert.setReport(report);
				alert = alertProvider.executeAlert(alert, true);
				
				if (!alert.isTriggered())
				{
					log.debug("Alert Not Triggered. Report not run.");
					return;
				}
				
				log.debug("Alert Triggered. Running report.");
			}
			//			

			// add standard report parameters
			reportParameters.put(ORStatics.USER_ID, user.getId());
			reportParameters.put(ORStatics.EXTERNAL_ID, user.getExternalId());
			reportParameters.put(ORStatics.USER_NAME, user.getName());
			reportParameters.put(ORStatics.IMAGE_DIR, new File(directoryProvider.getReportImageDirectory()));
			reportParameters.put(ORStatics.REPORT_DIR, new File(directoryProvider.getReportDirectory()));
			//
			
			reportLogProvider.insertReportLog(reportLog);			
			
			ReportEngineInput reportInput = new ReportEngineInput(report, reportParameters);
			reportInput.setExportType(ExportType.findByCode(reportSchedule.getExportType()));
            reportInput.setXmlInput(reportSchedule.getXmlInput());
            reportInput.setLocale(reportSchedule.getLocale());
			
			if (report.isJasperReport())
			{
				// add any charts
				if (report.getReportChart() != null)
				{
					log.debug("Adding chart: " + report.getReportChart().getName());
				
					ChartReportEngine chartEngine = new ChartReportEngine(
							dataSourceProvider, directoryProvider, propertiesProvider);
					
					ChartEngineOutput chartOutput = (ChartEngineOutput) chartEngine.generateReport(reportInput);
				
					reportParameters.put("ChartImage", chartOutput.getContent());				
				}

				if (report.isVirtualizationEnabled())
				{
					log.debug("Virtualization Enabled");
					virtualizer = new JRFileVirtualizer(2, directoryProvider.getTempDirectory());
					reportParameters.put(JRParameter.REPORT_VIRTUALIZER, virtualizer);
				}
				
				reportInput.setParameters(reportParameters);
				reportInput.setInlineImages(true);				
			}
			
			ReportEngine reportEngine = ReportEngineHelper.getReportEngine(report,
					dataSourceProvider, directoryProvider, propertiesProvider);	
			reportEngine.setApplicationContext(appContext);
			
			ReportEngineOutput reportOutput = reportEngine.generateReport(reportInput);            
           
            String[] deliveryMethods = reportSchedule.getDeliveryMethods();      
            
            if (deliveryMethods == null || deliveryMethods.length == 0)
            {
                deliveryMethods = new String[]{org.efs.openreports.ReportConstants.DeliveryMethod.EMAIL.getName()};
                log.warn("DeliveryMethod not set, defaulting to email delivery");
            }
            
            // set status to success. if a delivery method fails, this is updated to delivery failure
            reportLog.setStatus(ReportLog.STATUS_SUCCESS);
            
            ArrayList<ReportDeliveryLog> deliveryLogs = new ArrayList<ReportDeliveryLog>();
            
            for (int i=0; i < deliveryMethods.length; i++)
            {                                  
                ReportDeliveryLog deliveryLog = new ReportDeliveryLog(deliveryMethods[i], new Date());
            
                try
                {      
                	String deliveryMethodBeanId = deliveryMethods[i] + "DeliveryMethod";
                	
                    DeliveryMethod deliveryMethod = (DeliveryMethod) appContext.getBean(deliveryMethodBeanId, DeliveryMethod.class);            
                    deliveryMethod.deliverReport(reportSchedule, reportOutput);
                    
                    deliveryLog.setEndTime(new Date());
                    deliveryLog.setStatus(ReportLog.STATUS_SUCCESS);
                }                
                catch(DeliveryException de)
                {
                	log.error("Delivery Error: " + reportSchedule.getRequestId(), de);
                	
                    deliveryLog.setMessage(de.toString());
                    deliveryLog.setStatus(ReportLog.STATUS_DELIVERY_FAILURE);
                    
                    reportLog.setMessage(de.toString());
                    reportLog.setStatus(ReportLog.STATUS_DELIVERY_FAILURE);                    
                }
                
                deliveryLogs.add(deliveryLog);                
            }		

            reportLog.setDeliveryLogs(deliveryLogs);
			reportLog.setEndTime(new Date());			
            
			reportLogProvider.updateReportLog(reportLog);

			log.debug("Scheduled Report Finished...");
		}
		catch (Exception e)
		{
			if (e.getMessage() != null && e.getMessage().indexOf("Empty") > 0)
			{
				reportLog.setStatus(ReportLog.STATUS_EMPTY);
			}
			else
			{				
				log.error("ScheduledReport Error: " + reportSchedule.getRequestId(), e);

				reportLog.setMessage(e.toString());
				reportLog.setStatus(ReportLog.STATUS_FAILURE);
			}

			reportLog.setEndTime(new Date());

			try
			{
				reportLogProvider.updateReportLog(reportLog);
			}
			catch (Exception ex)
			{
				log.error("Unable to create ReportLog: " + ex.getMessage());
			}			
		}
		finally
		{
			if (virtualizer != null)
			{
				reportParameters.remove(JRParameter.REPORT_VIRTUALIZER);			
				virtualizer.cleanup();
			}          
		}	
		
		// execute all callbacks after the job is finished processing
		executeCallbacks(reportLog);
	}
	
	@SuppressWarnings("unchecked")
	private ApplicationContext init(JobExecutionContext context) throws JobExecutionException
	{
		ApplicationContext appContext = null;
        
        try
        {
            appContext =
                (ApplicationContext)context.getScheduler().getContext().get("applicationContext");
        }
        catch(SchedulerException se)
        {
            throw new JobExecutionException(se);
        }

        reportLogProvider = (ReportLogProvider) appContext.getBean("reportLogProvider", ReportLogProvider.class);
        directoryProvider = (DirectoryProvider) appContext.getBean("directoryProvider", DirectoryProvider.class);
        alertProvider = (AlertProvider) appContext.getBean("alertProvider", AlertProvider.class);
        dataSourceProvider = (DataSourceProvider) appContext.getBean("dataSourceProvider", DataSourceProvider.class);
        propertiesProvider = (PropertiesProvider) appContext.getBean("propertiesProvider", PropertiesProvider.class);
        
        if (appContext.containsBean("scheduledReportCallbacks"))
        {
        	callbacks = (List<ScheduledReportCallback>) appContext.getBean("scheduledReportCallbacks", List.class);
        }
        
        return appContext;
	}

	/*
	 * Execute all ScheduledReportCallbacks registered for this job. Callbacks are configured in the 
	 * Spring bean scheduledReportCallbacks
	 */
	private void executeCallbacks(ReportLog reportLog)
	{
		if (callbacks == null) return;
		
		for (ScheduledReportCallback callback : callbacks)
		{
			callback.callback(reportLog);
		}
	}	
}