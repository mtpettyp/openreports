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

package org.efs.openreports.services.input;

import java.io.Serializable;

import org.efs.openreports.services.ReportService;

/**
 * Standard ReportServiceInput object. UserInput and ReportName are required, the rest
 * are optional. If not specified, exportType defaults to PDF and deliveryMethod
 * defaults to API.
 * 
 * @author Erik Swenson
 */

public class ReportServiceInput implements Serializable
{	
	private static final long serialVersionUID = -3094443722330870862L;	
	
	private UserInput user;
	private String reportName;
	private int exportType = 0; // default to PDF
	private String deliveryMethod = ReportService.DELIVERY_API;	
	private ParameterInput[] parameters;  
   	
	// schedule options	
	private String startDate;		
	private String startHour;
	private String startMinute;
	private String startAmPm;
	private int scheduleType = 0; // default to once		
	private String scheduleDescription;
	private int hours;	
	private String cronExpression;
	// 	
	
	public UserInput getUser()
	{
		return user;
	}

	public void setUser(UserInput user)
	{
		this.user = user;
	}

	public String getDeliveryMethod()
	{
		return deliveryMethod;
	}
	
	public void setDeliveryMethod(String deliveryMethod)
	{
		this.deliveryMethod = deliveryMethod;
	}	

	public int getExportType()
	{
		return exportType;
	}

	public void setExportType(int exportType)
	{
		this.exportType = exportType;
	}
	
    public ParameterInput[] getParameters()
    {
        return parameters;
    }

    public void setParameters(ParameterInput[] parameters)
    {
        this.parameters = parameters;
    }   
	
	public String getReportName()
	{
		return reportName;
	}
	
	public void setReportName(String reportName)
	{
		this.reportName = reportName;
	}    

    public int getScheduleType()
	{
		return scheduleType;
	}

	public void setScheduleType(int scheduleType)
	{
		this.scheduleType = scheduleType;
	}

	public String getStartAmPm()
	{
		return startAmPm;
	}

	public void setStartAmPm(String startAmPm)
	{
		this.startAmPm = startAmPm;
	}

	public String getStartDate()
	{
		return startDate;
	}

	public void setStartDate(String startDate)
	{
		this.startDate = startDate;
	}

	public String getStartHour()
	{
		return startHour;
	}

	public void setStartHour(String startHour)
	{
		this.startHour = startHour;
	}

	public String getStartMinute()
	{
		return startMinute;
	}

	public void setStartMinute(String startMinute)
	{
		this.startMinute = startMinute;
	}

	public String getScheduleDescription()
	{
		return scheduleDescription;
	}

	public void setScheduleDescription(String scheduleDescription)
	{
		this.scheduleDescription = scheduleDescription;
	}

	public String getCronExpression()
	{
		return cronExpression;
	}

	public void setCronExpression(String cronExpression)
	{
		this.cronExpression = cronExpression;
	}

	public int getHours()
	{
		return hours;
	}

	public void setHours(int hours)
	{
		this.hours = hours;
	}		
}
