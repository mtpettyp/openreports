/*
 * Copyright (C) 2002 Erik Swenson - erik@oreports.com
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

import java.util.*;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import org.apache.struts2.interceptor.ParameterAware;
import org.apache.struts2.interceptor.SessionAware;
import org.efs.openreports.ORStatics;
import org.efs.openreports.objects.*;
import org.efs.openreports.providers.*;
import org.efs.openreports.util.LocalStrings;

public class ReportDetailAction extends ActionSupport implements SessionAware, ParameterAware
{	
	private static final long serialVersionUID = 724821018564650888L;
	
	private Map<Object,Object> session;
	private Map<String,Object> parameters;
	
	private Report report;
	private int reportId = Integer.MIN_VALUE;
	
	private String submitType;

	private ParameterProvider parameterProvider;
	private ReportProvider reportProvider;
	private DateProvider dateProvider;

	private List<ReportParameterMap> reportParameters;
	private int step = 0;
	
	private boolean displayInline;	

	@Override
	public String execute()
	{
		try
		{
			ReportUser user = (ReportUser) ActionContext.getContext().getSession().get(
					ORStatics.REPORT_USER);

			report = reportProvider.getReport(new Integer(reportId));			

			if (report == null)
			{
				addActionError(getText(LocalStrings.ERROR_REPORT_INVALID));
				return ERROR;
			}

			if (!user.isValidReport(report))
			{
				addActionError(getText(LocalStrings.ERROR_REPORT_NOTAUTHORIZED));
				return ERROR;
			}			

			report.setDisplayInline(displayInline);
			reportParameters = report.getReportParametersByStep(step);

			if (submitType == null)
			{
				// first time through create new map and add standard report parameters
				HashMap<String,Object> newMap = new HashMap<String,Object>();
				newMap.put(ORStatics.USER_ID, user.getId());
				newMap.put(ORStatics.EXTERNAL_ID, user.getExternalId());
				newMap.put(ORStatics.USER_NAME, user.getName());

				ActionContext.getContext().getSession().remove(ORStatics.REPORT_PARAMETERS);
				session.put(ORStatics.REPORT_PARAMETERS, newMap);
				session.put(ORStatics.REPORT, report);

				if (report.getParameters().size() > 0 && report.getParameters().size() != report.getSubReportParameters().size())
				{					
					parameterProvider.loadReportParameterValues(reportParameters, newMap);

					return INPUT;
				}
				else
				{
					return SUCCESS;
				}
			}

			parameterProvider.validateParameters(reportParameters, parameters);

			Map<String,Object> map = getReportParametersFromSession();

			Map<String,Object> currentMap = parameterProvider.getReportParametersMap(reportParameters,parameters);

			map.putAll(currentMap);

			session.put(ORStatics.REPORT_PARAMETERS, map);
			
			step++;

			reportParameters = report.getReportParametersByStep(step);

			if (reportParameters.size() > 0)
			{
				parameterProvider.loadReportParameterValues(reportParameters, map);

				return INPUT;
			}

			return SUCCESS;
		}
		catch (Exception e)
		{			
			Map<String,Object> map = getReportParametersFromSession();

			try
			{
				parameterProvider.loadReportParameterValues(reportParameters, map);
			}
			catch (ProviderException pe)
			{
				addActionError(getText(pe.getMessage()));
			}

			addActionError(getText(e.getMessage()));
			return INPUT;
		}
	}    
   
	@SuppressWarnings("unchecked")
	public void setSession(Map session) 
	{
		this.session = session;
	}
	
	@SuppressWarnings("unchecked")
	public void setParameters(Map parameters) 
	{
		this.parameters = parameters;
	}

	@SuppressWarnings("unchecked")
	public Map<String,Object> getReportParametersFromSession() 
	{
		return (Map) session.get(ORStatics.REPORT_PARAMETERS);
	}
		
	public String getSubmitType()
	{
		return submitType;
	}

	public void setSubmitType(String submitType)
	{
		this.submitType = submitType;
	}

	public int getReportId()
	{
		return reportId;
	}

	public void setReportId(int reportId)
	{
		this.reportId = reportId;
	}

	public void setParameterProvider(ParameterProvider parameterProvider)
	{
		this.parameterProvider = parameterProvider;
	}

	public void setReportProvider(ReportProvider reportProvider)
	{
		this.reportProvider = reportProvider;
	}

	public List<ReportParameterMap> getReportParameters()
	{
		return reportParameters;
	}

	public int getStep()
	{
		return step;
	}

	public void setStep(int step)
	{
		this.step = step;
	}
	
	public void setDateProvider(DateProvider dateProvider)
	{
		this.dateProvider = dateProvider;
	}
	
	public String getDateFormat()
	{
		return dateProvider.getDateFormat().toPattern();
	}
	
	public String getDefaultDate()
	{
		return dateProvider.formatDate(new Date());
	}
	
	public Report getReport()
	{
		return report;
	}
	
	public void setReport(Report report)
	{
		this.report = report;
	}

	public boolean isDisplayInline()
	{
		return displayInline;
	}

	public void setDisplayInline(boolean displayInline)
	{
		this.displayInline = displayInline;
	}
}