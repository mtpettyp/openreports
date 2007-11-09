/*
 * Copyright (C) 2003 Erik Swenson - erik@oreports.com
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

package org.efs.openreports.objects;

import java.io.Serializable;
import java.util.*;

public class ReportSchedule implements Serializable
{
	private static final long serialVersionUID = -679355847466582944l;
	
	public static final int ONCE = 0;
	public static final int DAILY = 1;
	public static final int WEEKLY = 2;
	public static final int MONTHLY = 3;
	public static final int WEEKDAYS = 4;
	public static final int HOURLY = 5;
	public static final int CRON = 6;
	
	private ReportUser user;
	private Report report;
	private ReportUserAlert alert;
	private Map<String,Object> reportParameters;
	private Date startDate;		
	private String startHour;
	private String startMinute;
	private String startAmPm;
	private int scheduleType;	
	private int exportType;
	private int hours;
	private String cronExpression;
	
	private String recipients;
	
	private String scheduleName;
    
    private String xmlInput;
    private String[] deliveryMethods;
    private String deliveryReturnAddress;
    private String requestId;
    private int schedulePriority = 5;
    
    private Locale locale;
	
	private transient String scheduleDescription;
	private transient Date nextFireDate;
	private transient String scheduleState;
	
	public ReportSchedule()
	{
		
	}

	public Report getReport()
	{
		return report;
	}

	public void setReport(Report report)
	{
		this.report = report;
	}

	public Map<String,Object> getReportParameters()
	{
		return reportParameters;
	}

	public void setReportParameters(Map<String,Object> reportParameters)
	{
		this.reportParameters = reportParameters;
	}

	public int getScheduleType()
	{
		return scheduleType;
	}
	
	public String getScheduleTypeName()
	{
		switch (scheduleType)
		{
			case ONCE:
				return "Once";
			case DAILY:
				return "Daily";
			case WEEKLY:
				return "Weekly";
			case MONTHLY:
				return "Monthly";
			case WEEKDAYS:
				return "Weekdays";
			case HOURLY:
				return "Hourly";
			case CRON:
				return "Cron";
			default:
				return "Unknown";
		}		
	}	

	public void setScheduleType(int scheduleType)
	{
		this.scheduleType = scheduleType;
	}

	public Date getStartDate()
	{
		return startDate;
	}

	public void setStartDate(Date startDate)
	{
		this.startDate = startDate;
	}

	public ReportUser getUser()
	{
		return user;
	}

	public void setUser(ReportUser user)
	{
		this.user = user;
	}

	public String getStartAmPm()
	{
		return startAmPm;
	}

	public void setStartAmPm(String startAmPm)
	{
		this.startAmPm = startAmPm;
	}

	public String getStartHour()
	{
		return startHour;
	}

	public void setStartHour(String startHour)
	{
		this.startHour = startHour;
	}
	
	public int getAbsoluteStartHour()
	{
		int hour = Integer.parseInt(startHour);
		
		if (startAmPm.equalsIgnoreCase("PM") && hour != 12)
			hour += 12;
		
		if (startAmPm.equalsIgnoreCase("AM") && hour == 12)
			hour -= 12;
		
		return hour;
	}
	
	public int getAbsoluteEndHour()
	{		
		return getAbsoluteStartHour() + hours;
	}
	
	public int getDayOfWeek()
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startDate);
		
		return calendar.get(Calendar.DAY_OF_WEEK);
	}
	
	public int getDayOfMonth()
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startDate);
		
		return calendar.get(Calendar.DAY_OF_MONTH);
	}
	

	public String getStartMinute()
	{
		return startMinute;
	}

	public void setStartMinute(String startMinute)
	{
		this.startMinute = startMinute;
	}
	
	public String getScheduleName()
	{
		return scheduleName;
	}
	
	public void setScheduleName(String scheduleName)
	{
		this.scheduleName = scheduleName;
	}
	
	public String getScheduleGroup()
	{
		return String.valueOf(user.getId());
	}
	
	public Date getStartDateTime()
	{
		Calendar calendar = Calendar.getInstance();
		
		if (startDate != null)
		{
			calendar.setTime(startDate);
			calendar.set(Calendar.HOUR_OF_DAY, getAbsoluteStartHour());
			calendar.set(Calendar.MINUTE, Integer.parseInt(startMinute));
		}
		
		return calendar.getTime();
	}

	public String getRecipients()
	{
		return recipients;
	}

	public void setRecipients(String recipients)
	{
		this.recipients = recipients;
	}
	
	public int getExportType()
	{
		return exportType;
	}

	public void setExportType(int exportType)
	{
		this.exportType = exportType;
	}
	
	public Date getNextFireDate()
	{
		return nextFireDate;
	}
	
	public void setNextFireDate(Date nextFireDate)
	{
		this.nextFireDate = nextFireDate;
	}

	public ReportUserAlert getAlert()
	{
		return alert;
	}

	public void setAlert(ReportUserAlert alert)
	{
		this.alert = alert;
	}

	public String getScheduleDescription()
	{
		return scheduleDescription;
	}

	public void setScheduleDescription(String scheduleDescription)
	{
		this.scheduleDescription = scheduleDescription;
	}

	public String getScheduleState()
	{
		return scheduleState;
	}

	public void setScheduleState(String scheduleState)
	{
		this.scheduleState = scheduleState;
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
    
    public String[] getDeliveryMethods() 
    {
        return deliveryMethods;
    }
    
    public void setDeliveryMethods(String[] deliveryMethods) 
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
    
    public String getDeliveryReturnAddress()
    {
        return deliveryReturnAddress;
    }
    
    public void setDeliveryReturnAddress(String deliveryReturnAddress)
    {
        this.deliveryReturnAddress = deliveryReturnAddress;
    }
    
    public String getRequestId() 
    {
        return requestId;
    }
    
    public void setRequestId(String requestId) 
    {
        this.requestId = requestId;
    }
    
    public int getSchedulePriority() 
    {
        return schedulePriority;
    }
    
    public void setSchedulePriority(int schedulePriority) 
    {
        this.schedulePriority = schedulePriority;
    }

	public Locale getLocale() 
	{
		return locale;
	}

	public void setLocale(Locale locale) 
	{
		this.locale = locale;
	}       
}