/*
 * Copyright (C) 2004 Erik Swenson - erik@oreports.com
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

public class ReportParameterMap implements Comparable<ReportParameterMap>, Serializable
{	
	private static final long serialVersionUID = -1062909233052733241L;
	
	private Report report;
	private ReportParameter reportParameter;
	private boolean required;
	private int sortOrder;
	private int step;

	public Report getReport()
	{
		return report;
		
	}

	public void setReport(Report report)
	{
		this.report = report;
	}

	public ReportParameter getReportParameter()
	{
		return reportParameter;
	}

	public void setReportParameter(ReportParameter reportParameter)
	{
		this.reportParameter = reportParameter;
	}

	public boolean isRequired()
	{
		return required;
	}

	public void setRequired(boolean required)
	{
		this.required = required;
	}

	public int getSortOrder()
	{
		return sortOrder;
	}

	public void setSortOrder(int sortOrder)
	{
		this.sortOrder = sortOrder;
	}	
	
	public int compareTo(ReportParameterMap rpMap)
	{		
		// sort by step and then sortOrder
		
		int compare = new Integer(step).compareTo(new Integer(rpMap.getStep()));
		
		if (compare == 0)
		{
			compare = new Integer(sortOrder).compareTo(new Integer(rpMap.getSortOrder()));
		}
		
		return compare;
	}
	
	public int getStep()
	{
		return step;
	}

	public void setStep(int step)
	{
		this.step = step;
	}

}