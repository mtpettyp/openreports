/*
 * Copyright (C) 2003 Erik Swenson - erik@oreports.com
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

package org.efs.openreports.providers.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.efs.openreports.ORStatics;
import org.efs.openreports.objects.ReportSchedule;
import org.efs.openreports.objects.ReportUser;
import org.efs.openreports.providers.ProviderException;
import org.efs.openreports.providers.SchedulerProvider;
import org.efs.openreports.util.ScheduledReportJob;
import org.quartz.*;

public class SchedulerProviderImpl implements SchedulerProvider
{
	protected static Logger log = Logger.getLogger(SchedulerProviderImpl.class.getName());

    private Scheduler scheduler;
    
	public SchedulerProviderImpl() 
	{		
		log.info("SchedulerProviderImpl created.");		
	}

	public void scheduleReport(ReportSchedule reportSchedule) throws ProviderException
	{
		JobDetail jobDetail = new JobDetail(reportSchedule.getScheduleName(), reportSchedule
				.getScheduleGroup(), ScheduledReportJob.class);

		jobDetail.getJobDataMap().put(ORStatics.REPORT_SCHEDULE, reportSchedule);
		jobDetail.setDescription(reportSchedule.getScheduleDescription());

		if (reportSchedule.getScheduleType() == ReportSchedule.DAILY
				|| reportSchedule.getScheduleType() == ReportSchedule.WEEKLY
				|| reportSchedule.getScheduleType() == ReportSchedule.MONTHLY 
				|| reportSchedule.getScheduleType() == ReportSchedule.WEEKDAYS
				|| reportSchedule.getScheduleType() == ReportSchedule.HOURLY
				|| reportSchedule.getScheduleType() == ReportSchedule.CRON)
		{			
			StringBuffer cronExpression = new StringBuffer();
			
			if (reportSchedule.getScheduleType() == ReportSchedule.CRON)
			{
				cronExpression.append(reportSchedule.getCronExpression());
			}
			else
			{
				cronExpression.append("0 ");
				cronExpression.append(reportSchedule.getStartMinute());
				cronExpression.append(" ");
				cronExpression.append(reportSchedule.getAbsoluteStartHour());

				if (reportSchedule.getScheduleType() == ReportSchedule.HOURLY)
				{
					cronExpression.append("-" + reportSchedule.getAbsoluteEndHour());
				}
				
				if (reportSchedule.getScheduleType() == ReportSchedule.WEEKLY)
				{
					cronExpression.append(" ? * ");
					cronExpression.append(reportSchedule.getDayOfWeek());
				}
				else if (reportSchedule.getScheduleType() == ReportSchedule.MONTHLY)
				{
					cronExpression.append(" " + reportSchedule.getDayOfMonth());
					cronExpression.append(" * ? ");
				}
				else if (reportSchedule.getScheduleType() == ReportSchedule.WEEKDAYS)
				{
					cronExpression.append(" ? * MON-FRI");
				}
				else
				{
					cronExpression.append(" * * ?");
				}
			}			

			CronTrigger cronTrigger = new CronTrigger(reportSchedule.getScheduleName(),
					reportSchedule.getScheduleGroup());

			try
			{
				cronTrigger.setCronExpression(cronExpression.toString());
			}
			catch(ParseException pe)
			{
				throw new ProviderException(pe);
			}			
            
			cronTrigger.setStartTime(reportSchedule.getStartDate());
            cronTrigger.setPriority(reportSchedule.getSchedulePriority());
            cronTrigger.getJobDataMap().put(reportSchedule.getScheduleName(), reportSchedule.getRequestId());

            try
            {
                scheduler.scheduleJob(jobDetail, cronTrigger);
            }
            catch(SchedulerException e)
            {
                throw new ProviderException(e);
            }
		}		
		else
		{
			// default to run once...
			SimpleTrigger trigger = new SimpleTrigger(reportSchedule.getScheduleName(),
					reportSchedule.getScheduleGroup(), reportSchedule.getStartDateTime(), null,
					0, 0L);
            trigger.setPriority(reportSchedule.getSchedulePriority());
            trigger.getJobDataMap().put(reportSchedule.getScheduleName(), reportSchedule.getRequestId());

            try
            {
                scheduler.scheduleJob(jobDetail, trigger);
            }
            catch(SchedulerException e)
            {
                throw new ProviderException(e);
            }
		}		
	}

	public List<ReportSchedule> getScheduledReports(ReportUser reportUser) throws ProviderException
	{
        
		List<ReportSchedule> scheduledReports = new ArrayList<ReportSchedule>();
        
        try
        {
    		String group = reportUser.getId().toString();
    
    		String[] jobNames = scheduler.getJobNames(group);
    
    		for (int i = 0; i < jobNames.length; i++)
    		{
    			try
    			{
    				JobDetail jobDetail = scheduler.getJobDetail(jobNames[i], group);
    
    				ReportSchedule reportSchedule = (ReportSchedule) jobDetail.getJobDataMap().get(
    						ORStatics.REPORT_SCHEDULE);
    				reportSchedule.setScheduleDescription(jobDetail.getDescription());
    				reportSchedule.setScheduleState(getTriggerStateName(jobNames[i], group));
    				
    				Trigger trigger = scheduler.getTrigger(jobNames[i], group);
    				if (trigger != null)
    				{
    					reportSchedule.setNextFireDate(trigger.getNextFireTime());					
    				}
    
    				scheduledReports.add(reportSchedule);
    			}
    			catch(ProviderException pe)
    			{
    				log.error(group + " | " + jobNames[i] + " | " + pe.toString());
    			}
    		}		
        }
        catch(SchedulerException e)
        {
            throw new ProviderException(e);
        }

		return scheduledReports;
	}

	public void deleteScheduledReport(ReportUser reportUser, String name)
			throws ProviderException
	{
        try
        {
            String group = reportUser.getId().toString();
            scheduler.deleteJob(name, group);	
        }
        catch(SchedulerException e)
        {
            throw new ProviderException(e);
        }
	}

	public ReportSchedule getScheduledReport(ReportUser reportUser, String name)
			throws ProviderException
	{
        try
        {
    		String group = reportUser.getId().toString();
    
    		JobDetail jobDetail = scheduler.getJobDetail(name,	group);
    
    		ReportSchedule reportSchedule = (ReportSchedule) jobDetail.getJobDataMap().get(ORStatics.REPORT_SCHEDULE);
    		reportSchedule.setScheduleDescription(jobDetail.getDescription());
    		reportSchedule.setScheduleState(getTriggerStateName(name, group));
    				
    		return reportSchedule;
        }
        catch(SchedulerException e)
        {
            throw new ProviderException(e);
        }
	}
	
	public void pauseScheduledReport(ReportUser reportUser, String name)
			throws ProviderException
	{
        try
        {
            scheduler.pauseJob(name, reportUser.getId().toString());
        }
        catch(SchedulerException e)
        {
            throw new ProviderException(e);
        }
	}
	
	public void resumeScheduledReport(ReportUser reportUser, String name)
			throws ProviderException
	{
        try
        {
            scheduler.resumeJob(name, reportUser.getId().toString());
        }
        catch(SchedulerException e)
        {
            throw new ProviderException(e);
        }
	}	
    
    public void setScheduler(Scheduler scheduler)
    {
        this.scheduler = scheduler;
    }
    
    private String getTriggerStateName(String name, String group) throws ProviderException
    {
        int state = -1;
        
        try
        {
            state = scheduler.getTriggerState(name, group);
        }
        catch(SchedulerException e)
        {
            throw new ProviderException(e);
        }
        
        switch (state)
        {
            case Trigger.STATE_BLOCKED:
                return "Blocked";
            
            case Trigger.STATE_COMPLETE:
                return "Complete";
            
            case Trigger.STATE_ERROR:
                return "ERROR";

            case Trigger.STATE_NORMAL:
                return "Normal";

            case Trigger.STATE_PAUSED:
                return "Paused";

            default:
                return "";
        }
    }
}