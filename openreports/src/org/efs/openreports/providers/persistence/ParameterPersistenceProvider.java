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
import org.efs.openreports.objects.ReportParameter;
import org.efs.openreports.providers.HibernateProvider;
import org.efs.openreports.providers.ProviderException;
import org.efs.openreports.util.LocalStrings;

import org.hibernate.*;
import org.hibernate.criterion.Restrictions;

public class ParameterPersistenceProvider 
{
	protected static Logger log =
		Logger.getLogger(ParameterPersistenceProvider.class.getName());

	public ParameterPersistenceProvider() throws ProviderException
	{
		super();

		log.info("ParameterPersistenceProvider Created.");
	}

	public ReportParameter getReportParameter(Integer id)
		throws ProviderException
	{
		return (ReportParameter) HibernateProvider.load(ReportParameter.class, id);
	}
	
	public ReportParameter getReportParameter(String name) throws ProviderException
	{
		Session session = null;
		
		try
		{
			session = HibernateProvider.openSession();
			
			Criteria criteria = session.createCriteria(ReportParameter.class);
			criteria.add(Restrictions.eq("name", name));
			
			return (ReportParameter) criteria.uniqueResult();
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

	@SuppressWarnings("unchecked")
	public List<ReportParameter> getReportParameters() throws ProviderException
	{
		String fromClause =
			"from org.efs.openreports.objects.ReportParameter reportParameter order by reportParameter.name ";
		
		return (List<ReportParameter>) HibernateProvider.query(fromClause);
	}

	public ReportParameter insertReportParameter(ReportParameter reportParameter)
		throws ProviderException
	{
		return (ReportParameter) HibernateProvider.save(reportParameter);
	}

	public void updateReportParameter(ReportParameter reportParameter)
		throws ProviderException
	{
		HibernateProvider.update(reportParameter);
	}

	public void deleteReportParameter(ReportParameter reportParameter)
		throws ProviderException
	{
		try
		{
			HibernateProvider.delete(reportParameter);
		}
		catch (ConstraintException ce)
		{
			throw new ProviderException(LocalStrings.ERROR_PARAMETER_DELETION);
		}
	}
}