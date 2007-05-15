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

import com.thoughtworks.xstream.XStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRVirtualizer;
import net.sf.jasperreports.engine.fill.JRFileVirtualizer;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.efs.openreports.ORStatics;
import org.efs.openreports.engine.ChartReportEngine;
import org.efs.openreports.engine.JasperReportEngine;
import org.efs.openreports.engine.ReportEngine;
import org.efs.openreports.engine.ReportEngineHelper;
import org.efs.openreports.engine.input.ReportEngineInput;
import org.efs.openreports.engine.output.ChartEngineOutput;
import org.efs.openreports.engine.output.JasperReportEngineOutput;
import org.efs.openreports.engine.output.ReportEngineOutput;
import org.efs.openreports.objects.GeneratedReport;
import org.efs.openreports.objects.MailMessage;
import org.efs.openreports.objects.Report;
import org.efs.openreports.objects.ReportLog;
import org.efs.openreports.objects.ReportSchedule;
import org.efs.openreports.objects.ReportUser;
import org.efs.openreports.objects.ReportUserAlert;
import org.efs.openreports.providers.AlertProvider;
import org.efs.openreports.providers.DataSourceProvider;
import org.efs.openreports.providers.DirectoryProvider;
import org.efs.openreports.providers.MailProvider;
import org.efs.openreports.providers.PropertiesProvider;
import org.efs.openreports.providers.ReportLogProvider;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;

public class ScheduledReportJob	implements Job
{
	protected static Logger log =
		Logger.getLogger(ScheduledReportJob.class.getName());
	
	private ReportLogProvider reportLogProvider;	
	private DirectoryProvider directoryProvider;
	private MailProvider mailProvider;
	private AlertProvider alertProvider;
	private DataSourceProvider dataSourceProvider;
	private PropertiesProvider propertiesProvider;

	public ScheduledReportJob()
	{
				
	}	

	public void execute(JobExecutionContext context)
		throws JobExecutionException
	{
		log.debug("Scheduled Report Executing....");
        
        try
        {
            ApplicationContext appContext =
                (ApplicationContext)context.getScheduler().getContext().get("applicationContext");

            reportLogProvider = (ReportLogProvider) appContext.getBean("reportLogProvider", ReportLogProvider.class);
            directoryProvider = (DirectoryProvider) appContext.getBean("directoryProvider", DirectoryProvider.class);
            mailProvider = (MailProvider) appContext.getBean("mailProvider", MailProvider.class);
            alertProvider = (AlertProvider) appContext.getBean("alertProvider", AlertProvider.class);
            dataSourceProvider = (DataSourceProvider) appContext.getBean("dataSourceProvider", DataSourceProvider.class);
            propertiesProvider = (PropertiesProvider) appContext.getBean("propertiesProvider", PropertiesProvider.class);
        }
        catch(Exception ex)
        {
            throw new JobExecutionException(ex);
        }

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
        FileOutputStream file = null;
		
		ReportLog reportLog = new ReportLog(user, report, new Date());

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
			reportInput.setExportType(reportSchedule.getExportType());
			
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
			
			//Use the JasperReportEngine to generate scheduled QueryReports
			if (report.isQueryReport())
			{
				reportEngine = new JasperReportEngine(dataSourceProvider,
						directoryProvider, propertiesProvider);
			}
			
			ReportEngineOutput reportOutput = reportEngine.generateReport(reportInput);
            
            Boolean generateFile = (Boolean) reportParameters.get(ORStatics.GENERATE_FILE);
            if (generateFile != null && generateFile.booleanValue() == true)
            {
                Date runDate = new Date();
                
                String fileName = runDate.getTime() + "-"
                        + StringUtils.deleteWhitespace(user.getName()) + "-"
                        + StringUtils.deleteWhitespace(report.getName());                       
                
                file = new FileOutputStream(directoryProvider
                        .getReportGenerationDirectory()
                        + fileName + reportOutput.getContentExtension());
                
                file.write(reportOutput.getContent());  
                file.flush();
                file.close();
                
                GeneratedReport info = new GeneratedReport();             
                info.setParameters(reportParameters);
                info.setReportDescription(reportSchedule.getScheduleDescription());
                info.setReportName(report.getName());
                info.setReportFileName(fileName + reportOutput.getContentExtension());
                info.setRunDate(runDate);
                info.setUserName(user.getName());
                
                file = new FileOutputStream(directoryProvider.getReportGenerationDirectory() + fileName + ".xml");

                XStream xStream = new XStream();
                xStream.alias("reportGenerationInfo", GeneratedReport.class);
                xStream.toXML(info, file);
                
                file.flush();
                file.close();           
                
                MailMessage mail = new MailMessage();               
                mail.setSender(user.getEmail());
                mail.parseRecipients(reportSchedule.getRecipients());
                mail.setText(report.getName() + ": Generated on " + new Date());
                mail.setSubject(reportSchedule.getScheduleDescription());
                
                mailProvider.sendMail(mail);
                
                log.debug(report.getName() + " written to: " + fileName);
            }
            else
            {			
    			ArrayList<ByteArrayDataSource> htmlImageDataSources = new ArrayList<ByteArrayDataSource>();
     			ByteArrayDataSource byteArrayDataSource = exportReport(reportOutput, reportSchedule, htmlImageDataSources);
    
    			MailMessage mail = new MailMessage();				
    			mail.setByteArrayDataSource(byteArrayDataSource);
    			mail.addHtmlImageDataSources(htmlImageDataSources);			 
    			mail.setSender(user.getEmail());
    			mail.parseRecipients(reportSchedule.getRecipients());
    			
    			if (reportSchedule.getScheduleDescription() != null && reportSchedule.getScheduleDescription().trim().length() > 0)
    			{
    				mail.setSubject(reportSchedule.getScheduleDescription());
    			}
    			else
    			{
    				mail.setSubject(report.getName());
    			}
    			
    			if (reportSchedule.getExportType() != ReportEngine.EXPORT_HTML)
    			{
    				mail.setText(report.getName() + ": Generated on " + new Date());
    			}
    
    			mailProvider.sendMail(mail);
                
                log.debug(byteArrayDataSource.getName() + " sent to: " + mail.formatRecipients(";"));
            }		

			reportLog.setEndTime(new Date());
			reportLog.setStatus(ReportLog.STATUS_SUCCESS);
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
				e.printStackTrace();
				log.error(e.toString());

				reportLog.setMessage(e.getMessage());
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
            
            if (file != null)
            {
                try
                {
                    file.flush();
                    file.close();
                }
                catch(IOException ioe)
                {
                    ioe.printStackTrace();
                }
            }
		}		
	}

	protected ByteArrayDataSource exportReport(ReportEngineOutput reportOutput, ReportSchedule reportSchedule,
			ArrayList<ByteArrayDataSource> htmlImageDataSources) throws JRException
	{		
		String reportName = StringUtils.deleteWhitespace(reportSchedule.getReport().getName());

		ByteArrayDataSource byteArrayDataSource = new ByteArrayDataSource(reportOutput.getContent(), reportOutput.getContentType());
		byteArrayDataSource.setName(reportName + reportOutput.getContentExtension());
		
		if (reportSchedule.getExportType() == ReportEngine.EXPORT_HTML
				&& reportSchedule.getReport().isJasperReport())
		{
			Map imagesMap = ((JasperReportEngineOutput) reportOutput).getImagesMap();

			for (Iterator entryIter = imagesMap.entrySet().iterator(); entryIter
					.hasNext();)
			{
				Map.Entry entry = (Map.Entry) entryIter.next();

				ByteArrayDataSource imageDataSource = new ByteArrayDataSource(
						(byte[]) entry.getValue(), getImageContentType((byte[]) entry
								.getValue()));

				imageDataSource.setName((String) entry.getKey());

				htmlImageDataSources.add(imageDataSource);
			}
		}

		return byteArrayDataSource;
	}

	/**
	 * Try to figure out the image type from its bytes.
	 */
	private String getImageContentType(byte[] bytes)
	{
		String header = new String(bytes, 0, (bytes.length > 100) ? 100 : bytes.length);
		if (header.startsWith("GIF"))
		{
			return "image/gif";
		}

		if (header.startsWith("BM"))
		{
			return "image/bmp";
		}

		if (header.indexOf("JFIF") >= 0)
		{
			return "image/jpeg";
		}

		if (header.indexOf("PNG") >= 0)
		{
			return "image/png";
		}

		// We are out of guesses, so just guess tiff
		return "image/tiff";
	}	
}