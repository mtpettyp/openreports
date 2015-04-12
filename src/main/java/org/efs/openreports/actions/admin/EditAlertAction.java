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

import java.util.List;

import org.apache.log4j.Logger;
import org.efs.openreports.objects.ReportAlert;
import org.efs.openreports.objects.ReportParameter;
import org.efs.openreports.providers.AlertProvider;
import org.efs.openreports.providers.DataSourceProvider;

import com.opensymphony.xwork2.ActionSupport;

public class EditAlertAction extends ActionSupport
{	
    private static final long serialVersionUID = 1322652952771381074L;

    protected static Logger log = Logger.getLogger(EditAlertAction.class);

	private String command;
	
	private boolean submitOk;

	private int id;
	private String name;
	private String description;
	private String query;
	private int dataSourceId = Integer.MIN_VALUE;	

	private ReportAlert reportAlert;		

	private DataSourceProvider dataSourceProvider;
	private AlertProvider alertProvider;
	
	public String execute()
	{
		try
		{
			if (command.equals("edit"))
			{
				reportAlert =
					alertProvider.getReportAlert(new Integer(id));
			}
			else
			{
				reportAlert = new ReportAlert();
			}

			if (command.equals("edit") && !submitOk)
			{
				name = reportAlert.getName();
				description = reportAlert.getDescription();
				query = reportAlert.getQuery();				
				id = reportAlert.getId().intValue();
				if (reportAlert.getDataSource() != null)
				{
					dataSourceId =
						reportAlert.getDataSource().getId().intValue();
				}			
			}

			if (!submitOk) return INPUT;

			reportAlert.setName(name);
			reportAlert.setDescription(description);
			reportAlert.setQuery(query);				
			if (dataSourceId != -1)
			{
				reportAlert.setDataSource(dataSourceProvider.getDataSource(
						new Integer(dataSourceId)));	
			}				

			if (command.equals("edit"))
			{
				alertProvider.updateReportAlert(reportAlert);
			}

			if (command.equals("add"))
			{
				alertProvider.insertReportAlert(reportAlert);
			}

			return SUCCESS;
		}
		catch (Exception e)
		{
			addActionError(e.getMessage());
			return INPUT;
		}
	}

	public String getCommand()
	{
		return command;
	}

	public void setCommand(String command)
	{
		this.command = command;
	}	
	
	public void setSubmitOk(String submitOk)
	{
		if (submitOk != null) this.submitOk = true;
	}	
	
	public int getDataSourceId()
	{
		return dataSourceId;
	}

	public String getName()
	{
		return name;
	}

	public void setDataSourceId(int dataSourceId)
	{
		this.dataSourceId = dataSourceId;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public List getDataSources()
	{
		try
		{
			return dataSourceProvider.getDataSources();
		}
		catch (Exception e)
		{
			addActionError(e.getMessage());
			return null;
		}
	}

	public String[] getTypes()
	{
		return ReportParameter.TYPES;
	}

	public String[] getClassNames()
	{
		return ReportParameter.CLASS_NAMES;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}	

	public void setDataSourceProvider(DataSourceProvider dataSourceProvider)
	{
		this.dataSourceProvider = dataSourceProvider;
	}	

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}		

	public String getQuery()
	{
		return query;
	}

	public void setQuery(String query)
	{
		this.query = query;
	}

	public void setAlertProvider(AlertProvider alertProvider)
	{
		this.alertProvider = alertProvider;
	}
}