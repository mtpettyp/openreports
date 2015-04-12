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

package org.efs.openreports.util.displaytag;

import javax.servlet.jsp.PageContext;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.displaytag.decorator.DefaultDecoratorFactory;
import org.displaytag.decorator.DisplaytagColumnDecorator;
import org.displaytag.decorator.TableDecorator;
import org.displaytag.exception.DecoratorInstantiationException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class SpringDecoratorFactory extends DefaultDecoratorFactory
{	
	protected static Logger log = Logger.getLogger(SpringDecoratorFactory.class.getName());
	
	@Override
	public DisplaytagColumnDecorator loadColumnDecorator(PageContext pageContext, String name) throws DecoratorInstantiationException 
	{
		if (StringUtils.isBlank(name)) return null;
		  
		ApplicationContext appContext = WebApplicationContextUtils
			.getRequiredWebApplicationContext(pageContext.getServletContext());
		
		Object decorator = null;
		
		try
		{
			decorator =  appContext.getBean(name, DisplaytagColumnDecorator.class);
		}
		catch(NoSuchBeanDefinitionException e)
		{
			log.debug("Decorator " + name + " not found in Spring ApplicationContext. Using DefaultDecoratorFactory.loadTableDecorator. ");
		}
		
		if (decorator != null && decorator instanceof DisplaytagColumnDecorator) return (DisplaytagColumnDecorator) decorator;
		
		return super.loadColumnDecorator(pageContext, name);		
	}
	
	@Override
	public TableDecorator loadTableDecorator(PageContext pageContext, String name) throws DecoratorInstantiationException 
	{
		if (StringUtils.isBlank(name)) return null;
		  
		ApplicationContext appContext = WebApplicationContextUtils
			.getRequiredWebApplicationContext(pageContext.getServletContext());
		
		Object decorator = null;
		
		try
		{
			decorator =  appContext.getBean(name, TableDecorator.class);
		}
		catch(NoSuchBeanDefinitionException e)
		{
			log.debug("Decorator " + name + " not found in Spring ApplicationContext. Using DefaultDecoratorFactory.loadTableDecorator. ");
		}
		
		if (decorator != null && decorator instanceof TableDecorator) return (TableDecorator) decorator;
		
		return super.loadTableDecorator(pageContext, name);		
	}

}
