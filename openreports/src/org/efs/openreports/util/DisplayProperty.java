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

package org.efs.openreports.util;

import java.util.StringTokenizer;

public class DisplayProperty
{
	private String name;
	private String displayName;
	private String decorator;
	
	public DisplayProperty(String name, String type)
	{
		this.name = name;		
		this.displayName = "";
				
		StringTokenizer st = new StringTokenizer(name, " ");		
		while (st.hasMoreElements())
		{
			String element = (String) st.nextElement();
			displayName = displayName + element.substring(0,1).toUpperCase() + element.substring(1) + " ";
		}
		
		if (type.equals("java.sql.Date") || type.equals("java.sql.Timestamp"))
		{			
			decorator = "org.efs.openreports.util.DateColumnDecorator";
		}		
	}

	public String getName()
	{
		return name;
	}
	
	public String getDisplayName()
	{		
		return displayName;
	}	
	
	public String getDecorator()
	{
		return decorator;
	}
}
