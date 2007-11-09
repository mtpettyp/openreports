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
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;
import org.efs.openreports.ORStatics;
import org.efs.openreports.ReportConstants.DeliveryMethod;
import org.efs.openreports.objects.Report;
import org.efs.openreports.objects.ReportLog;
import org.efs.openreports.objects.ReportSchedule;
import org.efs.openreports.objects.ReportUser;
import org.efs.openreports.providers.*;
import org.efs.openreports.util.LocalStrings;
import org.efs.openreports.util.ORUtil;

public class ReportOptionsAction extends ActionSupport implements SessionAware			
{	
	private static final long serialVersionUID = -1057678542422818134L;

	protected static Logger log = Logger.getLogger(ReportOptionsAction.class);

	private Map<Object, Object> session;
	
	private String exportType;
    private String description;
	private boolean submitRun;
	private boolean submitSchedule;
    private boolean submitRunToEmail;
    private boolean submitRunToFile;
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
        	ORUtil.resetOlapContext(ActionContext.getContext());  
        	   
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
        
        if (submitRunToEmail || submitRunToFile)
        {
            Map<String,Object> reportParameters = getReportParameterMap();            
            
            ReportSchedule schedule = new ReportSchedule();
            schedule.setReport(report);
            schedule.setUser(user);
            schedule.setReportParameters(reportParameters);
            schedule.setExportType(Integer.parseInt(exportType));
            schedule.setRecipients(user.getEmail());
            schedule.setScheduleName(report.getId() + "|" + new Date().getTime());
            schedule.setScheduleDescription(description);               
            schedule.setScheduleType(ReportSchedule.ONCE);

            if (submitRunToFile)
            {
                schedule.setDeliveryMethods(new String[]{DeliveryMethod.FILE.getName()});               
            }
            else
            {
                schedule.setDeliveryMethods(new String[]{DeliveryMethod.EMAIL.getName()});  
            }
            
            try
            {
                schedulerProvider.scheduleReport(schedule);
                
                if (submitRunToFile)
                {
                    return ORStatics.GENERATED_REPORTS_ACTION;
                }
                else
                {
                    addActionError(report.getName() + " sent to " + user.getEmail());
                }
            }
            catch(ProviderException pe)
            {
                addActionError(report.getName() + " failed: " + pe.toString());
            }           
        }

		return INPUT;
	}    	    

    @SuppressWarnings("unchecked")
    protected Map<String,Object> getReportParameterMap()
    {
        Map<String,Object> reportParameters = (Map) session.get(ORStatics.REPORT_PARAMETERS);
        return reportParameters;
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
    
    public void setSubmitRunToEmail(String submitRunToEmail)
    {
        if (submitRunToEmail != null) this.submitRunToEmail = true;
    }
    
    public void setSubmitRunToFile(String submitRunToFile)
    {
        if (submitRunToFile != null) this.submitRunToFile = true;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }
}