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
import java.util.Date;
import java.util.List;

public class ReportLog implements Serializable
{	
	private static final long serialVersionUID = -7260204621599035553L;
	
	public static final String STATUS_SUCCESS = "success";
	public static final String STATUS_FAILURE = "failure";
	public static final String STATUS_EMPTY = "empty";
	public static final String STATUS_TRIGGERED = "triggered";
	public static final String STATUS_NOT_TRIGGERED = "not triggered";
	public static final String STATUS_DELIVERY_FAILURE = "delivery failure";
	
	private Integer id;
	private Report report;
	private Date startTime;
	private Date endTime;
	private String status;
	private String message;
	private ReportUser user;
	private ReportAlert alert;
    private Integer exportType; 
    private String requestId;
    
    private List<ReportDeliveryLog> deliveryLogs;   

	public ReportLog()
	{
	}	

	public ReportLog(ReportUser user, Report report, Date startTime)
	{
		this.user = user;
		this.report = report;
		this.startTime = startTime;
	}		
	
	public ReportLog(ReportUser user, ReportAlert alert, Date startTime)
	{
		this.user = user;
		this.alert = alert;
		this.startTime = startTime;
	}		
	
	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}	

	public Report getReport()
	{
		return report;
	}

	public void setReport(Report report)
	{
		this.report = report;
	}

	public Date getStartTime()
	{
		return startTime;
	}

	public void setStartTime(Date startTime)
	{
		this.startTime = startTime;
	}

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public Date getEndTime()
	{
		return endTime;
	}

	public void setEndTime(Date endTime)
	{
		this.endTime = endTime;
	}
	
	public long getElapsedTime()
	{
		if (endTime != null && startTime != null)
		{
			long elapsedTime = endTime.getTime() -	startTime.getTime();
			return elapsedTime / 1000;
		}
		else
		{
			return 0;
		}
		
		
	}

	public ReportUser getUser()
	{
		return user;
	}

	public void setUser(ReportUser user)
	{
		this.user = user;
	}

	public ReportAlert getAlert()
	{
		return alert;
	}

	public void setAlert(ReportAlert alert)
	{
		this.alert = alert;
	}       
    
    public List<ReportDeliveryLog> getDeliveryLogs()
    {
        return deliveryLogs;
    }
    
    public void setDeliveryLogs(List<ReportDeliveryLog> deliveryLogs) 
    {
        this.deliveryLogs = deliveryLogs;
    }

    public Integer getExportType() 
    {
        return exportType;
    }
    
    public void setExportType(Integer exportType) 
    {
        this.exportType = exportType;
    }

	public String getRequestId() 
	{
		return requestId;
	}

	public void setRequestId(String requestId)
	{
		this.requestId = requestId;
	}    
}