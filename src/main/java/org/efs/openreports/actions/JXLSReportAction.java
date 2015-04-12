/*
 * Copyright (C) 2006 Erik Swenson - erik@oreports.com
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

import java.sql.Connection;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.efs.openreports.ORStatics;
import org.efs.openreports.engine.JXLSReportEngine;
import org.efs.openreports.engine.input.ReportEngineInput;
import org.efs.openreports.engine.output.ReportEngineOutput;
import org.efs.openreports.objects.Report;
import org.efs.openreports.objects.ReportLog;
import org.efs.openreports.objects.ReportUser;

import org.apache.struts2.ServletActionContext;
import com.opensymphony.xwork2.ActionContext;

public class JXLSReportAction extends QueryReportAction 
{	
	private static final long serialVersionUID = 824866564485287929L;
	
	protected static Logger log = Logger.getLogger(JXLSReportAction.class);		
	
	public String execute()
	{		
		ReportUser user = (ReportUser) ActionContext.getContext().getSession().get(
				ORStatics.REPORT_USER);

		report = (Report) ActionContext.getContext().getSession().get(ORStatics.REPORT);

		Map<String,Object> parameters = getReportParameterMap(user);

		ReportLog reportLog = new ReportLog(user, report, new Date());

		Connection conn = null;
		
		try
		{
			log.debug("Starting JXLS Report: " + report.getName());			

			reportLogProvider.insertReportLog(reportLog);

			ReportEngineInput input = new ReportEngineInput(report, parameters);
			
			JXLSReportEngine reportEngine = new JXLSReportEngine(
						dataSourceProvider, directoryProvider, propertiesProvider);
				
			ReportEngineOutput output = reportEngine.generateReport(input);

			HttpServletResponse response = ServletActionContext.getResponse();			
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition", "inline; filename="
					+ StringUtils.deleteWhitespace(report.getName()) + ".xls");

			ServletOutputStream out = response.getOutputStream();

			out.write(output.getContent(), 0, output.getContent().length);

			out.flush();
			out.close();

			reportLog.setEndTime(new Date());
			reportLog.setStatus(ReportLog.STATUS_SUCCESS);
			reportLogProvider.updateReportLog(reportLog);

			log.debug("Finished JRXLS Report: " + report.getName());
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
		finally
		{
			try
			{				
				if (conn != null) conn.close();
			}
			catch (Exception c)
			{
				log.error("Error closing");
			}
		}

		return NONE;
	}		
}