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

import java.util.*;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;
import org.efs.openreports.ORStatics;
import org.efs.openreports.objects.*;
import org.efs.openreports.providers.*;

public class EditReportGroupsAction	extends ActionSupport implements SessionAware
{	
	private static final long serialVersionUID = 949119678219163746L;

	protected static Logger log = Logger.getLogger(EditReportGroupsAction.class);
	
	private Map<Object,Object> session;

	private int id;
	private int[] groupIds = new int[0];

	private String submitType;

	private Report report;
	
	private List groups;
	private List groupsForReport;

	private ReportProvider reportProvider;
	private GroupProvider groupProvider;
	private UserProvider userProvider;

	public String execute()
	{
		try
		{
			report = reportProvider.getReport(new Integer(id));
			groupsForReport = groupProvider.getGroupsForReport(report);
			groups = groupProvider.getReportGroups();			

			if (submitType == null) return INPUT;		
			
			Iterator iterator = groups.iterator();
			while(iterator.hasNext())
			{
				ReportGroup group = (ReportGroup) iterator.next();

				boolean reportInGroup = false;
				
				for (int i = 0; i < groupIds.length; i++)
				{
					if (group.getId().equals(new Integer(groupIds[i])))
					{
						reportInGroup = true;
						
						if (!group.isValidReport(report))
						{
							group.getReports().add(report);
							groupProvider.updateReportGroup(group);
						}
					}
				}
				
				if (!reportInGroup)
				{					
					for (int i=0; i < group.getReports().size(); i++)
					{					
						Report groupReport = (Report) group.getReports().get(i);
						if (groupReport.getId().equals(report.getId()))
						{
							group.getReports().remove(groupReport);	
							groupProvider.updateReportGroup(group);	
							
							i=0;
						}
					}						
				}
			}	
			
			//refresh current user
			ReportUser user = (ReportUser) ActionContext.getContext().getSession().get(ORStatics.REPORT_USER);		
			if (user != null)
			{
				user = userProvider.getUser(user.getId());
				session.put(ORStatics.REPORT_USER, user);
			}

			return SUCCESS;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			addActionError(e.toString());
			return INPUT;
		}
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public List getGroupsForReport()
	{
		return groupsForReport;
	}

	public List getGroups()
	{
		return groups;
	}		

	public void setReportProvider(ReportProvider reportProvider)
	{
		this.reportProvider = reportProvider;
	}	

	public Report getReport()
	{
		return report;
	}

	public void setReport(Report report)
	{
		this.report = report;
	}	
	
	public void setGroupProvider(GroupProvider groupProvider)
	{
		this.groupProvider = groupProvider;		
	}

	public void setUserProvider(UserProvider userProvider)
	{
		this.userProvider = userProvider;
	}

	public int[] getGroupIds()
	{
		return groupIds;
	}

	public void setGroupIds(int[] groupIds)
	{
		this.groupIds = groupIds;
	}

	public String getSubmitType()
	{
		return submitType;
	}

	public void setSubmitType(String submitType)
	{
		this.submitType = submitType;
	}

	@SuppressWarnings("unchecked")
	public void setSession(Map session)
	{
		this.session = session;
	}
	
}