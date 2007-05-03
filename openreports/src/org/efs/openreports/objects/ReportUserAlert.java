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

package org.efs.openreports.objects;

import java.io.Serializable;

public class ReportUserAlert implements Comparable, Serializable
{		
	private static final long serialVersionUID = 6833718935442336961L;
	
	private ReportUser user;
	private ReportAlert alert;
	private Report report;
	private int limit;
	private String operator;	
	
	private int count;	
	
	public String getCondition()
	{
		return getOperator() + " " + limit;
	}
	
	public ReportAlert getAlert()
	{
		return alert;
	}

	public void setAlert(ReportAlert alert)
	{
		this.alert = alert;
	}

	public int getLimit()
	{
		return limit;
	}

	public void setLimit(int limit)
	{
		this.limit = limit;
	}

	public String getOperator()
	{
		if (operator == null || operator.trim().length() < 1) return ReportAlert.OPERATOR_EQUAL;		
		return operator;
	}

	public void setOperator(String operator)
	{
		this.operator = operator;
	}

	public ReportUser getUser()
	{
		return user;
	}

	public void setUser(ReportUser user)
	{
		this.user = user;
	}

	public int compareTo(Object object)
	{
		ReportUserAlert map = (ReportUserAlert) object;		
		return alert.getName().compareTo(map.getAlert().getName());
	}

	public int getCount()
	{
		return count;
	}

	public void setCount(int count)
	{
		this.count = count;
	}

	public boolean isTriggered()
	{
		if ("=".equals(operator))
		{
			if (limit == count) return true;
		}
		else if (">".equals(operator))
		{
			if (count > limit) return true;
		}
		else if ("<".equals(operator))
		{
			if (count < limit) return true;
		}
		
		return false;
	}

	public Report getReport()
	{
		return report;
	}

	public void setReport(Report report)
	{
		this.report = report;
	}	
}