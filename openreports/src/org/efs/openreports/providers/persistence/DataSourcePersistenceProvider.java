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

package org.efs.openreports.providers.persistence;

import java.util.List;

import org.apache.log4j.Logger;

import org.efs.openreports.objects.ReportDataSource;
import org.efs.openreports.providers.HibernateProvider;
import org.efs.openreports.providers.ProviderException;
import org.efs.openreports.util.LocalStrings;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;

public class DataSourcePersistenceProvider 
{
	protected static Logger log =
		Logger.getLogger(DataSourcePersistenceProvider.class.getName());

	public DataSourcePersistenceProvider() throws ProviderException
	{
		super();

		log.info("DataSourcePersistenceProvider Created.");
	}

	public ReportDataSource getDataSource(Integer id) throws ProviderException
	{
		return (ReportDataSource) HibernateProvider.load(ReportDataSource.class, id);
	}
	
	public ReportDataSource getDataSource(String name) throws ProviderException
	{
		Session session = null;
		
		try
		{
			session = HibernateProvider.openSession();
			
			Criteria criteria = session.createCriteria(ReportDataSource.class);
			criteria.add(Expression.eq("name", name));
			
			return (ReportDataSource) criteria.uniqueResult();
		}
		catch (HibernateException he)
		{
			throw new ProviderException(he);
		}
		finally
		{
			HibernateProvider.closeSession(session);
		}
	}

	public List getDataSources() throws ProviderException
	{
		String fromClause =
			"from org.efs.openreports.objects.ReportDataSource reportDataSource order by reportDataSource.name ";
		
		return HibernateProvider.query(fromClause);
	}

	public ReportDataSource insertDataSource(ReportDataSource reportDataSource)
		throws ProviderException
	{
		return (ReportDataSource) HibernateProvider.save(reportDataSource);
	}

	public void updateDataSource(ReportDataSource reportDataSource)
		throws ProviderException
	{
		HibernateProvider.update(reportDataSource);
	}

	public void deleteDataSource(ReportDataSource reportDataSource)
		throws ProviderException
	{
		try
		{
			HibernateProvider.delete(reportDataSource);
		}
		catch (ConstraintException ce)
		{
			throw new ProviderException(LocalStrings.ERROR_DATASOURCE_DELETION);
		}
	}
}