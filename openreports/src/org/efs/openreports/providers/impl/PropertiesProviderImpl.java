/*
 * Copyright (C) 2005 Erik Swenson - erik@oreports.com
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

package org.efs.openreports.providers.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.efs.openreports.objects.ORProperty;
import org.efs.openreports.providers.HibernateProvider;
import org.efs.openreports.providers.PropertiesProvider;
import org.efs.openreports.providers.ProviderException;
import org.efs.openreports.util.ConstraintException;
import org.hibernate.HibernateException;
import org.hibernate.Session;

public class PropertiesProviderImpl implements PropertiesProvider
{
	protected static Logger log = Logger.getLogger(PropertiesProviderImpl.class
			.getName());	

	private HibernateProvider hibernateProvider;
	
	public PropertiesProviderImpl(HibernateProvider hibernateProvider) throws ProviderException
	{
		this.hibernateProvider = hibernateProvider;

		log.info("PropertiesProviderImpl created");
	}	

	public void setProperty(String key, String value) throws ProviderException
	{
		ORProperty property = getProperty(key);
		
		if (property == null)
		{
			property = new ORProperty();
			property.setKey(key);
			property.setValue(value);
			
			insertProperty(property);
		}
		else
		{
			property.setValue(value);
			
			updateProperty(property);
		}
	}
	
	@SuppressWarnings("unchecked")
	public ORProperty getProperty(String key) throws ProviderException
	{
		try
		{
			Session session = hibernateProvider.openSession();			

			try
			{				
				List<ORProperty> list = session.createQuery(
						"from org.efs.openreports.objects.ORProperty as orProperty "
								+ "where orProperty.key = ?").setCacheable(true).setString(0, key).list();
					
				if (list.size() == 0)
					return null;

				ORProperty property = list.get(0);			

				return property;
			}
			catch (HibernateException he)
			{				
				throw he;
			}
			finally
			{
				session.close();
			}
		}
		catch (HibernateException he)
		{
			throw new ProviderException(he);
		}
	}

	public ORProperty insertProperty(ORProperty property)
			throws ProviderException
	{
		return (ORProperty) hibernateProvider.save(property);
	}

	public void updateProperty(ORProperty property) throws ProviderException
	{
		hibernateProvider.update(property);
	}

	public void deleteProperty(ORProperty property) throws ProviderException
	{
		try
		{
			hibernateProvider.delete(property);
		}
		catch (ConstraintException ce)
		{
			throw new ProviderException(ce.getMessage());
		}
	}	
}