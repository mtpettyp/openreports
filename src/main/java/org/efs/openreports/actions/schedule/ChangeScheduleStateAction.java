/*
 * Copyright (C) 2006 Erik Swenson - erik@oreports.com
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

package org.efs.openreports.actions.schedule;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import org.efs.openreports.ORStatics;
import org.efs.openreports.objects.ReportSchedule;
import org.efs.openreports.objects.ReportUser;
import org.efs.openreports.providers.*;

public class ChangeScheduleStateAction extends ActionSupport
{	
    private static final long serialVersionUID = 4703811534879840963L;
    
    private SchedulerProvider schedulerProvider;
	private DateProvider dateProvider;
	private UserProvider userProvider;

	protected boolean submitOk;
	protected boolean submitCancel;
	
	private String scheduleName;
	private int userId = Integer.MIN_VALUE;
	
	private String name;
	private String description;
	private String startDate;
	private String scheduleTypeName;
	private String scheduleState;
	private String buttonName;
	
	public String execute()
	{
		try
		{
			ReportUser reportUser = null;
			
			if (userId >= 0)
			{
				reportUser = userProvider.getUser(new Integer(userId));
			}
			else
			{
				reportUser = (ReportUser) ActionContext.getContext().getSession().get(
					ORStatics.REPORT_USER);
			}

			ReportSchedule reportSchedule =
				schedulerProvider.getScheduledReport(reportUser, scheduleName);

			name = reportSchedule.getReport().getName();
			description = reportSchedule.getScheduleDescription();
			startDate = dateProvider.formatDate(reportSchedule.getStartDate());
			scheduleTypeName = reportSchedule.getScheduleTypeName();
			scheduleState = reportSchedule.getScheduleState();

			if (!submitOk && !submitCancel)
			{
				if (scheduleState.equals("Normal")) buttonName = "Pause";
				if (scheduleState.equals("Paused")) buttonName = "Resume";				
				
				return INPUT;
			}

			if (submitOk)
			{
				if (scheduleState.equals("Normal"))
				{
					schedulerProvider.pauseScheduledReport(reportUser, scheduleName);
				}
				
				if (scheduleState.equals("Paused"))
				{
					schedulerProvider.resumeScheduledReport(reportUser, scheduleName);
				}
			}
		}
		catch (Exception e)
		{			
			addActionError(e.getMessage());
			return INPUT;
		}

		return SUCCESS;
	}

	public void setSchedulerProvider(SchedulerProvider schedulerProvider)
	{
		this.schedulerProvider = schedulerProvider;
	}	
	
	public void setSubmitOk(String submitOk)
	{
		if (submitOk != null) this.submitOk = true;
	}
	
	public void setSubmitCancel(String submitCancel)
	{
		if (submitCancel != null) this.submitCancel = true;
	}

	public String getScheduleName()
	{
		return scheduleName;
	}

	public void setScheduleName(String scheduleName)
	{
		this.scheduleName = scheduleName;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getScheduleTypeName()
	{
		return scheduleTypeName;
	}

	public void setScheduleTypeName(String scheduleTypeName)
	{
		this.scheduleTypeName = scheduleTypeName;
	}

	public String getStartDate()
	{
		return startDate;
	}

	public void setStartDate(String startDate)
	{
		this.startDate = startDate;
	}

	public void setDateProvider(DateProvider dateProvider)
	{
		this.dateProvider = dateProvider;
	}

	public void setUserProvider(UserProvider userProvider)
	{
		this.userProvider = userProvider;		
	}
	
	public int getUserId()
	{
		return userId;
	}

	public void setUserId(int userId)
	{
		this.userId = userId;
	}

	public String getScheduleState()
	{
		return scheduleState;
	}

	public void setScheduleState(String scheduleState)
	{
		this.scheduleState = scheduleState;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getButtonName()
	{
		return buttonName;
	}
}