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
import java.util.Date;

import org.efs.openreports.ReportConstants;
import org.efs.openreports.ReportConstants.DeliveryMethod;
import org.efs.openreports.ReportConstants.ExportType;
import org.efs.openreports.ReportConstants.ScheduleAmPm;
import org.efs.openreports.ReportConstants.ScheduleType;

/**
 * ReportService input object contains information used to identify the user, report,
 * delivery method, and export type of the request along with optional xml input, 
 * parameter and scheduling information. 
 * 
 * @author Erik Swenson
 * @see ReportConstants
 */

public class ReportServiceInput implements Serializable
{	
	private static final long serialVersionUID = -3094443722330870862L;		
	
	/** unique id of request  */
	private String requestId;
	
    /** identifies the user making the request */
	private UserInput user;  
    
    /** name of report */
	private String reportName;	
	
    /** exportType for the report. defaults to PDF */
	private ExportType exportType = ExportType.PDF;
    
    /** list of delivery methods for the report */
	private DeliveryMethod[] deliveryMethods;	
    
    /** delivery address for report */
    private String deliveryAddress;
    
    /** return or bounce address for the report */
    private String deliveryReturnAddress;
    
    /** parameters passed to the report */
	private ParameterInput[] parameters;  
    
    /** xml data for report generation */
    private String xmlInput;
   	
    /** start date for scheduled report */
	private Date startDate;		
    
    /** start hour for scheduled report */
	private String startHour;
    
    /** start minute for scheduled report */
	private String startMinute;
    
    /** am/pm indicator for scheduled report */
	private ScheduleAmPm startAmPm;
    
    /** schedule type (once, daily, weekly, etc.). defaulted to once */
	private ScheduleType scheduleType = ScheduleType.ONCE;
    
    /** sets priority of scheduled report. defaulted to five */
    private int schedulePriority = 5;
    
    /** description of scheduled report */
	private String scheduleDescription;
    
    /** number of hours to run hourly schedule report */
	private int hours;	
    
    /** cron expression for cron schedule report */
	private String cronExpression;	
	
	/** locale for the report in standard format, for example: "en_US" */
	private String locale;
			
	public String getRequestId() 
	{
		return requestId;
	}

	public void setRequestId(String requestId)
	{
		this.requestId = requestId;
	}

	public UserInput getUser()
	{
		return user;
	}

	public void setUser(UserInput user)
	{
		this.user = user;
	}

	public DeliveryMethod[] getDeliveryMethods()
	{
		return deliveryMethods;
	}
	
	public void setDeliveryMethods(DeliveryMethod... deliveryMethods)
	{
		this.deliveryMethods = deliveryMethods;
	}	
    
    public boolean isDeliveryMethodSelected(String deliveryMethod)
    {
        if (deliveryMethods == null) return false;
        
        for (int i=0; i < deliveryMethods.length; i++)
        {
            if (deliveryMethods[i].equals(deliveryMethod)) return true;
        }
            
        return false;
    }

	public ExportType getExportType()
	{
		return exportType;
	}

	public void setExportType(ExportType exportType)
	{
		this.exportType = exportType;
	}
	
    public ParameterInput[] getParameters()
    {
        return parameters;
    }

    public void setParameters(ParameterInput... parameters)
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

    public ScheduleType getScheduleType()
	{
		return scheduleType;
	}

	public void setScheduleType(ScheduleType scheduleType)
	{
		this.scheduleType = scheduleType;
	}

	public ScheduleAmPm getStartAmPm()
	{
		return startAmPm;
	}

	public void setStartAmPm(ScheduleAmPm startAmPm)
	{
		this.startAmPm = startAmPm;
	}

	public Date getStartDate()
	{
		return startDate;
	}

	public void setStartDate(Date startDate)
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
    
    public String getXmlInput() 
    {
        return xmlInput;
    }
    
    public void setXmlInput(String xmlInput) 
    {
        this.xmlInput = xmlInput;
    }
    
    public String getDeliveryAddress() 
    {
        return deliveryAddress;
    }
    
    public void setDeliveryAddress(String deliveryAddress) 
    {
        this.deliveryAddress = deliveryAddress;
    }
    
    public String getDeliveryReturnAddress()
    {
        return deliveryReturnAddress;
    }
    
    public void setDeliveryReturnAddress(String deliveryReturnAddress)
    {
        this.deliveryReturnAddress = deliveryReturnAddress;
    }
    
    public int getSchedulePriority() 
    {
        return schedulePriority;
    }
    
    public void setSchedulePriority(int schedulePriority) 
    {
        this.schedulePriority = schedulePriority;
    }

	public String getLocale()
	{
		return locale;
	}

	public void setLocale(String locale) 
	{
		this.locale = locale;
	}	    
}
