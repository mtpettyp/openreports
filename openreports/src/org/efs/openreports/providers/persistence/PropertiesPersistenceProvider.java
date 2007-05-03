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

package org.efs.openreports.providers.persistence;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import org.apache.log4j.Logger;
import org.efs.openreports.objects.ORProperty;
import org.efs.openreports.providers.HibernateProvider;
import org.efs.openreports.providers.ProviderException;

public class PropertiesPersistenceProvider 
{
	protected static Logger log = Logger
			.getLogger(PropertiesPersistenceProvider.class.getName());

	public PropertiesPersistenceProvider() throws ProviderException
	{
		super();

		log.info("PropertiesPersistenceProvider Created.");
	}

	public ORProperty getProperty(String key) throws ProviderException
	{
		try
		{
			Session session = HibernateProvider.openSession();			

			try
			{				
				List list = session.createQuery(
						"from org.efs.openreports.objects.ORProperty as orProperty "
								+ "where orProperty.key = ?").setCacheable(true).setString(0, key).list();
					
				if (list.size() == 0)
					return null;

				ORProperty property = (ORProperty) list.get(0);			

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
		return (ORProperty) HibernateProvider.save(property);
	}

	public void updateProperty(ORProperty property) throws ProviderException
	{
		HibernateProvider.update(property);
	}

	public void deleteProperty(ORProperty property) throws ProviderException
	{
		try
		{
			HibernateProvider.delete(property);
		}
		catch (ConstraintException ce)
		{
			throw new ProviderException(ce.getMessage());
		}
	}

}