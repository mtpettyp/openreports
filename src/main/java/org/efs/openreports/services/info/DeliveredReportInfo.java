/*
 * Copyright (C) 2007 Erik Swenson - erik@oreports.com
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
import java.util.Date;

public class DeliveredReportInfo implements Serializable
{		
	private static final long serialVersionUID = 4367765215724788357L;
	
    private String requestId;
	private String userName;
	private String reportName;
	private String reportDescription;
	private String reportFileName;
    private String deliveryMethod;
	private Date runDate;	
	
    public String getRequestId() 
    {
        return requestId;
    }
    
    public void setRequestId(String requestId)
    {
        this.requestId = requestId;
    }

    public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}
	
	public String getReportDescription()
	{
		return reportDescription;
	}
	
	public void setReportDescription(String reportDescription)
	{
		this.reportDescription = reportDescription;
	}
	
	public String getReportName()
	{
		return reportName;
	}
	
	public void setReportName(String reportName)
	{
		this.reportName = reportName;
	}
	
	public Date getRunDate()
	{
		return runDate;
	}
	
	public void setRunDate(Date runDate)
	{
		this.runDate = runDate;
	}

	public String getReportFileName()
	{
		return reportFileName;
	}

	public void setReportFileName(String reportFileName)
	{
		this.reportFileName = reportFileName;
	}
    
    public String getDeliveryMethod() 
    {
        return deliveryMethod;
    }
    
    public void setDeliveryMethod(String deliveryMethod) 
    {
        this.deliveryMethod = deliveryMethod;
    }
}