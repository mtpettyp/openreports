/*
 * Copyright (C) 2002 Erik Swenson - erik@oreports.com
 * 
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *  
 */

package org.efs.openreports.actions;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRVirtualizer;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.fill.JRFileVirtualizer;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;
import org.efs.openreports.ORStatics;
import org.efs.openreports.ReportConstants.ExportType;
import org.efs.openreports.engine.ChartReportEngine;
import org.efs.openreports.engine.JasperReportEngine;
import org.efs.openreports.engine.ReportEngine;
import org.efs.openreports.engine.ReportEngineHelper;
import org.efs.openreports.engine.input.ReportEngineInput;
import org.efs.openreports.engine.output.ChartEngineOutput;
import org.efs.openreports.engine.output.ReportEngineOutput;
import org.efs.openreports.objects.Report;
import org.efs.openreports.objects.ReportLog;
import org.efs.openreports.objects.ReportUser;
import org.efs.openreports.providers.DataSourceProvider;
import org.efs.openreports.providers.DirectoryProvider;
import org.efs.openreports.providers.PropertiesProvider;
import org.efs.openreports.providers.ReportLogProvider;
import org.efs.openreports.util.LocalStrings;

public class ReportRunAction extends ActionSupport implements SessionAware
{	
	private static final long serialVersionUID = 7473180642590984527L;

	protected static Logger log = Logger.getLogger(ReportRunAction.class);
	
	private Map<Object,Object> session;
	
	private ReportLogProvider reportLogProvider;
	private DirectoryProvider directoryProvider;
	private DataSourceProvider dataSourceProvider;
	private PropertiesProvider propertiesProvider;		

	public String execute()
	{		
		ReportUser user =
			(ReportUser) ActionContext.getContext().getSession().get(ORStatics.REPORT_USER);

		Report report = (Report) ActionContext.getContext().getSession().get(ORStatics.REPORT);

		int exportTypeCode =
			Integer.parseInt(
				(String) ActionContext.getContext().getSession().get(ORStatics.EXPORT_TYPE));
		
		ExportType exportType = ExportType.findByCode(exportTypeCode);

		Map<String, Object> reportParameters = getReportParameterMap(user, report, exportType);
		Map imagesMap = getImagesMap();

		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();

		// set headers to disable caching		
		response.setHeader("Pragma", "public");
		response.setHeader("Cache-Control", "max-age=0");

		ReportLog reportLog = new ReportLog(user, report, new Date());
        reportLog.setExportType(exportType.getCode());
		
		JRVirtualizer virtualizer = null;

		try
		{
			if (exportType == ExportType.PDF)				
			{
				// Handle "contype" request from Internet Explorer
				if ("contype".equals(request.getHeader("User-Agent"))) 
				{					
	                response.setContentType("application/pdf");	            
	                response.setContentLength(0);               
	                
	                ServletOutputStream outputStream = response.getOutputStream();
	                outputStream.close();	    
	                
	                return NONE;
				}
			}			
							
			log.debug("Filling report: " + report.getName());
			
			reportLogProvider.insertReportLog(reportLog);						

			if (report.isVirtualizationEnabled() && exportType != ExportType.IMAGE)
			{
				log.debug("Virtualization Enabled");
				virtualizer = new JRFileVirtualizer(2, directoryProvider.getTempDirectory());
				reportParameters.put(JRParameter.REPORT_VIRTUALIZER, virtualizer);
			}

			ReportEngineInput reportInput = new ReportEngineInput(report, reportParameters);
			reportInput.setExportType(exportType);
			reportInput.setImagesMap(imagesMap);

			// add any charts
			if (report.getReportChart() != null)
			{
				log.debug("Adding chart: " + report.getReportChart().getName());

				ChartReportEngine chartEngine = new ChartReportEngine(dataSourceProvider,
						directoryProvider, propertiesProvider);
				
				ChartEngineOutput chartOutput = (ChartEngineOutput) chartEngine
						.generateReport(reportInput);

				reportParameters.put("ChartImage", chartOutput.getContent());
			}
			
			ReportEngineOutput reportOutput = null;
			JasperPrint jasperPrint = null;
			
			if (report.isJasperReport())
			{
				JasperReportEngine jasperEngine = new JasperReportEngine(
						dataSourceProvider, directoryProvider, propertiesProvider);
				
				jasperPrint = jasperEngine.fillReport(reportInput);

				log.debug("Report filled - " + report.getName() + " : size = "
						+ jasperPrint.getPages().size());

				log.debug("Exporting report: " + report.getName());

				reportOutput = jasperEngine.exportReport(jasperPrint, exportType, report
						.getReportExportOption(), imagesMap, false);
			}
			else
			{
				ReportEngine reportEngine = ReportEngineHelper.getReportEngine(report, dataSourceProvider, directoryProvider, propertiesProvider);
				reportOutput = reportEngine.generateReport(reportInput);
			}
			
			response.setContentType(reportOutput.getContentType());
			
			if (exportType != ExportType.HTML && exportType != ExportType.IMAGE)
			{
				response.setHeader("Content-disposition", "inline; filename="
						+ StringUtils.deleteWhitespace(report.getName()) + reportOutput.getContentExtension());
			}			
			
			if (exportType == ExportType.IMAGE)
			{
				if (jasperPrint != null)
				{					
					session.put(ORStatics.JASPERPRINT, jasperPrint);
				}
			}
			else
			{				
				byte[] content = reportOutput.getContent();
				
				response.setContentLength(content.length);
				
				ServletOutputStream out = response.getOutputStream();
				out.write(content, 0, content.length);
				out.flush();
				out.close();				
			}

			reportLog.setEndTime(new Date());
			reportLog.setStatus(ReportLog.STATUS_SUCCESS);
			reportLogProvider.updateReportLog(reportLog);			

			log.debug("Finished report: " + report.getName());
		}
		catch (Exception e)
		{
			if (e.getMessage() != null && e.getMessage().equals(LocalStrings.ERROR_REPORT_EMPTY))
			{				
				reportLog.setStatus(ReportLog.STATUS_EMPTY);
			}
			else
			{				
				log.error(e.getMessage());

				reportLog.setMessage(e.getMessage());
				reportLog.setStatus(ReportLog.STATUS_FAILURE);
			}
            
            addActionError(getText(e.getMessage()));

			reportLog.setEndTime(new Date());

			try
			{
				reportLogProvider.updateReportLog(reportLog);
			}
			catch (Exception ex)
			{
				log.error("Unable to create ReportLog: " + ex.getMessage());
			}			
			
			return ERROR;
		}
		finally
		{
			if (virtualizer != null)
			{
				reportParameters.remove(JRParameter.REPORT_VIRTUALIZER);			
				virtualizer.cleanup();
			}
		}		
		
		if (exportType == ExportType.IMAGE) return SUCCESS;
		
		return NONE;
	}

	@SuppressWarnings("unchecked")
	protected Map<String,Object> getReportParameterMap(ReportUser user, Report report, ExportType exportType)
	{
		Map<String,Object> reportParameters = new HashMap<String,Object>();
		
		if (ActionContext.getContext().getSession().get(ORStatics.REPORT_PARAMETERS) != null)
		{
			reportParameters =
				(Map) ActionContext.getContext().getSession().get(ORStatics.REPORT_PARAMETERS);
		}
		
		reportParameters.put(ORStatics.IMAGE_DIR, new File(directoryProvider.getReportImageDirectory()));		
		reportParameters.put(ORStatics.REPORT_DIR, new File(directoryProvider.getReportDirectory()));		
		reportParameters.put(ORStatics.EXPORT_TYPE_PARAM, new Integer(exportType.getCode()));

		return reportParameters;
	}

	protected Map getImagesMap()
	{
		// used by JasperReports HTML export
		// see ImageLoaderAction for more information
		Map imagesMap = null;
		if (ActionContext.getContext().getSession().get(ORStatics.IMAGES_MAP) != null)
		{
			imagesMap = (Map) ActionContext.getContext().getSession().get(ORStatics.IMAGES_MAP);
		}
		else
		{
			imagesMap = new HashMap();
			session.put(ORStatics.IMAGES_MAP, imagesMap);
		}

		return imagesMap;
	}	

	@SuppressWarnings("unchecked")
	public void setSession(Map session) 
	{
		this.session = session;
	}

	public void setReportLogProvider(ReportLogProvider reportLogProvider)
	{
		this.reportLogProvider = reportLogProvider;
	}

	public void setDirectoryProvider(DirectoryProvider directoryProvider)
	{
		this.directoryProvider = directoryProvider;
	}		

	public void setDataSourceProvider(DataSourceProvider dataSourceProvider)
	{
		this.dataSourceProvider = dataSourceProvider;
	}

	public void setPropertiesProvider(PropertiesProvider propertiesProvider)
	{
		this.propertiesProvider = propertiesProvider;
	}
}