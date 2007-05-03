/*
 * Copyright (C) 2002 Erik Swenson - erik@oreports.com
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

package org.efs.openreports.providers;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.efs.openreports.objects.ORProperty;

public class DateProvider 
{
	protected static Logger log =
		Logger.getLogger(DateProvider.class.getName());

	protected SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

	private PropertiesProvider propertiesProvider;
	
	public DateProvider(PropertiesProvider propertiesProvider) throws ProviderException
	{
		this.propertiesProvider = propertiesProvider;			
		init();
	}	
	
	protected void init() throws ProviderException
	{
		String dateFormat = "MM/dd/yyyy";

		ORProperty property = propertiesProvider.getProperty(ORProperty.DATE_FORMAT);		
		if (property != null && property.getValue() != null
				&& property.getValue().trim().length() > 0)
		{
			dateFormat = property.getValue(); 
		}

		setDateFormat(dateFormat);

		log.info("DateFormat: " + dateFormat);
		log.info("Created");
	}
	
	public SimpleDateFormat getDateFormat()
	{
		return dateFormat;
	}

	public void setDateFormat(String format)
	{
		dateFormat = new SimpleDateFormat(format);
	}

	public Date parseDate(String date) throws ProviderException
	{
		try
		{
			return dateFormat.parse(date);
		}
		catch (Exception e)
		{
			throw new ProviderException("Use " + dateFormat.toPattern());
		}
	}
	
	public String formatDate(Date date) 
	{		
		return dateFormat.format(date);		
	}	

	public void setPropertiesProvider(PropertiesProvider propertiesProvider)
	{
		this.propertiesProvider = propertiesProvider;
	}

}