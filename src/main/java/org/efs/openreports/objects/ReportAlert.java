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

public class ReportAlert implements Serializable, Comparable<ReportAlert>
{		
	private static final long serialVersionUID = 6594082161499885218L;
	
	public static final String OPERATOR_EQUAL = "=";
	public static final String OPERATOR_GREATER = ">";
	public static final String OPERATOR_LESS = "<";	
	
	private Integer id;
	private String name;
	private String description;
	private String query;		
	
	private ReportDataSource dataSource;

	public ReportAlert()
	{
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public String toString()
	{
		return name;
	}

	public String getDescription()
	{
		return description;
	}

	public Integer getId()
	{
		return id;
	}

	public String getName()
	{
		return name;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public int compareTo(ReportAlert reportAlert)
	{		
		return name.compareTo(reportAlert.getName());
	}

	public ReportDataSource getDataSource()
	{
		return dataSource;
	}

	public void setDataSource(ReportDataSource dataSource)
	{
		this.dataSource = dataSource;
	}

	public String getQuery()
	{
		return query;
	}

	public void setQuery(String query)
	{
		this.query = query;
	}	
	
	public boolean equals(Object o)
	{
		ReportAlert alert = (ReportAlert) o;		
		return id.equals(alert.getId());
	}
}