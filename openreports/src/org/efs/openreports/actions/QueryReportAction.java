/*
 * Copyright (C) 2004 Erik Swenson - erik@oreports.com
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

package org.efs.openreports.actions;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;
import org.efs.openreports.ORStatics;
import org.efs.openreports.engine.JFreeReportEngine;
import org.efs.openreports.engine.QueryReportEngine;
import org.efs.openreports.engine.input.ReportEngineInput;
import org.efs.openreports.engine.output.QueryEngineOutput;
import org.efs.openreports.engine.output.ReportEngineOutput;
import org.efs.openreports.objects.Report;
import org.efs.openreports.objects.ReportLog;
import org.efs.openreports.objects.ReportUser;
import org.efs.openreports.providers.DataSourceProvider;
import org.efs.openreports.providers.DirectoryProvider;
import org.efs.openreports.providers.PropertiesProvider;
import org.efs.openreports.providers.ReportLogProvider;

public class QueryReportAction extends ActionSupport implements SessionAware
{	
	private static final long serialVersionUID = 5233748674680486245L;

	protected static Logger log = Logger.getLogger(QueryReportAction.class);
	
	protected Map<Object,Object> session;

	protected DataSourceProvider dataSourceProvider;
	protected ReportLogProvider reportLogProvider;
	protected PropertiesProvider propertiesProvider;
	protected DirectoryProvider directoryProvider;	
	
	protected Report report;
	
	private String html;	

	public String execute()
	{
		//remove results of any previous query report from session
		ActionContext.getContext().getSession().remove(ORStatics.QUERY_REPORT_RESULTS);
		ActionContext.getContext().getSession().remove(ORStatics.QUERY_REPORT_PROPERTIES);	
		
		ReportUser user = (ReportUser) ActionContext.getContext().getSession().get(
				ORStatics.REPORT_USER);

		report = (Report) ActionContext.getContext().getSession().get(ORStatics.REPORT);

		Map<String,Object> reportParameters = getReportParameterMap(user);

		ReportLog reportLog = new ReportLog(user, report, new Date());

		try
		{
			log.debug("Starting Query Report: " + report.getName());
			log.debug("Query: " + report.getQuery());

			reportLogProvider.insertReportLog(reportLog);		
			
			ReportEngineInput input = new ReportEngineInput(report, reportParameters);
			
			if (report.isJFreeReport())
			{
				JFreeReportEngine jfreeReportEngine = new JFreeReportEngine(
						dataSourceProvider, directoryProvider, propertiesProvider);
				
				ReportEngineOutput output = jfreeReportEngine.generateReport(input);				
				
				html = new String(output.getContent());
			}
			else
			{
				QueryReportEngine queryReportEngine = new QueryReportEngine(
						dataSourceProvider, directoryProvider, propertiesProvider);
				
				QueryEngineOutput output = (QueryEngineOutput) queryReportEngine.generateReport(input);				
				
				session.put(ORStatics.QUERY_REPORT_RESULTS, output.getResults());
				
				session.put(ORStatics.QUERY_REPORT_PROPERTIES, output.getProperties());
			}			

			reportLog.setEndTime(new Date());
			reportLog.setStatus(ReportLog.STATUS_SUCCESS);
			reportLogProvider.updateReportLog(reportLog);

			log.debug("Finished Query Report: " + report.getName());
		}
		catch (Exception e)
		{

			addActionError(e.getMessage());

			log.error(e.getMessage());

			reportLog.setMessage(e.getMessage());
			reportLog.setStatus(ReportLog.STATUS_FAILURE);

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
		
		if (report.isJFreeReport()) return ORStatics.JFREEREPORT_RESULT;		
		
		return SUCCESS;		
	}	

	@SuppressWarnings("unchecked")
	protected Map<String,Object> getReportParameterMap(ReportUser user)
	{
		Map<String,Object> reportParameters = new HashMap<String,Object>();

		if (session.get(ORStatics.REPORT_PARAMETERS) != null)
		{
			reportParameters = (Map) session.get(ORStatics.REPORT_PARAMETERS);
		}

		// add standard report parameters
		reportParameters.put(ORStatics.USER_ID, user.getId());
		reportParameters.put(ORStatics.EXTERNAL_ID, user.getExternalId());
		reportParameters.put(ORStatics.USER_NAME, user.getName());

		return reportParameters;
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

	public void setDataSourceProvider(DataSourceProvider dataSourceProvider)
	{
		this.dataSourceProvider = dataSourceProvider;
	}

	public void setPropertiesProvider(PropertiesProvider propertiesProvider)
	{
		this.propertiesProvider = propertiesProvider;		
	}	
	
	public void setDirectoryProvider(DirectoryProvider directoryProvider)
	{
		this.directoryProvider = directoryProvider;
	}

	public String getHtml()
	{
		return html;
	}
	
	public Report getReport()
	{
		return report;
	}
}