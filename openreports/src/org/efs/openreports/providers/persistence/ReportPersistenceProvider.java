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

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.efs.openreports.objects.Report;
import org.efs.openreports.objects.ReportGroup;
import org.efs.openreports.objects.ReportLog;
import org.efs.openreports.providers.HibernateProvider;
import org.efs.openreports.providers.ProviderException;
import org.efs.openreports.util.LocalStrings;

import org.hibernate.*;
import org.hibernate.criterion.Restrictions;

public class ReportPersistenceProvider 
{
	protected static Logger log =
		Logger.getLogger(ReportPersistenceProvider.class.getName());

	public ReportPersistenceProvider() throws ProviderException
	{
		super();

		log.info("ReportPersistenceProvider Created.");
	}

	public Report getReport(Integer id) throws ProviderException
	{
		return (Report) HibernateProvider.load(Report.class, id);
	}

	 public Report getReport(String name) throws ProviderException
	{
		Session session = null;
		
		try
		{
			session = HibernateProvider.openSession();
			
			Criteria criteria = session.createCriteria(Report.class);
			criteria.add(Restrictions.eq("name", name));
			
			return (Report) criteria.uniqueResult();
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
	public List<Report> getReports() throws ProviderException
	{
		String fromClause =
			"from org.efs.openreports.objects.Report report order by report.name ";
		
		return (List<Report>) HibernateProvider.query(fromClause);
	}

	public Report insertReport(Report report) throws ProviderException
	{
		return (Report) HibernateProvider.save(report);
	}

	public void updateReport(Report report) throws ProviderException
	{
		HibernateProvider.update(report);
	}

	public void deleteReport(Report report) throws ProviderException
	{
		Session session = HibernateProvider.openSession();
		Transaction tx = null;

		try
		{
			tx = session.beginTransaction();
			
			//delete report			
			session.delete(report);		
			
			//delete report log entries for report
			Iterator<?> iterator =  session
				.createQuery(
					"from  org.efs.openreports.objects.ReportLog reportLog where reportLog.report.id = ? ")
				.setInteger(0, report.getId().intValue()).iterate();
					
			while(iterator.hasNext())
			{
				ReportLog reportLog = (ReportLog) iterator.next();		 	
				session.delete(reportLog);
			}		
			
			//remove report from groups
			iterator =  session
				.createQuery(
						"from org.efs.openreports.objects.ReportGroup reportGroup").iterate();
						
			 while(iterator.hasNext())
			 {
			 	ReportGroup reportGroup = (ReportGroup) iterator.next();
			 	
			 	List<Report> reports = reportGroup.getReports();			 	
			 	if (reports.contains(report))
			 	{
			 		reports.remove(report);
			 	}
			 }	
			
			tx.commit();
		}
		catch (HibernateException he)
		{
			HibernateProvider.rollbackTransaction(tx);
						
			if (he.getCause() != null && he.getCause().getMessage() != null && he.getCause().getMessage().toUpperCase().indexOf("CONSTRAINT") > 0)
			{
				throw new ProviderException(LocalStrings.ERROR_REPORT_DELETION);
			}
			
			log.error("deleteReport", he);			
			throw new ProviderException(LocalStrings.ERROR_SERVERSIDE);
		}
		finally
		{
			HibernateProvider.closeSession(session);
		}
	}
}