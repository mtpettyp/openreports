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

package org.efs.openreports.services.info;

import java.io.Serializable;
import java.util.*;

/**
 * ReportScheduleInfo object. 
 *  
 * @author Erik Swenson
 * @see AlertInfo
 * 
 */

public class ReportScheduleInfo implements Serializable
{		
	private static final long serialVersionUID = 2130467571173388494L;
	
	private String reportName;
	private AlertInfo alert;	
	private String scheduleTypeName;			
	private String scheduleDescription;
	private Date nextFireDate;
	
	public ReportScheduleInfo()
	{
		
	}	
	
	public AlertInfo getAlert()
	{
		return alert;
	}

	public void setAlert(AlertInfo alert)
	{
		this.alert = alert;
	}

	public String getReportName()
	{
		return reportName;
	}

	public void setReportName(String reportName)
	{
		this.reportName = reportName;
	}

	public String getScheduleTypeName()
	{
		return scheduleTypeName;
	}

	public void setScheduleTypeName(String scheduleTypeName)
	{
		this.scheduleTypeName = scheduleTypeName;
	}

	public Date getNextFireDate()
	{
		return nextFireDate;
	}
	
	public void setNextFireDate(Date nextFireDate)
	{
		this.nextFireDate = nextFireDate;
	}	

	public String getScheduleDescription()
	{
		return scheduleDescription;
	}

	public void setScheduleDescription(String scheduleDescription)
	{
		this.scheduleDescription = scheduleDescription;
	}	
}