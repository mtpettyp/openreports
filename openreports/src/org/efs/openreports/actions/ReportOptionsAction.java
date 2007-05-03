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

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;
import org.efs.openreports.ORStatics;
import org.efs.openreports.objects.Report;
import org.efs.openreports.objects.ReportLog;
import org.efs.openreports.objects.ReportUser;
import org.efs.openreports.providers.*;
import org.efs.openreports.util.LocalStrings;

public class ReportOptionsAction extends ActionSupport implements SessionAware			
{	
	private static final long serialVersionUID = -1057678542422818134L;

	protected static Logger log = Logger.getLogger(ReportOptionsAction.class);

	private Map<Object, Object> session;
	
	private String exportType;
	private boolean submitRun;
	private boolean submitSchedule;
	private Report report;
	
	private SchedulerProvider schedulerProvider;
	private ReportLogProvider reportLogProvider;

	public String execute()
	{
		ReportUser user = (ReportUser) ActionContext.getContext().getSession().get(
				ORStatics.REPORT_USER);
		
		report = (Report) ActionContext.getContext().getSession().get(ORStatics.REPORT);		

		if (report == null)
		{
			addActionError(LocalStrings.ERROR_REPORT_INVALID);
			return ERROR;
		}

		if (!user.isValidReport(report))
		{
			addActionError(LocalStrings.ERROR_REPORT_NOTAUTHORIZED);
			return ERROR;
		}

		session.put(ORStatics.REPORT, report);		

		if (report.isQueryReport() && !submitSchedule)
		{
			return ORStatics.QUERY_REPORT_ACTION;
		}
        else if (report.isChartReport() && !submitSchedule)
		{
			return ORStatics.CHART_REPORT_ACTION;
		}	
        else if (report.isJPivotReport() && !submitSchedule)
        {  
        	resetOlapContext(ActionContext.getContext());  
        	   
        	try
        	{
        		ReportLog reportLog = new ReportLog(user, report, new Date());           
        		reportLogProvider.insertReportLog(reportLog);
        	}
        	catch(ProviderException pe)
        	{
        		log.warn(pe);
        	}            
                     
            return ORStatics.JPIVOT_ACTION;
        }   
		
		/*
		 * if report is displayed inline, export type already selected and
		 * scheduling not currently supported, so just run report...
		 */
		if (report.isDisplayInline()) return SUCCESS;		
		
		if (submitRun || submitSchedule)
		{
			if (exportType == null || exportType.length() < 1) return INPUT;
			
			session.put(ORStatics.EXPORT_TYPE, exportType);			
			
			if (submitRun)return SUCCESS;	
			if (submitSchedule)	return ORStatics.SCHEDULE_REPORT_ACTION;			
		}

		return INPUT;
	}
    	
    /*
     * All JPivot objects must be removed from session each time a new JPivot report is run.
     */
    protected void resetOlapContext(ActionContext context)
    {
        Iterator i = context.getSession().keySet().iterator();
        while(i.hasNext())
        {
            String key = (String) i.next();
            if (key.indexOf("tonbeller") > -1 || key.indexOf("01") > -1)
            {                    
                context.getSession().remove(key);
                log.debug(key + " removed from session");
            }
        }
    }

    @SuppressWarnings("unchecked")
	public void setSession(Map session) 
	{
		this.session = session;
	}
    
	public String getExportType()
	{
		return exportType;
	}

	public void setExportType(String exportType)
	{
		this.exportType = exportType;
	}

	public Report getReport()
	{
		return report;
	}

	public void setReport(Report report)
	{
		this.report = report;
	}

	public boolean isSchedulerAvailable()
	{
		if (schedulerProvider != null) return true;
		return false;
	}

	public void setSchedulerProvider(SchedulerProvider schedulerProvider)
	{
		this.schedulerProvider = schedulerProvider;
	}

	public void setReportLogProvider(ReportLogProvider reportLogProvider)
	{
		this.reportLogProvider = reportLogProvider;
	}

	public void setSubmitRun(String submitRun)
	{
		if (submitRun != null) this.submitRun = true;
	}

	public void setSubmitSchedule(String submitSchedule)
	{
		if (submitSchedule != null) this.submitSchedule = true;
	}	
}