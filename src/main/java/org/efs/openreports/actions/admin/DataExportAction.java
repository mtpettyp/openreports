/*
 * Copyright (C) 2007 Erik Swenson - erik@oreports.com
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

package org.efs.openreports.actions.admin;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.struts2.ServletActionContext;
import org.efs.openreports.objects.Report;
import org.efs.openreports.objects.ReportAlert;
import org.efs.openreports.objects.ReportChart;
import org.efs.openreports.objects.ReportExportOption;
import org.efs.openreports.objects.ReportParameter;
import org.efs.openreports.providers.ReportProvider;

import com.opensymphony.xwork2.ActionSupport;
import com.thoughtworks.xstream.XStream;

public class DataExportAction extends ActionSupport  
{	
	private boolean submitOk;	
	
	private String fileName;
	private List reports;
	private int[] reportIds;
	
	private ReportProvider reportProvider;
	
	public String execute()
	{
		try
		{
			if (!submitOk) 
			{
				reports = reportProvider.getReports();
				return SUCCESS;
			}

			XStream xStream = new XStream();
			xStream.omitField(BasicDataSource.class, "logWriter");			
			xStream.omitField(Report.class, "id");
			xStream.omitField(ReportAlert.class, "id");
			xStream.omitField(ReportChart.class, "id");
			xStream.omitField(ReportParameter.class, "id");
			xStream.omitField(ReportExportOption.class, "id");

			List<Report> selectedReports = new ArrayList<Report>();
			
			for (int i = 0; i < reportIds.length; i++) 
			{
				Report report = reportProvider.getReport(new Integer(reportIds[i]));
				selectedReports.add(report);				
			}
			
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setHeader("Content-disposition", "inline; filename=or-report-export.xml");
			response.setContentType("application/xml");
					
			ServletOutputStream outputStream = response.getOutputStream();
			
			xStream.toXML(selectedReports, outputStream);
			
			outputStream.flush();
			outputStream.close();
			
			return NONE;
		} 
		catch (Exception e) 
		{
			addActionError(e.toString());
			return INPUT;
		}
	}		

	public void setSubmitOk(String submitOk)
	{
		if (submitOk != null) this.submitOk = true;
	}	
	
	public List getReports()
	{
		return reports;
	}	

	public int[] getReportIds()
	{
		return reportIds;
	}

	public void setReportIds(int[] reportIds)
	{
		this.reportIds = reportIds;
	}	
	
	public String getFileName() 
	{
		return fileName;
	}

	public void setFileName(String fileName) 
	{
		this.fileName = fileName;
	}
	
	public void setReportProvider(ReportProvider reportProvider)
	{
		this.reportProvider = reportProvider;
	}
}