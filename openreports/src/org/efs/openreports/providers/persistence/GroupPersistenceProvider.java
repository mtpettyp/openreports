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
import org.efs.openreports.objects.Report;
import org.efs.openreports.objects.ReportGroup;
import org.efs.openreports.providers.HibernateProvider;
import org.efs.openreports.providers.ProviderException;
import org.efs.openreports.util.LocalStrings;
import org.hibernate.HibernateException;
import org.hibernate.Session;

public class GroupPersistenceProvider 
{
	protected static Logger log =
		Logger.getLogger(GroupPersistenceProvider.class.getName());

	public GroupPersistenceProvider() throws ProviderException
	{
		super();

		log.info("GroupPersistenceProvider Created.");
	}

	public ReportGroup getReportGroup(Integer id) throws ProviderException
	{
		return (ReportGroup) HibernateProvider.load(ReportGroup.class, id);
	}

	public List getReportGroups() throws ProviderException
	{
		String fromClause =
			"from org.efs.openreports.objects.ReportGroup reportGroup order by reportGroup.name ";
		
		return HibernateProvider.query(fromClause);
	}
	
	public List getGroupsForReport(Report report) throws ProviderException
	{
		try
		{
			Session session = HibernateProvider.openSession();			
			
			try
			{			
				List list = session.createQuery(
						"from org.efs.openreports.objects.ReportGroup as reportGroup "
								+ "where ? in elements(reportGroup.reports)").setEntity(0, report).list();					

				if (list.size() == 0) return null;						

				return list;
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

	public ReportGroup insertReportGroup(ReportGroup reportGroup)
		throws ProviderException
	{
		return (ReportGroup) HibernateProvider.save(reportGroup);
	}

	public void updateReportGroup(ReportGroup reportGroup)
		throws ProviderException
	{
		HibernateProvider.update(reportGroup);
	}

	public void deleteReportGroup(ReportGroup reportGroup)
		throws ProviderException
	{
		try
		{
			HibernateProvider.delete(reportGroup);
		}
		catch (ConstraintException ce)
		{
			throw new ProviderException(LocalStrings.ERROR_GROUP_DELETION);
		}
	}
}