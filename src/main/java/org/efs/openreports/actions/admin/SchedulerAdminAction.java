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

package org.efs.openreports.actions.admin;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.efs.openreports.objects.ReportSchedule;
import org.efs.openreports.objects.ReportUser;
import org.efs.openreports.providers.ProviderException;
import org.efs.openreports.providers.SchedulerProvider;
import org.efs.openreports.providers.UserProvider;

import com.opensymphony.xwork2.ActionSupport;

public class SchedulerAdminAction extends ActionSupport
{	
    private static final long serialVersionUID = 5622428772719627736L;

    private List<ReportSchedule> scheduledReports;

	private SchedulerProvider schedulerProvider;
	private UserProvider userProvider;

	public String execute()
	{
		try
		{
			scheduledReports = new ArrayList<ReportSchedule>();
			
			List users = userProvider.getUsers();
			
			Iterator iterator = users.iterator();
			while(iterator.hasNext())
			{
				ReportUser user = (ReportUser) iterator.next();
				
				List<ReportSchedule> schedules = schedulerProvider.getScheduledReports(user);				
				
				scheduledReports.addAll(schedules);
			}				
		}
		catch (ProviderException pe)
		{
			addActionError(pe.getMessage());
			return ERROR;
		}

		return SUCCESS;
	}

	public void setSchedulerProvider(SchedulerProvider schedulerProvider)
	{
		this.schedulerProvider = schedulerProvider;
	}

	public void setUserProvider(UserProvider userProvider)
	{
		this.userProvider = userProvider;
	}

	public List getScheduledReports()
	{
		return scheduledReports;
	}

}