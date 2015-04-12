/*
 * Copyright (C) 2002 Erik Swenson - erik@oreports.com
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
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

import org.apache.struts2.interceptor.SessionAware;
import org.efs.openreports.ORStatics;
import org.efs.openreports.objects.ORTag;
import org.efs.openreports.objects.Report;
import org.efs.openreports.objects.ReportGroup;
import org.efs.openreports.objects.ReportUser;
import org.efs.openreports.providers.*;

public class EditGroupAction extends ActionSupport  implements SessionAware
{	
	private static final long serialVersionUID = 139626346269860245L;
	
	private String command;
	private boolean submitOk;	
	private boolean submitDuplicate;

	private Map<Object, Object> session;
	
	private int id;
	private String name;
	private String description;
    private String tags;
    private String tagList;

	private ReportGroup reportGroup;
	private int[] reportIds;

	private GroupProvider groupProvider;
	private ReportProvider reportProvider;
	private UserProvider userProvider;
    private TagProvider tagProvider;

	public String execute()
	{
		try
		{
			if (command.equals("edit"))
			{
				reportGroup = groupProvider.getReportGroup(new Integer(id));
			}
			else
			{
				reportGroup = new ReportGroup();
			}

			if (command.equals("edit") && !submitOk && !submitDuplicate)
			{
				name = reportGroup.getName();
				description = reportGroup.getDescription();
                tags = tagProvider.getTagsForObject(reportGroup.getId(), ReportGroup.class, ORTag.TAG_TYPE_UI);                
                reportIds = null;                  
			}

            tagList = tagProvider.getTagList(ReportGroup.class, ORTag.TAG_TYPE_UI);
            
			if (!submitOk && !submitDuplicate)	return INPUT;
			
			 if (submitDuplicate)
	            {
	            	command = "add";
	            	reportGroup.setId(null);
	            	
	            	if (reportGroup.getName().equals(name))
	            	{
	            		name = "Copy of ".concat(name);
	            	}
	            }

			reportGroup.setName(name);
			reportGroup.setDescription(description);
			reportGroup.setReports(convertIdsToReports(reportIds));
            
			if (command.equals("edit"))
			{
				groupProvider.updateReportGroup(reportGroup);               
			}

			if (command.equals("add"))
			{
				reportGroup = groupProvider.insertReportGroup(reportGroup);
			}
            
            // save tags
            tagProvider.setTags(reportGroup.getId(), ReportGroup.class, tags, ORTag.TAG_TYPE_UI);
			
			// refresh current user 
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
			addActionError(e.toString());
			return INPUT;
		}
	}
	
	@SuppressWarnings("unchecked")
	public void setSession(Map session) 
	{
		this.session = session;
	}
    
    public String getTagList()
    {
        return tagList;
    }
    
    public String getTags(Integer reportId)
    {
        try
        {
            return tagProvider.getTagsForObject(reportId, Report.class, ORTag.TAG_TYPE_UI);           
        }
        catch(Exception e)
        {
            addActionError(e.toString());
            return null;
        }
    }

	public String getCommand()
	{
		return command;
	}

	public String getDescription()
	{
		return description;
	}

	public String getName()
	{
		return name;
	}

	public void setCommand(String command)
	{
		this.command = command;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public void setSubmitOk(String submitOk)
	{
		if (submitOk != null) this.submitOk = true;
	}	
	
	public void setSubmitDuplicate(String submitDuplicate)
	{
		if (submitDuplicate != null) this.submitDuplicate = true;
	}

	public List getReports()
	{
		try
		{
			return reportProvider.getReports();
		}
		catch (Exception e)
		{
			addActionError(e.getMessage());
			return null;
		}
	}

	public List getReportsInGroup()
	{  
        if (reportGroup.getReports() != null)
        {
            Collections.sort(reportGroup.getReports());
        }
        return reportGroup.getReports();
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public int[] getReportIds()
	{
		return reportIds;
	}

	public void setReportIds(int[] reportIds)
	{
		this.reportIds = reportIds;
	}

	private List<Report> convertIdsToReports(int[] ids)
	{
		if (ids == null)
			return null;

		List<Report> reports = new ArrayList<Report>();

		try
		{
			for (int i = 0; i < ids.length; i++)
			{
				Report report = reportProvider.getReport(new Integer(ids[i]));
				reports.add(report);
			}
		}
		catch (Exception e)
		{
			addActionError(e.toString());
		}

		return reports;
	}
    
	public String getTags() 
    {
	    return tags;
    }
        
	public void setTags(String tags)
    {
	    this.tags = tags;
    }

	public void setGroupProvider(GroupProvider groupProvider)
	{
		this.groupProvider = groupProvider;
	}

	public void setReportProvider(ReportProvider reportProvider)
	{
		this.reportProvider = reportProvider;
	}

	public void setUserProvider(UserProvider userProvider)
	{
		this.userProvider = userProvider;
	}
    
    public void setTagProvider(TagProvider tagProvider) 
    {
        this.tagProvider = tagProvider;
    }

}