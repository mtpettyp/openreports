/*
 * Copyright (C) 2002 Erik Swenson - eswenson@opensourcesoft.net
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

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.jsp.PageContext;

import org.displaytag.decorator.DisplaytagColumnDecorator;
import org.displaytag.exception.DecoratorException;
import org.displaytag.properties.MediaTypeEnum;
import org.efs.openreports.providers.DateProvider;

public class DateColumnDecorator implements DisplaytagColumnDecorator 
{
	protected SimpleDateFormat dateFormat;		

	public Object decorate(Object object, PageContext pgeContext, MediaTypeEnum mediaTypeEnum) throws DecoratorException 
	{
		if (object == null)	return null;		
		return dateFormat.format((Date) object);
	}

	public void setDateProvider(DateProvider dateProvider) 
	{
		dateFormat = new SimpleDateFormat(dateProvider.getDateFormat().toPattern());
	}
}
