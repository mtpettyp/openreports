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

/**
 * AlertInfo object. 
 *  
 * @author Erik Swenson
 * 
 */

public class AlertInfo implements Serializable
{	
	private static final long serialVersionUID = 7015514094862252982L;
	
	private String alertName;
	private String condition;
	private int count;
	private boolean triggered;
	private String reportName;
	private String message;
	
	public String getAlertName()
	{
		return alertName;
	}
	
	public void setAlertName(String alertName)
	{
		this.alertName = alertName;
	}
	
	public String getCondition()
	{
		return condition;
	}
	
	public void setCondition(String condition)
	{
		this.condition = condition;
	}
	
	public int getCount()
	{
		return count;
	}
	
	public void setCount(int count)
	{
		this.count = count;
	}
	
	public String getReportName()
	{
		return reportName;
	}
	
	public void setReportName(String reportName)
	{
		this.reportName = reportName;
	}	

	public boolean isTriggered()
	{
		return triggered;
	}

	public void setTriggered(boolean triggered)
	{
		this.triggered = triggered;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}
}
